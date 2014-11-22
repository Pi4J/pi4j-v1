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
 * Signature: (I[S)[S
 */
JNIEXPORT jshortArray JNICALL Java_com_pi4j_wiringpi_Spi_wiringPiSPIDataRW__I_3S
(JNIEnv *env, jclass class, jint channel, jshortArray data)
{
	// copy the bytes from the data array argument into a native character buffer
	jshort *body = (*env)->GetShortArrayElements(env, data, 0);
	jint length = (*env)->GetArrayLength(env, data); 
 
	unsigned char buffer[2048];
	int i;
	for (i = 0; i < length; i++) {
	    // cast to unsigned char here since we have short, which is 16-bit and signed, so we need 8-bit unsigned
	    buffer[i] = (unsigned char)body[i];
	}
	(*env)->ReleaseShortArrayElements(env, data, body, 0);

	jint resultCode = wiringPiSPIDataRW(channel, (unsigned char *)buffer, length);	
	if (resultCode < 0) 
	{
	    return NULL; // indicate some error happened
	} 
	else 
	{
	    jshort result[length];
	    // copy the resulting buffer bytes into new array we will return from here
	    for (i = 0; i < length; i++) 
	    {
		result[i] = buffer[i];
	    }
	
	    jshortArray javaResult = (*env)->NewShortArray(env, length);
	    (*env)->SetShortArrayRegion(env, javaResult, 0, length, result);
	    return javaResult;
	}
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
