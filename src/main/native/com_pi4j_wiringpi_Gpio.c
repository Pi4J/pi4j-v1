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
