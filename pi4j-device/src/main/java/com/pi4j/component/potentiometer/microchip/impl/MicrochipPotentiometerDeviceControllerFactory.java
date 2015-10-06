package com.pi4j.component.potentiometer.microchip.impl;

import com.pi4j.io.i2c.I2CDevice;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerDeviceControllerFactory.java  
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

/**
 * Factory which builds controller-instances. Usually the user does
 * not need to know about the controller-factory. It is used to
 * build testable potentiometers used by the JUnit-tests.
 * 
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public interface MicrochipPotentiometerDeviceControllerFactory {

	/**
	 * @param i2cDevice The underlying I2CDevice
	 * @return The controller built
	 * @throws IOException Thrown if any communication to the device fails
	 */
	public MicrochipPotentiometerDeviceController getController(final I2CDevice i2cDevice)
			throws IOException;
	
}
