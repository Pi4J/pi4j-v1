/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_Spi.c  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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
#include <wiringPiSPI.h>
#include "com_pi4j_wiringpi_Spi.h"

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
	char buffer[2048];
	int len = (*env)->GetStringUTFLength(env, data);
	(*env)->GetStringUTFRegion(env, data, 0, len, buffer);
	jint result = wiringPiSPIDataRW(channel, (unsigned char *)buffer, length);
	jstring returnString = (*env)->NewStringUTF(env, buffer);
    data = returnString;

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
    unsigned char buffer[2048];

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
    unsigned char buffer[2048];

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
	return wiringPiSPISetupMode(channel, speed, mode);
}
