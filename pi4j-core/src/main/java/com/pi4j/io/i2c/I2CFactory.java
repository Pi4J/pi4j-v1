package com.pi4j.io.i2c;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CFactory.java
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.pi4j.io.i2c.impl.I2CProviderImpl;

/**
 * I2C factory - it returns instances of {@link I2CBus} interface.
 *
 * @author Robert Savage (<a href="http://www.savagehomeautomation.com">http://www .savagehomeautomation.com</a>)
 */
public class I2CFactory {

    public static final long DEFAULT_LOCKAQUIRE_TIMEOUT = 1000;

    public static final TimeUnit DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS = TimeUnit.MILLISECONDS;

    public static class UnsupportedBusNumberException extends Exception {
        private static final long serialVersionUID = 1L;

        public UnsupportedBusNumberException() {
            super();
        }
    }

    volatile static I2CFactoryProvider provider = new I2CProviderImpl();

    // private constructor
    private I2CFactory() {
        // forbid object construction
    }

    /**
     * Create new I2CBus instance.
     * <p>
     * The timeout for locking the bus for exclusive communication is set to DEFAULT_LOCKAQUIRE_TIMEOUT.
     *
     * @param busNumber The bus number
     * @return Return a new I2CBus instance
     * @throws UnsupportedBusNumberException If the given bus-number is not supported by the underlying system
     * @throws IOException If communication to i2c-bus fails
     * @see I2CFactory#DEFAULT_LOCKAQUIRE_TIMEOUT
     * @see I2CFactory#DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS
     */
    public static I2CBus getInstance(int busNumber) throws UnsupportedBusNumberException, IOException {
        return provider.getBus(busNumber, DEFAULT_LOCKAQUIRE_TIMEOUT, DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS);
    }

    /**
     * Create new I2CBus instance.
     *
     * @param busNumber The bus number
     * @param lockAquireTimeout The timeout for locking the bus for exclusive communication
     * @param lockAquireTimeoutUnit The units of lockAquireTimeout
     * @return Return a new I2CBus instance
     * @throws UnsupportedBusNumberException If the given bus-number is not supported by the underlying system
     * @throws IOException If communication to i2c-bus fails
     */
    public static I2CBus getInstance(int busNumber, long lockAquireTimeout, TimeUnit lockAquireTimeoutUnit) throws UnsupportedBusNumberException, IOException {
        return provider.getBus(busNumber, lockAquireTimeout, lockAquireTimeoutUnit);
    }

    /**
     * allow changing the provider for the factory
     *
     * @param factoryProvider
     */
    public static void setFactory(I2CFactoryProvider factoryProvider) {
        provider = factoryProvider;
    }

    /**
     * Fetch all available I2C bus numbers from sysfs.
     * Returns null, if nothing was found.
     *
     * @return Return found I2C bus numbers or null
     * @throws IOException If fetching from sysfs interface fails
     */
    public static int[] getBusIds() throws IOException {
        Set<Integer> set = null;
        for (Path device: Files.newDirectoryStream(Paths.get("/sys/bus/i2c/devices"), "*")) {
            String[] tokens = device.toString().split("-");
            if (tokens.length == 2) {
                if (set == null) {
                    set = new HashSet<Integer>();
                }
                set.add(Integer.valueOf(tokens[1]));
            }
        }

        int[] result = null;
        if (set != null) {
            int counter = 0;
            result = new int[set.size()];
            for (Integer value : set) {
                result[counter] = value.intValue();
                counter = counter + 1;
            }
        }
        return result;
    }
}
