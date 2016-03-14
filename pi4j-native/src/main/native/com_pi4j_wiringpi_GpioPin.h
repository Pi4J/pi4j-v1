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
#ifndef _Included_com_pi4j_wiringpi_GpioPin
#define _Included_com_pi4j_wiringpi_GpioPin
#ifdef __cplusplus
extern "C" {
#endif


// constants
#define	MAX_GPIO_PINS   46L


#ifdef AML_GPIO
#define GPIO_CLASS               "/sys/class/aml_gpio"
#else
#define GPIO_CLASS               "/sys/class/gpio"
#endif
#define GPIO_EXPORT_FILE        GPIO_CLASS "/export"
#define GPIO_UNEXPORT_FILE      GPIO_CLASS "/unexport"
#define GPIO_PIN_DIRECTORY      GPIO_CLASS "/gpio%d"
#define GPIO_PIN_DIRECTION_FILE GPIO_PIN_DIRECTORY "/direction"
#define GPIO_PIN_EDGE_FILE      GPIO_PIN_DIRECTORY "/edge"
#define GPIO_PIN_VALUE_FILE     GPIO_PIN_DIRECTORY "/value"

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


#ifdef __cplusplus
}
#endif
#endif
