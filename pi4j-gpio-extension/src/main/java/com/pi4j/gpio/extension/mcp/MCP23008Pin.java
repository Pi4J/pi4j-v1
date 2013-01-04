package com.pi4j.gpio.extension.mcp;

import java.util.EnumSet;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.impl.PinImpl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP23008Pin.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * #L%
 */

/**
 * <p>
 * This GPIO provider implements the MCP23008 I2C GPIO expansion board as native Pi4J GPIO pins.
 * More information about the board can be found here: *
 * http://ww1.microchip.com/downloads/en/DeviceDoc/21919e.pdf
 * http://learn.adafruit.com/mcp230xx-gpio-expander-on-the-raspberry-pi/overview
 * </p>
 * 
 * <p>
 * The MCP23008 is connected via I2C connection to the Raspberry Pi and provides
 * 8 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * 
 * @author Robert Savage
 * 
 */
public class MCP23008Pin {

    public static final Pin GPIO_00 = createDigitalPin(1, "GPIO 0");
    public static final Pin GPIO_01 = createDigitalPin(2, "GPIO 1");
    public static final Pin GPIO_02 = createDigitalPin(4, "GPIO 2");
    public static final Pin GPIO_03 = createDigitalPin(6, "GPIO 3");
    public static final Pin GPIO_04 = createDigitalPin(16, "GPIO 4");
    public static final Pin GPIO_05 = createDigitalPin(32, "GPIO 5");
    public static final Pin GPIO_06 = createDigitalPin(64, "GPIO 6");
    public static final Pin GPIO_07 = createDigitalPin(128, "GPIO 7");

    public static Pin[] ALL = { MCP23008Pin.GPIO_00, MCP23008Pin.GPIO_01, MCP23008Pin.GPIO_02, MCP23008Pin.GPIO_03,
                                MCP23008Pin.GPIO_04, MCP23008Pin.GPIO_05, MCP23008Pin.GPIO_06, MCP23008Pin.GPIO_07 };
    
    private static Pin createDigitalPin(int address, String name) {
        return new PinImpl(MCP23008GpioProvider.NAME, address, name, 
                    EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT),
                    EnumSet.of(PinPullResistance.PULL_UP, PinPullResistance.OFF));
    }       
}
