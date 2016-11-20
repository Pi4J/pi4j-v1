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
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.i2c.*;
import com.pi4j.io.file.LinuxFile;

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
    protected LinuxFile file = null;

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
        if (file != null) {
            return;
        }

        file = new LinuxFile(filename, "rw");

        lastAddress = -1;
    }

    /**
     * Closes this i2c bus. Can be used in a thread safe way during bus operations.
     *
     * @throws IOException never in this implementation
     */
    @Override
    public synchronized void close() throws IOException {
        if(file != null) {
            file.close();
            file = null;
        }
    }

    public int readByteDirect(final I2CDevice device) throws IOException {
        return runBusLockedDeviceAction(device, () -> file.readUnsignedByte());
    }

    public int readBytesDirect(final I2CDevice device, final int size, final int offset, final byte[] buffer) throws IOException {
        return runBusLockedDeviceAction(device, () -> file.read(buffer, offset, size));
    }

    public int readByte(final I2CDevice device, final int localAddress) throws IOException {
        return runBusLockedDeviceAction(device, () -> {
            file.writeByte(localAddress);

            return file.readUnsignedByte();
        });
    }

    public int readBytes(final I2CDevice device, final int localAddress, final int size, final int offset, final byte[] buffer) throws IOException {
        return runBusLockedDeviceAction(device, () -> {
            file.writeByte(localAddress);

            return file.read(buffer, offset, size);
        });
    }

    public void writeByteDirect(final I2CDevice device, final byte data) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            file.writeByte(data & 0xFF);

            return null;
        });
    }

    public void writeBytesDirect(final I2CDevice device, final int size, final int offset, final byte[] buffer) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            file.write(buffer, offset, size);

            return null;
        });
    }

    public void writeByte(final I2CDevice device, final int localAddress, final byte data) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            file.write(new byte[] { (byte)localAddress, data });

            return null;
        });
    }

    public void writeBytes(final I2CDevice device, final int localAddress, final int size, final int offset, final byte[] buffer) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            byte[] buf = new byte[size + 1];

            buf[0] = (byte)localAddress;

            for(int i = 0 ; i < size ; i++)
                buf[i + 1] = buffer[i + offset];

            file.write(buf);

            return null;
        });
    }

    public int writeAndReadBytesDirect(final I2CDevice device, final int writeSize, final int writeOffset, final byte[] writeBuffer,
                                       final int readSize, final int readOffset, final byte[] readBuffer) throws IOException {
        return runBusLockedDeviceAction(device, () -> {
            file.write(writeBuffer, writeOffset, writeSize);

            return file.read(readBuffer, readOffset, readSize);
        });
    }

    public void ioctl(final I2CDevice device, final long command, final int value) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            file.ioctl(command, value);

            return null;
        });
    }

    public void ioctl(final I2CDevice device, final long command, final ByteBuffer values, final IntBuffer offsets) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            file.ioctl(command, values, offsets);

            return null;
        });
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
    public <T> T runBusLockedDeviceAction(final I2CDevice device, final Callable<T> action) throws IOException {
        if (action == null) {
            throw new NullPointerException("Parameter 'action' is mandatory!");
        }

        testForProperOperationConditions(device);

        try {
            if (accessLock.tryLock(lockAquireTimeout, lockAquireTimeoutUnit)) {
                try {
                    testForProperOperationConditions(device);

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

    /**
     * Selects the slave device if not already selected on this bus.
     * Uses SharedSecrets to get the POSIX file descriptor, and runs
     * the required ioctl's via JNI.
     *
     * @param device Device to select
     */
    protected void selectBusSlave(final I2CDevice device) throws IOException {
        final int addr = device.getAddress();

        if(lastAddress != addr) {
            lastAddress = addr;

            file.ioctl(I2CConstants.I2C_SLAVE, addr & 0xFF);
        }
    }

    protected void testForProperOperationConditions(final I2CDevice device) throws IOException {
        if (file == null) {
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
