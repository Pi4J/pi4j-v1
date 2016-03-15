package com.pi4j.component.potentiometer.microchip.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  DeviceControllerDeviceStatus.java  
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

/**
 * The device's status
 */
class DeviceControllerDeviceStatus {
	
	private boolean eepromWriteActive;
	private boolean eepromWriteProtected;
	private boolean channelALocked;
	private boolean channelBLocked;
	
	public DeviceControllerDeviceStatus(boolean eepromWriteActive, boolean eepromWriteProtected,
                                        boolean channelALocked, boolean channelBLocked) {
		this.eepromWriteActive = eepromWriteActive;
		this.eepromWriteProtected = eepromWriteProtected;
		this.channelALocked = channelALocked;
		this.channelBLocked = channelBLocked;
	}

	/**
	 * Writing to EEPROM takes a couple of cycles. During this time
	 * any actions taken on non-volatile wipers fail due to active
	 * writing.
	 * 
	 * @return Whether writing to EEPROM is active
	 */
	public boolean isEepromWriteActive() {
		return eepromWriteActive;
	}

	/**
	 * @return Whether EEPROM write-protection is enabled
	 */
	public boolean isEepromWriteProtected() {
		return eepromWriteProtected;
	}

	/**
	 * @return Whether wiper of channel 'A' is locked
	 */
	public boolean isChannelALocked() {
		return channelALocked;
	}

	/**
	 * @return Whether wiper of channel 'B' is locked
	 */
	public boolean isChannelBLocked() {
		return channelBLocked;
	}
	
}
