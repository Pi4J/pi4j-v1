package com.pi4j.io.spi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SpiDevice.java  
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


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public interface SpiDevice {

    public static final SpiMode DEFAULT_SPI_MODE = SpiMode.MODE_0;
    public static final int DEFAULT_SPI_SPEED = 1000000; // 1MHz (range is 500kHz - 32MHz)
    public static final int MAX_SUPPORTED_BYTES = 2048;

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes (encoded in string) to write to the SPI device
     * @param charset
     *            character encoding for bytes in string
     * @return resulting bytes read from the SPI device after the write operation
     */
    public String write(String data, Charset charset) throws IOException;

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes (encoded in string) to write to the SPI device
     * @param charset
     *            character encoding for bytes in string
     * @return resulting bytes read from the SPI device after the write operation
     */
    public String write(String data, String charset) throws IOException;

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes to write to the SPI device
     * @return resulting bytes read from the SPI device after the write operation
     */
    public ByteBuffer write(ByteBuffer data) throws IOException;

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param input
     *            input stream to read from to get
     *            bytes to write to the SPI device
     * @return resulting bytes read from the SPI device after the write operation
     */
    public byte[] write(InputStream input) throws IOException;

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param input
     *            input stream to read from to get
     *            bytes to write to the SPI device
     * @param output
     *            output stream to write bytes
     *            read from the SPI device
     * @return number of resulting bytes read from the SPI device and written to the output stream
     */
    public int write(InputStream input, OutputStream output) throws IOException;

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes to write to the SPI device
     * @param start
     *            start index position in the data buffer to start writing from
     * @param length
     *            length of bytes to write from the data buffer
     * @return resulting bytes read from the SPI device after the write operation
     */
    public byte[] write(byte[] data, int start, int length) throws IOException;

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes to write to the SPI device
     * @return resulting bytes read from the SPI device after the write operation
     */
    public byte[] write(byte ... data) throws IOException;

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes to write to the SPI device
     * @param start
     *            start index position in the data buffer to start writing from
     * @param length
     *            length of bytes to write from the data buffer
     * @return resulting bytes read from the SPI device after the write operation
     */
    public short[] write(short[] data, int start, int length) throws IOException;

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes to write to the SPI device (Note: short value should not exceed 255.)
     * @return resulting bytes read from the SPI device after the write operation
     */
    public short[] write(short ... data) throws IOException;

}
