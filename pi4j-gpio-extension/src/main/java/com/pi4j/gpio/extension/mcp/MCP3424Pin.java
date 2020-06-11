package com.pi4j.gpio.extension.mcp;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;

import java.util.EnumSet;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP3424Pin.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
 *
 * <p>
 * This GPIO provider implements the MCP3424 I2C GPIO expansion board as native Pi4J GPIO pins. It is a 18-bit
 * delta/sigma ADC providing 4 input channels. More information about the board can be found here:
 * http://ww1.microchip.com/downloads/en/DeviceDoc/22088c.pdf
 * </p>
 *
 * <p>
 * The MCP3424 is connected via I2C connection to the Raspberry Pi and provides 4 analog input channels.
 * </p>
 *
 * @author Alexander Falkenstern
 */
public class MCP3424Pin {

    public static final Pin GPIO_CH0 = createAnalogInputPin(0, "ANALOG INPUT 0");
    public static final Pin GPIO_CH1 = createAnalogInputPin(1, "ANALOG INPUT 1");
    public static final Pin GPIO_CH2 = createAnalogInputPin(2, "ANALOG INPUT 2");
    public static final Pin GPIO_CH3 = createAnalogInputPin(3, "ANALOG INPUT 3");

    public static Pin[] ALL_PINS = { MCP3424Pin.GPIO_CH0,
                                     MCP3424Pin.GPIO_CH1,
                                     MCP3424Pin.GPIO_CH2,
                                     MCP3424Pin.GPIO_CH3 };

    private static Pin createAnalogInputPin(int channel, String name) {
        return new PinImpl(MCP3424GpioProvider.NAME, channel, name, EnumSet.of(PinMode.ANALOG_INPUT));
    }
}
