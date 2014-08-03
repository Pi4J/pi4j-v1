/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_Gpio.c  
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
#include "com_pi4j_wiringpi_Gpio.h"

/* Source for com_pi4j_wiringpi_Gpio */

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    wiringPiSetup
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_wiringPiSetup
  (JNIEnv *env, jclass obj)
{
	return wiringPiSetup();
}
  

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    wiringPiSetupSys
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_wiringPiSetupSys
(JNIEnv *env, jclass obj)
{
	return wiringPiSetupSys();
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    wiringPiSetupGpio
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_wiringPiSetupGpio
(JNIEnv *env, jclass obj)
{
	return wiringPiSetupGpio();
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    pinMode
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_pinMode
(JNIEnv *env, jclass obj, jint pin, jint mode)
{
	return pinMode(pin, mode);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    pullUpDnControl
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_pullUpDnControl
(JNIEnv *env, jclass obj, jint pin, jint pud)
{
	pullUpDnControl(pin, pud);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    digitalWrite
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_digitalWrite
(JNIEnv *env, jclass obj, jint pin, jint value)
{
	digitalWrite(pin, value);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    pwmWrite
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_pwmWrite
(JNIEnv *env, jclass obj, jint pin, jint value)
{
	pwmWrite(pin, value);
}


/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    digitalRead
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_digitalRead
(JNIEnv *env, jclass obj, jint pin)
{
	return digitalRead(pin);
}


/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    delay
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_delay
(JNIEnv *env, jclass obj, jlong milliseconds)
{
	delay(milliseconds);
}


/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    millis
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_pi4j_wiringpi_Gpio_millis
(JNIEnv *env, jclass class)
{
	return millis();
}


/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    delayMicroseconds
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_delayMicroseconds
(JNIEnv *env, jclass obj, jlong howLong)
{
	delayMicroseconds(howLong);
}


/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    piHiPri
 * Signature: (I)V
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_piHiPri
(JNIEnv *env, jclass class, jint priority)
{
	return piHiPri(priority);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    waitForInterrupt
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_waitForInterrupt
(JNIEnv *env, jclass class, jint pin, jint timeOut)
{
	return waitForInterrupt(pin, timeOut);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    piBoardRev
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_piBoardRev
(JNIEnv *env, jclass class)
{
	return piBoardRev();
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    wpiPinToGpio
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_wpiPinToGpio
(JNIEnv *env, jclass class, jint wpiPin)
{
	return wpiPinToGpio(wpiPin);
}
