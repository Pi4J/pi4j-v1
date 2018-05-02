package com.pi4j.io.spi.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SpiDeviceImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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


import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiMode;
import com.pi4j.wiringpi.Spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class SpiDeviceImpl implements SpiDevice {

    protected final SpiChannel channel;
    protected final SpiMode mode;

    /**
     * Creates the SPI Device at the given spi and input channel
     *
     * @param channel
     *            spi channel to use
     * @param speed
     *            spi speed/rate (in Hertz) for channel to communicate at
     *            (range is 500kHz - 32MHz)
     * @param mode
     *            spi mode (see http://en.wikipedia.org/wiki/Serial_Peripheral_Interface_Bus#Mode_numbers)
     *
     */
    public SpiDeviceImpl(SpiChannel channel, int speed, SpiMode mode) throws IOException {
        this.channel = channel;
        this.mode = mode;
        try {
            int fd;
            if(mode == SpiMode.MODE_0){
                // using this hack because the Odroid port of WiringPi does not currently support 'wiringPiSPISetupMode'
                fd = Spi.wiringPiSPISetup(channel.getChannel(), speed);
            }
            else{
                fd = Spi.wiringPiSPISetupMode(channel.getChannel(), speed, mode.getMode());
            }
            if (fd <= -1) {
                throw new IOException("SPI port setup failed, wiringPiSPISetupMode returned " + fd);
            }
        } catch (UnsatisfiedLinkError e) {
            throw new IOException("SPI port setup failed, no SPI available.", e);
        }
    }

    /**
     * Creates the SPI Device at the given spi and input channel
     *
     * @param channel
     *            spi channel to use
     * @param speed
     *            spi speed/rate (in Hertz) for channel to communicate at
     *            (range is 500kHz - 32MHz)
     */
    public SpiDeviceImpl(SpiChannel channel, int speed) throws IOException {
        this(channel, speed, DEFAULT_SPI_MODE);
    }

    /**
     * Creates the SPI Device at the given SPI and input channel
     * (A default speed of 1 MHz will be used)
     *
     * @param channel
     *            spi channel to use
     * @param mode
     *            spi mode (see http://en.wikipedia.org/wiki/Serial_Peripheral_Interface_Bus#Mode_numbers)
     */
    public SpiDeviceImpl(SpiChannel channel, SpiMode mode) throws IOException {
        this(channel, DEFAULT_SPI_SPEED, mode);
    }

    /**
     * Creates the SPI Device at the given SPI and input channel
     * (A default speed of 1 MHz will be used)
     *
     * @param channel
     *            spi channel to use
     */
    public SpiDeviceImpl(SpiChannel channel) throws IOException {
        this(channel, DEFAULT_SPI_SPEED);
    }

    @Override
    public String write(String data, String charset) throws IOException {
        byte[] buffer = data.getBytes(charset);
        return new String(write(buffer), charset);
    }

    @Override
    public String write(String data, Charset charset) throws IOException {
        byte[] buffer = data.getBytes(charset);
        return new String(write(buffer), charset);
    }

    @Override
    public ByteBuffer write(ByteBuffer data) throws IOException {
        return ByteBuffer.wrap(write(data.array()));
    }

    @Override
    public byte[] write(InputStream input) throws IOException {

        // ensure bytes are available
        if(input.available() <= 0){
            throw new IOException("No available bytes in input stream to write to SPI channel: " + channel.getChannel());
        }
        else if(input.available() > MAX_SUPPORTED_BYTES){
            throw new IOException("Number of bytes in stream exceed the maximum bytes allowed to write SPI channel in a single call");
        }

        // create a temporary buffer to store read bytes from stream
        byte[] buffer = new byte[MAX_SUPPORTED_BYTES];

        // read maximum number of supported bytes
        int length = input.read(buffer, 0 , MAX_SUPPORTED_BYTES);

        // write bytes to SPI channel
        return write(buffer, 0, length);
    }

    @Override
    public int write(InputStream input, OutputStream output) throws IOException {
        // write stream data to SPI device
        byte[] buffer = write(input);

        //write resulting byte array to output stream
        output.write(buffer);

        // return data length
        return buffer.length;
    }

    @Override
    public byte[] write(byte... data) throws IOException {
        return write(data, 0, data.length);
    }

    @Override
    public short[] write(short... data) throws IOException {
        return write(data, 0, data.length);
    }

    @Override
    public byte[] write(byte[] data, int start, int length) throws IOException {

        // ensure the length does not exceed the data array
        length = Math.min(data.length - start, length);

        // validate max length allowed
        if (length > MAX_SUPPORTED_BYTES) {
            throw new IOException("Number of bytes in data to write exceed the maximum bytes allowed to write SPI channel in a single call");
        }

        // we make a copy of the data argument because we don't want to modify the original source data
        byte[] buffer = new byte[length];
        System.arraycopy(data, start, buffer, 0, length);

        synchronized (channel) {
                // write the bytes from the temporary buffer to the SPI channel
                if (Spi.wiringPiSPIDataRW(channel.getChannel(), buffer) <= 0) {
                    throw new IOException("Failed to write data to SPI channel: " + channel.getChannel());
                }
            }
        // return the updated byte buffer as the SPI read results
        return buffer;
    }

    @Override
    public short[] write(short[] data, int start, int length) throws IOException {

        // ensure the length does not exceed the data array
        length = Math.min(data.length - start, length);

        // validate max length allowed
        if (length > MAX_SUPPORTED_BYTES) {
            throw new IOException("Number of bytes in data to write exceed the maximum bytes allowed to write SPI channel in a single call");
        }

        // we make a copy of the data argument because we don't want to modify the original source data
        short[] buffer = new short[length];
        System.arraycopy(data, start, buffer, 0, length);

        synchronized (channel) {
            // write the bytes from the temporary buffer to the SPI channel
            if (Spi.wiringPiSPIDataRW(channel.getChannel(), buffer) <= 0) {
                throw new IOException("Failed to write data to SPI channel: " + channel.getChannel());
            }

            // return the updated byte buffer as the SPI read results
            return buffer;
        }
    }

}
