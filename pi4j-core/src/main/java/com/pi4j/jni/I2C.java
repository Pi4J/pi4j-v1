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
        NativeLibraryLoader.load("pi4j", "libpi4j.so");
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
     * Writes one byte to i2c. It uses ioctl to define device address and then writes one byte.
     * 
     * @param fd file descriptor of i2c bus
     * @param deviceAddress device address
     * @param data byte to be written to the device
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public static native int i2cWriteByteDirect(int fd, int deviceAddress, byte data);

    /**
     * Writes several bytes to i2c. It uses ioctl to define device address and then writes number of bytes defined 
     * in size argument.
     * 
     * @param fd file descriptor of i2c bus
     * @param deviceAddress device address
     * @param size number of bytes to be written 
     * @param offset offset in buffer to read from
     * @param buffer data buffer to be written
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public static native int i2cWriteBytesDirect(int fd, int deviceAddress, int size, int offset, byte[] buffer);

    /**
     * Writes one byte to i2c. It uses ioctl to define device address and then writes two bytes: address in
     * the device itself and value.
     * 
     * @param fd file descriptor of i2c bus
     * @param deviceAddress device address
     * @param localAddress address in the device
     * @param data byte to be written to the device
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public static native int i2cWriteByte(int fd, int deviceAddress, int localAddress, byte data);

    /**
     * Writes several bytes to i2c. It uses ioctl to define device address and then writes number of bytes defined 
     * in size argument plus one.
     * 
     * @param fd file descriptor of i2c bus
     * @param deviceAddress device address
     * @param localAddress address in the device
     * @param size number of bytes to be written 
     * @param offset offset in buffer to read from
     * @param buffer data buffer to be written
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public static native int i2cWriteBytes(int fd, int deviceAddress, int localAddress, int size, int offset, byte[] buffer);

    /**
     * Reads one byte from i2c device. It uses ioctl to define device address and then reads one byte.
     * 
     * @param fd file descriptor of i2c bus
     * @param deviceAddress device address
     * @return positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     */
    public static native int i2cReadByteDirect(int fd, int deviceAddress);

    /**
     * Reads more bytes from i2c device. It uses ioctl to define device address and then reads
     * size number of bytes.
     * 
     * @param fd file descriptor of i2c bus
     * @param deviceAddress device address
     * @param size number of bytes to be read
     * @param offset offset in buffer to stored read data
     * @param buffer buffer for data to be written to
     * @return number of bytes read or negative number if reading failed.
     */
    public static native int i2cReadBytesDirect(int fd, int deviceAddress, int size, int offset, byte[] buffer);

    /**
     * Reads one byte from i2c device. It uses ioctl to define device address, writes addres in device and then reads
     * one byte.
     * 
     * @param fd file descriptor of i2c bus
     * @param deviceAddress device address
     * @param localAddress address in the device
     * @return positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     */
    public static native int i2cReadByte(int fd, int deviceAddress, int localAddress);

    /**
     * Reads more bytes from i2c device. It uses ioctl to define device address, writes addres in device and then reads
     * size number of bytes.
     * 
     * @param fd file descriptor of i2c bus
     * @param deviceAddress device address
     * @param localAddress address in the device
     * @param size number of bytes to be read
     * @param offset offset in buffer to stored read data
     * @param buffer buffer for data to be written to
     * @return number of bytes read or negative number if reading failed.
     */
    public static native int i2cReadBytes(int fd, int deviceAddress, int localAddress, int size, int offset, byte[] buffer);
}
