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
