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
import java.nio.file.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.io.i2c.I2CFactoryProvider;

public class I2CProviderImpl implements I2CFactoryProvider {

    public I2CProviderImpl() {
    }

    public I2CBus getBus(final int busNumber, final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit) throws UnsupportedBusNumberException, IOException {
        Map<Integer, String> busses = new HashMap<Integer, String>();

        DirectoryStream<Path> devices = Files.newDirectoryStream(Paths.get("/sys/bus/i2c/devices"), "*");
        for (Path device: devices) {
            String[] tokens = device.toString().split("-");
            if(tokens.length == 2) {
                busses.put(Integer.valueOf(tokens[1]), "/dev/i2c-" + tokens[1]);
            }
        }

        Integer number = Integer.valueOf(busNumber);
        if(!busses.containsKey(number)) {
            throw new UnsupportedBusNumberException();
        }

        I2CBusImpl result =  new I2CBusImpl(busNumber, busses.get(number), lockAquireTimeout, lockAquireTimeoutUnit);
        result.open();

        return result;
    }
}
