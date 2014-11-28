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


import java.nio.ByteBuffer;

public interface SpiDevice {

    /**
     * Attempts to write data to this SPI device
     *
     * @param data
     *            bytes (encoded in string) to write to the SPI device
     * @param encoding
     *            character encoding for bytes in string
     */
    public void write(String data, String encoding);

    /**
     * Attempts to write data to this SPI device
     *
     * @param data
     *            bytes to write to the SPI device
     * @return output of the operation from wiringPI
     */
    public void write(ByteBuffer data);

    /**
     * Attempts to write data to this SPI device
     *
     * @param data
     *            bytes to write to the SPI device
     * @return output of the operation from wiringPI
     */
    public void write(byte ... data);

    /**
     * Attempts to write data to this SPI device
     *
     * @param data
     *            byte buffer from which to write bytes to the SPI device
     * @param start
     *            start index position in the data buffer to start writing from
     * @param length
     *            length of bytes to write from the data buffer
     * @return output of the operation from wiringPI
     */
    public void write(byte[] data, int start, int length);

    /**
     * Attempts to write data to this SPI device
     *
     * @param data
     *            bytes to write to the SPI device (Note: short value should not exceed 255.)
     * @return output of the operation from wiringPI
     */
    public void write(short ... data);

    /**
     * Attempts to write data to this SPI device
     *
     * @param data
     *            byte buffer from which to write bytes to the SPI device
     * @param start
     *            start index position in the data buffer to start writing from
     * @param length
     *            length of bytes to write from the data buffer
     * @return output of the operation from wiringPI
     */
    public void write(short[] data, int start, int length);

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes (encoded in string) to write to the SPI device
     * @param encoding
     *            character encoding for bytes in string
     * @return output of the operation from wiringPI
     */
    public String readWrite(String data, String encoding);

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes to write to the SPI device
     * @return output of the operation from wiringPI
     */
    public ByteBuffer readWrite(ByteBuffer data);

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes to write to the SPI device
     * @param start
     *            start index position in the data buffer to start writing from
     * @param length
     *            length of bytes to write from the data buffer
     * @return output of the operation from wiringPI
     */
    public ByteBuffer readWrite(byte[] data, int start, int length);

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes to write to the SPI device
     * @return output of the operation from wiringPI
     */
    public byte[] readWrite(byte ... data);

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes to write to the SPI device
     * @param start
     *            start index position in the data buffer to start writing from
     * @param length
     *            length of bytes to write from the data buffer
     * @return output of the operation from wiringPI
     */
    public ByteBuffer readWrite(short[] data, int start, int length);

    /**
     * Attempts to read/write data through this SPI device
     *
     * @param data
     *            bytes to write to the SPI device (Note: short value should not exceed 255.)
     * @return output of the operation from wiringPI
     */
    public short[] readWrite(short ... data);

}
