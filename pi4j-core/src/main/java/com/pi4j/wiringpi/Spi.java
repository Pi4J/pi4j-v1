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


import com.pi4j.util.NativeLibraryLoader;

/**
 * <p>
 * WiringPi includes a library which can make it easier to use the Raspberry Pi’s on-board SPI interface.
 * </p>
 *
 * <p>
 * Before you can use SPI interface, you may need to use the gpio utility to load the SPI drivers into the kernel:
 *  > gpio load spi
 *
 * If you need a buffer size of greater than 4KB, then you can specify the size (in KB) on the command line:
 *  > gpio load spi 100
 *
 * will allocate a 100KB buffer. (You should rarely need this though, the default is more than enough
 * for most applications).
 * </p>
 *
 * <p>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com</a>)
 * </blockquote>
 * </p>
 *
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com</a>
 * @see <a
 *      href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Spi {

    public static int CHANNEL_0 = 0;
    public static int CHANNEL_1 = 1;

    public static int MODE_0 = 0;
    public static int MODE_1 = 1;
    public static int MODE_2 = 2;
    public static int MODE_3 = 3;

    // private constructor
    private Spi()  {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * <p>wiringPiSPISetup:</p>
     *
     * <p>
     * This is the way to initialise a channel (The Pi has 2 channels; 0 and 1). The speed parameter is an integer in
     * the range 500,000 through 32,000,000 and represents the SPI clock speed in Hz.
     * </p>
     *
     * <p>
     * The returned value is the Linux file-descriptor for the device, or -1 on error. If an error has happened, you
     * may use the standard errno global variable to see why.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
     * @param channel SPI channel
     * @param speed SPI speed
     * @return return -1 on error
     */
    public static native int wiringPiSPISetup(int channel, int speed);


    /**
     * <p>wiringPiSPISetupMode:</p>
     *
     * <p>
     * This is the way to initialise a channel (The Pi has 2 channels; 0 and 1). The speed parameter is an integer in
     * the range 500,000 through 32,000,000 and represents the SPI clock speed in Hz.
     * </p>
     *
     * <p>
     * The returned value is the Linux file-descriptor for the device, or -1 on error. If an error has happened, you
     * may use the standard errno global variable to see why.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
     * @see <a
     *      hfref="http://en.wikipedia.org/wiki/Serial_Peripheral_Interface_Bus#Mode_numbers">http://en.wikipedia.org/wiki/Serial_Peripheral_Interface_Bus#Mode_numbers</a>
     * @param channel SPI channel
     * @param speed SPI speed
     * @param mode SPI mode (Mode is 0, 1, 2 or 3; see http://en.wikipedia.org/wiki/Serial_Peripheral_Interface_Bus#Mode_numbers)
     * @return return -1 on error
     */
    public static native int wiringPiSPISetupMode (int channel, int speed, int mode);

    /**
     * <p>wiringPiSPIGetFd:</p>
     *
     * <p>
     * Return the file-descriptor for the given channel
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
     * @param channel SPI channel
     * @return file-descriptor for the given channel
     */
    public static native int wiringPiSPIGetFd(int channel);

    /**
     * <p>wiringPiSPIDataRW:</p>
     *
     * <p>
     * This performs a simultaneous write/read transaction over the selected SPI bus. Data that was in your buffer is
     * overwritten by data returned from the SPI bus.
     * </p>
     *
     * <p>
     * (ATTENTION: the 'data' argument can only be a maximum of 1024 characters.)
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
     * @param channel SPI channel</p>
     * @param data string data payload
     * @return return -1 on error
     */
    public static int wiringPiSPIDataRW(int channel, String data){
        return wiringPiSPIDataRW(channel, data, data.length());
    }

    /**
     * <p>wiringPiSPIDataRW:</p>
     *
     * <p>
     * This performs a simultaneous write/read transaction over the selected SPI bus. Data that was in your buffer is
     * overwritten by data returned from the SPI bus.
     * </p>
     *
     * <p>
     * (ATTENTION: the 'data' argument can only be a maximum of 1024 characters.)
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
     * @param channel SPI channel</p>
     * @param data string data payload
     * @param len length of characters in string (must be total string length, not a substring)
     * @return return -1 on error
     */
    public static native int wiringPiSPIDataRW(int channel, String data, int len);

    /**
     * <p>wiringPiSPIDataRW:</p>
     *
     * <p>
     * This performs a simultaneous write/read transaction over the selected SPI bus. Data that was in your buffer is
     * overwritten by data returned from the SPI bus.
     * </p>
     *
     * <p>
     * (ATTENTION: the 'data' argument can only be a maximum of 1024 characters.)
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
     * @param channel
     *             SPI channel
     * @param data
     *             byte array data payload
     * @param len
     *             length of bytes in data array argument
     * @return return -1 on error
     */
    public static native int wiringPiSPIDataRW(int channel, byte[] data, int len);


    /**
     * <p>wiringPiSPIDataRW:</p>
     *
     * <p>
     * This performs a simultaneous write/read transaction over the selected SPI bus. Data that was in your buffer is
     * overwritten by data returned from the SPI bus.
     * </p>
     *
     * <p>
     * (ATTENTION: the 'data' argument can only be a maximum of 1024 characters.)
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/spi-library/">http://wiringpi.com/reference/spi-library</a>
     * @param channel
     *             SPI channel
     * @param data
     *             byte array data payload
     * @return return -1 on error
     */
    public static int wiringPiSPIDataRW(int channel, byte[] data){
        return wiringPiSPIDataRW(channel, data, data.length);
    }

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
     *            SPI channel
     * @param data
     *            short array data payload. Note that wiringPi uses unsigned char for the data transmission. That is 8-bit. in other words values 0-255. So make sure the values in data do not exceed
     *            this range, otherwise the numbers would overflow in the native code and unexpected results would yield
     * @param len
     *             length of bytes in data array argument
     * @return return -1 on error
     */
    public static native int wiringPiSPIDataRW(int channel, short[] data, int len);

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
     *            SPI channel
     * @param data
     *            short array data payload. Note that wiringPi uses unsigned char for the data transmission. That is 8-bit. in other words values 0-255. So make sure the values in data do not exceed
     *            this range, otherwise the numbers would overflow in the native code and unexpected results would yield
     * @return return -1 on error
     */
    public static int wiringPiSPIDataRW(int channel, short[] data){
        return wiringPiSPIDataRW(channel, data, data.length);
    }
}
