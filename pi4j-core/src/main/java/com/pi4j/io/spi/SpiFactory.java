package com.pi4j.io.spi;

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
     * @return Return a new SpiDevice impl instance.
     *
     * @throws java.io.IOException
     */
    public static SpiDevice getInstance(SpiChannel channel, int speed) throws IOException {
        return new SpiDeviceImpl(channel, speed);
    }

}
