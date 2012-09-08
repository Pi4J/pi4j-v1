#include <jni.h>
#include <wiringSerial.h>
#include "com_pi4j_wiringpi_Serial.h"

/* Source for com_pi4j_wiringpi_Serial */

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialOpen
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Serial_serialOpen
  (JNIEnv *env, jobject obj, jstring device, jint baud)
{
	char devchararr[256];
	int len = (*env)->GetStringLength(env, device);
	(*env)->GetStringUTFRegion(env, device, 0, len, devchararr);
	return serialOpen(devchararr, baud);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialClose
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialClose
  (JNIEnv *env, jobject obj, jint fd)
{
	serialClose(fd);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialFlush
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialFlush
  (JNIEnv *env, jobject obj, jint fd)
{
	serialFlush(fd);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialPutchar
 * Signature: (IC)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialPutchar
  (JNIEnv *env, jobject obj, jint fd, jchar data)
{
	serialPutchar(fd, data);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialPuts
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Serial_serialPuts
  (JNIEnv *env, jobject obj, jint fd, jstring data)
{
	char datachararr[2048];
	int len = (*env)->GetStringLength(env, data);
	(*env)->GetStringUTFRegion(env, data, 0, len, datachararr);
	serialPuts(fd, datachararr);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialDataAvail
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Serial_serialDataAvail
  (JNIEnv *env, jobject obj, jint fd)
{
	return serialDataAvail(fd);
}

/*
 * Class:     com_pi4j_wiringpi_Serial
 * Method:    serialGetchar
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Serial_serialGetchar
  (JNIEnv *env, jobject obj, jint fd)
{
	return serialGetchar(fd);
}
