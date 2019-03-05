package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2C.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
 * WiringPi includes a library which can make it easier to use the Raspberry Pi’s on-board I2C interface.
 * </p>
 *
 * <p>
 * Before you can use the I2C interface, you may need to use the gpio utility to load the I2C drivers into the kernel:
 *  > gpio load i2c
 *
 * If you need a baud rate other than the default 100Kbps, then you can supply this on the command-line:
 *  > gpio load i2c 1000
 *
 * will set the baud rate to 1000Kbps – ie. 1,000,000 bps. (K here is times 1000) *
 * </p>
 *
 * <p>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see <a href="https://www.pi4j.com/">https://www.pi4j.com</a>
 * @see <a
 *      href="http://wiringpi.com/reference/i2c-library/">http://wiringpi.com/reference/i2c-library</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class I2C {

    public static int CHANNEL_0 = 0;
    public static int CHANNEL_1 = 1;

    // private constructor
    private I2C()  {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * <p>wiringPiI2CSetup:</p>
     *
     * <p>
     * This initialises the I2C system with your given device identifier. The ID is the I2C number of the device
     * and you can use the i2cdetect program to find this out. wiringPiI2CSetup() will work out which revision
     * Raspberry Pi you have and open the appropriate device in /dev.
     * </p>
     *
     * <p>
     * The return value is the standard Linux filehandle, or -1 if any error – in which case, you can consult
     * errno as usual.
     * </p>
     *
     * <p>
     * E.g. the popular MCP23017 GPIO expander is usually device Id 0×20, so this is the number you would pass
     * into wiringPiI2CSetup().
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/i2c-library/">http://wiringpi.com/reference/i2c-library</a>
     *
     * @param devid I2C device id
     * @return return -1 on error; else returns Linux file handle
     */
    public static native int wiringPiI2CSetup(int devid);

    /**
     * <p>wiringPiI2CRead:</p>
     *
     * <p>
     * Simple I2C device read. Some devices present data when you read them without having to do any register transactions.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/i2c-library/">http://wiringpi.com/reference/i2c-library</a>
     *
     * @param fd Linux file handle obtained from call to wiringPiI2CSetup
     * @return return -1 on error; else data read from I2C device
     */
    public static native int wiringPiI2CRead(int fd);

    /**
     * <p>wiringPiI2CWrite:</p>
     *
     * <p>
     * Simple I2C device write. Some devices accept data this way without needing to access any internal registers.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/i2c-library/">http://wiringpi.com/reference/i2c-library</a>
     *
     * @param fd Linux file handle obtained from call to wiringPiI2CSetup
     * @param data data to write
     * @return return -1 on error
     */
    public static native int wiringPiI2CWrite(int fd, int data);

    /**
     * <p>wiringPiI2CWriteReg8:</p>
     *
     * <p>
     * I2C device write. Write an 8-bit data value into the device register indicated.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/i2c-library/">http://wiringpi.com/reference/i2c-library</a>
     *
     * @param fd Linux file handle obtained from call to wiringPiI2CSetup
     * @param reg I2C device register address
     * @param data data byte to write (8 bits/1 byte)
     * @return return -1 on error
     */
    public static native int wiringPiI2CWriteReg8(int fd, int reg, int data);

    /**
     * <p>wiringPiI2CWriteReg16:</p>
     *
     * <p>
     * I2C device write. Write a 16-bit data value into the device register indicated.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/i2c-library/">http://wiringpi.com/reference/i2c-library</a>
     *
     * @param fd Linux file handle obtained from call to wiringPiI2CSetup
     * @param reg I2C device register address
     * @param data data bytes to write (16 bits/2 bytes)
     * @return return -1 on error
     */
    public static native int wiringPiI2CWriteReg16(int fd, int reg, int data);

    /**
     * <p>wiringPiI2CReadReg8:</p>
     *
     * <p>
     * I2C device read. Read an 8-bit (1 byte) data value from the device register indicated.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/i2c-library/">http://wiringpi.com/reference/i2c-library</a>
     *
     * @param fd Linux file handle obtained from call to wiringPiI2CSetup
     * @param reg I2C device register address
     * @return return -1 on error; else returns 8-bit value read from I2C device register
     */
    public static native int wiringPiI2CReadReg8(int fd, int reg);

    /**
     * <p>wiringPiI2CReadReg16:</p>
     *
     * <p>
     * I2C device read. Read a 16-bit (2 bytes) data value from the device register indicated.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/i2c-library/">http://wiringpi.com/reference/i2c-library</a>
     *
     * @param fd Linux file handle obtained from call to wiringPiI2CSetup
     * @param reg I2C device register address
     * @return return -1 on error; else returns 16-bit value read from I2C device register
     */
    public static native int wiringPiI2CReadReg16(int fd, int reg);
}
