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
 * Copyright (C) 2012 - 2014 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

// constants
#define GPIO_FN_MAXLEN  32
#define POLL_TIMEOUT    10000
#define RDBUF_LEN       5


// java callback variables
JavaVM *callback_jvm;
jclass callback_class;
jmethodID callback_method;

// monitoring thread data structure
struct monitor_data{
   int  thread_id;
   int  pin;
   int  edgePin;
   int  lastKnownState;
   int  running;
};

// monitoring thread data structure array
struct monitor_data monitor_data_array[MAX_GPIO_PINS];

// monitoring threads array
pthread_t threads[MAX_GPIO_PINS];


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
	struct monitor_data *monitorData;
	monitorData = (struct monitor_data *) threadarg;

	// cache a local pin value variable
	int pin = monitorData->pin;
	int edgePin = monitorData->edgePin;

	//printf("\nNATIVE (GpioInterrupt) MONITORING PIN %d @ EDGE %d\n", pin, edgePin);

    // monitoring instance variables
	char fn[GPIO_FN_MAXLEN];
	int fd,ret;
	struct pollfd pfd;
	char rdbuf[RDBUF_LEN];

	// allocate memory
	memset(rdbuf, 0x00, RDBUF_LEN);
	memset(fn, 0x00, GPIO_FN_MAXLEN);

	// attempt to access the pin state from the linux sysfs
	// (each GPIO pin value is stored in file: '/sys/class/gpio/gpio#/value' )
	snprintf(fn, GPIO_FN_MAXLEN-1, "/sys/class/gpio/gpio%d/value", edgePin);
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
	ret=read(fd, rdbuf, RDBUF_LEN-1);
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
		memset(rdbuf, 0x00, RDBUF_LEN);

		// seek to the fist position in the data file
		lseek(fd, 0, SEEK_SET);

		// wait for data to be written to the GPIO value file
		// (timeout every 10 seconds and restart)
		ret=poll(&pfd, 1, POLL_TIMEOUT);

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
			ret=read(fd, rdbuf, RDBUF_LEN-1);
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
				if (callback_class != NULL && callback_method != NULL)
				{
					// get attached JVM
					JNIEnv *env;
					(*callback_jvm)->AttachCurrentThread(callback_jvm, (void **)&env, NULL);

					// ensure that the JVM exists
					if(callback_jvm != NULL)
					{
						// invoke callback to java state method to notify event listeners
						if(compareResult == 0)
							(*env)->CallStaticVoidMethod(env, callback_class, callback_method, (jint)pin, (jboolean)1);
						else
							(*env)->CallStaticVoidMethod(env, callback_class, callback_method, (jint)pin, (jboolean)0);
					}

                    // detach from thread
                    (*callback_jvm)->DetachCurrentThread(callback_jvm);
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
		if(monitor_data_array[index].running <= 0)
		{
			// configure the monitor instance data
			monitor_data_array[index].thread_id = index;
			monitor_data_array[index].pin = pin;
			monitor_data_array[index].edgePin = edgePin;

			// create monitoring instance thread
			pthread_create(&threads[index], NULL, (void*) monitorPinInterrupt, (void *) &monitor_data_array[index]);

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
		if(monitor_data_array[index].running > 0)
		{
			pthread_cancel(threads[index]);

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
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved)
{
	JNIEnv *env;
	jclass cls;

	//printf("\nNATIVE (GpioInterrupt) LOADING\n");

	// cache the JavaVM pointer
	callback_jvm = jvm;

	// ensure that the calling environment is a supported JNI version
    if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2))
    {
    	// JNI version not supported
    	printf("NATIVE (GpioInterrupt) ERROR; JNI version not supported.\n");
        return JNI_ERR;
    }

    // search the attached java enviornment for the 'GpioInterrupt' class
    cls = (*env)->FindClass(env, "com/pi4j/wiringpi/GpioInterrupt");
    if (cls == NULL)
    {
    	// expected class not found
    	printf("NATIVE (GpioInterrupt) ERROR; GpioInterrupt class not found.\n");
        return JNI_ERR;
    }

    // use weak global ref to allow C class to be unloaded
    callback_class = (*env)->NewWeakGlobalRef(env, cls);
    if (callback_class == NULL)
    {
    	// unable to create weak reference to java class
    	printf("NATIVE (GpioInterrupt) ERROR; Java class reference is NULL.\n");
        return JNI_ERR;
    }

    // lookup and cache the static method ID for the 'pinStateChangeCallback' callback
    callback_method = (*env)->GetStaticMethodID(env, cls, "pinStateChangeCallback", "(IZ)V");
    if (callback_method == NULL)
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
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *jvm, void *reserved)
{
	// kill all running monitor threads
	int index = 0;
	for(index = 0; index < MAX_GPIO_PINS; index++)
	{
		if(monitor_data_array[index].running > 0)
			pthread_cancel(threads[index]);
	}

	// destroy cached java references
	JNIEnv *env;
    if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2))
    {
    	return;
	}
	(*env)->DeleteWeakGlobalRef(env, callback_class);

	return;
}
