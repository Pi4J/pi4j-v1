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
