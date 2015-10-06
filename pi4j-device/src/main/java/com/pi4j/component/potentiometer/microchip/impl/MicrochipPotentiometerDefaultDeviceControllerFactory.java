package com.pi4j.component.potentiometer.microchip.impl;

import com.pi4j.io.i2c.I2CDevice;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerDefaultDeviceControllerFactory.java  
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
 * Default-factory which is used by the potentiometer's constructor which
 * hides the 'controllerFactory'-parameter.
 * 
 * @see com.pi4j.component.potentiometer.microchip.impl.MicrochipPotentiometerDeviceControllerFactory
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public class MicrochipPotentiometerDefaultDeviceControllerFactory
		implements MicrochipPotentiometerDeviceControllerFactory {

	/**
	 * A static instance
	 */
	private static final MicrochipPotentiometerDeviceControllerFactory defaultFactory
			= new MicrochipPotentiometerDefaultDeviceControllerFactory();
	
	/**
	 * @return The static instance
	 */
	public static MicrochipPotentiometerDeviceControllerFactory getInstance() {
		
		return defaultFactory;
		
	}
	
	/**
	 * @param i2cDevice The underlying device
	 * @return The controller for the given device
	 */
	@Override
	public MicrochipPotentiometerDeviceController getController(final I2CDevice i2cDevice)
			throws IOException {
		
		return new MicrochipPotentiometerDeviceController(i2cDevice);
		
	}
	
}
