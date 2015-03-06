package com.pi4j.component.potentiometer.impl.microchip;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerDeviceControllerDeviceStatus.java  
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
 * The device's status
 */
class MicrochipPotentiometerDeviceControllerDeviceStatus {
	
	private boolean eepromWriteActive;
	private boolean eepromWriteProtected;
	private boolean channelALocked;
	private boolean channelBLocked;
	
	public MicrochipPotentiometerDeviceControllerDeviceStatus(boolean eepromWriteActive, boolean eepromWriteProtected,
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
