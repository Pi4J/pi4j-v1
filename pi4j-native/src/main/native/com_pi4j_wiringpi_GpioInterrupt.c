/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_GpioInterrupt.c
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
#include <stdio.h>
#include <stdint.h>
#include <stdarg.h>
#include <stdlib.h>
#include <poll.h>
#include <fcntl.h>
#include <jni.h>
#include <string.h>
#include <pthread.h>
#include <termios.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "com_pi4j_wiringpi_GpioPin.h"
#include "com_pi4j_wiringpi_GpioInterrupt.h"
#include "com_pi4j_wiringpi_GpioUtil.h"

// constants
#define GPIO_FN_MAXLEN  128
#define GPIO_POLL_TIMEOUT    30000 // 30 seconds
#define GPIO_RDBUF_LEN       5


// java callback variables
jclass gpio_callback_class;
jmethodID gpio_callback_method;
JavaVM *gpio_callback_jvm;

// monitoring thread data structure
struct gpio_monitor_data{
   int  thread_id;
   int  pin;
   int  edgePin;
   int  lastKnownState;
   int  running;
};

// monitoring thread data structure array
struct gpio_monitor_data gpio_monitor_data_array[MAX_GPIO_PINS];

// monitoring threads array
pthread_t gpio_monitor_threads[MAX_GPIO_PINS];


/**
 * --------------------------------------------------------
 * GPIO PIN MONITORING HANDLER
 * --------------------------------------------------------
 * This method is invoked in a new thread for each pin that
 * is being monitored.  This way multiple pins can be
 * monitored simultaneously and discretely.
 */
int monitorPinInterrupt(void *threadarg)
{
	// obtain the monitoring data structure from the thread argument
	struct gpio_monitor_data *monitorData;
	monitorData = (struct gpio_monitor_data *) threadarg;

	// cache a local pin value variable
	int pin = monitorData->pin;
	int edgePin = monitorData->edgePin;

	//printf("\nNATIVE (GpioInterrupt) MONITORING PIN %d @ EDGE %d\n", pin, edgePin);

    // monitoring instance variables
	char fn[GPIO_FN_MAXLEN];
	int fd,ret;
	struct pollfd pfd;
	char rdbuf[GPIO_RDBUF_LEN];

	// allocate memory
	memset(rdbuf, 0x00, GPIO_RDBUF_LEN);
	memset(fn, 0x00, GPIO_FN_MAXLEN);

	// attempt to access the pin state from the linux sysfs
	// (each GPIO pin value is stored in file: '/sys/class/gpio/gpio#/value' )
	snprintf(fn, GPIO_FN_MAXLEN-1, GPIO_PIN_VALUE_FILE, edgePin);
	fd=open(fn, O_RDONLY);
	if(fd<0)
	{
		// return error; unable to get file descriptor
		// (this is likely because the pin has not been exported)
		perror(fn);
		return 2;
	}

	// set polling config structure
	pfd.fd=fd;
	pfd.events=POLLPRI; // High priority data may be read.

	// attempt to read the pin state from the linux sysfs
	ret=read(fd, rdbuf, GPIO_RDBUF_LEN-1);
	if(ret<0)
	{
		// return error; unable to read the data file
		// (this is likely because the user has insufficient permissions)
		perror("read()");
		return 4;
	}

	//printf("value is: %s\n", rdbuf);

	// set the running state of the instance monitor data structure
	monitorData->running = 1;

	// initialize last known value and cache the value as the last known state
	int compareLastKnown = strncmp(rdbuf, "1", 1); // only compare the first character; rdbuff may have more junk chars
	monitorData->lastKnownState = compareLastKnown;

	// continuous thread loop
	for(;;)
	{
		// clear/reset the data buffer
		memset(rdbuf, 0x00, GPIO_RDBUF_LEN);

		// seek to the fist position in the data file
		lseek(fd, 0, SEEK_SET);

		// wait for data to be written to the GPIO value file
		// (timeout every 10 seconds and restart)
		ret=poll(&pfd, 1, GPIO_POLL_TIMEOUT);

		// if the return value is less than '0' then
		// an error was thrown; bail out of the thread
		if(ret<0)
		{
			perror("poll()");
			close(fd);
			return 6;
		}

		// if the return value is equal to '0' then
		// the polling simply timed out and we can restart
		else if(ret==0)
		{
			//printf("timeout\n");
			continue;
		}

		// if the return value is greater than '0' then
		// a change to the GPIO data file occurred
		else
		{
			// read the data from the file into the data buffer
			ret=read(fd, rdbuf, GPIO_RDBUF_LEN-1);
			if(ret<0)
			{
				// data read error
				perror("read()");
				continue;
			}

			//printf("interrupt, value is: %s\n", rdbuf);

			// compare the data in the data buffer with the last known value state
			// (we do this to prevent double event invocation for the same value)
			int compareResult = strncmp(rdbuf, "1", 1); // only compare the first character; rdbuff may have more junk chars
			if(compareResult != monitorData->lastKnownState)
			{
				// cache new last known state in the instance data structure
				monitorData->lastKnownState = compareResult;

				// ensure the callback class and method are available
				if (gpio_callback_class != NULL && gpio_callback_method != NULL)
				{
					// get attached JVM
					JNIEnv *env;
					(*gpio_callback_jvm)->AttachCurrentThread(gpio_callback_jvm, (void **)&env, NULL);

					// ensure that the JVM exists
					if(gpio_callback_jvm != NULL)
					{
						// invoke callback to java state method to notify event listeners
						if(compareResult == 0)
							(*env)->CallStaticVoidMethod(env, gpio_callback_class, gpio_callback_method, (jint)pin, (jboolean)1);
						else
							(*env)->CallStaticVoidMethod(env, gpio_callback_class, gpio_callback_method, (jint)pin, (jboolean)0);
					}

                    // detach from thread
                    (*gpio_callback_jvm)->DetachCurrentThread(gpio_callback_jvm);
				}
			}
		}
	}

	// if we reached this code (unlikely),
	// then close the data file and exit the thread
	close(fd);
	return 0;
}

/*
 * --------------------------------------------------------
 * ENABLE PIN STATE CHANGES (for callback notifications)
 * --------------------------------------------------------
 * Class:     com_pi4j_wiringpi_GpioInterrupt
 * Method:    enablePinStateChangeCallback
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioInterrupt_enablePinStateChangeCallback
  (JNIEnv *env, jclass class, jint pin)
{
	// get the index position for the requested pin number
	int index = pin;
	int edgePin = getEdgePin(index);

	// ensure that the requested pin index is valid
	if(index >= 0 && edgePin >= 0)
	{
		// only start this thread monitor if it has not already been started
		if(gpio_monitor_data_array[index].running <= 0)
		{
            // get existing pin edge trigger
            int edge;
            edge = (int)Java_com_pi4j_wiringpi_GpioUtil_getEdgeDetection(env, class, pin);

            // if pin edge trigger is not set to "both", then attempt to set it now
            if(edge != com_pi4j_wiringpi_GpioUtil_EDGE_BOTH){
                int retval;
                retval = (int)Java_com_pi4j_wiringpi_GpioUtil_setEdgeDetection(env, class, pin, com_pi4j_wiringpi_GpioUtil_EDGE_BOTH);

                // exit if pin edge trigger configuration was not successful
                if(retval <= 0){
                    return -2; // unable to set edge trigger
                }
            }

			// configure the monitor instance data
			gpio_monitor_data_array[index].thread_id = index;
			gpio_monitor_data_array[index].pin = pin;
			gpio_monitor_data_array[index].edgePin = edgePin;

			// create monitoring instance thread
			pthread_create(&gpio_monitor_threads[index], NULL, (void*) monitorPinInterrupt, (void *) &gpio_monitor_data_array[index]);

			// return '1' when a thread was actively created and started
			return 1;
		}

		// return '0' when no action was taken;
		// (monitor already running)
		return 0;
	}

	// return '-1' on error; not a valid pin
	return -1;
}

/*
 * --------------------------------------------------------
 * DISABLE PIN STATE CHANGES (for callback notifications)
 * --------------------------------------------------------
 * Class:     com_pi4j_wiringpi_GpioInterrupt
 * Method:    disablePinStateChangeCallback
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioInterrupt_disablePinStateChangeCallback
  (JNIEnv *env, jclass class, jint pin)
{
	// get the index position for the requested pin number
	int index = pin;

	// ensure that the requested pin index is valid
	if(index >= 0)
	{
		// kill the monitoring thread
		if(gpio_monitor_data_array[index].running > 0)
		{
            // remove existing pin edge trigger
            int retval;
            retval = (int)Java_com_pi4j_wiringpi_GpioUtil_setEdgeDetection(env, class, pin, com_pi4j_wiringpi_GpioUtil_EDGE_NONE);

			// cancel monitoring thread
			pthread_cancel(gpio_monitor_threads[index]);

            // reset running flag
            gpio_monitor_data_array[index].running = 0;

			// return '1' when a thread was actively killed
			return 1;
		}

		// return '0' when no action was taken
		// (monitor is not currently active/running)
		return 0;
	}

	// return '-1' on error; not a valid pin
	return -1;
}


/**
 * --------------------------------------------------------
 * JNI LIBRARY LOADED
 * --------------------------------------------------------
 * capture java references to be used later for callback methods
 */
jint GpioInterrupt_JNI_OnLoad(JavaVM *jvm)
{
	JNIEnv *env;
	jclass cls;

	//printf("\nNATIVE (GpioInterrupt) LOADING\n");

	// cache the JavaVM pointer
	gpio_callback_jvm = jvm;

	// ensure that the calling environment is a supported JNI version
    if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2))
    {
    	// JNI version not supported
    	printf("NATIVE (GpioInterrupt) ERROR; JNI version not supported.\n");
        return JNI_ERR;
    }

    // search the attached java environment for the 'GpioInterrupt' class
    cls = (*env)->FindClass(env, "com/pi4j/wiringpi/GpioInterrupt");
    if (cls == NULL)
    {
    	// expected class not found
    	printf("NATIVE (GpioInterrupt) ERROR; GpioInterrupt class not found.\n");
        return JNI_ERR;
    }

    // use weak global ref to allow C class to be unloaded
    gpio_callback_class = (*env)->NewWeakGlobalRef(env, cls);
    if (gpio_callback_class == NULL)
    {
    	// unable to create weak reference to java class
    	printf("NATIVE (GpioInterrupt) ERROR; Java class reference is NULL.\n");
        return JNI_ERR;
    }

    // lookup and cache the static method ID for the 'pinStateChangeCallback' callback
    gpio_callback_method = (*env)->GetStaticMethodID(env, cls, "pinStateChangeCallback", "(IZ)V");
    if (gpio_callback_method == NULL)
    {
    	// callback method could not be found in attached java class
    	printf("NATIVE (GpioInterrupt) ERROR; Static method 'GpioInterrupt.pinStateChangeCallback()' could not be found.\n");
        return JNI_ERR;
    }

	// return JNI version; success
	return JNI_VERSION_1_2;
}


/**
 * --------------------------------------------------------
 * JNI LIBRARY UNLOADED
 * --------------------------------------------------------
 * stop all monitoring threads and clean up references
 */
void GpioInterrupt_JNI_OnUnload(JavaVM *jvm)
{
	// kill all running monitor threads
	int index = 0;
	for(index = 0; index < MAX_GPIO_PINS; index++)
	{
		if(gpio_monitor_data_array[index].running > 0)
			pthread_cancel(gpio_monitor_threads[index]);
	}

	// destroy cached java references
	JNIEnv *env;
    if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2))
    {
    	return;
	}
	(*env)->DeleteWeakGlobalRef(env, gpio_callback_class);

	return;
}
