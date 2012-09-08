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
