package com.pi4j.component.potentiometer.microchip;


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerChannel.java  
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
 * A channel an instance of MCP45XX_MCP46XX_Potentiometer can be configured for.
 * 
 * @see com.pi4j.component.potentiometer.microchip.impl.MicrochipPotentiometerBase
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public enum MicrochipPotentiometerChannel {
	
	/**
	 * Pins P0A, P0W, P0B
	 */
	A,
	
	/**
	 * Pins P1A, P1W, P1B
	 */
	B,

	/**
	 * Pins P2A, P2W, P2B
	 */
	C,

	/**
	 * Pins P3A, P3W, P3B
	 */
	D;

	@Override
	public String toString() {

		final StringBuffer result = new StringBuffer(getClass().getName());
		result.append(".").append(name());
		return result.toString();

	}
}
