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

    public static Pin[] ALL = { MCP4725Pin.OUTPUT };

    private static Pin createAnalogOutputPin(int address, String name) {
        return new PinImpl(MCP4725GpioProvider.NAME, address, name, EnumSet.of(PinMode.ANALOG_OUTPUT));
    }
}
