package com.pi4j.io.i2c;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CBus.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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

/**
 * This is abstraction of i2c bus. This interface allows the bus to return i2c device.
 *
 * @author Daniel Sendula, refactored by <a href="http://raspelikan.blogspot.co.at">RasPelikan</a>
 */
public interface I2CBus {

    int BUS_0 = 0;
    int BUS_1 = 1;
    int BUS_2 = 2;
    int BUS_3 = 3;
    int BUS_4 = 4;
    int BUS_5 = 5;
    int BUS_6 = 6;
    int BUS_7 = 7;
    int BUS_8 = 8;
    int BUS_9 = 9;
    int BUS_10 = 10;
    int BUS_11 = 11;
    int BUS_12 = 12;
    int BUS_13 = 13;
    int BUS_14 = 14;
    int BUS_15 = 15;
    int BUS_16 = 16;
    int BUS_17 = 17;

    /**
     * Returns i2c device.
     * @param address i2c device's address
     * @return i2c device
     *
     * @throws IOException thrown in case this bus cannot return i2c device.
     */
    I2CDevice getDevice(int address) throws IOException;

    /**
     * @return The bus' number
     */
    int getBusNumber();

    /**
     * Closes this bus. This usually means closing underlying file.
     *
     * @throws IOException thrown in case there are problems closing this i2c bus.
     */
    void close() throws IOException;
}
