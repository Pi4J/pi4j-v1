/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_Serial.c
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
#include <wiringSerial.h>
#include "com_pi4j_wiringpi_Serial.h"

/* Source for com_pi4j_wiringpi_Serial */

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialOpen
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Serial_serialOpen
  (JNIEnv *env, jclass obj, jstring device, jint baud)
{
	char devchararr[256];
	int len = (*env)->GetStringLength(env, device);
	(*env)->GetStringUTFRegion(env, device, 0, len, devchararr);

    // return file descriptor
	return serialOpen(devchararr, baud);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialClose
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialClose
  (JNIEnv *env, jclass obj, jint fd)
{
    serialFlush(fd);
	serialClose(fd);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialFlush
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialFlush
  (JNIEnv *env, jclass obj, jint fd)
{
	serialFlush(fd);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialPutchar
 * Signature: (IC)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialPutchar
  (JNIEnv *env, jclass obj, jint fd, jchar data)
{
	serialPutchar(fd, data);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialPutByte
 * Signature: (IB)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialPutByte
  (JNIEnv *env, jobject obj, jint fd, jbyte data)
{
	serialPutchar(fd, (unsigned char) data);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialPutBytes
 * Signature: (I[BI)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialPutBytes
(JNIEnv *env, jclass obj, jint fd, jbyteArray data, jint length)
{
    int i;
    jbyte *body = (*env)->GetByteArrayElements(env, data, 0);
    for (i = 0; i < length; i++) {
	    serialPutchar(fd, (unsigned char) body[i]);
    }
	(*env)->ReleaseByteArrayElements(env, data, body, 0);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialPuts
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialPuts
  (JNIEnv *env, jclass obj, jint fd, jstring data)
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
  (JNIEnv *env, jclass obj, jint fd)
{
	return serialDataAvail(fd);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialGetchar
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Serial_serialGetchar
  (JNIEnv *env, jclass obj, jint fd)
{
	return serialGetchar(fd);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialGetByte
 * Signature: (I)B
 */
JNIEXPORT jbyte JNICALL Java_com_pi4j_wiringpi_Serial_serialGetByte
  (JNIEnv *env, jclass obj, jint fd)
{
    int result = serialGetchar(fd);
    if(result >= 0){
	    return (unsigned char) serialGetchar(fd);
	}
	else{
	    return (unsigned char)0;
	}
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialGetBytes
 * Signature: (II)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_pi4j_wiringpi_Serial_serialGetBytes
  (JNIEnv *env, jclass obj, jint fd, jint length)
{
    int num_available;
    num_available = serialDataAvail(fd);

    // reduce length if it exceeds the number available
    if(length > num_available){
        length = num_available;
    }

    jbyte result[length];
    int i;
    for (i = 0; i < length; i++) {
        result[i] = (unsigned char)serialGetchar(fd);
    }

    jbyteArray javaResult = (*env)->NewByteArray(env, length);
    (*env)->SetByteArrayRegion(env, javaResult, 0, length, result);
    return javaResult;
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialGetAvailableBytes
 * Signature: (I)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_pi4j_wiringpi_Serial_serialGetAvailableBytes
  (JNIEnv *env, jclass obj, jint fd)
{
    int length;
    length = serialDataAvail(fd);
    jbyte result[length];

    int i;
    for (i = 0; i < length; i++) {
        result[i] = (unsigned char)serialGetchar(fd);
    }

    jbyteArray javaResult = (*env)->NewByteArray(env, length);
    (*env)->SetByteArrayRegion(env, javaResult, 0, length, result);
    return javaResult;
}
