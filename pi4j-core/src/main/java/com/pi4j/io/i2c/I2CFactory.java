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

/**
 * I2C factory - it returns instances of {@link I2CBus} interface.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www
 *         .savagehomeautomation.com</a>)
 */
public class I2CFactory {
	
	public static class UnsupportedBusNumberException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public UnsupportedBusNumberException() {
			super();
		}
	}

	volatile static I2CFactoryProvider provider = new I2CFactoryProviderRaspberry();

	// private constructor
	private I2CFactory()
	{
		// forbid object construction
	}

	/**
	 * Create new I2CBus instance
	 * 
	 * @return Return a new I2CBus impl instance.
	 * 
	 * @throws IOException
	 */
	public static I2CBus getInstance(int busNumber)
			throws UnsupportedBusNumberException, IOException
	{
		return provider.getBus(busNumber);
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
