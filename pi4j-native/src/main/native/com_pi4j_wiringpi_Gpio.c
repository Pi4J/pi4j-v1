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
 * Copyright (C) 2012 - 2019 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

#include <jni.h>
#include <wiringPi.h>
#include "com_pi4j_wiringpi_GpioPin.h"
#include "com_pi4j_wiringpi_Gpio.h"
#include "com_pi4j_wiringpi_GpioInterrupt.h"

// java ISR callback variables
jclass isr_callback_class;
jmethodID isr_callback_method;

/* Source for com_pi4j_wiringpi_Gpio */

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    wiringPiSetup
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_wiringPiSetup
  (JNIEnv *env, jclass obj)
{
	wiringpi_init_mode = WPI_MODE_PINS;
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
    wiringpi_init_mode = WPI_MODE_GPIO_SYS;
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
	wiringpi_init_mode = WPI_MODE_GPIO;
	return wiringPiSetupGpio();
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    wiringPiSetupPhys
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_wiringPiSetupPhys
  (JNIEnv *env, jclass obj)
{
    wiringpi_init_mode = WPI_MODE_PHYS;
    return wiringPiSetupPhys();
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    pinMode
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_pinMode
(JNIEnv *env, jclass obj, jint pin, jint mode)
{
	pinMode(pin, mode);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    pinModeAlt
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_pinModeAlt
(JNIEnv *env, jclass obj, jint pin, jint mode)
{
	pinModeAlt(pin, mode);
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
 * Method:    analogRead
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_analogRead
  (JNIEnv *env, jclass obj, jint pin)
{
    return analogRead(pin);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    analogWrite
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_analogWrite
  (JNIEnv *env, jclass obj, jint pin, jint value)
{
    analogWrite(pin, value);
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
 * Method:    micros
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_pi4j_wiringpi_Gpio_micros
  (JNIEnv *env, jclass obj)
{
    return micros();
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

void CallbackWrapperFunc(int pin)
{
    // validate pin range
    if(pin > MAX_GPIO_PINS){
        printf("NATIVE (wiringPiISR) ERROR; CallbackWrapperFunc pin number exceeds MAX_GPIO_PINS.\n");
        return;
    }

    // ensure that the JVM exists
    if(gpio_callback_jvm == NULL){
        printf("NATIVE (wiringPiISR) ERROR; CallbackWrapperFunc 'gpio_callback_jvm' is NULL.\n");
        return;
    }

    // ensure the ISR callback class is available
    if (isr_callback_class == NULL){
        printf("NATIVE (wiringPiISR) ERROR; CallbackWrapperFunc 'isr_callback_class' is NULL.\n");
        return;
    }

    // ensure the ISR callback method is available
    if (isr_callback_method == NULL){
        printf("NATIVE (wiringPiISR) ERROR; CallbackWrapperFunc 'isr_callback_class' is NULL.\n");
        return;
    }

    // attached to JVM thread
    JNIEnv *env;
    (*gpio_callback_jvm)->AttachCurrentThread(gpio_callback_jvm, (void **)&env, NULL);

    // clear any exceptions on the stack
    (*env)->ExceptionClear(env);

    // invoke callback to java state method to notify event listeners
    (*env)->CallStaticVoidMethod(env, isr_callback_class, isr_callback_method, (jint)pin);

    // clear any user caused exceptions on the stack
    if((*env)->ExceptionCheck(env)){
      (*env)->ExceptionClear(env);
    }

    // detach from thread
    (*gpio_callback_jvm)->DetachCurrentThread(gpio_callback_jvm);
}

void cwf_0()  { CallbackWrapperFunc(0);  }
void cwf_1()  { CallbackWrapperFunc(1);  }
void cwf_2()  { CallbackWrapperFunc(2);  }
void cwf_3()  { CallbackWrapperFunc(3);  }
void cwf_4()  { CallbackWrapperFunc(4);  }
void cwf_5()  { CallbackWrapperFunc(5);  }
void cwf_6()  { CallbackWrapperFunc(6);  }
void cwf_7()  { CallbackWrapperFunc(7);  }
void cwf_8()  { CallbackWrapperFunc(8);  }
void cwf_9()  { CallbackWrapperFunc(9);  }
void cwf_10() { CallbackWrapperFunc(10); }
void cwf_11() { CallbackWrapperFunc(11); }
void cwf_12() { CallbackWrapperFunc(12); }
void cwf_13() { CallbackWrapperFunc(13); }
void cwf_14() { CallbackWrapperFunc(14); }
void cwf_15() { CallbackWrapperFunc(15); }
void cwf_16() { CallbackWrapperFunc(16); }
void cwf_17() { CallbackWrapperFunc(17); }
void cwf_18() { CallbackWrapperFunc(18); }
void cwf_19() { CallbackWrapperFunc(19); }
void cwf_20() { CallbackWrapperFunc(20); }
void cwf_21() { CallbackWrapperFunc(21); }
void cwf_22() { CallbackWrapperFunc(22); }
void cwf_23() { CallbackWrapperFunc(23); }
void cwf_24() { CallbackWrapperFunc(24); }
void cwf_25() { CallbackWrapperFunc(25); }
void cwf_26() { CallbackWrapperFunc(26); }
void cwf_27() { CallbackWrapperFunc(27); }
void cwf_28() { CallbackWrapperFunc(28); }
void cwf_29() { CallbackWrapperFunc(29); }
void cwf_30() { CallbackWrapperFunc(30); }
void cwf_31() { CallbackWrapperFunc(31); }
void cwf_32() { CallbackWrapperFunc(32); }
void cwf_33() { CallbackWrapperFunc(33); }
void cwf_34() { CallbackWrapperFunc(34); }
void cwf_35() { CallbackWrapperFunc(35); }
void cwf_36() { CallbackWrapperFunc(36); }
void cwf_37() { CallbackWrapperFunc(37); }
void cwf_38() { CallbackWrapperFunc(38); }
void cwf_39() { CallbackWrapperFunc(39); }
void cwf_40() { CallbackWrapperFunc(40); }
void cwf_41() { CallbackWrapperFunc(41); }
void cwf_42() { CallbackWrapperFunc(42); }
void cwf_43() { CallbackWrapperFunc(43); }
void cwf_44() { CallbackWrapperFunc(44); }
void cwf_45() { CallbackWrapperFunc(45); }
void cwf_46() { CallbackWrapperFunc(46); }
void cwf_47() { CallbackWrapperFunc(47); }
void cwf_48() { CallbackWrapperFunc(48); }
void cwf_49() { CallbackWrapperFunc(49); }
void cwf_50() { CallbackWrapperFunc(50); }

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    _wiringPiISR
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio__1wiringPiISR
  (JNIEnv *env, jclass obj, jint pin, jint mode)
{
    //printf("NATIVE (wiringPiISR) LISTEN FOR INTERRUPTS ON PIN: %d.\n", pin);

    // ensure requested pin in in valid range
    if(pin > MAX_GPIO_PINS)
    {
        printf("NATIVE (wiringPiISR) ERROR; unsupported pin number; exceeds MAX_GPIO_PINS.\n");
        return -997;
    }

    // if the ISR callback class has not previsouly been configiured, then establish it now
    if (isr_callback_class == NULL){
        jclass cls;

        // search the attached java environment for the 'Gpio' class
        cls = (*env)->FindClass(env, "com/pi4j/wiringpi/Gpio");
        if (cls == NULL)
        {
            // expected class not found
            printf("NATIVE (wiringPiISR) ERROR; Gpio class not found.\n");
            return JNI_ERR;
        }

        // use weak global ref to allow C class to be unloaded
        isr_callback_class = (*env)->NewWeakGlobalRef(env, cls);
        if (isr_callback_class == NULL)
        {
    	    // unable to create weak reference to java class
    	    printf("NATIVE (wiringPiISR) ERROR; Java class reference is NULL.\n");
            return JNI_ERR;
        }
    }

    // lookup and cache the static method ID for the 'isrCallback' callback
    isr_callback_method = (*env)->GetStaticMethodID(env, isr_callback_class, "isrCallback", "(I)V");
    if (isr_callback_method == NULL)
    {
    	// callback method could not be found in attached java class
    	printf("NATIVE (wiringPiISR) ERROR; Static method 'Gpio.isrCallback(pin)' could not be found.\n");
        return JNI_ERR;
    }

    // setup the real wiringPiISR function with explicit callback method reference
    switch(pin){
        case 0:  { return wiringPiISR(pin, mode, &cwf_0);  }
        case 1:  { return wiringPiISR(pin, mode, &cwf_1);  }
        case 2:  { return wiringPiISR(pin, mode, &cwf_2);  }
        case 3:  { return wiringPiISR(pin, mode, &cwf_3);  }
        case 4:  { return wiringPiISR(pin, mode, &cwf_4);  }
        case 5:  { return wiringPiISR(pin, mode, &cwf_5);  }
        case 6:  { return wiringPiISR(pin, mode, &cwf_7);  }
        case 7:  { return wiringPiISR(pin, mode, &cwf_7);  }
        case 8:  { return wiringPiISR(pin, mode, &cwf_8);  }
        case 9:  { return wiringPiISR(pin, mode, &cwf_9);  }
        case 10: { return wiringPiISR(pin, mode, &cwf_10); }
        case 11: { return wiringPiISR(pin, mode, &cwf_11); }
        case 12: { return wiringPiISR(pin, mode, &cwf_12); }
        case 13: { return wiringPiISR(pin, mode, &cwf_13); }
        case 14: { return wiringPiISR(pin, mode, &cwf_14); }
        case 15: { return wiringPiISR(pin, mode, &cwf_15); }
        case 16: { return wiringPiISR(pin, mode, &cwf_16); }
        case 17: { return wiringPiISR(pin, mode, &cwf_17); }
        case 18: { return wiringPiISR(pin, mode, &cwf_18); }
        case 19: { return wiringPiISR(pin, mode, &cwf_19); }
        case 20: { return wiringPiISR(pin, mode, &cwf_20); }
        case 21: { return wiringPiISR(pin, mode, &cwf_21); }
        case 22: { return wiringPiISR(pin, mode, &cwf_22); }
        case 23: { return wiringPiISR(pin, mode, &cwf_23); }
        case 24: { return wiringPiISR(pin, mode, &cwf_24); }
        case 25: { return wiringPiISR(pin, mode, &cwf_25); }
        case 26: { return wiringPiISR(pin, mode, &cwf_26); }
        case 27: { return wiringPiISR(pin, mode, &cwf_27); }
        case 28: { return wiringPiISR(pin, mode, &cwf_28); }
        case 29: { return wiringPiISR(pin, mode, &cwf_29); }
        case 30: { return wiringPiISR(pin, mode, &cwf_30); }
        case 31: { return wiringPiISR(pin, mode, &cwf_31); }
        case 32: { return wiringPiISR(pin, mode, &cwf_32); }
        case 33: { return wiringPiISR(pin, mode, &cwf_33); }
        case 34: { return wiringPiISR(pin, mode, &cwf_34); }
        case 35: { return wiringPiISR(pin, mode, &cwf_35); }
        case 36: { return wiringPiISR(pin, mode, &cwf_36); }
        case 37: { return wiringPiISR(pin, mode, &cwf_37); }
        case 38: { return wiringPiISR(pin, mode, &cwf_38); }
        case 39: { return wiringPiISR(pin, mode, &cwf_39); }
        case 40: { return wiringPiISR(pin, mode, &cwf_40); }
        case 41: { return wiringPiISR(pin, mode, &cwf_41); }
        case 42: { return wiringPiISR(pin, mode, &cwf_42); }
        case 43: { return wiringPiISR(pin, mode, &cwf_43); }
        case 44: { return wiringPiISR(pin, mode, &cwf_44); }
        case 45: { return wiringPiISR(pin, mode, &cwf_45); }
        case 46: { return wiringPiISR(pin, mode, &cwf_46); }
        case 47: { return wiringPiISR(pin, mode, &cwf_47); }
        case 48: { return wiringPiISR(pin, mode, &cwf_48); }
        case 49: { return wiringPiISR(pin, mode, &cwf_49); }
        case 50: { return wiringPiISR(pin, mode, &cwf_50); }
    }

    // we should never get here under valid conditions
    printf("NATIVE (wiringPiISR) ERROR; unsupported pin number.\n");
    return -996;
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

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    physPinToGpio
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_physPinToGpio
  (JNIEnv *env, jclass obj, jint physPin)
{
    return physPinToGpio(physPin);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    digitalWriteByte
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_digitalWriteByte
  (JNIEnv *env, jclass obj, jint value)
{
    digitalWriteByte(value);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    pwmSetMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_pwmSetMode
  (JNIEnv *env, jclass obj, jint mode)
{
    pwmSetMode(mode);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    pwmSetRange
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_pwmSetRange
  (JNIEnv *env, jclass obj, jint range)
{
    pwmSetRange((unsigned int)range);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    pwmSetClock
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_pwmSetClock
  (JNIEnv *env, jclass obj, jint divisor)
{
    pwmSetClock(divisor);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    setPadDrive
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_wiringpi_Gpio_setPadDrive
  (JNIEnv *env, jclass obj, jint group, jint value)
{
    setPadDrive(group, value);
}

/*
 * Class:     com_pi4j_wiringpi_Gpio
 * Method:    getAlt
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_Gpio_getAlt
  (JNIEnv *env, jclass obj, jint pin)
{
    return (jint) getAlt(pin);
}
