/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_GpioPin.h
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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
