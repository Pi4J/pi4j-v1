package com.pi4j.io.i2c.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CBusImpl.java
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

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CIOException;
import com.pi4j.jni.I2C;

/**
 * This is implementation of i2c bus. This class keeps underlying linux file descriptor of particular bus. As all reads and writes from/to i2c bus are blocked I/Os current implementation uses only one file per bus for all devices. Device
 * implementations use this class file handle.
 *
 * Hint: For concurrency-locking the methods lock() and unlock() are provided. This requires that there is exactly one I2CBus-instance per bus-number what is guaranteed by the I2CFactory class. The locking is done by I2CDeviceImpl by using
 * those methods. The reason for this is to enable other locking-strategies than the simple "lock before and release after access"-strategy.
 *
 * @author Daniel Sendula, refactored by <a href="http://raspelikan.blogspot.co.at">RasPelikan</a>
 *
 */
public class I2CBusImpl implements I2CBus {

    private static final Logger logger = Logger.getLogger(I2CBusImpl.class.getCanonicalName());

    /** File handle for this i2c bus */
    protected int fd = -1;

    protected int lastAddress = -1;

    /** File name of this i2c bus */
    protected String filename;

    /** Used to identifiy the i2c bus within Pi4J **/
    protected int busNumber;

    protected long lockAquireTimeout;

    protected TimeUnit lockAquireTimeoutUnit;

    private final ReentrantLock accessLock = new ReentrantLock(true);

    /**
     * Constructor of i2c bus implementation.
     *
     * @param busNumber used to identifiy the i2c bus within Pi4J

     * @throws IOException thrown in case that file cannot be opened
     */
    protected I2CBusImpl(final int busNumber, final String fileName, final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit) {
        this.filename = fileName;
        this.busNumber = busNumber;

        if (lockAquireTimeout < 0) {
            this.lockAquireTimeout = I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT;
        } else {
            this.lockAquireTimeout = lockAquireTimeout;
        }

        if (lockAquireTimeoutUnit == null) {
            this.lockAquireTimeoutUnit = I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS;
        } else {
            this.lockAquireTimeoutUnit = lockAquireTimeoutUnit;
        }
    }

    /**
     * Returns i2c device implementation ({@link I2CDeviceImpl}).
     *
     * @param address address of i2c device
     *
     * @return implementation of i2c device with given address
     *
     * @throws IOException never in this implementation
     */
    @Override
    public I2CDevice getDevice(int address) throws IOException {
        return new I2CDeviceImpl(this, address);
    }

    /**
     * Opens the bus.
     *
     * @throws IOException thrown in case there are problems opening the i2c bus.
     */
    protected void open() throws IOException {
        if (fd != -1) {
            return;
        }

        fd = I2C.i2cOpen(filename);
        if (fd < 0) {
            throw new IOException("Cannot open file handle for " + filename + " got " + fd + " back.");
        }

        lastAddress = -1;
    }

    /**
     * Closes this i2c bus. Can be used in a thread safe way during bus operations.
     *
     * @throws IOException never in this implementation
     */
    @Override
    public synchronized void close() throws IOException {
        if (fd == -1) {
            return;
        }

        I2C.i2cClose(fd);
        fd = -1;
    }

    public int readByteDirect(final I2CDeviceImpl device) throws IOException {
        return runBusLockedDeviceAction(device, () -> I2C.i2cReadByteDirect(fd));
    }

    public int readBytesDirect(final I2CDeviceImpl device, final int size, final int offset, final byte[] buffer) throws IOException {
        validateBufferOffsets(buffer, offset, size);

        return runBusLockedDeviceAction(device, () -> I2C.i2cReadBytesDirect(fd, size, offset, buffer));
    }

    public int readByte(final I2CDeviceImpl device, final int localAddress) throws IOException {
        return runBusLockedDeviceAction(device, () -> I2C.i2cReadByte(fd, localAddress));
    }

    public int readBytes(final I2CDeviceImpl device, final int localAddress, final int size, final int offset, final byte[] buffer) throws IOException {
        validateBufferOffsets(buffer, offset, size);

        return runBusLockedDeviceAction(device, () -> I2C.i2cReadBytes(fd, localAddress, size, offset, buffer));
    }

    public int writeByteDirect(final I2CDeviceImpl device, final byte data) throws IOException {
        return runBusLockedDeviceAction(device, () -> I2C.i2cWriteByteDirect(fd, data));
    }

    public int writeBytesDirect(final I2CDeviceImpl device, final int size, final int offset, final byte[] buffer) throws IOException {
        validateBufferOffsets(buffer, offset, size);

        return runBusLockedDeviceAction(device, () -> I2C.i2cWriteBytesDirect(fd, size, offset, buffer));
    }

    public int writeByte(final I2CDeviceImpl device, final int localAddress, final byte data) throws IOException {
        return runBusLockedDeviceAction(device, () -> I2C.i2cWriteByte(fd, localAddress, data));
    }

    public int writeBytes(final I2CDeviceImpl device, final int localAddress, final int size, final int offset, final byte[] buffer) throws IOException {
        validateBufferOffsets(buffer, offset, size);

        return runBusLockedDeviceAction(device, () -> I2C.i2cWriteBytes(fd, localAddress, size, offset, buffer));
    }

    public int writeAndReadBytesDirect(final I2CDeviceImpl device, final int writeSize, final int writeOffset, final byte[] writeBuffer,
                                       final int readSize, final int readOffset, final byte[] readBuffer) throws IOException {
        validateBufferOffsets(writeBuffer, writeOffset, writeSize);
        validateBufferOffsets(readBuffer, readOffset, readSize);

        return runBusLockedDeviceAction(device, () ->
                I2C.i2cWriteAndReadBytes(fd, writeSize, writeOffset, writeBuffer, readSize, readOffset, readBuffer));
    }

    /**
     * Selects a device on the bus for an action, and locks parallel access around file descriptor operations.
     * Multiple bus instances may be used in parallel, but a single bus instance must limit parallel access.
     * <p>
     * The timeout used for the acquisition of the lock may be defined on getting the I2CBus from I2CFactory.
     * <p>
     * The 'run'-method of 'action' may throw an 'IOExceptionWrapperException' to wrap IOExceptions. The wrapped IOException is unwrapped by this method and rethrown as IOException.
     *
     * @param <T> The result-type of the method
     * @param device Device to be selected on the bus
     * @param action The action to be run
     * @throws RuntimeException thrown by the custom code
     * @throws IOException see method description above
     * @see I2CFactory#getInstance(int, long, java.util.concurrent.TimeUnit)
     */
    protected <T> T runBusLockedDeviceAction(final I2CDeviceImpl device, final Callable<T> action) throws IOException {
        if (action == null) {
            throw new NullPointerException("Parameter 'action' is mandatory!");
        }

        testForProperOperationConditions(device);

        try {
            if (accessLock.tryLock(lockAquireTimeout, lockAquireTimeoutUnit)) {
                try {
                    selectBusSlave(device);
                    
                    return action.call();
                } finally {
                    accessLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            logger.log(Level.FINER, "Failed locking I2CBusImpl-" + busNumber, e);
            throw new RuntimeException("Could not obtain an access-lock!", e);
        } catch (IOException e) { // unwrap IOExceptionWrapperException
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) { // unexpected exceptions
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Could not obtain an access-lock!");
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }

    protected void validateBufferOffsets(final byte[] data, final int offset, final int size) {
        if(offset < 0)
            throw new IllegalArgumentException("offset must be non-negative");

        if(size < 0)
            throw new IllegalArgumentException("size must be non-negative");

        if(data == null)
            throw new NullPointerException("must provide data buffer!");

        if((offset + size) > data.length)
            throw new IndexOutOfBoundsException("buffer overrun");
    }

    /**
     * Selects the slave device if not already selected on this bus.
     *
     * @param device Device to select
     */
    protected void selectBusSlave(final I2CDeviceImpl device) throws IOException {
        final int addr = device.getAddress();

        if(lastAddress != addr) {
            lastAddress = addr;
            final int response = I2C.i2cSlaveSelect(fd, addr);

            if(response < 0)
                throw new I2CIOException("Failed to select slave device!", response);
        }
    }

    protected void testForProperOperationConditions(final I2CDeviceImpl device) throws IOException {
        if (fd == -1) {
            throw new IOException(toString() + " has already been closed! A new bus has to be acquired.");
        }

        if (device == null) {
            throw new NullPointerException("Parameter 'device' is mandatory!");
        }
    }

    @Override
    public int getBusNumber() {
        return busNumber;
    }

    @Override
    public String toString() {
        return "I2CBus '" + busNumber + "' ('" + filename + "')";
    }
}
