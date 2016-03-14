/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_GpioPin.c
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

#include <wiringPi.h>
#include "com_pi4j_wiringpi_GpioPin.h"

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

	// validate upper bounds
	if(pin >= MAX_GPIO_PINS)
		return -1;

    // check for macro definion for Compute Module, some older versions of WiringPi may be missing this macro.
    // (I'm looking at you LeMaker!)
    #ifndef PI_MODEL_CM
    return wpiPinToGpio(pin);
    #else

    int model, rev, mem, maker, overVolted ;

	// return the edge pin index
	// (will return -1 for invalid pin)
    piBoardId (&model, &rev, &mem, &maker, &overVolted) ;
    if (model == PI_MODEL_CM){
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
