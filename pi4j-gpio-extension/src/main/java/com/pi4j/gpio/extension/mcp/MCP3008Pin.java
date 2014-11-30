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
 * FILENAME      :  MCP3008Pin.java  
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
 * 
 * <p>
 * This GPIO provider implements the MCP3008 SPI GPIO expansion board as native Pi4J GPIO pins. It is a 10-bit ADC providing 8 input
 * channels. More information about the board can be found here: * http://ww1.microchip.com/downloads/en/DeviceDoc/21295d.pdf
 * </p>
 * 
 * <p>
 * The MCP3008 is connected via SPI connection to the Raspberry Pi and provides 8 GPIO pins that can be used for analog input pins.
 * </p>
 * 
 * @author pojd
 */
public enum MCP3008Pin {

	CH0(0, "GPIO CH0"),
	CH1(1, "GPIO CH1"),
	CH2(2, "GPIO CH2"),
	CH3(3, "GPIO CH3"),
	CH4(4, "GPIO CH4"),
	CH5(5, "GPIO CH5"),
	CH6(6, "GPIO CH6"),
	CH7(7, "GPIO CH7");

	public final Pin pin;

	private MCP3008Pin(int channel, String name) {
		this.pin = new PinImpl(MCP3008GpioProvider.NAME, channel, name, EnumSet.of(PinMode.ANALOG_INPUT));
	}
}
