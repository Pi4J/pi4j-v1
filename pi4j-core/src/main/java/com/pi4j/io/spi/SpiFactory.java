package com.pi4j.io.spi;

import com.pi4j.io.spi.impl.SoftSpiDeviceImpl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SpiFactory.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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

import com.pi4j.io.spi.impl.SpiDeviceImpl;

import java.io.IOException;

/**
 * SPI factory - it returns instances of {@link com.pi4j.io.spi.SpiDevice} interface.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SpiFactory {

    // private constructor
    private SpiFactory() {
        // forbid object construction
    }

    /**
     * Create a new Software SPI instance with the desired pins, with a default SPI Speed of 250KHz and default SPI Mode of 0
     * @param chipSelect Chip Select Pin
     * @param clock Clock Pin
     * @param masterOut Master Out Slave In Pin
     * @param masterIn Master In Slave Out Pin
     * @return Return a new software SpiDevice instance
     */
    public static SpiDevice getInstance(int chipSelect, int clock, int masterOut, int masterIn) {
    	return new SoftSpiDeviceImpl(chipSelect, clock, masterOut, masterIn);
    }
    
    /**
     * Create a new Software SPI instance with the desired pins and mode, with a default SPI Mode of 0
     * @param chipSelect Chip Select Pin
     * @param clock Clock Pin
     * @param masterOut Master Out Slave In Pin
     * @param masterIn Master In Slave Out Pin
     * @param speed Speed of the SPI bus
     * @return Return a new software SpiDevice instance
     */
	public static SpiDevice getInstance(int chipSelect, int clock, int masterOut, int masterIn, long speed) {
		return new SoftSpiDeviceImpl(chipSelect, clock, masterOut, masterIn, speed);	
	}
	
	/**
     * Create a new Software SPI instance with the desired pins, speed and mode
     * @param chipSelect Chip Select Pin
     * @param clock Clock Pin
     * @param masterOut Master Out Slave In Pin
     * @param masterIn Master In Slave Out Pin
     * @param speed Speed of the SPI bus
     * @param mode SPI Mode to use
     * @return Return a new software SpiDevice instance
     */
	public static SpiDevice getInstance(int chipSelect, int clock, int masterOut, int masterIn, long speed, SpiMode mode) {
		return new SoftSpiDeviceImpl(chipSelect, clock, masterOut, masterIn, speed, mode);
	}
	
    /**
     * Create new SpiDevice instance with a default SPI speed of 1 MHz.
     *
     * @param channel
     *            spi channel to use
     *
     * @return Return a new SpiDevice impl instance.
     *
     * @throws java.io.IOException
     */
    public static SpiDevice getInstance(SpiChannel channel) throws IOException {
        return new SpiDeviceImpl(channel);
    }

    /**
     * Create new SpiDevice instance
     *
     * @param channel
     *            spi channel to use
     * @param mode
     *            spi mode (see http://en.wikipedia.org/wiki/Serial_Peripheral_Interface_Bus#Mode_numbers)
     *
     * @return Return a new SpiDevice impl instance.
     *
     * @throws java.io.IOException
     */
    public static SpiDevice getInstance(SpiChannel channel, SpiMode mode) throws IOException {
        return new SpiDeviceImpl(channel, mode);
    }

    /**
     * Create new SpiDevice instance
     *
     * @param channel
     *            spi channel to use
     * @param speed
     *            spi speed/rate (in Hertz) for channel to communicate at
     *            (range is 500kHz - 32MHz)
     *
     * @return Return a new SpiDevice impl instance.
     *
     * @throws java.io.IOException
     */
    public static SpiDevice getInstance(SpiChannel channel, int speed) throws IOException {
        return new SpiDeviceImpl(channel, speed);
    }

    /**
     * Create new SpiDevice instance
     *
     * @param channel
     *            spi channel to use
     * @param speed
     *            spi speed/rate (in Hertz) for channel to communicate at
     *            (range is 500kHz - 32MHz)
     * @param mode
     *            spi mode (see http://en.wikipedia.org/wiki/Serial_Peripheral_Interface_Bus#Mode_numbers)
     *
     * @return Return a new SpiDevice impl instance.
     *
     * @throws java.io.IOException
     */
    public static SpiDevice getInstance(SpiChannel channel, int speed, SpiMode mode) throws IOException {
        return new SpiDeviceImpl(channel, speed, mode);
    }

}
