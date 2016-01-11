/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_GpioInterrupt.h  
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
/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_pi4j_wiringpi_GpioInterrupt */

#ifndef _Included_com_pi4j_wiringpi_GpioInterrupt
#define _Included_com_pi4j_wiringpi_GpioInterrupt
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_pi4j_wiringpi_GpioInterrupt
 * Method:    initPoll
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioInterrupt_initPoll
(JNIEnv *, jclass, jint);

/*
 * Class:     com_pi4j_wiringpi_GpioInterrupt
 * Method:    pollPinStateChange
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioInterrupt_pollPinStateChange
  (JNIEnv *, jclass, jint, jint, jint);

/*
 * Class:     com_pi4j_wiringpi_GpioInterrupt
 * Method:    closePoll
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioInterrupt_closePoll
(JNIEnv *, jclass, jint);

  
#ifdef __cplusplus
}
#endif
#endif
