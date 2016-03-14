package com.pi4j.io.i2c.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CDeviceImpl.java
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

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.jni.I2C;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of i2c device. This class only holds reference to i2c bus (so it can use its handle) and
 * device address.
 *
 * @author Daniel Sendula
 *
 */
public class I2CDeviceImpl implements I2CDevice {

    /** Reference to i2c bus */
    private I2CBus bus;

    /** I2c device address */
    private int deviceAddress;

    private final static Lock lock = new ReentrantLock( true );

    /**
     * Constructor.
     *
     * @param bus i2c bus
     * @param address i2c device address
     */
    public I2CDeviceImpl(I2CBus bus, int address) {
        this.bus = bus;
        this.deviceAddress = address;
    }

    /**
     * This method writes one byte to i2c device.
     *
     * @param data byte to be written
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(byte data) throws IOException {
        lock.lock();
        try {
            int ret = I2C.i2cWriteByteDirect(bus.getFileDescriptor(), deviceAddress, data);
            if (ret < 0) {
                throw new IOException("Error writing to " + makeDescription() + ". Got " + ret + ".");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * This method writes several bytes to the i2c device from given buffer at given offset.
     *
     * @param buffer buffer of data to be written to the i2c device in one go
     * @param offset offset in buffer
     * @param size number of bytes to be written
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(byte[] buffer, int offset, int size) throws IOException {
        lock.lock();
        try {
            int ret = I2C.i2cWriteBytesDirect(bus.getFileDescriptor(), deviceAddress, size, offset, buffer);
            if (ret < 0) {
                throw new IOException("Error writing to " + makeDescription() + ". Got " + ret + ".");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * This method writes all bytes included in the given buffer directly to the i2c device.
     *
     * @param buffer buffer of data to be written to the i2c device in one go
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(byte[] buffer) throws IOException {
        write(buffer, 0, buffer.length);
    }

    /**
     * This method writes one byte to i2c device.
     *
     * @param address local address in the i2c device
     * @param data byte to be written
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(int address, byte data) throws IOException {
        lock.lock();
        try {
            int ret = I2C.i2cWriteByte(bus.getFileDescriptor(), deviceAddress, address, data);
            if (ret < 0) {
                throw new IOException("Error writing to " + makeDescription(address) + ". Got " + ret + ".");
            }
        } finally {
            lock.unlock();
        }
    }

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
    @Override
    public void write(int address, byte[] buffer, int offset, int size) throws IOException {
        lock.lock();
        try {
            int ret = I2C.i2cWriteBytes(bus.getFileDescriptor(), deviceAddress, address, size, offset, buffer);
            if (ret < 0) {
                throw new IOException("Error writing to " + makeDescription(address) + ". Got " + ret + ".");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * This method writes all bytes included in the given buffer directoy to the register address on the i2c device
     *
     * @param address local address in the i2c device
     * @param buffer buffer of data to be written to the i2c device in one go
     *
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    public void write(int address, byte[] buffer) throws IOException{
        write(address, buffer, 0, buffer.length);
    }

    /**
     * This method reads one byte from the i2c device.
     * Result is between 0 and 255 if read operation was successful, else a negative number for an error.
     *
     * @return byte value read: positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     *
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read() throws IOException {
        lock.lock();
        try {
            int ret = I2C.i2cReadByteDirect(bus.getFileDescriptor(), deviceAddress);
            if (ret < 0) {
                throw new IOException("Error reading from " + makeDescription() + ". Got " + ret + ".");
            }
            return ret;
        } finally {
            lock.unlock();
        }
    }

    /**
     * <p>This method reads bytes from the i2c device to given buffer at asked offset. </p>
     *
     * <p>Note: Current implementation calls {@link #read(int)}. That means for each read byte
     * i2c bus will send (next) address to i2c device.
     * </p>
     *
     * @param buffer buffer of data to be read from the i2c device in one go
     * @param offset offset in buffer
     * @param size number of bytes to be read
     *
     * @return number of bytes read
     *
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(byte[] buffer, int offset, int size) throws IOException {
        lock.lock();
        try {
            // It doesn't work for some reason.
            int ret = I2C.i2cReadBytesDirect(bus.getFileDescriptor(), deviceAddress, size, offset, buffer);
            if (ret < 0) {
                throw new IOException("Error reading from " + makeDescription() + ". Got " + ret + ".");
            }
            return ret;
        } finally {
            lock.unlock();
        }
    }

    /**
     * This method reads one byte from the i2c device.
     * Result is between 0 and 255 if read operation was successful, else a negative number for an error.
     *
     * @param address local address in the i2c device
     * @return byte value read: positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     *
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(int address) throws IOException {
        lock.lock();
        try {
            int ret = I2C.i2cReadByte(bus.getFileDescriptor(), deviceAddress, address);
            if (ret < 0) {
                throw new IOException("Error reading from " + makeDescription(address) + ". Got " + ret + ".");
            }
            return ret;
        } finally {
            lock.unlock();
        }
    }

    /**
     * <p>This method reads bytes from the i2c device to given buffer at asked offset. </p>
     *
     * <p>Note: Current implementation calls {@link #read(int)}. That means for each read byte
     * i2c bus will send (next) address to i2c device.
     * </p>
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
    @Override
    public int read(int address, byte[] buffer, int offset, int size) throws IOException {
        lock.lock();
        try {
            // It doesn't work for some reason.
            int ret = I2C.i2cReadBytes(bus.getFileDescriptor(), deviceAddress, address, size, offset, buffer);
            if (ret < 0) {
                throw new IOException("Error reading from " + makeDescription(address) + ". Got " + ret + ".");
            }
            return ret;
        } finally {
            lock.unlock();
        }
    }

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
    @Override
    public int read(byte[] writeBuffer, int writeOffset, int writeSize, byte[] readBuffer, int readOffset, int readSize) throws IOException {
        lock.lock();
        try {
            int ret = I2C.i2cWriteAndReadBytes(bus.getFileDescriptor(), deviceAddress, writeSize, writeOffset, writeBuffer, readSize, readOffset, readBuffer);
            if (ret < 0) {
                throw new IOException("Error reading from " + makeDescription() + ". Got " + ret + ".");
            }
            return ret;
        } finally {
            lock.unlock();
        }
    }

    /**
     * This helper method creates a string describing bus file name and device address (in hex).
     *
     * @return string with all details
     */
    protected String makeDescription() {
        return bus.getFileName() + " at address 0x" + Integer.toHexString(deviceAddress);
    }

    /**
     * This helper method creates a string describing bus file name, device address (in hex)
     * and local i2c address.
     *
     * @param address local address in i2c device
     * @return string with all details
     */
    protected String makeDescription(int address) {
        return bus.getFileName() + " at address 0x" + Integer.toHexString(deviceAddress)
                + " to address 0x" + Integer.toHexString(address);
    }

}

