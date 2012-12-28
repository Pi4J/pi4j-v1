package com.pi4j.gpio.extension.ti;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  PCF8574Pin.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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


import java.util.EnumSet;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;


/**
 * <p>
 * This GPIO provider implements the PCF8574 I2C GPIO expansion board as native Pi4J GPIO pins.
 * More information about the board can be found here: *
 * http://www.ti.com/lit/ds/symlink/pcf8574.pdf
 * </p>
 * 
 * <p>
 * The PCF8574 is connected via I2C connection to the Raspberry Pi and provides
 * 8 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * 
 * @author Robert Savage
 * 
 */
public class PCF8574Pin
{
    public static final Pin GPIO_00 = createDigitalPin(0, "GPIO 0");
    public static final Pin GPIO_01 = createDigitalPin(1, "GPIO 1");
    public static final Pin GPIO_02 = createDigitalPin(2, "GPIO 2");
    public static final Pin GPIO_03 = createDigitalPin(3, "GPIO 3");
    public static final Pin GPIO_04 = createDigitalPin(4, "GPIO 4");
    public static final Pin GPIO_05 = createDigitalPin(5, "GPIO 5");
    public static final Pin GPIO_06 = createDigitalPin(6, "GPIO 6");
    public static final Pin GPIO_07 = createDigitalPin(7, "GPIO 7");

    public static Pin[] ALL = { PCF8574Pin.GPIO_00, PCF8574Pin.GPIO_01, PCF8574Pin.GPIO_02, PCF8574Pin.GPIO_03,
                                PCF8574Pin.GPIO_04, PCF8574Pin.GPIO_05, PCF8574Pin.GPIO_06, PCF8574Pin.GPIO_07 };
    
    private static Pin createDigitalPin(int address, String name)
    {
        return new PinImpl(PCF8574GpioProvider.NAME, address, name, 
                    EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT));
    }       
}
