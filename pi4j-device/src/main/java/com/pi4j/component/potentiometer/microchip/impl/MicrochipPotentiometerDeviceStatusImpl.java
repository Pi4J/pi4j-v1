package com.pi4j.component.potentiometer.microchip.impl;


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerDeviceStatus.java  
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

import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerChannel;
import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerDeviceStatus;

/**
 * The device-status concerning the channel this
 * instance of MCP45XX_MCP46XX_Potentiometer is configured for.
 * 
 * @see MicrochipPotentiometerBase
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public class MicrochipPotentiometerDeviceStatusImpl implements MicrochipPotentiometerDeviceStatus {

	private boolean eepromWriteActive;
	private boolean eepromWriteProtected;
	private MicrochipPotentiometerChannel wiperLockChannel;
	private boolean wiperLockActive;

	/**
	 * Visibility 'package' should hide the constructor for users.
	 */
	MicrochipPotentiometerDeviceStatusImpl(
            final boolean eepromWriteActive,
            final boolean eepromWriteProtected,
            final MicrochipPotentiometerChannel wiperLockChannel,
            final boolean wiperLockActive) {
		
		if (wiperLockChannel == null) {
			throw new RuntimeException("null-wiperLockChannel is not allowed. For devices "
					+ "knowing just one wiper Channel.A is mandatory for "
					+ "parameter 'channel'");
		}
		
		this.eepromWriteActive = eepromWriteActive;
		this.eepromWriteProtected = eepromWriteProtected;
		this.wiperLockChannel = wiperLockChannel;
		this.wiperLockActive = wiperLockActive;
		
	}

	/**
	 * @return Whether the device is writing to EEPROM at the moment
	 */
	public boolean isEepromWriteActive() {
		return eepromWriteActive;
	}

	/**
	 * @return Whether EEPROM is write-protected
	 */
	public boolean isEepromWriteProtected() {
		return eepromWriteProtected;
	}
	
	/**
	 * @return The channel the wiper-lock-active status is for
	 */
	public MicrochipPotentiometerChannel getWiperLockChannel() {
		return wiperLockChannel;
	}

	/**
	 * @return Whether the wiper's lock is active
	 */
	public boolean isWiperLockActive() {
		return wiperLockActive;
	}
	
	@Override
	public boolean equals(final Object obj) {
		
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		final MicrochipPotentiometerDeviceStatusImpl other = (MicrochipPotentiometerDeviceStatusImpl) obj;
		if (eepromWriteActive != other.eepromWriteActive) {
			return false;
		}
		if (eepromWriteProtected != other.eepromWriteProtected) {
			return false;
		}
		if (wiperLockChannel != other.wiperLockChannel) {
			return false;
		}
		if (wiperLockActive != other.wiperLockActive) {
			return false;
		}
		return true;
		
	}
	
	@Override
	public String toString() {
		
		final StringBuffer result = new StringBuffer(getClass().getName());
		result.append("{\n");
		result.append("  eepromWriteActive='").append(eepromWriteActive);
		result.append("',\n  eepromWriteProtected='").append(eepromWriteProtected);
		result.append("',\n  wiperLockChannel='").append(wiperLockChannel);
		result.append("',\n  wiperLockActive='").append(wiperLockActive);
		result.append("'\n}");
		return result.toString();
		
	}
	
}
