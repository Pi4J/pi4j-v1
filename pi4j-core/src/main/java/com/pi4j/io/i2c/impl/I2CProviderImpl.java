package com.pi4j.io.i2c.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CProviderImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
