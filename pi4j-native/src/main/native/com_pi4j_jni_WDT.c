/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_jni_WDT.c
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <jni.h>
#include <linux/watchdog.h> 

#include "com_pi4j_jni_WDT.h"

/*
 * Class:     com_pi4j_jni_WDT
 * Method:    open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_WDT_open
(JNIEnv *env, jclass obj, jstring file) {
    char fileName[256];
    int len = (*env)->GetStringLength(env, file);
    (*env)->GetStringUTFRegion(env, file, 0, len, fileName);
    return open(fileName, O_WRONLY);
}

/*
 * Class:     com_pi4j_jni_WDT
 * Method:    close
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_WDT_close
  (JNIEnv *env, jclass obj, jint fd) {
    return close(fd);
}

/*
 * Class:     com_pi4j_jni_WDT
 * Method:    disable
 * Signature: (I)V
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_WDT_disable
(JNIEnv *env, jclass obj, jint fd) {
    return write(fd, "V", 1);    
}

/*
 * Class:     com_pi4j_jni_WDT
 * Method:    getTimeOut
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_WDT_getTimeOut
(JNIEnv *env, jclass obj, jint fd) {
    int timeout;
    int ret = ioctl(fd, WDIOC_GETTIMEOUT, &timeout);
    if(ret<0) {
        return ret;
    }    
    return timeout;
}

/*
 * Class:     com_pi4j_jni_WDT
 * Method:    setTimeOut
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_WDT_setTimeOut
  (JNIEnv *env, jclass obj, jint fd, jint timeout) {
    return ioctl(fd, WDIOC_SETTIMEOUT, &timeout);
}

/*
 * Class:     com_pi4j_jni_WDT
 * Method:    ping
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_WDT_ping
  (JNIEnv *env, jclass obj, jint fd) {
    return ioctl(fd, WDIOC_KEEPALIVE, 0);
}
