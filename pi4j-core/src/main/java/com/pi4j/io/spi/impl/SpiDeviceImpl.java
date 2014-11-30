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


import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.wiringpi.Spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class SpiDeviceImpl implements SpiDevice {

    protected final SpiChannel channel;

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
        this.channel = channel;
        try {
            int fd = Spi.wiringPiSPISetup(channel.getChannel(), speed);
            if (fd <= -1) {
                throw new IOException("SPI port setup failed, wiringSPISetup returned " + fd);
            }
        } catch (UnsatisfiedLinkError e) {
            throw new IOException("SPI port setup failed, no SPI available.", e);
        }
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
