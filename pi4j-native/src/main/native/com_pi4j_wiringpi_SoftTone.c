/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_SoftTone.c  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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
#include <softTone.h>
#include "com_pi4j_wiringpi_SoftTone.h"
#include "com_pi4j_jni_Exception.h"

/* Source for com_pi4j_wiringpi_SoftTone */

/*
 * Class:     com_pi4j_wiringpi_SoftTone
 * Method:    softToneCreate
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_SoftTone_softToneCreate
  (JNIEnv *env, jclass class, jint pin)
{
    return softToneCreate(pin);
}

/*
 * Class:     com_pi4j_wiringpi_SoftTone
 * Method:    softToneWrite
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_SoftTone_softToneWrite
  (JNIEnv *env, jclass class, jint pin, jint frequency)
{
    softToneWrite(pin, frequency);
}

/*
 * Class:     com_pi4j_wiringpi_SoftTone
 * Method:    softToneStop
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_SoftTone_softToneStop
  (JNIEnv *env, jclass class, jint pin)
{
	#ifdef WIRINGPI_SOFTTONE_STOP_UNSUPPORTED
	throwUnsupportedOperationException(env,
	    "This implementation of WiringPi does not support method 'softToneStop'.");
	#else
	softToneStop(pin);
	#endif
}
