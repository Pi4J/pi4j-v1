package com.pi4j.jni;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2C.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import com.pi4j.util.NativeLibraryLoader;

/**
 * <h1>I2C Communication</h1>
 *
 * <p>
 * Set of native methods for interacting with i2c bus on RPi.
 * </p>
 *
 * <p>
 * Note: The file descriptor (fd) returned is a standard Linux filehandle. You can use the standard
 * read(), write(), etc. system calls on this filehandle as required.
 * </p>
 *
 * @author Daniel Sendula
 */
public class I2C {

    // private constructor
    private I2C() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * Opens linux file for r/w returning file handle.
     *
     * @param device file name of device. For i2c should be /dev/i2c-0 or /dev/i2c-1 for first or second bus.
     * @return file descriptor or i2c bus.
     */
    public static native int i2cOpen(String device);

    /**
     * Closes linux file.
     *
     * @param fd file descriptor
     */
    public static native int i2cClose(int fd);

    /**
     * Select the slave device to be target by the bus.

     * @param fd file descriptor
     * @param deviceAddress device address
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public static native int i2cSlaveSelect(int fd, int deviceAddress);

    /**
     * Writes one byte to i2c. It uses ioctl to define device address and then writes one byte.
     *
     * @param fd            file descriptor of i2c bus
     * @param data          byte to be written to the device
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public static native int i2cWriteByteDirect(int fd, byte data);

    /**
     * Writes several bytes to i2c. It uses ioctl to define device address and then writes number of bytes defined
     * in size argument.
     *
     * @param fd            file descriptor of i2c bus
     * @param size          number of bytes to be written
     * @param offset        offset in buffer to read from
     * @param buffer        data buffer to be written
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public static native int i2cWriteBytesDirect(int fd, int size, int offset, byte[] buffer);

    /**
     * Writes one byte to i2c. It uses ioctl to define device address and then writes two bytes: address in
     * the device itself and value.
     *
     * @param fd            file descriptor of i2c bus
     * @param localAddress  address in the device
     * @param data          byte to be written to the device
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public static native int i2cWriteByte(int fd, int localAddress, byte data);

    /**
     * Writes several bytes to i2c. It uses ioctl to define device address and then writes number of bytes defined
     * in size argument plus one.
     *
     * @param fd            file descriptor of i2c bus
     * @param localAddress  address in the device
     * @param size          number of bytes to be written
     * @param offset        offset in buffer to read from
     * @param buffer        data buffer to be written
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public static native int i2cWriteBytes(int fd, int localAddress, int size, int offset, byte[] buffer);

    /**
     * Reads one byte from i2c device. It uses ioctl to define device address and then reads one byte.
     *
     * @param fd            file descriptor of i2c bus
     * @return positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     */
    public static native int i2cReadByteDirect(int fd);

    /**
     * Reads more bytes from i2c device. It uses ioctl to define device address and then reads
     * size number of bytes.
     *
     * @param fd            file descriptor of i2c bus
     * @param size          number of bytes to be read
     * @param offset        offset in buffer to stored read data
     * @param buffer        buffer for data to be written to
     * @return number of bytes read or negative number if reading failed.
     */
    public static native int i2cReadBytesDirect(int fd, int size, int offset, byte[] buffer);

    /**
     * Reads one byte from i2c device. It uses ioctl to define device address, writes addres in device and then reads
     * one byte.
     *
     * @param fd            file descriptor of i2c bus
     * @param localAddress  address in the device
     * @return positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     */
    public static native int i2cReadByte(int fd, int localAddress);

    /**
     * Reads more bytes from i2c device. It uses ioctl to define device address, writes addres in device and then reads
     * size number of bytes.
     *
     * @param fd            file descriptor of i2c bus
     * @param localAddress  address in the device
     * @param size          number of bytes to be read
     * @param offset        offset in buffer to stored read data
     * @param buffer        buffer for data to be written to
     * @return number of bytes read or negative number if reading failed.
     */
    public static native int i2cReadBytes(int fd, int localAddress, int size, int offset, byte[] buffer);


    /**
     * Reads more bytes from i2c device. It uses ioctl to define device address, writes addres in device and then reads
     * size number of bytes.
     *
     * @param fd            file descriptor of i2c bus
     * @param writeSize     number of bytes to write
     * @param writeOffset   offset in write buffer to start write data
     * @param writeBuffer   buffer for data to be written from
     * @param readSize      number of bytes to be read
     * @param readOffset    offset in read buffer to stored read data
     * @param readBuffer    buffer for data read to be stored in
     * @return number of bytes read or negative number if reading failed.
     */
    public static native int i2cWriteAndReadBytes(int fd, int writeSize, int writeOffset, byte[] writeBuffer, int readSize, int readOffset, byte[] readBuffer);
}
