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
 * Copyright (C) 2012 - 2017 Pi4J
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


