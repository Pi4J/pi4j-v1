package com.pi4j.gpio.extension.mcp;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP3008GpioProvider.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import java.io.IOException;

/**
 * 
 * <p>
 * This GPIO provider implements the MCP3008 SPI GPIO expansion board as native Pi4J GPIO pins. It is a 10-bit ADC providing 8 input
 * channels. More information about the board can be found here: http://ww1.microchip.com/downloads/en/DeviceDoc/21295d.pdf
 * </p>
 * 
 * <p>
 * The MCP3008 is connected via SPI connection to the Raspberry Pi and provides 8 GPIO pins that can be used for analog input pins. The
 * values returned are in the range 0-1023 (max 10 bit value).
 * </p>
 * 
 * @author pojd
 */
public class MCP3008GpioProvider extends GpioProviderBase implements GpioProvider {

	public static final String NAME = "com.pi4j.gpio.extension.mcp.MCP3008GpioProvider";
	public static final String DESCRIPTION = "MCP3008 GPIO Provider";
	public static final int INVALID_VALUE = -1;

	private final SpiDevice spiDevice;

	/**
	 * Create new instance of this MCP3008 provider.
	 * 
	 * @param spiChannel
	 *            spi channel the MCP3008 is connected to
	 * @throws IOException
	 *             if an error occurs during initialization of the SpiDevice
	 */
	public MCP3008GpioProvider(SpiChannel spiChannel) throws IOException {
		this.spiDevice = SpiFactory.getInstance(spiChannel);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public double getValue(Pin pin) {
		// do not return, only let parent handle whether this pin is OK
		super.getValue(pin);
		return isInitiated() ? readAnalog(toCommand((short) pin.getAddress())) : INVALID_VALUE;
	}

	private short toCommand(short channel) {
		short command = (short) ((channel + 8) << 4);
		return command;
	}

	private boolean isInitiated() {
		return spiDevice != null;
	}

	private int readAnalog(short channelCommand) {
		// send 3 bytes command - "1", channel command and some extra byte 0
		// http://hertaville.com/2013/07/24/interfacing-an-spi-adc-mcp3008-chip-to-the-raspberry-pi-using-c
		short[] data = new short[] { 1, channelCommand, 0 };
		short[] result;
		try {
			result = spiDevice.write(data);
		} catch (IOException e) {
			return INVALID_VALUE;
		}

		// now take 8 and 9 bit from second byte (& with 0b11 and shift) and the whole last byte to form the value
		int analogValue = ((result[1] & 3) << 8) + result[2];
		return analogValue;
	}
}
