package com.pi4j.io.i2c.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CProviderImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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

import java.io.File;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.io.i2c.I2CFactoryProvider;

public class I2CProviderImpl implements I2CFactoryProvider {

    public I2CProviderImpl() {
    }

    public I2CBus getBus(final int busNumber, final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit) throws UnsupportedBusNumberException, IOException {
        final File sysfs = new File("/sys/bus/i2c/devices/i2c-" + busNumber);
        if (!sysfs.exists() || !sysfs.isDirectory()) {
            throw new UnsupportedBusNumberException();
        }

        final File devfs = new File("/dev/i2c-" + busNumber);
        if (!devfs.exists() || !devfs.canRead() || !devfs.canWrite()) {
            throw new UnsupportedBusNumberException();
        }

        I2CBusImpl result = new I2CBusImpl(busNumber, devfs.getCanonicalPath(), lockAquireTimeout, lockAquireTimeoutUnit);
        result.open();

        return result;
    }
}
