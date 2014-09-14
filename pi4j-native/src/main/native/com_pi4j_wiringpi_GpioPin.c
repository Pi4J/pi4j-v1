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
 * Copyright (C) 2012 - 2014 Pi4J
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

#include <wiringPi.h>
#include "com_pi4j_wiringpi_GpioPin.h"


/**
 * --------------------------------------------------------
 * GET EDGE GPIO PIN
 * --------------------------------------------------------
 */
int getEdgePin(int pin)
{
    int model, rev, mem, maker, overVolted ;

	// validate lower bounds
	if(pin < 0)
		return -1;

	// validate upper bounds
	if(pin >= MAX_GPIO_PINS)
		return -1;

	// return the edge pin index
	// (will return -1 for invalid pin)
    piBoardId (&model, &rev, &mem, &maker, &overVolted) ;
    if (model == PI_MODEL_CM){
        return pin;
    }
    else{
        return wpiPinToGpio(pin);
    }
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
