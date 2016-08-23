/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_io_file_LinuxFile.c
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
#include <fcntl.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <jni.h>
#include <errno.h>
#include <stdint.h>
#include <sys/mman.h>

#include "com_pi4j_io_file_LinuxFile.h"

int directIOCTLStructure
  (int fd, unsigned long command, uint8_t *data, uint32_t *offsetMap, uint32_t offsetSize);

jobject boxedErrno(JNIEnv *env);

/* Source for com_pi4j_io_file_LinuxFile */

/*
 * Class:     com_pi4j_io_file_LinuxFile
 * Method:    i2cIOCTL
 * Signature: (IJI)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_io_file_LinuxFile_directIOCTL
  (JNIEnv *env, jclass obj, jint fd, jlong command, jlong value)
{
    int response = ioctl(fd, command, value);

    if(response < 0) {
        response = -errno;
    }

    return response;
}

/*
 * Class:     com_pi4j_io_file_LinuxFile
 * Method:    mmap
 * Signature: (IIIII)Ljava.lang.Object;
 */
JNIEXPORT jobject JNICALL Java_com_pi4j_io_file_LinuxFile_mmap
  (JNIEnv *env, jclass obj, jint fd, jint length, jint prot, jint flags, jint offset)
{
    void *addr = mmap(NULL, length, prot, flags, fd, offset);

    if(addr == MAP_FAILED)
        return boxedErrno(env);

    return (*env)->NewDirectByteBuffer(env, addr, length);
}

/*
 * Class:     com_pi4j_io_file_LinuxFile
 * Method:    munmapDirect
 * Signature: (Ljava.nio.ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_io_file_LinuxFile_munmapDirect
  (JNIEnv *env, jclass obj, jobject data)
{
    int response;

    uint8_t *buffer = (uint8_t *)((*env)->GetDirectBufferAddress(env, data));
    jlong capacity = (*env)->GetDirectBufferCapacity(env, data);

    response = munmap(buffer, (size_t)capacity);

    if(response < 0) {
        response = -errno;
    }

    return response;
}

/*
 * Class:     com_pi4j_io_file_LinuxFile
 * Method:    directIOCTLStructure
 * Signature: (IJLjava.nio.ByteBuffer;ILjava.nio.IntBuffer;II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_io_file_LinuxFile_directIOCTLStructure
  (JNIEnv *env, jclass obj, jint fd, jlong command, jobject data, jint dataOffset, jobject offsetMap, jint offsetMapOffset, jint offsetCapacity)
{
    int response;

    uint8_t *dataBuffer = (uint8_t *)((*env)->GetDirectBufferAddress(env, data));
    uint32_t *offsetBuffer = (uint32_t *)((*env)->GetDirectBufferAddress(env, offsetMap));

    response = directIOCTLStructure(fd, command, dataBuffer + dataOffset, offsetBuffer + offsetMapOffset, offsetCapacity);

    return response;
}

jobject boxedErrno(JNIEnv *env) {
    jclass cls = (*env)->FindClass(env, "java/lang/Integer");
    jmethodID methodID = (*env)->GetMethodID(env, cls, "<init>", "(I)V");

    return (*env)->NewObject(env, cls, methodID, errno);
}

int directIOCTLStructure
  (int fd, unsigned long command, uint8_t *data, uint32_t *offsetMap, uint32_t offsetSize)
{
    uint32_t i;
    int response;

    //iterate through offsets, convert and apply pointers
    for(i = 0 ; i < offsetSize ; i += 2) {
      uint32_t pointerOffset = offsetMap[i];
      uint32_t pointingOffset = offsetMap[i + 1];

      void **ptr = (void **)(data + pointerOffset);

      //add in base ptr here to offset
      *ptr = data + pointingOffset;
    }

    response = ioctl(fd, command, data);

    if(response < 0) {
        response = -errno;
    }

    return response;
}
