package com.pi4j.io.spi;

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
 * Copyright (C) 2012 - 2015 Pi4J
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
