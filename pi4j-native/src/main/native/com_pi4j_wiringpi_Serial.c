/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_Serial.c  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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
#include <wiringSerial.h>
#include "com_pi4j_wiringpi_Serial.h"

/* Source for com_pi4j_wiringpi_Serial */

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialOpen
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Serial_serialOpen
  (JNIEnv *env, jobject obj, jstring device, jint baud)
{
	char devchararr[256];
	int len = (*env)->GetStringLength(env, device);
	(*env)->GetStringUTFRegion(env, device, 0, len, devchararr);
	return serialOpen(devchararr, baud);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialClose
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialClose
  (JNIEnv *env, jobject obj, jint fd)
{
	serialClose(fd);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialFlush
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialFlush
  (JNIEnv *env, jobject obj, jint fd)
{
	serialFlush(fd);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialPutchar
 * Signature: (IC)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialPutchar
  (JNIEnv *env, jobject obj, jint fd, jchar data)
{
	serialPutchar(fd, data);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialPuts
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialPuts
  (JNIEnv *env, jobject obj, jint fd, jstring data)
{
	char datachararr[2048];
	int len = (*env)->GetStringUTFLength(env, data);
	(*env)->GetStringUTFRegion(env, data, 0, len, datachararr);
	serialPuts(fd, datachararr);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialDataAvail
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Serial_serialDataAvail
  (JNIEnv *env, jobject obj, jint fd)
{
	return serialDataAvail(fd);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialGetchar
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Serial_serialGetchar
  (JNIEnv *env, jobject obj, jint fd)
{
	return serialGetchar(fd);
}
