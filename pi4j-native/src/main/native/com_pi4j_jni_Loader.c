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
 * Copyright (C) 2012 - 2016 Pi4J
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
