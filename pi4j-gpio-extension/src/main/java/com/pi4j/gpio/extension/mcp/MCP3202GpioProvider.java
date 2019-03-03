package com.pi4j.gpio.extension.mcp;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP3202GpioProvider.java
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

import java.io.IOException;

import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiMode;

/**
 *
 * <p>
 * This GPIO provider implements the MCP3202 SPI GPIO expansion board as native Pi4J GPIO pins. It is a 12-bit ADC
 * providing 4 input channels.
 * </p>
 *
 * <p>
 * The MCP3202 is connected via SPI connection to the Raspberry Pi and provides 2 GPIO pins that can be used for analog
 * input pins. The values returned are in the range 0-4095 (12 bit value).
 *
 * Note: This implementation currently only supports single-ended inputs.
 * </p>
 *
 * @author Benny Megidish (based on pojd & Hendrik Motza work)
 */
public class MCP3202GpioProvider extends MCP3x0xGpioProvider implements AdcGpioProvider {

	public static final String NAME = "com.pi4j.gpio.extension.mcp.MCP3202GpioProvider";
	public static final String DESCRIPTION = "MCP3202 GPIO Provider";
	public static final int INPUT_COUNT = 2;
	public static final int RESOLUTION = 12;

	/**
	 * Create new instance of this MCP3202 provider with background monitoring and pin notification events enabled.
	 *
	 * @param channel
	 *            spi channel the MCP3202 is connected to
	 * @throws IOException
	 *             if an error occurs during initialization of the SpiDevice
	 */
	public MCP3202GpioProvider(final SpiChannel channel) throws IOException {
		this(channel, SpiDevice.DEFAULT_SPI_SPEED, SpiDevice.DEFAULT_SPI_MODE, true);
	}

	/**
	 * Create new instance of this MCP3202 provider with background monitoring and pin notification events enabled.
	 *
	 * @param channel
	 *            spi channel the MCP3202 is connected to
	 * @param speed
	 *            spi speed to communicate with MCP3202
	 * @throws IOException
	 *             if an error occurs during initialization of the SpiDevice
	 */
	public MCP3202GpioProvider(final SpiChannel channel, final int speed) throws IOException {
		this(channel, speed, SpiDevice.DEFAULT_SPI_MODE, true);
	}

	/**
	 * Create new instance of this MCP3202 provider with background monitoring and pin notification events enabled.
	 *
	 * @param channel
	 *            spi channel the MCP3202 is connected to
	 * @param mode
	 *            spi mode to communicate with MCP3202
	 * @throws IOException
	 *             if an error occurs during initialization of the SpiDevice
	 */
	public MCP3202GpioProvider(final SpiChannel channel, final SpiMode mode) throws IOException {
		this(channel, SpiDevice.DEFAULT_SPI_SPEED, mode, true);
	}

	/**
	 * Create new instance of this MCP3202 provider with background monitoring and pin notification events enabled.
	 *
	 * @param channel
	 *            spi channel the MCP3202 is connected to
	 * @param speed
	 *            spi speed to communicate with MCP3202
	 * @param mode
	 *            spi mode to communicate with MCP3202
	 * @throws IOException
	 *             if an error occurs during initialization of the SpiDevice
	 */
	public MCP3202GpioProvider(final SpiChannel channel, final int speed, final SpiMode mode) throws IOException {
		this(channel, speed, mode, true);
	}

	/**
	 * Create new instance of this MCP3202 provider. Optionally enable or disable background monitoring and pin
	 * notification events.
	 *
	 * @param channel
	 *            spi channel the MCP3202 is connected to
	 * @param speed
	 *            spi speed to communicate with MCP3202
	 * @param mode
	 *            spi mode to communicate with MCP3202
	 * @param enableBackgroundMonitoring
	 *            if enabled, then a background thread will be created to constantly acquire the ADC input values and
	 *            publish pin change listeners if the value change is beyond the configured threshold.
	 * @throws IOException
	 *             if an error occurs during initialization of the SpiDevice
	 */
	public MCP3202GpioProvider(final SpiChannel channel, final int speed, final SpiMode mode,
			final boolean enableBackgroundMonitoring) throws IOException {
		super(MCP3202Pin.ALL, channel, speed, RESOLUTION, mode);

		// default background monitoring interval
		setMonitorInterval(DEFAULT_MONITOR_INTERVAL);

		// enable|disable background monitoring
		if (enableBackgroundMonitoring) {
			setMonitorEnabled(enableBackgroundMonitoring);
		}
	}

	// ------------------------------------------------------------------------------------------
	// PUBLIC METHODS
	// ------------------------------------------------------------------------------------------
	@Override
    private short toCommand(final short channel) {
        final short command = (short) ((((channel + 2) << 1) + 1) << 5);
        return command;
    }

	@Override
	public String getName() {
		return NAME;
	}
}
