/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_jni_I2C.c
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
#include <stdlib.h>
#include <linux/i2c-dev.h>
#include <fcntl.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <jni.h>
#include <errno.h>

#include "com_pi4j_jni_I2C.h"

/* Source for com_pi4j_jni_I2C */

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cSlaveSelect
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cSlaveSelect
 (JNIEnv *env, jclass obj, jint fd, jint deviceAddress)
{
   int response = ioctl(fd, I2C_SLAVE, deviceAddress);

   if (response < 0) {
       return -errno - 10000;
   }

   return response;
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cOpen
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cOpen
  (JNIEnv *env, jclass obj, jstring device)
{
    char fileName[256];
    int len = (*env)->GetStringLength(env, device);
    (*env)->GetStringUTFRegion(env, device, 0, len, fileName);

    return open(fileName, O_RDWR);
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cClose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cClose
  (JNIEnv *env, jclass obj, jint fd)
{
    return close(fd);
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cWriteByteDirect
 * Signature: (IB)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cWriteByteDirect
  (JNIEnv *env, jclass obj, jint fd, jbyte b)
{
    int response;
    
    response = write(fd, &b, 1);
    if(response != 1) {
        return -errno - 20000;
    }
    
    return 0;
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cWriteBytesDirect
 * Signature: (III[B)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cWriteBytesDirect
  (JNIEnv *env, jclass obj, jint fd, jint size, jint offset, jbyteArray bytes)
{
    int response;

    jbyte *body = (*env)->GetByteArrayElements(env, bytes, 0);

    response = write(fd, body + offset, size);

    (*env)->ReleaseByteArrayElements(env, bytes, body, 0);

    if (response != size) {
        return -errno - 20000;
    }
    
    return 0;
}


/*
 * Class:     com_pi4j_wiringpi_I2C
 * Method:    i2cWriteByte
 * Signature: (IIB)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cWriteByte
  (JNIEnv *env, jclass obj, jint fd, jint localAddress, jbyte b)
  
{
    int response;
    unsigned char buf[2];

    buf[0] = localAddress;
    buf[1] = b;
    
    response = write(fd, buf, 2);
    if (response != 2) {
        return -errno - 20000;
    }
    
    return 0;
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cWriteBytes
 * Signature: (IIII[B)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cWriteBytes
  (JNIEnv *env, jclass obj, jint fd, jint localAddress, jint size, jint offset, jbyteArray bytes)
  
{
    int i;
    unsigned char buf[257];
    int response;

    if(size > 256) {
      return -E2BIG;
    }

    buf[0] = localAddress;
    
    jbyte *body = (*env)->GetByteArrayElements(env, bytes, 0);
    for (i = 0; i < size; i++) {
      buf[i + 1] = body[i + offset];
    }
    (*env)->ReleaseByteArrayElements(env, bytes, body, 0);
    
    response = write(fd, buf, size + 1);
    if (response != size + 1) {
        return -errno - 20000;
    }
    
    return 0;
}


/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cReadByteDirect
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cReadByteDirect
  (JNIEnv *env, jclass obj, jint fd)
{
    unsigned char data;
    int response;

    response = read(fd, &data, 1);
    if (response != 1) {
        return -errno - 30000;
    }

    return data;
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cReadBytesDirect
 * Signature: (III[B)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cReadBytesDirect
  (JNIEnv *env, jclass obj, jint fd, jint size, jint offset, jbyteArray bytes)
{
    int response;

    jbyte *body = (*env)->GetByteArrayElements(env, bytes, 0);

    response = read(fd, body + offset, size);

    (*env)->ReleaseByteArrayElements(env, bytes, body, 0);

    if (response < 0) {
        return -errno - 30000;
    }

    return response;
}


/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cReadByte
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cReadByte
  (JNIEnv *env, jclass obj, jint fd, jint localAddress)
{
    unsigned char data;
    int response;

    response = write(fd, &localAddress, 1);
    if (response != 1) {
        return -errno - 20000;
    }

    response = read(fd, &data, 1);
    if (response != 1) {
        return -errno - 30000;
    }

    return data;
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cReadBytes
 * Signature: (IIII[B)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cReadBytes
  (JNIEnv *env, jclass obj, jint fd, jint localAddress, jint size, jint offset, jbyteArray bytes)
{
    int response;

    response = write(fd, &localAddress, 1);
    if (response != 1) {
        return -errno - 20000;
    }

    jbyte *body = (*env)->GetByteArrayElements(env, bytes, 0);

    response = read(fd, body + offset, size);

    (*env)->ReleaseByteArrayElements(env, bytes, body, 0);

    if (response < 0) {
        return -errno - 30000;
    }

    return response;
}

/*
Class:     com_pi4j_jni_I2C
Method:    i2cWriteAndReadBytes
Signature: (III[BII[B)I
*/
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cWriteAndReadBytes
  (JNIEnv *env, jclass obj, jint fd, jint writeSize, jint writeOffset, jbyteArray writeBytes, jint readSize, jint readOffset, jbyteArray readBytes)
{
    jbyte *body;
    int response;

    // writing writeSize bytes
    body = (*env)->GetByteArrayElements(env, writeBytes, 0);

    response = write(fd, body + writeOffset, writeSize);

    (*env)->ReleaseByteArrayElements(env, writeBytes, body, 0);

    if (response != writeSize) {
        return -errno - 20000;
    }

    // reading bytes
    body = (*env)->GetByteArrayElements(env, readBytes, 0);

    response = read(fd, body + readOffset, readSize);

    (*env)->ReleaseByteArrayElements(env, readBytes, body, 0);

    if (response < 0) {
        return -errno - 30000;
    }

    return response;
}

