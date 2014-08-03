/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_Shift.c  
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
#include <jni.h>
#include <wiringPi.h>
#include <wiringShift.h>
#include "com_pi4j_wiringpi_Shift.h"

/* Source for com_pi4j_wiringpi_Shift */

/*
 * Class:     com_pi4j_wiringpi_Shift
 * Method:    shiftIn
 * Signature: (BBB)B
 */
JNIEXPORT jbyte JNICALL Java_com_pi4j_wiringpi_Shift_shiftIn
  (JNIEnv *env, jclass class, jbyte dPin, jbyte cPin, jbyte order)
{
	return shiftIn(dPin, cPin, order);
}

/*
 * Class:     com_pi4j_wiringpi_Shift
 * Method:    shiftOut
 * Signature: (BBBB)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Shift_shiftOut
  (JNIEnv *env, jclass class, jbyte dPin, jbyte cPin, jbyte order, jbyte val)
{
	shiftOut(dPin, cPin, order, val);
}
