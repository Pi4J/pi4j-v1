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
import java.util.concurrent.Callable;

/**
 * This is abstraction of i2c bus. This interface allows the bus to return i2c device.
 * 
 * @author Daniel Sendula, refactored by <a href="http://raspelikan.blogspot.co.at">RasPelikan</a>
 */
public interface I2CBus {

    public static final int BUS_0 = 0;
    public static final int BUS_1 = 1;
    public static final int BUS_2 = 2;
    public static final int BUS_3 = 3;

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
    
    /**
     * Sometimes communication to an i2c device must not be disturbed by
     * communication to another i2c device. This method can be used to run
     * a custom sequence of writes/reads.
     * <p>
     * The timeout used for the acquisition of the lock may be defined
     * on getting the I2CBus from I2CFactory.
	 * <p>
	 * The 'run'-method of 'action' may throw an 'IOExceptionWrapperException'
	 * to wrap IOExceptions. The wrapped IOException is unwrapped by this method
	 * and rethrown as IOException.
     *  
     * @param <T> The result-type of the method
     * @param action The action to be run
     * @throws RuntimeException thrown by the custom code
     * @throws IOException see method description above
     * @see I2CFactory#getInstance(int, long, java.util.concurrent.TimeUnit)
     */
    <T> T runActionOnExclusivLockedBus(Callable<T> action) throws IOException;
    
}
