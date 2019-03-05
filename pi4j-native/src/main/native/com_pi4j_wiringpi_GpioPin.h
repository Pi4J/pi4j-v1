/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_GpioPin.h
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
#ifndef _Included_com_pi4j_wiringpi_GpioPin
#define _Included_com_pi4j_wiringpi_GpioPin
#ifdef __cplusplus
extern "C" {
#endif

#include <wiringPi.h>

// constants
#define	MAX_GPIO_PINS   46L
#define GPIO_FN_MAXLEN  200

/**
 * --------------------------------------------------------
 * GLOBAL WIRING PI MODE STATE
 * --------------------------------------------------------
 */
extern int wiringpi_init_mode;

/**
 * --------------------------------------------------------
 * GET GPIO PIN INDEX
 * --------------------------------------------------------
 */
int getEdgePin(int);

/**
 * --------------------------------------------------------
 * DETERMINE IF GPIO PIN IS VALID
 * --------------------------------------------------------
 */
int isPinValid(int);

/**
 * --------------------------------------------------------
 * INITIALIZE THE GPIO PIN CLASS
 * --------------------------------------------------------
 * AUTO DETECTED THE BOARD IDENTIFICATION INFO
 */
void GpioPin_Init();

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
int getGpioExportFile(char *file);

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
int getGpioUnexportFile(char *file);

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
int getGpioPinDirectory(char *file, int pin);

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
int getGpioPinDirectionFile(char *file, int pin);

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
int getGpioPinEdgeFile(char *file, int pin);

/* --------------------------------------------------------------
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
int getGpioPinValueFile(char *file, int pin);

#ifdef __cplusplus
}
#endif
#endif
