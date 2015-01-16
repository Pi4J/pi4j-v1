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
#ifndef _Included_com_pi4j_wiringpi_GpioPin
#define _Included_com_pi4j_wiringpi_GpioPin
#ifdef __cplusplus
extern "C" {
#endif


// constants
#define	MAX_GPIO_PINS   46L


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
