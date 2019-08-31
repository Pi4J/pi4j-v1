/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_GpioUtil.c
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <fcntl.h>
#include "com_pi4j_wiringpi_GpioPin.h"
#include "com_pi4j_wiringpi_GpioUtil.h"

#define ENVIRON_GPIOMEM          "WIRINGPI_GPIOMEM"
#define ENVIRON_GPIOMEM_ENABLED  "1"

/* Source for com_pi4j_wiringpi_GpioUtil */

#define RDBUF_LEN       10

// pin directions
#define DIRECTION_IN 0
#define DIRECTION_OUT 1
#define DIRECTION_HIGH 2
#define DIRECTION_LOW 3

// edge detection conditions
#define EDGE_NONE 0
#define EDGE_BOTH 1
#define EDGE_RISING 2
#define EDGE_FALLING 3

int getExistingPinDirection(int edgePin)
{
	FILE *fd ;
	char fName [GPIO_FN_MAXLEN] ;
	char data[RDBUF_LEN];

	// construct the gpio direction file path
	getGpioPinDirectionFile(fName, edgePin);

	// open the gpio direction file
	if ((fd = fopen (fName, "r")) == NULL)
	{
		return -1;
	}

	// read the data from the file into the data buffer
	if(fgets(data, RDBUF_LEN, fd) == NULL)
	{
        // close the gpio direction file
  	    fclose (fd) ;
		return -2;
	}

	// close the gpio direction file
	fclose (fd) ;

	// determine direction mode
	if (strncasecmp(data, "in", 2) == 0)
	{
		return DIRECTION_IN;
	}
	else if (strncasecmp(data, "out", 3) == 0)
	{
		return DIRECTION_OUT;
	}
	else
	{
		return -3;
	}
}

/*
 * Class:     com_pi4j_wiringpi_GpioUtil
 * Method:    export
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_GpioUtil_export
  (JNIEnv *env, jclass class, jint pin, jint direction)
{
	FILE *fd ;
	char fName [GPIO_FN_MAXLEN];

	// validate the pin number
	if(isPinValid(pin) <= 0)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid GPIO pin [%d]\n", pin) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return;
	}

	// validate the pin direction
	if(direction != DIRECTION_IN &&
	   direction != DIRECTION_OUT &&
	   direction != DIRECTION_HIGH &&
	   direction != DIRECTION_LOW)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid GPIO pin [%d] direction [%d]\n", pin, direction) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return;
	}

	// get the edge pin number
	int edgePin = getEdgePin(pin);

	// validate that the export file can be accessed
	getGpioExportFile(fName);
	if ((fd = fopen (fName, "w")) == NULL)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unable to open GPIO export interface: %s\n", strerror (errno)) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return;
	}

	// add the pin to the export file; then close the file
	fprintf (fd, "%d\n", edgePin) ;
	fclose (fd) ;

	// get the pin direction
	int existing_direction = getExistingPinDirection(edgePin);

    // wait 100 milliseconds
    // for some reason we need to wait a short time after exporting the pin
    // before we can access the direction file; probably to allow for the
    // 'bcm2835_gpiomem' kernel driver enough time to apply the udev rules
    // to grant permisisons to the /sys/class/gpio/gpio%d/* interface files.
    usleep(100000);

	// set direction if its not already configured with the same direction value
	if(direction != existing_direction)
	{
        // attempt to access the gpio pin's direction file
        getGpioPinDirectionFile(fName, edgePin);
        if ((fd = fopen (fName, "w")) == NULL)
        {
            // throw exception
            char errstr[255];
            sprintf (errstr, "Unable to open GPIO direction interface for pin [%d]: %s\n", pin, strerror (errno)) ;
            (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
            return;
        }

        // write the IN/OUT direction to the direction file
        if (direction == DIRECTION_IN)
        {
          fprintf (fd, "in\n") ;
        }
        else if (direction == DIRECTION_OUT)
        {
          fprintf (fd, "out\n") ;
        }
        else if (direction == DIRECTION_HIGH)
        {
          fprintf (fd, "high\n") ;
        }
        else if (direction == DIRECTION_LOW)
        {
          fprintf (fd, "low\n") ;
        }
        else
        {
            // close the direction file
            fclose (fd) ;

            // throw exception
            char errstr[255];
            sprintf (errstr, "Unsupported DIRECTION [%d] for GPIO pin [%d]\n", direction, pin) ;
            (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
            return;
        }

	    // close the direction file
	    fclose (fd) ;
    }
}


/*
 * Class:     com_pi4j_wiringpi_GpioUtil
 * Method:    unexport
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_GpioUtil_unexport
(JNIEnv *env, jclass class, jint pin)
{
	FILE *fd ;
	char fName [GPIO_FN_MAXLEN];

	// validate the pin number
	if(isPinValid(pin) <= 0)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid GPIO pin [%d]\n", pin) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return;
	}

	// get the edge pin number
	int edgePin = getEdgePin(pin);

	// construct the gpio export file path
	getGpioUnexportFile(fName);
	if ((fd = fopen (fName, "w")) == NULL)
	{
		// throw exception
		char errstr[255];
		sprintf (errstr, "Unable to open GPIO unexport interface for pin [%d]: %s\n", pin, strerror (errno)) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return;
	}

	fprintf (fd, "%d\n", edgePin) ;
	fclose (fd) ;
}


/*
 * Class:     com_pi4j_wiringpi_GpioUtil
 * Method:    isExported
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_pi4j_wiringpi_GpioUtil_isExported
  (JNIEnv *env, jclass class, jint pin)
{
	int result;
	char fName [GPIO_FN_MAXLEN] ;

	// validate the pin number
	if(isPinValid(pin) <= 0)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid GPIO pin [%d]\n", pin) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return (jboolean)0;
	}

	// get the edge pin number
	int edgePin = getEdgePin(pin);

	// construct directory path for gpio pin
	getGpioPinDirectory(fName, edgePin);

	// check for exported gpio directory
	result = access(fName, F_OK);

	if (result == 0)
	{
		// is exported
		return (jboolean)1;
	}

	// not exported
	return (jboolean)0;
}


/*
 * Class:     com_pi4j_wiringpi_GpioUtil
 * Method:    setDirection
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_pi4j_wiringpi_GpioUtil_setDirection
(JNIEnv *env, jclass class, jint pin, jint direction)
{
	FILE *fd ;
	char fName [GPIO_FN_MAXLEN] ;

	// validate the pin number
	if(isPinValid(pin) <= 0)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid GPIO pin [%d]\n", pin) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return (jboolean)0;
	}

	// validate the pin direction
	if(direction != DIRECTION_IN &&
	   direction != DIRECTION_OUT &&
	   direction != DIRECTION_HIGH &&
	   direction != DIRECTION_LOW)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid GPIO pin [%d] direction [%d]\n", pin, direction) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return (jboolean)0;
	}

	// get the edge pin number
	int edgePin = getEdgePin(pin);

	// get the pin direction
	int existing_direction = getExistingPinDirection(edgePin);

	// no need to set direction if its already configured with the same direction value
	if(direction == existing_direction)
	{
        // success
        return (jboolean)1;
	}

	// attempt to access the gpio pin's direction file
	getGpioPinDirectionFile(fName, edgePin);
	if ((fd = fopen (fName, "w")) == NULL)
	{
		// throw exception
		char errstr[255];
		sprintf (errstr, "Unable to open GPIO direction interface for pin [%d]: %s\n", pin, strerror (errno)) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return (jboolean)0;
	}

	// write the IN/OUT direction to the direction file
	if (direction == DIRECTION_IN)
	{
	  fprintf (fd, "in\n") ;
	}
	else if (direction == DIRECTION_OUT)
	{
	  fprintf (fd, "out\n") ;
	}
	else if (direction == DIRECTION_HIGH)
	{
	  fprintf (fd, "high\n") ;
	}
	else if (direction == DIRECTION_LOW)
	{
	  fprintf (fd, "low\n") ;
	}
	else
	{
        // close the direction file
        fclose (fd) ;

		// throw exception
		char errstr[255];
		sprintf (errstr, "Unsupported DIRECTION [%d] for GPIO pin [%d]\n", direction, pin) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return JNI_FALSE;
	}

	// close the direction file
	fclose (fd) ;

	// success
	return (jboolean)1;
}

/*
 * Class:     com_pi4j_wiringpi_GpioUtil
 * Method:    getDirection
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioUtil_getDirection
(JNIEnv *env, jclass class, jint pin)
{
	// validate the pin number
	if(isPinValid(pin) <= 0)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid GPIO pin [%d]\n", pin) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return -1;
	}

	// get the edge pin number
	int edgePin = getEdgePin(pin);

	// get the pin direction
	int direction = getExistingPinDirection(edgePin);

	// check for errors
	if(direction < 0)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unable to open GPIO direction interface for pin [%d]: %s\n", pin, strerror (errno)) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return -1;
	}

	return direction;
}


/*
 * Class:     com_pi4j_wiringpi_GpioUtil
 * Method:    setEdgeDetection
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_com_pi4j_wiringpi_GpioUtil_setEdgeDetection
(JNIEnv *env, jclass class, jint pin, jint edge)
{
	FILE *fd ;
	char fName [GPIO_FN_MAXLEN];
	char data[RDBUF_LEN];

	// validate the pin number
	if(isPinValid(pin) <= 0)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid GPIO pin [%d]\n", pin) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return (jboolean)0;
	}

	// validate the pin edge detection option
	if(edge != EDGE_NONE &&
	   edge != EDGE_BOTH &&
	   edge != EDGE_RISING &&
	   edge != EDGE_FALLING)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid GPIO pin [%d] edge condition [%d]\n", pin, edge) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return (jboolean)0;
	}

	// get the edge pin number
	int edgePin = getEdgePin(pin);

	// export gpio pin
	getGpioExportFile(fName);
	if ((fd = fopen (fName, "w")) == NULL)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unable to open GPIO export interface: %s\n", strerror (errno)) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return (jboolean)0;
	}

	// add the pin to the export file
	fprintf (fd, "%d\n", edgePin) ;

	// close the export file
	fclose (fd) ;

    // wait 100 milliseconds
    // for some reason we need to wait a short time after exporting the pin
    // before we can access the direction file; probably to allow for the
    // 'bcm2835_gpiomem' kernel driver enough time to apply the udev rules
    // to grant permisisons to the /sys/class/gpio/gpio%d/* interface files.
    usleep(100000);

	// access the pin direction file and force the pin direction to IN
	getGpioPinDirectionFile(fName, edgePin);
	if ((fd = fopen (fName, "w")) == NULL)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unable to open GPIO direction interface for pin %d: %s\n", pin, strerror (errno)) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return (jboolean)0;
	}

	// set the pin direction to IN
	fprintf (fd, "in\n") ;

	// close the gpio direction file
	fclose (fd) ;

	// construct the gpio edge file path
	getGpioPinEdgeFile(fName, edgePin);

	// open the gpio edge file
	if ((fd = fopen (fName, "w")) == NULL)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unable to open GPIO edge interface for pin %d: %s\n", pin, strerror (errno));
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return (jboolean)0;
	}

	// determine edge mode and write to edge file
	if (edge == EDGE_NONE) fprintf (fd, "none\n") ;
	else if (edge == EDGE_BOTH) fprintf (fd, "both\n") ;
	else if (edge == EDGE_RISING) fprintf (fd, "rising\n") ;
	else if (edge == EDGE_FALLING) fprintf (fd, "falling\n") ;
	else
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid edge mode [%d]. Should be none (0), rising (2), falling (3) or both (1)\n", edge);
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return (jboolean)0;
	}

	// close the gpio edge file
	fclose (fd);

	// read the configured edge mode to verify it was set correctly
    // open the gpio edge file
	if ((fd = fopen (fName, "r")) == NULL)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unable to open GPIO edge interface for pin %d: %s\n", pin, strerror (errno)) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return -1;
	}

	// read the data from the file into the data buffer
	if(fgets(data, RDBUF_LEN, fd) == NULL)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unable to open GPIO edge interface for pin %d: %s\n", pin, strerror (errno)) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return -1;
	}

	// close the gpio edge file
	fclose (fd) ;

	// determine active configured edge mode
	int active_edge;

	if (strncasecmp(data, "none", 4) == 0) { active_edge = EDGE_NONE; }
	else if (strncasecmp(data, "both", 4) == 0) { active_edge = EDGE_BOTH; }
	else if (strncasecmp(data, "rising", 6) == 0) { active_edge = EDGE_RISING; }
	else if (strncasecmp(data, "falling", 7) == 0) { active_edge = EDGE_FALLING; }
	else
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unrecognized mode: %s. Should be 'none', 'rising', 'falling' or 'both'.\n",data);
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return -1;
	}

	// verify active edge matches requested edge
	if(active_edge != edge){
		// throw exception
		char errstr[255];
		sprintf(errstr, "Failed to set GPIO edge for pin %d to [%d]; active edge is [%d]\n", pin, edge, active_edge);
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return -1;
	}

	// success
	return (jboolean)1;
}

/*
 * Class:     com_pi4j_wiringpi_GpioUtil
 * Method:    getEdgeDetection
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioUtil_getEdgeDetection
(JNIEnv *env, jclass class, jint pin)
{
	FILE *fd ;
	char fName [GPIO_FN_MAXLEN] ;
	char data[RDBUF_LEN];

	// validate the pin number
	if(isPinValid(pin) <= 0)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Invalid GPIO pin: %d\n", pin) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return -1;
	}

	// get the edge pin number
	int edgePin = getEdgePin(pin);

	// construct the gpio edge file path
	getGpioPinEdgeFile(fName, edgePin);

	// open the gpio edge file
	if ((fd = fopen (fName, "r")) == NULL)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unable to open GPIO edge interface for pin %d: %s\n", pin, strerror (errno)) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return -1;
	}

	// read the data from the file into the data buffer
	if(fgets(data, RDBUF_LEN, fd) == NULL)
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unable to open GPIO edge interface for pin %d: %s\n", pin, strerror (errno)) ;
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return -1;
	}

	// close the gpio edge file
	fclose (fd) ;

	// determine edge mode
	if (strncasecmp(data, "none", 4) == 0) return EDGE_NONE;
	else if (strncasecmp(data, "both", 4) == 0) return EDGE_BOTH;
	else if (strncasecmp(data, "rising", 6) == 0) return EDGE_RISING;
	else if (strncasecmp(data, "falling", 7) == 0) return EDGE_FALLING;
	else
	{
		// throw exception
		char errstr[255];
		sprintf(errstr, "Unrecognized mode: %s. Should be 'none', 'rising', 'falling' or 'both'.\n",data);
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
		return -1;
	}
}

/*
 * Class:     com_pi4j_wiringpi_GpioUtil
 * Method:    isPinSupported
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioUtil_isPinSupported
(JNIEnv *env, jclass class, jint pin)
{
	// validate the pin number
	return isPinValid(pin);
}

/*
 * This method will return a value of '1' if Privileged access is required.
 * This method will return a value of '0' if Privileged access is NOT required.
 * Privileged access is required if any of the the following conditions are not met:
 *    - You are running with Linux kernel version 4.1.7 or greater
 *    - The Device Tree is enabled
 *    - The 'bcm2835_gpiomem' kernel module loaded.
 *    - Udev rules are configured to permit write access to '/sys/class/gpio/'
 */
int isPrivilegedAccessRequired()
{
    int gpiomem_fd;
    FILE *export_fd;
    char fName [GPIO_FN_MAXLEN] ;

    // check for read/write access to the the /dev/gpiomem' device
    // this device will only exist if the 'bcm2835_gpiomem' kernel model is loaded
    if ((gpiomem_fd = open ("/dev/gpiomem", O_RDWR | O_SYNC | O_CLOEXEC) ) < 0){
      return 1; // Privileged access is required
    }

	// close the gpiomem device file
	close (gpiomem_fd);

	// validate that the export file can be accessed
	getGpioExportFile(fName);
	if ((export_fd = fopen (fName, "w")) == NULL)
	{
	  return 1; // Privileged access is required
	}

	// close export file
	fclose (export_fd) ;

    // Privileged access is NOT required
    return 0;
}

/*
 * Class:     com_pi4j_wiringpi_GpioUtil
 * Method:    isPrivilegedAccessRequired
 * Signature: (I)Z
 *
 * This method will return a value of 'true' if Privileged access is required.
 * This method will return a value of 'false' if Privileged access is NOT required.
 * Privileged access is required if any of the the following conditions are not met:
 *    - You are running with Linux kernel version 4.1.7 or greater
 *    - The Device Tree is enabled
 *    - The 'bcm2835_gpiomem' kernel module loaded.
 *    - Udev rules are configured to permit write access to '/sys/class/gpio/'
 */
JNIEXPORT jboolean JNICALL Java_com_pi4j_wiringpi_GpioUtil_isPrivilegedAccessRequired
(JNIEnv *env, jclass class)
{
    return (jboolean)isPrivilegedAccessRequired();
}

/*
 * Class:     com_pi4j_wiringpi_GpioUtil
 * Method:    enableNonPrivilegedAccess
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_GpioUtil_enableNonPrivilegedAccess
(JNIEnv *env, jclass class)
{
   // first check to see if priviliged access is required
   if(isPrivilegedAccessRequired()){
	  // throw exception
	  char errstr[255];
	  sprintf(errstr, "ERROR; Access to GPIO pins on this system requires priviliged access.") ;
	  (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), errstr);
   }

   // next set the environment variable ''
   setenv(ENVIRON_GPIOMEM, ENVIRON_GPIOMEM_ENABLED, 1);
}

