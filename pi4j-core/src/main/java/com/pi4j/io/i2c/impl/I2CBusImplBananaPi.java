package com.pi4j.io.i2c.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CBusImplBananaPi.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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
import java.util.concurrent.TimeUnit;

/**
 * This is implementation of i2c bus. This class keeps underlying linux file descriptor of
 * particular bus. As all reads and writes from/to i2c bus are blocked I/Os current implementation uses only
 * one file per bus for all devices. Device implementations use this class file handle.
 *
 * @author Daniel Sendula
 *
 */
public class I2CBusImplBananaPi extends I2CBusImpl {

    public static I2CBusImplBananaPi getBus(int busNumber, long lockAquireTimeout, TimeUnit lockAquireTimeoutUnit) throws UnsupportedBusNumberException, IOException {
        return (I2CBusImplBananaPi) I2CBusImpl.getBus(new I2CBusImplBananaPi(busNumber, lockAquireTimeout, lockAquireTimeoutUnit));
    }

    private I2CBusImplBananaPi(final int busNumber, final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit) throws UnsupportedBusNumberException, IOException {
        super(busNumber, lockAquireTimeout, lockAquireTimeoutUnit);
    }

    @Override
    protected String getFilenameForBusnumber(int busNumber) throws UnsupportedBusNumberException {
        if ((busNumber < 0) || (busNumber > 3)) {
            throw new UnsupportedBusNumberException();
        }

        return "/dev/i2c-" + busNumber;
    }
}
