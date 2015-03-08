package com.pi4j.component.potentiometer.microchip;

import com.pi4j.component.potentiometer.microchip.impl.MicrochipPotentiometerBase;
import com.pi4j.io.i2c.I2CBus;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MCP4541.java  
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
 * Pi4J-device for MCP4541.
 * 
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public class MCP4541 extends MicrochipPotentiometerBase implements MicrochipPotentiometer {

	private static final MicrochipPotentiometerChannel[] supportedChannels = new MicrochipPotentiometerChannel[] {
		MicrochipPotentiometerChannel.A
	};
	
	/**
	 * Builds an instance which is ready to use.
	 * 
	 * @param i2cBus The Pi4J-I2CBus to which the device is connected to
	 * @param pinA0 Whether the device's address pin A0 is high (true) or low (false)
	 * @param nonVolatileMode The way non-volatile reads or writes are done
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public MCP4541(final I2CBus i2cBus, final boolean pinA0,
                   final MicrochipPotentiometerNonVolatileMode nonVolatileMode) throws IOException {
		
		super(i2cBus, pinA0, PIN_NOT_AVAILABLE, PIN_NOT_AVAILABLE,
				MicrochipPotentiometerChannel.A, nonVolatileMode, INITIALVALUE_LOADED_FROM_EEPROM);
		
	}
	
	/**
	 * @return Whether device is capable of non volatile wipers (true for MCP4541)
	 */
	@Override
	public boolean isCapableOfNonVolatileWiper() {
		
		return true;
		
	}
	
	/**
	 * @param nonVolatileMode The way non-volatile reads or writes are done
	 */
	@Override
	public void setNonVolatileMode(final MicrochipPotentiometerNonVolatileMode nonVolatileMode) {
		
		super.setNonVolatileMode(nonVolatileMode);
		
	}
	
	/**
	 * @return The maximal value at which a wiper can be (128 for MCP4541)
	 */
	@Override
	public int getMaxValue() {
		
		return maxValue();
		
	}
	
	/**
	 * @return The maximal value at which a wiper can be (128 for MCP4541)
	 */
	public static int maxValue() {
		
		return 128;
		
	}
	
	/**
	 * @return Whether this device is a potentiometer or a rheostat (false for MCP4541)
	 */
	@Override
	public boolean isRheostat() {
		
		return false;
		
	}
	
	/**
	 * @return All channels supported by the underlying device (A only for MCP4541)
	 */
	@Override
	public MicrochipPotentiometerChannel[] getSupportedChannelsByDevice() {
		
		return supportedChannels;
		
	}
	
}
