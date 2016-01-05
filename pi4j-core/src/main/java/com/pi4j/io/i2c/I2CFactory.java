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
 * Copyright (C) 2012 - 2016 Pi4J
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
 * I2C factory - it returns instances of {@link I2CBus} interface.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www
 *         .savagehomeautomation.com</a>)
 */
public class I2CFactory
{

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
	public static I2CBus getInstance(int busNumber) throws IOException
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
