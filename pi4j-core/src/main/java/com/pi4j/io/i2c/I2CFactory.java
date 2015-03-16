package com.pi4j.io.i2c;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CFactory.java  
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
import java.util.concurrent.TimeUnit;

/**
 * I2C factory - it returns instances of {@link I2CBus} interface.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www
 *         .savagehomeautomation.com</a>)
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

	volatile static I2CFactoryProvider provider = new I2CFactoryProviderRaspberryPi();

	// private constructor
	private I2CFactory()
	{
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
	 * @see I2CProvider#DEFAULT_LOCKAQUIRE_TIMEOUT
	 * @see I2CProvider#DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS
	 */
	public static I2CBus getInstance(int busNumber)
			throws UnsupportedBusNumberException, IOException
	{
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
	public static I2CBus getInstance(int busNumber,
			long lockAquireTimeout, TimeUnit lockAquireTimeoutUnit)
			throws UnsupportedBusNumberException, IOException
	{
		return provider.getBus(busNumber, lockAquireTimeout, lockAquireTimeoutUnit);
	}
	
	/**
	 * allow changing the provider for the factory
	 * @param factoryProvider
	 */
	public static void setFactory(I2CFactoryProvider factoryProvider)
	{
		provider = factoryProvider;
	}
	
}
