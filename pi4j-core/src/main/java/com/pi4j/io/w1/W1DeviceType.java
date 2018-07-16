package com.pi4j.io.w1;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  W1DeviceType.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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

/**
 *
 * http://en.wikipedia.org/wiki/1-Wire
 * @author Peter Schuebl
 */
public interface W1DeviceType {

    /**
     * Returns the FID of the W1 device e.g. 0x28 for DS18B20
     *
     * Each device has 48 bit (six bytes) globally unique address where last eight bits are
     * CRC of first 56 bits. First byte stores a device family code, that identifies device type.
     *
     * @return the family id of the device
     */
    int getDeviceFamilyCode();

    /**
     * Gets the implementation class of the device which must be a sub-class of W1Device
     * @return the implementation class
     */
    Class<? extends W1Device> getDeviceClass();

    /**
     * Creates a new instance of a concrete device.
     * @param deviceDir
     * @return
     */
    W1Device create(File deviceDir);

}
