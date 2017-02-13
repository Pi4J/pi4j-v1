package com.pi4j.jni;


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SoftSPI.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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
 * This class exposes the call to the SoftSPI read/write function.
 * It takes the pins, speed and clock definitions and writes the
 * values in data, returning the data read from the SPI port in
 * an array with the same dimensions.
 * 
 * @author Tommy « LeChat » Savaria
 */
public class SoftSPI {
	/**
	 * Read/Write on the SPI Port.
	 * @param cs Chip Select Pin
	 * @param clk Clock Pin
	 * @param mosi Master Out Slave In Pin
	 * @param miso Master In Slave Out Pin
	 * @param speed Bus speed (in bits per second)
	 * @param cpol Clock Polarity
	 * @param cpha Clock Phase
	 * @param data Output data
	 * @return Input data
	 */
	public static native byte[] readWrite(int cs, int clk, int mosi, int miso, long speed, boolean cpol, boolean cpha, byte[] data);
}
