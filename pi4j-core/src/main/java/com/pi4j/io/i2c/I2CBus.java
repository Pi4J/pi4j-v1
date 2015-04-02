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

import com.pi4j.io.i2c.impl.IOExceptionWrapperException;

/**
 * This is abstraction of i2c bus. This interface allows bus to return i2c device.
 * 
 * @author Daniel Sendula, refactored by <a href="http://raspelikan.blogspot.co.at">RasPelikan</a>
 */
public interface I2CBus {

	/**
	 * Base-class for parameter 'action' of method 'runActionOnExclusivLockedBus'.
	 *  
	 * @param <T> The result-type
	 * @see I2CBus#runActionOnExclusivLockedBus(I2CRunnable)
	 * @see IOExceptionWrapperException
	 */
	static abstract class I2CRunnable<T> implements Runnable {
		
		/**
		 * May be used as generic-type if there should be a void-result
		 */
		public static final class Void extends Object { };
		
		/**
		 * The value which has to be returned by 'runActionOnExclusivLockedBus'.
		 * Set this property within the 'run'-method.
		 */
		protected T result;
		
		public T getResult() {
			return result;
		}
		
	}

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
     * one getting the I2CBus from I2CFactory.
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
    <T> T runActionOnExclusivLockedBus(I2CRunnable<T> action) throws IOException;
    
}
