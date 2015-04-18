package com.pi4j.io.i2c;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CBus.java  
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

/**
 * This is abstraction of i2c bus. This interface allows bus to return i2c device.
 * 
 * @author Daniel Sendula
 *
 */

public interface I2CBus {

    public static final int BUS_0 = 0;
    public static final int BUS_1 = 1;

    /**
     * Returns i2c device.
     * @param address i2c device's address
     * @return i2c device
     * 
     * @throws IOException thrown in case this bus cannot return i2c device.
     */
    I2CDevice getDevice(int address) throws IOException;
    
    String getFileName();
    
    int getFileDescriptor();
    
    /**
     * Closes this bus. This usually means closing underlying file.
     * 
     * @throws IOException thrown in case there are problems closing this i2c bus.
     */
    void close() throws IOException;
}
