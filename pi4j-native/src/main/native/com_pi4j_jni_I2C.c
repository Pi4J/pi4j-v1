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
#include <stdlib.h>
#include <linux/i2c-dev.h>
#include <fcntl.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <jni.h>

#include "com_pi4j_jni_I2C.h"

/* Source for com_pi4j_jni_I2C */

unsigned char buf[257];	

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cOpen
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cOpen
  (JNIEnv *env, jobject obj, jstring device)
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
  (JNIEnv *env, jobject obj, jint fd)
{
	return close(fd);
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cWriteByteDirect
 * Signature: (IIB)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cWriteByteDirect
  (JNIEnv *env, jobject obj, jint fd, jint deviceAddress, jbyte b)  
{
    int response = ioctl(fd, I2C_SLAVE, deviceAddress);

    if (response < 0) {
        return response - 10000;
	}

	buf[0] = b;
    
    response = write(fd, buf, 1);
	if (response != 1) {
	    return response - 20000;
	}
	
    return 0;
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cWriteBytesDirect
 * Signature: (IIII[B)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cWriteBytesDirect
  (JNIEnv *env, jobject obj, jint fd, jint deviceAddress, jint size, jint offset, jbyteArray bytes)
{
    int i;

    int response = ioctl(fd, I2C_SLAVE, deviceAddress);
    if (response < 0) {
        return response - 10000;
	}

    jbyte *body = (*env)->GetByteArrayElements(env, bytes, 0);
    for (i = 0; i < size; i++) {
      buf[i] = body[i + offset];
    }
    (*env)->ReleaseByteArrayElements(env, bytes, body, 0);
    
    response = write(fd, buf, size);
	if (response != size) {
	    return response - 20000;
	}
	
    return 0;
}


/*
 * Class:     com_pi4j_wiringpi_I2C
 * Method:    i2cWriteByte
 * Signature: (IIIB)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cWriteByte
  (JNIEnv *env, jobject obj, jint fd, jint deviceAddress, jint localAddress, jbyte b)
  
{
    int response = ioctl(fd, I2C_SLAVE, deviceAddress);

    if (response < 0) {
        return response - 10000;
	}

	buf[0] = localAddress;
	buf[1] = b;
    
    response = write(fd, buf, 2);
	if (response != 2) {
	    return response - 20000;
	}
	
    return 0;
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cWriteBytes
 * Signature: (IIIII[B)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cWriteBytes
  (JNIEnv *env, jobject obj, jint fd, jint deviceAddress, jint localAddress, jint size, jint offset, jbyteArray bytes)
  
{
    int i;

    int response = ioctl(fd, I2C_SLAVE, deviceAddress);
    if (response < 0) {
        return response - 10000;
	}

	buf[0] = localAddress;
    
    jbyte *body = (*env)->GetByteArrayElements(env, bytes, 0);
    for (i = 0; i < size; i++) {
      buf[i + 1] = body[i + offset];
    }
    (*env)->ReleaseByteArrayElements(env, bytes, body, 0);
    
    response = write(fd, buf, size + 1);
	if (response != size + 1) {
	    return response - 20000;
	}
	
    return 0;
}


/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cReadByteDirect
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cReadByteDirect
  (JNIEnv *env, jobject obj, jint fd, jint deviceAddress)
{
    int response = ioctl(fd, I2C_SLAVE, deviceAddress);
    if (response < 0) {
        return response - 10000;
	}

    response = read(fd, buf, 1);
    if (response != 1) {
	    return response - 30000;
	}

    response = (int)buf[0];

    return response;
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cReadBytesDirect
 * Signature: (IIII[B)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cReadBytesDirect
  (JNIEnv *env, jobject obj, jint fd, jint deviceAddress, jint size, jint offset, jbyteArray bytes)
{
    int i;
    
    int response = ioctl(fd, I2C_SLAVE, deviceAddress);
    if (response < 0) {
        return response - 10000;
    }

    response = read(fd, buf, size);
    if (response > 0) {

        jbyte *body = (*env)->GetByteArrayElements(env, bytes, 0);
        for (i = 0; i < size; i++) {
            body[i + offset] = buf[i];
        }
        (*env)->ReleaseByteArrayElements(env, bytes, body, 0);
    }

    return response;
}


/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cReadByte
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cReadByte
  (JNIEnv *env, jobject obj, jint fd, jint deviceAddress, jint localAddress)
{
    int response = ioctl(fd, I2C_SLAVE, deviceAddress);
    if (response < 0) {
        return response - 10000;
    }

    buf[0] = localAddress;												
	
    response = write(fd, buf, 1);
    if (response != 1) {
        return response - 20000;
    }
	
    response = ioctl(fd, I2C_SLAVE, deviceAddress);
    if (response < 0) {
        return response - 10000;
    }

    response = read(fd, buf, 1);
    if (response != 1) {
	    return response - 30000;
    }

    response = (int)buf[0];

    return response;
}

/*
 * Class:     com_pi4j_jni_I2C
 * Method:    i2cReadBytes
 * Signature: (IIIII[B)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cReadBytes
  (JNIEnv *env, jobject obj, jint fd, jint deviceAddress, jint localAddress, jint size, jint offset, jbyteArray bytes)
{
    int i;
    
    int response = ioctl(fd, I2C_SLAVE, deviceAddress);
    if (response < 0) {
        return response - 10000;
    }

    buf[0] = localAddress;
	
    response = write(fd, buf, 1);
    if (response != 1) {
        return response - 20000;
    }
	
    response = ioctl(fd, I2C_SLAVE, deviceAddress);
    if (response < 0) {
        return response - 10000;
    }

    response = read(fd, buf, size);
    if (response > 0) {

        jbyte *body = (*env)->GetByteArrayElements(env, bytes, 0);
        for (i = 0; i < size; i++) {
            body[i + offset] = buf[i];
        }
        (*env)->ReleaseByteArrayElements(env, bytes, body, 0);
    }

    return response;
}


/*
Class:     com_pi4j_jni_I2C
Method:    i2cWriteAndReadBytes
Signature: (IIII[BII[B)I
*/
JNIEXPORT jint JNICALL Java_com_pi4j_jni_I2C_i2cWriteAndReadBytes
(JNIEnv *env, jobject obj, jint fd, jint deviceAddress, jint writeSize, jint writeOffset, jbyteArray writeBytes, jint readSize, jint readOffset, jbyteArray readBytes)
{
    int i;

    int response = ioctl(fd, I2C_SLAVE, deviceAddress);
    if (response < 0) {
        return response - 10000;
    }

    // writing writeSize bytes
    jbyte *body = (*env)->GetByteArrayElements(env, writeBytes, 0);
    for (i = 0; i < writeSize; i++) {
        buf[i] = body[i + writeOffset];
    }
    (*env)->ReleaseByteArrayElements(env, writeBytes, body, 0);

    response = write(fd, buf, writeSize);
    if (response != writeSize) {
        return response - 20000;
    }

    // reading bytes
    response = ioctl(fd, I2C_SLAVE, deviceAddress);
    if (response < 0) {
        return response - 10000;
    }

    response = read(fd, buf, readSize);
    if (response > 0) {
        body = (*env)->GetByteArrayElements(env, readBytes, 0);
        for (i = 0; i < readSize; i++) {
            body[i + readOffset] = buf[i];
        }
        (*env)->ReleaseByteArrayElements(env, readBytes, body, 0);
    }

    return response;
}
