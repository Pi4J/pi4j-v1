/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_Nes.c
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
#include <jni.h>
#include <piNes.h>
#include "com_pi4j_wiringpi_Nes.h"

/* Source for com_pi4j_wiringpi_Nes */

/*
 * Class:     com_pi4j_wiringpi_Nes
 * Method:    setupNesJoystick
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Nes_setupNesJoystick
  (JNIEnv *env, jclass class, jint dPin, jint cPin, jint lPin)
{
	return setupNesJoystick(dPin, cPin, lPin);
}

/*
 * Class:     com_pi4j_wiringpi_Nes
 * Method:    readNesJoystick
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Nes_readNesJoystick
  (JNIEnv *env, jclass class, jint joystick)
{
	return readNesJoystick(joystick);
}
