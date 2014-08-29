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
