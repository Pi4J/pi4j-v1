package com.pi4j.component.potentiometer.microchip;


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
 * The device-status concerning the channel this
 * instance of MCP45XX_MCP46XX_Potentiometer is configured for.
 * 
 * @see com.pi4j.component.potentiometer.microchip.impl.MicrochipPotentiometerBase
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public interface MicrochipPotentiometerDeviceStatus {
	
	/**
	 * @return Whether the device is writing to EEPROM at the moment
	 */
	public boolean isEepromWriteActive();

	/**
	 * @return Whether EEPROM is write-protected
	 */
	public boolean isEepromWriteProtected();
	
	/**
	 * @return The channel the wiper-lock-active status is for
	 */
	public MicrochipPotentiometerChannel getWiperLockChannel();

	/**
	 * @return Whether the wiper's lock is active
	 */
	public boolean isWiperLockActive();
}
