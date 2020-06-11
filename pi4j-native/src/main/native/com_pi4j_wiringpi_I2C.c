/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_I2C.c
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
#include <wiringPiI2C.h>
#include "com_pi4j_wiringpi_I2C.h"

/* Source for com_pi4j_wiringpi_I2C */

/*
 * Class:     com_pi4j_wiringpi_I2C
 * Method:    wiringPiI2CSetup
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_I2C_wiringPiI2CSetup
  (JNIEnv *env, jclass class, jint devId)
{
    return wiringPiI2CSetup(devId);
}

/*
 * Class:     com_pi4j_wiringpi_I2C
 * Method:    wiringPiI2CRead
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_I2C_wiringPiI2CRead
  (JNIEnv *env, jclass class, jint handle)
{
    return wiringPiI2CRead(handle);
}

/*
 * Class:     com_pi4j_wiringpi_I2C
 * Method:    wiringPiI2CWrite
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_I2C_wiringPiI2CWrite
  (JNIEnv *env, jclass class, jint handle, jint data)
{
    return wiringPiI2CWrite(handle, data);
}

/*
 * Class:     com_pi4j_wiringpi_I2C
 * Method:    wiringPiI2CWriteReg8
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_I2C_wiringPiI2CWriteReg8
  (JNIEnv *env, jclass class, jint handle, jint reg, jint data)
{
    return wiringPiI2CWriteReg8(handle, reg, data);
}

/*
 * Class:     com_pi4j_wiringpi_I2C
 * Method:    wiringPiI2CWriteReg16
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_I2C_wiringPiI2CWriteReg16
  (JNIEnv *env, jclass class, jint handle, jint reg, jint data)
{
    return wiringPiI2CWriteReg16(handle, reg, data);
}

/*
 * Class:     com_pi4j_wiringpi_I2C
 * Method:    wiringPiI2CReadReg8
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_I2C_wiringPiI2CReadReg8
  (JNIEnv *env, jclass class, jint handle, jint reg)
{
    return wiringPiI2CReadReg8(handle, reg);
}

/*
 * Class:     com_pi4j_wiringpi_I2C
 * Method:    wiringPiI2CReadReg16
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_I2C_wiringPiI2CReadReg16
  (JNIEnv *env, jclass class, jint handle, jint reg)
{
    return wiringPiI2CReadReg16(handle, reg);
}
