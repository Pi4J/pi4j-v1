package com.pi4j.component.potentiometer.microchip.impl;


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerDeviceStatusImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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

		final StringBuffer result = new StringBuffer(MicrochipPotentiometerDeviceStatus.class.getName());
		result.append("{\n");
		result.append("  eepromWriteActive='").append(eepromWriteActive);
		result.append("',\n  eepromWriteProtected='").append(eepromWriteProtected);
		result.append("',\n  wiperLockChannel='").append(wiperLockChannel);
		result.append("',\n  wiperLockActive='").append(wiperLockActive);
		result.append("'\n}");
		return result.toString();

	}

}
