package com.pi4j.component.potentiometer.impl.microchip;

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

/**
 * Default-factory which is used by the potentiometer's constructor which
 * hides the 'controllerFactory'-parameter.
 * 
 * @see MicrochipPotentiometerDeviceControllerFactory
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
