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
 * FILENAME      :  MCP3202Pin.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
 * This GPIO provider implements the MCP3202 SPI GPIO expansion board as native Pi4J GPIO pins. It is a 12-bit ADC
 * providing 2 input channels.
 * </p>
 *
 * <p>
 * The MCP3202 is connected via SPI connection to the Raspberry Pi and provides 2 GPIO pins that can be used for analog
 * input pins.
 * </p>
 *
 * @author Benny Megidish (based on pojd & Hendrik Motza work)
 */
public class MCP3202Pin {

	public static final Pin CH0 = createAnalogInputPin(0, "ANALOG INPUT 0");
	public static final Pin CH1 = createAnalogInputPin(1, "ANALOG INPUT 1");

	public static Pin[] ALL = { MCP3202Pin.CH0, MCP3202Pin.CH1 };

	private static Pin createAnalogInputPin(final int channel, final String name) {
		return new PinImpl(MCP3202GpioProvider.NAME, channel, name, EnumSet.of(PinMode.ANALOG_INPUT));
	}
}
