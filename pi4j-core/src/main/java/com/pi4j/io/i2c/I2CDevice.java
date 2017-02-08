package com.pi4j.io.i2c;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CDevice.java
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

import java.io.IOException;

/**
 * This is abstraction of an i2c device. It allows data to be read or written to the device.
 *
 * @author Daniel Sendula, refactored by <a href="http://raspelikan.blogspot.co.at">RasPelikan</a>
 *
 */
public interface I2CDevice {

    /**
     * @return The address for which this instance is constructed for.
     */
    int getAddress();

    /**
     * This method writes one byte directly to i2c device.
     *
     * @param b byte to be written
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    void write(byte b) throws IOException;

    /**
     * This method writes several bytes directly to the i2c device from given buffer at given offset.
     *
     * @param buffer buffer of data to be written to the i2c device in one go
     * @param offset offset in buffer
     * @param size number of bytes to be written
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    void write(byte[] buffer, int offset, int size) throws IOException;

    /**
     * This method writes all bytes included in the given buffer directly to the i2c device.
     *
     * @param buffer buffer of data to be written to the i2c device in one go
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    void write(byte[] buffer) throws IOException;

    /**
     * This method writes one byte to i2c device.
     *
     * @param address local address in the i2c device
     * @param b byte to be written
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    void write(int address, byte b) throws IOException;

    /**
     * This method writes several bytes to the i2c device from given buffer at given offset.
     *
     * @param address local address in the i2c device
     * @param buffer buffer of data to be written to the i2c device in one go
     * @param offset offset in buffer
     * @param size number of bytes to be written
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    void write(int address, byte[] buffer, int offset, int size) throws IOException;

    /**
     * This method writes all bytes included in the given buffer directoy to the register address on the i2c device
     *
     * @param address local address in the i2c device
     * @param buffer buffer of data to be written to the i2c device in one go
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    void write(int address, byte[] buffer) throws IOException;

    /**
     * This method reads one byte from the i2c device.
     * Result is between 0 and 255 if read operation was successful, else a negative number for an error.
     *
     * @return byte value read: positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     *
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    int read() throws IOException;

    /**
     * This method reads bytes directly from the i2c device to given buffer at asked offset.
     *
     * @param buffer buffer of data to be read from the i2c device in one go
     * @param offset offset in buffer
     * @param size number of bytes to be read
     *
     * @return number of bytes read
     *
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    int read(byte[] buffer, int offset, int size) throws IOException;

    /**
     * This method reads one byte from the i2c device.
     * Result is between 0 and 255 if read operation was successful, else a negative number for an error.
     *
     * @param address local address in the i2c device
     * @return byte value read: positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     *
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    int read(int address) throws IOException;

    /**
     * This method reads bytes from the i2c device to given buffer at asked offset.
     *
     * @param address local address in the i2c device
     * @param buffer buffer of data to be read from the i2c device in one go
     * @param offset offset in buffer
     * @param size number of bytes to be read
     *
     * @return number of bytes read
     *
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    int read(int address, byte[] buffer, int offset, int size) throws IOException;

    /**
     * This method writes and reads bytes to/from the i2c device in a single method call
     *
     * @param writeBuffer buffer of data to be written to the i2c device in one go
     * @param writeOffset offset in write buffer
     * @param writeSize number of bytes to be written from buffer
     * @param readBuffer buffer of data to be read from the i2c device in one go
     * @param readOffset offset in read buffer
     * @param readSize number of bytes to be read
     *
     * @return number of bytes read
     *
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    int read(byte[] writeBuffer, int writeOffset, int writeSize, byte[] readBuffer, int readOffset, int readSize) throws IOException;

}
