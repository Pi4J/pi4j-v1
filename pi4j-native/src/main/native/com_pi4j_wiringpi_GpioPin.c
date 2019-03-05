/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_GpioPin.c
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
#include <wiringPi.h>
#include "com_pi4j_wiringpi_GpioPin.h"

#define GPIO_CLASS_DIRECTORY    "/sys/class/gpio"
#define GPIO_EXPORT_FILE        GPIO_CLASS_DIRECTORY "/export"
#define GPIO_UNEXPORT_FILE      GPIO_CLASS_DIRECTORY "/unexport"
#define GPIO_PIN_DIRECTORY      GPIO_CLASS_DIRECTORY "/gpio%d"
#define GPIO_PIN_DIRECTION_FILE GPIO_PIN_DIRECTORY "/direction"
#define GPIO_PIN_EDGE_FILE      GPIO_PIN_DIRECTORY "/edge"
#define GPIO_PIN_VALUE_FILE     GPIO_PIN_DIRECTORY "/value"

int wiringpi_detected_model;
int wiringpi_detected_revision;
int wiringpi_detected_maker;

/**
 * --------------------------------------------------------
 * GLOBAL WIRING PI MODE STATE
 * --------------------------------------------------------
 */
int wiringpi_init_mode = WPI_MODE_UNINITIALISED;

/**
 * --------------------------------------------------------
 * INITIALIZE THE GPIO PIN CLASS
 * --------------------------------------------------------
 * AUTO DETECTED THE BOARD IDENTIFICATION INFO
 */
void GpioPin_Init(){
    // some wiringPi ports are old and don't support the 'piBoardId()' method; I'm looking at you LeMaker BananaPi!
    #ifdef PI_MODEL_UNKNOWN
    int mem, overVolted ;

	// get the board identifier that we are running on
    piBoardId (&wiringpi_detected_model, &wiringpi_detected_revision, &mem, &wiringpi_detected_maker, &overVolted) ;
    #endif
}

/**
 * --------------------------------------------------------
 * GET EDGE GPIO PIN
 * --------------------------------------------------------
 */
int getEdgePin(int pin)
{
	// validate lower bounds
	if(pin < 0)
		return -1;

    // in GPIO mode, the edge pin is the same as the GPIO pin number
    if(wiringpi_init_mode == WPI_MODE_GPIO){
      return pin;
    }

	// validate upper bounds
	if(pin >= MAX_GPIO_PINS)
		return -1;

    // check for macro definition for Compute Module, some older versions of WiringPi may be missing this macro.
    // (I'm looking at you LeMaker!)
    #ifndef PI_MODEL_CM
    return wpiPinToGpio(pin);
    #else

	// return the edge pin index
	// (will return -1 for invalid pin)
    if (wiringpi_detected_model == PI_MODEL_CM){
        return pin;
    }
    else{
        return wpiPinToGpio(pin);
    }

    #endif
}


/**
 * --------------------------------------------------------
 * GET GPIO PIN INDEX
 * --------------------------------------------------------
 */
int isPinValid(int pin)
{
	// validate lower bounds
	if(pin < 0)
		return 0;

	// validate upper bounds
	if(pin > MAX_GPIO_PINS)
		return 0;

	// if the pin index is zero or greater, then the pin is valid
	// (will return 0 for invalid pin)
	if(getEdgePin(pin) >= 0)
		return 1;
	else
		return 0;
}


/**
 * --------------------------------------------------------
 * GET THE GPIO EXPORT FILE PATH FOR THIS PLATFORM/BOARD
 * --------------------------------------------------------
 *
 * ARGS: file <character array pointer> to write the path to
 *
 * RETURN VALUE: If successful, the total number of characters written is returned excluding
 * the null-character appended at the end of the string, otherwise a negative number is
 * returned in case of failure.
 */
int getGpioExportFile(char *file){

    // all platforms and models use default "gpio" class
    return sprintf(file, GPIO_EXPORT_FILE);
}

/**
 * --------------------------------------------------------
 * GET THE GPIO UNEXPORT FILE PATH FOR THIS PLATFORM/BOARD
 * --------------------------------------------------------
 *
 * ARGS: file <character array pointer> to write the path to
 *
 * RETURN VALUE: If successful, the total number of characters written is returned excluding
 * the null-character appended at the end of the string, otherwise a negative number is
 * returned in case of failure.
 */
int getGpioUnexportFile(char *file){
    // all platforms and models use default "gpio" class
    return sprintf(file, GPIO_UNEXPORT_FILE);
}

/**
 * --------------------------------------------------------
 * GET THE GPIO PIN DIRECTORY PATH FOR THIS PLATFORM/BOARD
 * --------------------------------------------------------
 *
 * ARGS: file <character array pointer> to write the path to
 *       pin <integer> the GPIO (edge) pin address
 *
 * RETURN VALUE: If successful, the total number of characters written is returned excluding
 * the null-character appended at the end of the string, otherwise a negative number is
 * returned in case of failure.
 */
int getGpioPinDirectory(char *file, int pin){
    // all platforms and models use default "gpio" class
    return sprintf(file, GPIO_PIN_DIRECTORY, pin);
}

/**
 * -------------------------------------------------------------
 * GET THE GPIO PIN DIRECTION FILE PATH FOR THIS PLATFORM/BOARD
 * -------------------------------------------------------------
 *
 * ARGS: file <character array pointer> to write the path to
 *       pin <integer> the GPIO (edge) pin address
 *
 * RETURN VALUE: If successful, the total number of characters written is returned excluding
 * the null-character appended at the end of the string, otherwise a negative number is
 * returned in case of failure.
 */
int getGpioPinDirectionFile(char *file, int pin){
    // all platforms and models use default "gpio" class
    return sprintf(file, GPIO_PIN_DIRECTION_FILE, pin);
}

/**
 * ------------------------------------------------------------------
 * GET THE GPIO PIN EDGE (TRIGGER) FILE PATH FOR THIS PLATFORM/BOARD
 * ------------------------------------------------------------------
 *
 * ARGS: file <character array pointer> to write the path to
 *       pin <integer> the GPIO (edge) pin address
 *
 * RETURN VALUE: If successful, the total number of characters written is returned excluding
 * the null-character appended at the end of the string, otherwise a negative number is
 * returned in case of failure.
 */
int getGpioPinEdgeFile(char *file, int pin){
    // all platforms and models use default "gpio" class
    return sprintf(file, GPIO_PIN_EDGE_FILE, pin);
}

/**
 * --------------------------------------------------------------
 * GET THE GPIO PIN VALUE/DATA FILE PATH FOR THIS PLATFORM/BOARD
 * --------------------------------------------------------------
 *
 * ARGS: file <character array pointer> to write the path to
 *       pin <integer> the GPIO (edge) pin address
 *
 * RETURN VALUE: If successful, the total number of characters written is returned excluding
 * the null-character appended at the end of the string, otherwise a negative number is
 * returned in case of failure.
 */
int getGpioPinValueFile(char *file, int pin){
    // all platforms and models use default "gpio" class
    return sprintf(file, GPIO_PIN_VALUE_FILE, pin);
}

