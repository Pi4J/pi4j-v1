/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_Shift.c
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
