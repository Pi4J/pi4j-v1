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
import java.nio.ByteBuffer;

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

    @Override
    public void write(String data, String encoding) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void write(ByteBuffer data) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void write(byte... data) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void write(byte[] data, int start, int length) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void write(short... data) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void write(short[] data, int start, int length) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public String readWrite(String data, String encoding) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public ByteBuffer readWrite(ByteBuffer data) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public ByteBuffer readWrite(byte[] data, int start, int length) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public byte[] readWrite(byte... data) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public ByteBuffer readWrite(short[] data, int start, int length) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public short[] readWrite(short... data) {
        throw new  UnsupportedOperationException("Not yet implemented!");
    }
}
