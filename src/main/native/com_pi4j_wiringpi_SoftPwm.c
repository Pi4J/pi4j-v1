#include <jni.h>
#include <wiringPi.h>
#include <softPwm.h>
#include "com_pi4j_wiringpi_SoftPwm.h"

/* Source for com_pi4j_wiringpi_SoftPwm */

/*
 * Class:     com_pi4j_wiringpi_SoftPwm
 * Method:    softPwmCreate
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_SoftPwm_softPwmCreate
  (JNIEnv *env, jclass class, jint pin, jint value, jint range)
{
	return softPwmCreate(pin, value, range);
}

/*
 * Class:     com_pi4j_wiringpi_SoftPwm
 * Method:    softPwmWrite
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_SoftPwm_softPwmWrite
  (JNIEnv *env, jclass class, jint pin, jint value)
{
	softPwmWrite(pin, value);
}
