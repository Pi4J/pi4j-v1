/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_jni_SerialInterrupt.c
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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
#include <errno.h>
#include <sys/epoll.h>
#include <linux/serial.h>

// constants
#define SERIAL_MAX_LISTENERS   32
#define SERIAL_POLL_TIMEOUT    30000 // 30 seconds

// java callback variables
jclass serial_callback_class;
jmethodID serial_callback_method;
JavaVM *serial_callback_jvm;

// monitoring thread data structure
struct serial_monitor_data{
   int  thread_id;
   int  fileDescriptor;
   int  running;
};

// monitoring thread data structure array
struct serial_monitor_data serial_monitor_data_array[SERIAL_MAX_LISTENERS];

// monitoring serial_monitor_threads array
pthread_t serial_monitor_threads[SERIAL_MAX_LISTENERS];

// determine result data array length from the number of bytes available on the receive buffer
int getAvailableDataLength(int fd){
    int length;
    if (ioctl (fd, FIONREAD, &length) == -1){
        return -1;
    }
    return length;
}

/**
 * --------------------------------------------------------
 * SERIAL PORT DATA RECEIVE MONITORING HANDLER
 * --------------------------------------------------------
 * This method is invoked in a new thread for each serial port
 * this is being monitored.  This way multiple ports can be
 * monitored simultaneously and discretely.
 */
int monitorSerialInterrupt(void *threadarg)
{
	// obtain the monitoring data structure from the thread argument
	struct serial_monitor_data *monitor;
	monitor = (struct serial_monitor_data *) threadarg;

	// cache a local serial port file descriptor value variable
	int fileDescriptor = monitor->fileDescriptor;

	//printf("\nNATIVE (SerialInterrupt) MONITORING FD %d \n", fileDescriptor);

    // monitoring instance variables
    struct epoll_event ev;
    struct epoll_event events;

    // create epoll
    int epfd = epoll_create(1); // argument must be greater than 0; but value is not used

    // set polling event triggers and file descriptor
    ev.events = EPOLLIN;
    ev.data.fd = fileDescriptor;

    // configure polling parameters for this serial port
    int res = epoll_ctl(epfd, EPOLL_CTL_ADD, fileDescriptor, &ev);
    if( res < 0 )
    {
        //printf("Error epoll_ctl: %i\n", errno);

		// return error; unable to get serial polling control options
		perror("ERROR SERIAL EPOLL  CTL");
		return res;
    }

	// set the running state of the instance monitor data structure
	monitor->running = 1;

	// continuous thread loop
	for(;;)
	{
        // wait for input
        int ret = epoll_wait(epfd, &events, 1, SERIAL_POLL_TIMEOUT);
        //printf("SERIAL EPOLL unblocked: %i\n", events.events);

		// if the return value is less than'0' then
		// the polling encountered and error and we
		// need to restart
        if(ret < 0)
        {
            perror("SERIAL EPOLL failed\n");
            //printf("SERIAL EPOLL failed\n");
            close(epfd);

            // reset the running state of the instance monitor data structure
            monitor->running = 0;

            return ret;
        }

		// if the return value is equal to '0' then
		// the polling simply timed out and we can restart
        if(ret == 0)
        {
            //printf("SERIAL EPOLL TIMEOUT\n");
            continue;
        }

		// if the return value is greater than '0' then
		// data have been received on the serial port

       // sleep a little before raising event notification (callback)
       usleep (50000) ;	// 50mS

       int length = getAvailableDataLength(fileDescriptor);

       //printf("SERIAL EPOLL - Bytes available: %d\n", length);

       // create a new payload result byte array
       jbyte result[length];

       // copy the data bytes from the serial receive buffer into the payload result
       int i;
       for (i = 0; i < length; i++) {

            // read a single byte from the RX buffer
            uint8_t x ;
            if (read (fileDescriptor, &x, 1) != 1){
                int err_number = errno;
                char err_message[100];
                sprintf(err_message, "Failed to read data from serial port. (Error #%d)", err_number);
                perror("SERIAL FAILED TO READ DATA\n");
                close(epfd);
                continue;
            }

            // assign the single byte; cast to unsigned char
            result[i] = (unsigned char)(((int)x) & 0xFF);
       }

       // ensure the callback class and method are available
       if (serial_callback_class != NULL && serial_callback_method != NULL)
       {
            // get attached JVM
            JNIEnv *env;
            (*serial_callback_jvm)->AttachCurrentThread(serial_callback_jvm, (void **)&env, NULL);

            // create a java array object and copy the raw payload bytes
            jbyteArray payload = (*env)->NewByteArray(env, length);
            (*env)->SetByteArrayRegion(env, payload, 0, length, result);

            // ensure that the JVM exists
            if(serial_callback_jvm != NULL)
            {
                // invoke callback to java state method to notify event listeners
                (*env)->CallStaticVoidMethod(env, serial_callback_class, serial_callback_method, (jint)fileDescriptor, payload);
            }

            // detach from thread
            (*serial_callback_jvm)->DetachCurrentThread(serial_callback_jvm);
       }
    }

	// if we reached this code (unlikely),
	// then close the epoll file descriptor and exit the thread
	close(epfd);
	return 0;
}


int findSerialMonitorIndex(int fileDescriptor)
{
   int index;

   // ensure that the requested file descriptor is valid
   if(fileDescriptor < 0 )
   {
      return -2;
   }

   // first, attempt to find and existing file descriptor in the data array
   for (index = 0; index < SERIAL_MAX_LISTENERS; index++)
   {
        if(serial_monitor_data_array[index].fileDescriptor == fileDescriptor)
        {
          return index;
        }
   }

   // if an existing instance was not found, then find a new available (not running) index
   for (index = 0; index < SERIAL_MAX_LISTENERS; index++)
   {
        if(serial_monitor_data_array[index].running <= 0)
        {
          return index;
        }
   }

   return -1;
}


/*
 * --------------------------------------------------------
 * ENABLE SERIAL DATA RECEIVE CALLBACKS
 * --------------------------------------------------------
 *
 * Class:     com_pi4j_jni_SerialInterrupt
 * Method:    enableSerialDataReceiveCallback
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_SerialInterrupt_enableSerialDataReceiveCallback
  (JNIEnv *env, jclass class, jint fileDescriptor)
{
	// get the file descriptor
	int index = findSerialMonitorIndex(fileDescriptor);

	// ensure that the index is valid
	if(index >= 0)
	{
		// only start this thread monitor if it has not already been started
		if(serial_monitor_data_array[index].running <= 0)
		{
			// configure the monitor instance data
			serial_monitor_data_array[index].thread_id = index;
			serial_monitor_data_array[index].fileDescriptor = fileDescriptor;

			// create monitoring instance thread
			pthread_create(&serial_monitor_threads[index], NULL, (void*) monitorSerialInterrupt, (void *) &serial_monitor_data_array[index]);

			// return '1' when a thread was actively created and started
			return 1;
		}

		// return '0' when no action was taken;
		// (monitor already running)
		return 0;
	}

	// return '-1' on error; not a valid serial file descriptor
	return -1;
}

/*
 * --------------------------------------------------------
 * DISABLE SERIAL DATA RECEIVE CALLBACKS
 * --------------------------------------------------------
 *
 * Class:     com_pi4j_jni_SerialInterrupt
 * Method:    disableSerialDataReceiveCallback
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_SerialInterrupt_disableSerialDataReceiveCallback
  (JNIEnv *env, jclass class, jint fileDescriptor)
{
	// get the index position for the requested file descriptor
	int index = findSerialMonitorIndex(fileDescriptor);

	// ensure that the index is valid
	if(index >= 0)
	{
		// kill the monitoring thread
		if(serial_monitor_data_array[index].running > 0)
		{
			pthread_cancel(serial_monitor_threads[index]);

            // reset running flag
            serial_monitor_data_array[index].running = 0;
            serial_monitor_data_array[index].fileDescriptor = 0;

			// return '1' when a thread was actively killed
			return 1;
		}

		// return '0' when no action was taken
		// (monitor is not currently active/running)
		return 0;
	}

	// return '-1' on error; not a valid serial descriptor
	return -1;
}


/**
 * --------------------------------------------------------
 * JNI LIBRARY LOADED
 * --------------------------------------------------------
 * capture java references to be used later for callback methods
 */
jint SerialInterrupt_JNI_OnLoad(JavaVM *jvm)
{
	JNIEnv *env;
	jclass cls;

	//printf("\nNATIVE (SerialInterrupt) LOADING\n");

	// cache the JavaVM pointer
	serial_callback_jvm = jvm;

	// ensure that the calling environment is a supported JNI version
    if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2))
    {
    	// JNI version not supported
    	printf("NATIVE (SerialInterrupt) ERROR; JNI version not supported.\n");
        return JNI_ERR;
    }

    // search the attached java environment for the 'SerialInterrupt' class
    cls = (*env)->FindClass(env, "com/pi4j/jni/SerialInterrupt");
    if (cls == NULL)
    {
    	// expected class not found
    	printf("NATIVE (SerialInterrupt) ERROR; SerialInterrupt class not found.\n");
        return JNI_ERR;
    }

    // use weak global ref to allow C class to be unloaded
    serial_callback_class = (*env)->NewWeakGlobalRef(env, cls);
    if (serial_callback_class == NULL)
    {
    	// unable to create weak reference to java class
    	printf("NATIVE (SerialInterrupt) ERROR; Java class reference is NULL.\n");
        return JNI_ERR;
    }

    // lookup and cache the static method ID for the 'onDataReceiveCallback' callback
    serial_callback_method = (*env)->GetStaticMethodID(env, cls, "onDataReceiveCallback", "(I[B)V");
    if (serial_callback_method == NULL)
    {
    	// callback method could not be found in attached java class
    	printf("NATIVE (SerialInterrupt) ERROR; Static method 'SerialInterrupt.onDataReceiveCallback()' could not be found.\n");
        return JNI_ERR;
    }

	// return JNI version; success
	return JNI_VERSION_1_2;
}


/**
 * --------------------------------------------------------
 * JNI LIBRARY UNLOADED
 * --------------------------------------------------------
 * stop all monitoring serial_monitor_threads and clean up references
 */
void SerialInterrupt_JNI_OnUnload(JavaVM *jvm)
{
	//printf("\nNATIVE (SerialInterrupt) UNLOADING\n");

	// kill all running monitor serial_monitor_threads
	int index = 0;
	for(index = 0; index < SERIAL_MAX_LISTENERS; index++)
	{
		if(serial_monitor_data_array[index].running > 0)
			pthread_cancel(serial_monitor_threads[index]);
	}

	// destroy cached java references
	JNIEnv *env;
    if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2))
    {
    	return;
	}
	(*env)->DeleteWeakGlobalRef(env, serial_callback_class);

	return;
}
