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
 * FILENAME      :  MCP23S17Pin.java
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
 * This GPIO provider implements the MCP23S17 SPI GPIO expansion board as native Pi4J GPIO pins.
 * More information about the board can be found here: *
 * http://ww1.microchip.com/downloads/en/DeviceDoc/21952b.pdf
 * </p>
 *
 * <p>
 * The MCP23S17 is connected via SPI connection to the Raspberry Pi and provides
 * 16 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 *
 * @author Robert Savage
 *
 */
public class MCP23S17Pin {

    public static final Pin GPIO_A0 = createDigitalPin(1, "GPIO A0");
    public static final Pin GPIO_A1 = createDigitalPin(2, "GPIO A1");
    public static final Pin GPIO_A2 = createDigitalPin(4, "GPIO A2");
    public static final Pin GPIO_A3 = createDigitalPin(8, "GPIO A3");
    public static final Pin GPIO_A4 = createDigitalPin(16, "GPIO A4");
    public static final Pin GPIO_A5 = createDigitalPin(32, "GPIO A5");
    public static final Pin GPIO_A6 = createDigitalPin(64, "GPIO A6");
    public static final Pin GPIO_A7 = createDigitalPin(128, "GPIO A7");
    public static final Pin GPIO_B0 = createDigitalPin(1001, "GPIO B0");
    public static final Pin GPIO_B1 = createDigitalPin(1002, "GPIO B1");
    public static final Pin GPIO_B2 = createDigitalPin(1004, "GPIO B2");
    public static final Pin GPIO_B3 = createDigitalPin(1008, "GPIO B3");
    public static final Pin GPIO_B4 = createDigitalPin(1016, "GPIO B4");
    public static final Pin GPIO_B5 = createDigitalPin(1032, "GPIO B5");
    public static final Pin GPIO_B6 = createDigitalPin(1064, "GPIO B6");
    public static final Pin GPIO_B7 = createDigitalPin(1128, "GPIO B7");

    public static Pin[] ALL_A_PINS = { MCP23S17Pin.GPIO_A0, MCP23S17Pin.GPIO_A1, MCP23S17Pin.GPIO_A2, MCP23S17Pin.GPIO_A3,
                                       MCP23S17Pin.GPIO_A4, MCP23S17Pin.GPIO_A5, MCP23S17Pin.GPIO_A6, MCP23S17Pin.GPIO_A7 };

    public static Pin[] ALL_B_PINS = { MCP23S17Pin.GPIO_B0, MCP23S17Pin.GPIO_B1, MCP23S17Pin.GPIO_B2, MCP23S17Pin.GPIO_B3,
                                       MCP23S17Pin.GPIO_B4, MCP23S17Pin.GPIO_B5, MCP23S17Pin.GPIO_B6, MCP23S17Pin.GPIO_B7 };

    public static Pin[] ALL = { MCP23S17Pin.GPIO_A0, MCP23S17Pin.GPIO_A1, MCP23S17Pin.GPIO_A2, MCP23S17Pin.GPIO_A3,
                                MCP23S17Pin.GPIO_A4, MCP23S17Pin.GPIO_A5, MCP23S17Pin.GPIO_A6, MCP23S17Pin.GPIO_A7,
                                MCP23S17Pin.GPIO_B0, MCP23S17Pin.GPIO_B1, MCP23S17Pin.GPIO_B2, MCP23S17Pin.GPIO_B3,
                                MCP23S17Pin.GPIO_B4, MCP23S17Pin.GPIO_B5, MCP23S17Pin.GPIO_B6, MCP23S17Pin.GPIO_B7 };

    private static Pin createDigitalPin(int address, String name) {
        return new PinImpl(MCP23S17GpioProvider.NAME, address, name,
                    EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT),
                    EnumSet.of(PinPullResistance.PULL_UP, PinPullResistance.OFF));
    }
}
