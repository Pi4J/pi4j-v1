package com.pi4j.gpio.extension.mcp;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.impl.PinImpl;

import java.util.EnumSet;

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
 * Copyright (C) 2012 - 2015 Pi4J
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
    public static final Pin GPIO_03 = createDigitalPin(8, "GPIO 3");
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
