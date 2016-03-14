/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_Gertboard.c
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

#include <jni.h>
#include <gertboard.h>
#include "com_pi4j_wiringpi_Gertboard.h"

/* Source for com_pi4j_wiringpi_Gertboard */

/*
 * Class:     com_pi4j_wiringpi_Gertboard
 * Method:    gertboardAnalogWrite
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gertboard_gertboardAnalogWrite
  (JNIEnv *env, jclass class, jint channel, jint value)
{
	gertboardAnalogWrite(channel, value);
}

/*
 * Class:     com_pi4j_wiringpi_Gertboard
 * Method:    gertboardAnalogRead
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gertboard_gertboardAnalogRead
  (JNIEnv *env, jclass class, jint channel)
{
	return gertboardAnalogRead(channel);
}

/*
 * Class:     com_pi4j_wiringpi_Gertboard
 * Method:    gertboardSPISetup
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gertboard_gertboardSPISetup
  (JNIEnv *env, jclass class)
{
	return gertboardSPISetup();
}


/*
 * Class:     com_pi4j_wiringpi_Gertboard
 * Method:    gertboardAnalogSetup
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gertboard_gertboardAnalogSetup
  (JNIEnv *env, jclass class, jint pinBase)
{
	return gertboardAnalogSetup(pinBase);
}
