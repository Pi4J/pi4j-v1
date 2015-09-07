package com.pi4j.gpio.extension.mcp;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP300xGpioProvider.java  
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

import com.pi4j.gpio.extension.base.AdcGpioProviderBase;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;

import java.io.IOException;

/**
 *
 * <p>
 * This GPIO provider implements the base functionality for the MCP3008 & MCP3008 SPI ADC as native Pi4J GPIO pins.
 * It is a 10-bit ADC providing between 4 and 8 input channels.
 *
 * More information about the ADC chip can be found here: http://ww1.microchip.com/downloads/en/DeviceDoc/21295d.pdf
 * </p>
 *
 * <p>
 * The MCP3008 is connected via SPI connection to the Raspberry Pi and provides 8 GPIO pins that can be used for analog input pins. The
 * values returned are in the range 0-1023 (max 10 bit value).
 *
 * Note: This implementation currently only supports single-ended inputs.
 * </p>
 *
 * @author Robert Savage, pojd
 */
public abstract class MCP300xGpioProvider extends AdcGpioProviderBase implements GpioProvider {

	public static final int MAX_VALUE = 1023; // 10-bit ADC can produce values from 0 to 1023
    public static final int MIN_VALUE = 0;    // 10-bit ADC can produce values from 0 to 1023

	private final SpiDevice device;

    /**
     * Create new instance of this MCP300x provider.  Optionally enable or disable background monitoring
     * and pin notification events.
     *
     * @param pins
     *            the collection of all GPIO pins used with this ADC provider implementation
     * @param channel
     *            spi channel the MCP300x is connected to
     * @param speed
     *            spi speed to communicate with MCP300x
     * @param mode
     *            spi mode to communicate with MCP300x
     * @throws IOException
     *             if an error occurs during initialization of the SpiDevice
     */
    public MCP300xGpioProvider(Pin[] pins, SpiChannel channel, int speed, SpiMode mode) throws IOException {
        super(pins);
        this.device = SpiFactory.getInstance(channel, speed, mode);
    }

    // ------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    // ------------------------------------------------------------------------------------------

    /**
     * This method will perform an immediate data acquisition directly to the ADC chip to get the
     * requested pin's input conversion value.
     *
     * @param pin requested input pin to acquire conversion value
     * @return conversion value for requested analog input pin
     * @throws IOException
     */
    @Override
    public double getImmediateValue(Pin pin) throws IOException {
        if(isInitiated()){
            // read in value from ADC chip
            double value = readAnalog(toCommand((short) pin.getAddress()));
            // validate value withing acceptable range
            if(value >= MIN_VALUE && value <= MAX_VALUE) {
                getPinCache(pin).setAnalogValue(value);
                return value;
            }
        }
        return INVALID_VALUE;
    }

    // ------------------------------------------------------------------------------------------
    // base implementation methods for MCP300x ADC chips
    // ------------------------------------------------------------------------------------------

    private short toCommand(short channel) {
		short command = (short) ((channel + 8) << 4);
		return command;
	}

	private boolean isInitiated() {
		return device != null;
	}

	private synchronized int readAnalog(short channelCommand) {
		// send 3 bytes command - "1", channel command and some extra byte 0
		// http://hertaville.com/2013/07/24/interfacing-an-spi-adc-mcp3008-chip-to-the-raspberry-pi-using-c
		short[] data = new short[] { 1, channelCommand, 0 };
		short[] result;
		try {
			result = device.write(data);
		} catch (IOException e) {
			return INVALID_VALUE;
		}

		// now take 8 and 9 bit from second byte (& with 0b11 and shift) and the whole last byte to form the value
		int analogValue = ((result[1] & 3) << 8) + result[2];
		return analogValue;
	}

}
