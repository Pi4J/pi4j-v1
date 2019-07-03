/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_jni_AnalogInputMonitor.c
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
#include <math.h>
#include <fcntl.h>
#include <jni.h>
#include <string.h>
#include <pthread.h>
#include <termios.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <wiringPi.h>
#include "com_pi4j_wiringpi_GpioPin.h"
#include "com_pi4j_wiringpi_GpioUtil.h"
#include "com_pi4j_jni_AnalogInputMonitor.h"

/*
 * Analog input monitor JVM instance to perform callbacks on
 */
JavaVM *analog_input_monitor_callback_jvm;

// java callback variables
jclass analog_input_monitor_callback_class;
jmethodID analog_input_monitor_callback_method;

// monitoring thread data structure
struct analog_input_monitor_data{
   int  thread_id;
   int  pin;
   int  pollingRate;
   double  changeThreshold;
   double  lastKnownValue;
   int  running;
};

// monitoring thread data structure array
struct analog_input_monitor_data analog_input_monitor_data_array[MAX_GPIO_PINS];

// monitoring threads array
pthread_t analog_input_monitor_threads[MAX_GPIO_PINS];


/**
 * --------------------------------------------------------
 * GPIO ANALOG PIN MONITORING HANDLER
 * --------------------------------------------------------
 * This method is invoked in a new thread for each pin that
 * is being monitored.  This way multiple pins can be
 * monitored simultaneously and discretely.
 */
int analog_input_monitor_pin(void *threadarg)
{
	// obtain the monitoring data structure from the thread argument
	struct analog_input_monitor_data *monitorData;
	monitorData = (struct analog_input_monitor_data *) threadarg;

	// cache a local pin value variable
	int pin = monitorData->pin;

	// debug
	//printf("\nNATIVE (AnalogInputMonitor) MONITORING PIN %d\n", pin);

 	// set the running state of the instance monitor data structure
	monitorData->running = 1;

	// initialize last known value and cache the value as the last known value
	monitorData->lastKnownValue = analogRead(pin);

	// continuous thread loop
	while(monitorData->running > 0)
	{
		// sleep
		delay(monitorData->pollingRate); // milliseconds

		// read latest analog input value
		double immediateValue = analogRead(pin);

        //printf("\nNATIVE (AnalogInputMonitor) ANALOG VALUE (PIN %d) = %f\n", pin, compareResult);

        // get absolute value of the difference between the last known analog value and the current analog value
        double change = fabs(immediateValue - monitorData->lastKnownValue);

		// check for change in analog value
		if(change > monitorData->changeThreshold)
		{
			// cache new last known value in the instance data structure
			monitorData->lastKnownValue = immediateValue;

			// ensure the callback class and method are available
			if (analog_input_monitor_callback_class != NULL && analog_input_monitor_callback_method != NULL)
			{
				// get attached JVM
				JNIEnv *env;
				(*analog_input_monitor_callback_jvm)->AttachCurrentThread(analog_input_monitor_callback_jvm, (void **)&env, NULL);

				// ensure that the JVM exists
				if(analog_input_monitor_callback_jvm != NULL)
				{
					// invoke the java callback method to notify event listeners
					(*env)->CallStaticVoidMethod(env, analog_input_monitor_callback_class, analog_input_monitor_callback_method, (jint)pin, (jdouble)immediateValue);
				}

				// detach from thread
				(*analog_input_monitor_callback_jvm)->DetachCurrentThread(analog_input_monitor_callback_jvm);
			}
		}
	}

	return 0;
}

/*
 * --------------------------------------------------------
 * ENABLE ANALOG PIN MONITORING (for callback notifications)
 * --------------------------------------------------------
 * Class:     com_pi4j_jni_AnalogInputMonitor
 * Method:    enablePinValueChangeCallback
 * Signature: (I)IID
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_AnalogInputMonitor_enablePinValueChangeCallback
  (JNIEnv *env, jclass class, jint pin, jint pollingRate, jdouble changeThreshold)
{
	// get the index position for the requested pin number
	int index = pin;

	// ensure that the requested pin index is valid
	if(index >= 0)
	{
		// only start this thread monitor if it has not already been started
		if(analog_input_monitor_data_array[index].running <= 0)
		{
			// configure the monitor instance data
			analog_input_monitor_data_array[index].thread_id = index;
			analog_input_monitor_data_array[index].pin = pin;

			// assign a polling rate for the monitoring thread
			if(pollingRate < 0) pollingRate = 50; // bounds validation
			analog_input_monitor_data_array[index].pollingRate = pollingRate;

			// assign a change threshold for event notifications in the monitoring thread
			if(changeThreshold < 0) changeThreshold = 0; // bounds validation
			analog_input_monitor_data_array[index].changeThreshold = changeThreshold;

			// create monitoring instance thread
			pthread_create(&analog_input_monitor_threads[index], NULL, (void*) analog_input_monitor_pin, (void *) &analog_input_monitor_data_array[index]);

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
 * DISABLE ANALOG PIN MONITORING (for callback notifications)
 * --------------------------------------------------------
 * Class:     com_pi4j_jni_AnalogInputMonitor
 * Method:    disablePinValueChangeCallback
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_AnalogInputMonitor_disablePinValueChangeCallback
  (JNIEnv *env, jclass class, jint pin)
{
	// get the index position for the requested pin number
	int index = pin;

	// ensure that the requested pin index is valid
	if(index >= 0)
	{
		// kill the monitoring thread
		if(analog_input_monitor_data_array[index].running > 0)
		{
			// cancel monitoring thread
			pthread_cancel(analog_input_monitor_threads[index]);

            // reset running flag
            analog_input_monitor_data_array[index].running = 0;

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
jint AnalogInputMonitor_JNI_OnLoad(JavaVM *jvm)
{
	JNIEnv *env;
	jclass cls;

	//printf("\nNATIVE (AnalogInputMonitor) LOADING\n");

	// cache the JavaVM pointer
	analog_input_monitor_callback_jvm = jvm;

	// ensure that the calling environment is a supported JNI version
    if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2))
    {
    	// JNI version not supported
    	printf("NATIVE (AnalogInputMonitor) ERROR; JNI version not supported.\n");
        return JNI_ERR;
    }

    // search the attached java environment for the 'AnalogInputMonitor' class
    cls = (*env)->FindClass(env, "com/pi4j/jni/AnalogInputMonitor");
    if (cls == NULL)
    {
    	// expected class not found
    	printf("NATIVE (AnalogInputMonitor) ERROR; AnalogInputMonitor class not found.\n");
        return JNI_ERR;
    }

    // use weak global ref to allow C class to be unloaded
    analog_input_monitor_callback_class = (*env)->NewWeakGlobalRef(env, cls);
    if (analog_input_monitor_callback_class == NULL)
    {
    	// unable to create weak reference to java class
    	printf("NATIVE (AnalogInputMonitor) ERROR; Java class reference is NULL.\n");
        return JNI_ERR;
    }

    // lookup and cache the static method ID for the 'pinValueChangeCallback' callback
    analog_input_monitor_callback_method = (*env)->GetStaticMethodID(env, cls, "pinValueChangeCallback", "(ID)V");
    if (analog_input_monitor_callback_method == NULL)
    {
    	// callback method could not be found in attached java class
    	printf("NATIVE (AnalogInputMonitor) ERROR; Static method 'AnalogInputMonitor.pinValueChangeCallback()' could not be found.\n");
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
void AnalogInputMonitor_JNI_OnUnload(JavaVM *jvm)
{
    //printf("\nNATIVE (AnalogInputMonitor) UNLOADING\n");

	// kill all running monitor threads
	int index = 0;
	for(index = 0; index < MAX_GPIO_PINS; index++)
	{
		if(analog_input_monitor_data_array[index].running > 0){
            // kill monitoring thread
            pthread_cancel(analog_input_monitor_threads[index]);

            // reset running flag
            analog_input_monitor_data_array[index].running = 0;
		}
	}

	// destroy cached java references
	JNIEnv *env;
    if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2))
    {
    	return;
	}
	(*env)->DeleteWeakGlobalRef(env, analog_input_monitor_callback_class);

	return;
}
