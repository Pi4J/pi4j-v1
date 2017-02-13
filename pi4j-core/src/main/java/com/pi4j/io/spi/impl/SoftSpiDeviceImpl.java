package com.pi4j.io.spi.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiMode;
import com.pi4j.jni.SoftSPI;
import com.pi4j.wiringpi.Gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SoftSpiDeviceImpl.java
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

/**
 * Software Implementation of an SPI port
 * @author Tommy « LeChat » Savaria
 */
public class SoftSpiDeviceImpl implements SpiDevice {
	private final static long DEFAULT_SPI_SPEED = 250000;
	private final static SpiMode DEFAULT_SPI_MODE = SpiMode.MODE_0;
	
	public final int chipSelect;
	public final int clock;
	public final int masterOut;
	public final int masterIn;
	public final long speed;
	public final SpiMode mode;
	private final boolean clockPolarity;
	private final boolean clockPhase;
	
	public SoftSpiDeviceImpl(int chipSelect, int clock, int masterOut, int masterIn) {
		this(chipSelect, clock, masterOut, masterIn, DEFAULT_SPI_SPEED);
		
	}
	
	public SoftSpiDeviceImpl(int chipSelect, int clock, int masterOut, int masterIn, long speed) {
		this(chipSelect, clock, masterOut, masterIn, speed, DEFAULT_SPI_MODE);
	}
	
	/**
	 * Creates a Software SPI Device. 
	 * @param chipSelect Pin to use for Chip Select
	 * @param clock Pin to use for Clock
	 * @param masterOut Pin to use for Master Out Slave In
	 * @param masterIn Pin to use for Master In Slave Out
	 * @param speed Speed of the SPI Port
	 */
	public SoftSpiDeviceImpl(int chipSelect, int clock, int masterOut, int masterIn, long speed, SpiMode mode) {
		super();
		this.chipSelect = chipSelect;
		this.clock = clock;
		this.masterOut = masterOut;
		this.masterIn = masterIn;
		this.speed = speed;
		this.mode = mode;
		
		Gpio.pinMode(chipSelect, Gpio.OUTPUT);
		Gpio.pinMode(clock, Gpio.OUTPUT);
		Gpio.pinMode(masterOut, Gpio.OUTPUT);
		Gpio.pinMode(masterIn, Gpio.INPUT);
		
		switch(mode) {
		default:
		case MODE_0:
			clockPolarity = false;
			clockPhase = false;
			break;
		case MODE_1:
			clockPolarity = false;
			clockPhase = true;
			break;
		case MODE_2:
			clockPolarity = true;
			clockPhase = false;
			break;
		case MODE_3:
			clockPolarity = true;
			clockPhase = true;
			break;
		}
	}

	@Override
	public String write(String data, Charset charset) throws IOException {
		byte[] buffer = data.getBytes(charset);
        return new String(write(buffer), charset);
	}

	@Override
	public String write(String data, String charset) throws IOException {
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
            throw new IOException("No available bytes in input stream to write to SPI device");
        }
        else if(input.available() > MAX_SUPPORTED_BYTES){
            throw new IOException("Number of bytes in stream exceed the maximum bytes alloweds to write SPI channel in a single call");
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
	public byte[] write(byte[] data, int start, int length) throws IOException {
		return SoftSPI.readWrite(this.chipSelect, this.clock, this.masterOut, this.masterIn, this.speed, this.clockPolarity, this.clockPhase, Arrays.copyOfRange(data, start, start+length));
	}

	@Override
	public byte[] write(byte... data) throws IOException {
		return write(data, 0, data.length);
	}

	@Override
	public short[] write(short[] data, int start, int length) throws IOException {
		return null;
	}

	@Override
	public short[] write(short... data) throws IOException {
		return write(data, 0, data.length);
	}
}
