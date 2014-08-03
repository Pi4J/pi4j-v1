/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_Lcd.c  
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
#include <stdint.h>
#include <wiringPi.h>
#include <lcd.h>
#include "com_pi4j_wiringpi_Lcd.h"

/* Source for com_pi4j_wiringpi_Lcd */

/*
 * Class:     com_pi4j_wiringpi_Lcd
 * Method:    lcdInit
 * Signature: (IIIIIIIIIIIII)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Lcd_lcdInit
  (JNIEnv *env, jclass class, jint rows, jint cols, jint bits, jint rs, jint strb, jint d0, jint d1, jint d2, jint d3, jint d4, jint d5, jint d6, jint d7)
{
	return lcdInit(rows, cols, bits, rs, strb, d0, d1, d2, d3, d4, d5, d6, d7);
}

/*
 * Class:     com_pi4j_wiringpi_Lcd
 * Method:    lcdHome
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Lcd_lcdHome
  (JNIEnv *env, jclass class, jint handle)
{
	lcdHome(handle);
}

/*
 * Class:     com_pi4j_wiringpi_Lcd
 * Method:    lcdClear
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Lcd_lcdClear
  (JNIEnv *env, jclass class, jint handle)
{
	lcdClear(handle);
}

/*
 * Class:     com_pi4j_wiringpi_Lcd
 * Method:    lcdPosition
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Lcd_lcdPosition
  (JNIEnv *env, jclass class, jint handle, jint x, jint y)
{
	lcdPosition(handle, x, y);
}

/*
 * Class:     com_pi4j_wiringpi_Lcd
 * Method:    lcdPutchar
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Lcd_lcdPutchar
  (JNIEnv *env, jclass class, jint handle, jbyte data)
{
	lcdPutchar(handle, data);
}

/*
 * Class:     com_pi4j_wiringpi_Lcd
 * Method:    lcdPuts
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Lcd_lcdPuts
  (JNIEnv *env, jclass class, jint handle, jstring data)
{
	char datachararr[1024];
	int len = (*env)->GetStringLength(env, data);
	(*env)->GetStringUTFRegion(env, data, 0, len, datachararr);
	lcdPuts(handle, datachararr);
}
