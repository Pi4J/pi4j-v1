#include <jni.h>
#include <wiringPiSPI.h>
#include "com_pi4j_wiringpi_Spi.h"

/* Source for com_pi4j_wiringpi_Spi */

/*
 * Class:     com_pi4j_wiringpi_Spi
 * Method:    wiringPiSPIGetFd
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Spi_wiringPiSPIGetFd
  (JNIEnv *env, jclass class, jint channel)
{
	return wiringPiSPIGetFd(channel);
}

/*
 * Class:     com_pi4j_wiringpi_Spi
 * Method:    wiringPiSPIDataRW
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Spi_wiringPiSPIDataRW
  (JNIEnv *env, jclass class, jint channel, jstring data, jint length)
{
	char datachararr[2048];
	int len = (*env)->GetStringLength(env, data);
	(*env)->GetStringUTFRegion(env, data, 0, len, datachararr);
	return wiringPiSPIDataRW(channel, (unsigned char *)datachararr, length);
}


/*
 * Class:     com_pi4j_wiringpi_Spi
 * Method:    wiringPiSPISetup
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Spi_wiringPiSPISetup
  (JNIEnv *env, jclass class, jint channel, jint speed)
{
	return wiringPiSPISetup(channel, speed);
}
