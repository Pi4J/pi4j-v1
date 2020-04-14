/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_I2C.c
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
