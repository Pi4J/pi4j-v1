package com.pi4j.gpio.extension.mcp;

import java.util.EnumSet;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP4725Pin.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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
 * This GPIO provider implements the MCP4725 12-Bit Digital-to-Analog Converter as native Pi4J GPIO pins.
 * More information about the board can be found here: 
 * http://http://www.adafruit.com/product/935
 * </p>
 * 
 * <p>
 * The MCP4725 is connected via SPI connection to the Raspberry Pi and provides 1 GPIO analog output pin.
 * </p>
 * 
 * @author Christian Wehrli
 * @version 1.0, Feb 11, 2014
 * 
 */
public class MCP4725Pin {

    public static final Pin OUTPUT = createAnalogOutputPin(0, "ANALOG OUTPUT");

    private static Pin createAnalogOutputPin(int address, String name) {
        return new PinImpl(MCP4725GpioProvider.NAME, address, name, EnumSet.of(PinMode.ANALOG_OUTPUT));
    }
}
