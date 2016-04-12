package com.pi4j.io.i2c.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CProviderImpl.java
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.io.i2c.I2CFactoryProvider;

public abstract class I2CProviderImpl implements I2CFactoryProvider {

    /** Singletons */
    private static final Map<Integer, I2CBus> busSingletons = new HashMap<>();

    /** to lock the creation/destruction of the bus singletons */
    private static final Lock singletonPerBusLock = new ReentrantLock(true);

    /**
     * Factory method that returns bus implementation.
     *
     * @param newInstanceCandidate if no bus has been created yet, this instance is used
     * @return appropriate bus implementation
     * @throws IOException thrown in case there is a problem opening bus file or bus number is not 0 or 1.
     */
    protected static I2CBus getBus(int busNumber, Callable<I2CBusImpl> constructor, long lockAquireTimeout, TimeUnit lockAquireTimeoutUnit) throws UnsupportedBusNumberException, IOException {
        InterruptedException lockException = null;
        try {
            if (singletonPerBusLock.tryLock(lockAquireTimeout, lockAquireTimeoutUnit)) {
                I2CBus bus;
                try {
                    bus = busSingletons.get(busNumber);

                    if (bus == null) {
                        I2CBusImpl newInstanceCandidate = constructor.call();
                        newInstanceCandidate.open();
                        bus = newInstanceCandidate;
                        busSingletons.put(newInstanceCandidate.busNumber, bus);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Could not instantiate I2CBus", e);
                } finally {
                    singletonPerBusLock.unlock();
                }
                return bus;
            }
        } catch (InterruptedException e) {
            lockException = e;
        }
        throw new RuntimeException("Could not abtain lock to build new bus!", lockException);
    }

    protected static void closeBus(int busNumber, long lockAquireTimeout, TimeUnit lockAquireTimeoutUnit, Callable<Void> closeAction) {
        InterruptedException lockException = null;
        try {
            if (singletonPerBusLock.tryLock(lockAquireTimeout, lockAquireTimeoutUnit)) {
                try {
                    closeAction.call();
                    return;
                } catch (Exception e) {
                    throw new RuntimeException("Cannot close bus", e);
                } finally {
                    //  after closing the fd, we must "forget" the singleton bus instance, otherwise further request to this bus will always fail
                    try {
                        busSingletons.remove(busNumber);
                    } finally {
                        singletonPerBusLock.unlock();
                    }
                }
            }
        } catch (InterruptedException e) {
            lockException = e;
        }

        throw new RuntimeException("Could not abtain lock to close the bus!", lockException);
    }

    public I2CBus getBus(final int busNumber, final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit) throws UnsupportedBusNumberException, IOException {
        return getBus(busNumber, new Callable<I2CBusImpl>() {
            @Override
            public I2CBusImpl call() throws UnsupportedBusNumberException {
                return new I2CBusImpl(busNumber, getFilenameForBusnumber(busNumber), lockAquireTimeout, lockAquireTimeoutUnit);
            }
        }, lockAquireTimeout, lockAquireTimeoutUnit);
    }

    protected abstract String getFilenameForBusnumber(int busNumber) throws UnsupportedBusNumberException;
}
