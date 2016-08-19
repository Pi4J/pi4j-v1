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
#include <linux/i2c-dev.h>
#include <fcntl.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <jni.h>
#include <errno.h>
#include <stdint.h>

#include "com_pi4j_io_file_LinuxFile.h"

int directIOCTLStructure
  (int fd, unsigned long command, uint8_t *data, uint32_t *offsetMap, uint32_t offsetSize);

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
 * Method:    directIOCTLStructure
 * Signature: (IJLjava.nio.ByteBuffer;Ljava.nio.IntBuffer;)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_io_file_LinuxFile_directIOCTLStructure
  (JNIEnv *env, jclass obj, jint fd, jlong command, jobject data, jobject offsetMap)
{
    int response;

    uint8_t *dataBuffer = (uint8_t *)((*env)->GetDirectBufferAddress(env, data));
    uint32_t *offsetBuffer = (uint32_t *)((*env)->GetDirectBufferAddress(env, offsetMap));
    jlong offsetCapacity = (*env)->GetDirectBufferCapacity(env, offsetMap);

    response = directIOCTLStructure(fd, command, dataBuffer, offsetBuffer, (uint32_t)offsetCapacity);

    return response;
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
