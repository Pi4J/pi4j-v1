package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Spi.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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

import com.pi4j.util.NativeLibraryLoader;

/**
 * <p>
 * WiringPi includes a library which can make it easier to use the Raspberry Pi’s on-board SPI interface.
 * </p>
 *
 * <p>
 * Before you can use SPI interface, you may need to use the gpio utility to load the SPI drivers into the kernel: > gpio load spi
 *
 * If you need a buffer size of greater than 4KB, then you can specify the size (in KB) on the command line: > gpio load spi 100
 *
 * will allocate a 100KB buffer. (You should rarely need this though, the default is more than enough for most applications).
 * </p>
 * 
 * <p>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com</a>) </blockquote>
 * </p>
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com</a>
 * @see <a href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
 * @author Robert Savage (<a href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Spi {

	public static int CHANNEL_0 = 0;
	public static int CHANNEL_1 = 1;

	// private constructor
	private Spi() {
		// forbid object construction
	}

	static {
		// Load the platform library
		NativeLibraryLoader.load("libpi4j.so");
	}

	/**
	 * <p>
	 * wiringPiSPISetup:
	 * </p>
	 *
	 * <p>
	 * This is the way to initialise a channel (The Pi has 2 channels; 0 and 1). The speed parameter is an integer in the range 500,000 through 32,000,000 and represents the SPI clock speed in Hz.
	 * </p>
	 *
	 * <p>
	 * The returned value is the Linux file-descriptor for the device, or -1 on error. If an error has happened, you may use the standard errno global variable to see why.
	 * </p>
	 *
	 * @see <a href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
	 * @param channel
	 *            SPI channel
	 * @param speed
	 *            SPI speed
	 * @return return -1 on error
	 */
	public static native int wiringPiSPISetup(int channel, int speed);

	/**
	 * <p>
	 * wiringPiSPIGetFd:
	 * </p>
	 * 
	 * <p>
	 * Return the file-descriptor for the given channel
	 * </p>
	 *
	 * @see <a href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
	 * @param channel
	 *            SPI channel
	 * @return file-descriptor for the given channel
	 */
	public static native int wiringPiSPIGetFd(int channel);

	/**
	 * <p>
	 * wiringPiSPIDataRW:
	 * </p>
	 * 
	 * <p>
	 * This performs a simultaneous write/read transaction over the selected SPI bus. Data that was in your buffer is overwritten by data returned from the SPI bus.
	 * </p>
	 *
	 * <p>
	 * (ATTENTION: the 'data' argument can only be a maximum of 1024 characters.)
	 * </p>
	 *
	 * @see <a href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
	 * @param channel
	 *            SPI channel</p>
	 * @param data
	 *            string data payload
	 * @param len
	 *            length of characters in string
	 * @return return -1 on error
	 */
	public static native int wiringPiSPIDataRW(int channel, String data, int len);

	/**
	 * <p>
	 * wiringPiSPIDataRW:
	 * </p>
	 *
	 * <p>
	 * This performs a simultaneous write/read transaction over the selected SPI bus. The data argument is passed into the wiringPI function as the argument and the output from Spi is returned by this
	 * method
	 * </p>
	 *
	 * @see <a href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
	 * @param channel
	 *            SPI channel</p>
	 * @param data
	 *            short array data payload. Note that wiringPi uses unsigned char for the data transmission. That is 8-bit. in other words values 0-255. So make sure the values in data do not exceed
	 *            this range, otherwise the numbers would overflow in the native code and unexpected results would yield
	 * @return return null on error, otherwise the short array with the data
	 */
	public static native short[] wiringPiSPIDataRW(int channel, short[] data);
}
