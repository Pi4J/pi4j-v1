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
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * Select the slave device to be target by the bus.

     * @param fd file descriptor
     * @param deviceAddress device address
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public static native int i2cSlaveSelect(int fd, int deviceAddress);
}
