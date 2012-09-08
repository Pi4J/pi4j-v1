#include <jni.h>
#include <wiringPi.h>
#include <wiringShift.h>
#include "com_pi4j_wiringpi_Shift.h"

/* Source for com_pi4j_wiringpi_Shift */

/*
 * Class:     com_pi4j_wiringpi_Shift
 * Method:    shiftIn
 * Signature: (BBB)B
 */
JNIEXPORT jbyte JNICALL Java_com_pi4j_wiringpi_Shift_shiftIn
  (JNIEnv *env, jclass class, jbyte dPin, jbyte cPin, jbyte order)
{
	return shiftIn(dPin, cPin, order);
}

/*
 * Class:     com_pi4j_wiringpi_Shift
 * Method:    shiftOut
 * Signature: (BBBB)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Shift_shiftOut
  (JNIEnv *env, jclass class, jbyte dPin, jbyte cPin, jbyte order, jbyte val)
{
	shiftOut(dPin, cPin, order, val);
}
