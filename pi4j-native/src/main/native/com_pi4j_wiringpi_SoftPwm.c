/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_SoftPwm.c
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
#include <wiringPi.h>
#include <softPwm.h>
#include "com_pi4j_wiringpi_SoftPwm.h"

/* Source for com_pi4j_wiringpi_SoftPwm */

/*
 * Class:     com_pi4j_wiringpi_SoftPwm
 * Method:    softPwmCreate
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_SoftPwm_softPwmCreate
  (JNIEnv *env, jclass class, jint pin, jint value, jint range)
{
	return softPwmCreate(pin, value, range);
}

/*
 * Class:     com_pi4j_wiringpi_SoftPwm
 * Method:    softPwmWrite
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_SoftPwm_softPwmWrite
  (JNIEnv *env, jclass class, jint pin, jint value)
{
	softPwmWrite(pin, value);
}

/*
 * Class:     com_pi4j_wiringpi_SoftPwm
 * Method:    softPwmStop
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_SoftPwm_softPwmStop
  (JNIEnv *env, jclass class, jint pin)
{
	softPwmStop(pin);
}
