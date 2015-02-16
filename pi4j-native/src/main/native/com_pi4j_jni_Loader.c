/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_jni_Loader.c  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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
#include <stdint.h>
#include <stdlib.h>
#include <jni.h>
#include <string.h>

#include "com_pi4j_jni_SerialInterrupt.h"
#include "com_pi4j_wiringpi_GpioInterrupt.h"

/**
 * --------------------------------------------------------
 * JNI LIBRARY LOADED
 * --------------------------------------------------------
 * capture java references to be used later for callback methods
 */
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved)
{
	JNIEnv *env;

	//printf("\nNATIVE (JNI LOADER) LOADING\n");

	// ensure that the calling environment is a supported JNI version
    if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2))
    {
    	// JNI version not supported
    	printf("NATIVE (JNI LOADER) ERROR; JNI version not supported.\n");
        return JNI_ERR;
    }

    jint ret;

    // call the JNI_OnLoad method inside the serial interrupt class
    ret = SerialInterrupt_JNI_OnLoad(jvm);
    if(ret < 0){
        printf("NATIVE (JNI LOADER) ERROR; SerialInterrupt failed to load.\n");
        return ret;
    }

    // call the JNI_OnLoad method inside the GPIO interrupt class
    ret = GpioInterrupt_JNI_OnLoad(jvm);
    if(ret < 0){
        printf("NATIVE (JNI LOADER) ERROR; GpioInterrupt failed to load.\n");
        return ret;
    }

	// return JNI version; success
	return JNI_VERSION_1_2;
}


/**
 * --------------------------------------------------------
 * JNI LIBRARY UNLOADED
 * --------------------------------------------------------
 * stop all monitoring threads and clean up references
 */
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *jvm, void *reserved)
{

	//printf("\nNATIVE (JNI LOADER) UNLOADING\n");

    // call the JNI_OnLoad method inside the serial interrupt class
    SerialInterrupt_JNI_OnUnload(jvm);

    // call the JNI_OnLoad method inside the GPIO interrupt class
    GpioInterrupt_JNI_OnUnload(jvm);

	return;
}
