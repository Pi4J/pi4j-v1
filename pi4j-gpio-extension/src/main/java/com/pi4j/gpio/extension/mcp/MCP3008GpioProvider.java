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
