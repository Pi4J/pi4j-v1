package com.pi4j.io.i2c;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CBus.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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
