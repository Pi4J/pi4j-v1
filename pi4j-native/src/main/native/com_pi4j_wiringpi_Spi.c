/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_Spi.c
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
#include <jni.h>
#include <stdlib.h>
#include <wiringPiSPI.h>
#include "com_pi4j_wiringpi_Spi.h"
#include "com_pi4j_jni_Exception.h"

/* Source for com_pi4j_wiringpi_Spi */

/*
 * Class:     com_pi4j_wiringpi_Spi
 * Method:    wiringPiSPIGetFd
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Spi_wiringPiSPIGetFd
  (JNIEnv *env, jclass class, jint channel)
{
	return wiringPiSPIGetFd(channel);
}

/*
 * Class:     com_pi4j_wiringpi_Spi
 * Method:    wiringPiSPIDataRW
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Spi_wiringPiSPIDataRW__ILjava_lang_String_2I
  (JNIEnv *env, jclass class, jint channel, jstring data, jint length)
{
	char *buffer;

    // get the number of total possible bytes in the UTF string
	int bufferSize = (*env)->GetStringUTFLength(env, data);

	// dynamically allocate buffer size
    buffer = malloc(bufferSize);

	// copy UTF string bytes to buffer
	(*env)->GetStringUTFRegion(env, data, 0, length, buffer);

	// SPI transfer (read/write) the data buffer
	jint result = wiringPiSPIDataRW(channel, (unsigned char *)buffer, bufferSize);

	// create a return string from the data buffer returned after SPI transfer (read/write)
	jstring returnString = (*env)->NewStringUTF(env, buffer);
    data = returnString;

    // free allocated buffer memory
    free(buffer);

    // return success/failure
	return result;
}

/*
 * Class:     com_pi4j_wiringpi_Spi
 * Method:    wiringPiSPIDataRW
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Spi_wiringPiSPIDataRW__I_3BI
(JNIEnv *env, jclass class, jint channel, jbyteArray data, jint length)
{
    int i;
	unsigned char *buffer;

	// dynamically allocate buffer size
    buffer = malloc(length);

	// copy the bytes from the data array argument into a native unsigned character buffer
    jbyte *body = (*env)->GetByteArrayElements(env, data, 0);
    for (i = 0; i < length; i++) {
    	buffer[i] = (unsigned char) body[i];
    }

	jint result = wiringPiSPIDataRW(channel, (unsigned char *)buffer, length);

	// copy the resulting buffer bytes back into the data array argument
	for (i = 0; i < length; i++) {
		body[i] = buffer[i];
	}
	(*env)->ReleaseByteArrayElements(env, data, body, 0);

    // free allocated buffer memory
    free(buffer);

	return result;
}

/*
 * Class:     com_pi4j_wiringpi_Spi
 * Method:    wiringPiSPIDataRW
 * Signature: (I[SI)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Spi_wiringPiSPIDataRW__I_3SI
(JNIEnv *env, jclass class, jint channel, jshortArray data, jint length)
{
    int i;
	unsigned char *buffer;

	// dynamically allocate buffer size
    buffer = malloc(length);

	// copy the bytes (short values) from the data array argument into a native unsigned character buffer
	jshort *body = (*env)->GetShortArrayElements(env, data, 0);
	for (i = 0; i < length; i++) {
	    // cast to unsigned char here since we have short, which is 16-bit and signed, so we need 8-bit unsigned
	    buffer[i] = (unsigned char)body[i];
	}

	jint result = wiringPiSPIDataRW(channel, (unsigned char *)buffer, length);

	// copy the resulting buffer bytes back into the data array argument
	for (i = 0; i < length; i++) {
		body[i] = buffer[i];
	}
	(*env)->ReleaseShortArrayElements(env, data, body, 0);

    // free allocated buffer memory
    free(buffer);

	return result;
}

/*
 * Class:     com_pi4j_wiringpi_Spi
 * Method:    wiringPiSPISetup
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Spi_wiringPiSPISetup
  (JNIEnv *env, jclass class, jint channel, jint speed)
{
	return wiringPiSPISetup(channel, speed);
}

/*
 * Class:     com_pi4j_wiringpi_Spi
 * Method:    wiringPiSPISetupMode
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Spi_wiringPiSPISetupMode
  (JNIEnv *env, jclass class, jint channel, jint speed, jint mode)
{
	#ifdef WIRINGPI_SPI_SETUP_MODE_UNSUPPORTED
	throwUnsupportedOperationException(env,
	    "This implementation of WiringPi does not support method 'wiringPiSPISetupMode'.");
	return -1;
	#else
	return wiringPiSPISetupMode(channel, speed, mode);
	#endif
}
