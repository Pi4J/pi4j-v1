package bananapro;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  I2CExample.java
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

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.util.Console;

/**
 * This example code demonstrates how to perform simple I2C
 * communication on the BananaPro.  For this example we will
 * connect to a 'TSL2561' LUX sensor.
 *
 * Data Sheet:
 * https://www.adafruit.com/datasheets/TSL256x.pdf
 *
 * You should get something similar printed in the console
 * when executing this program:
 *
 * > <--Pi4J--> I2C Example ... started.
 * > ... reading ID register from TSL2561
 * > TSL2561 ID = 0x50 (should be 0x50)
 * > ... powering up TSL2561
 * > ... reading DATA registers from TSL2561
 * > TSL2561 DATA 0 = 0x1e
 * > TSL2561 DATA 1 = 0x04
 * > ... powering down TSL2561
 * > Exiting I2CExample
 *
 *
 * @author Robert Savage
 */
public class I2CExample {

    // TSL2561 I2C address
    public static final int TSL2561_ADDR = 0x39; // address pin not connected (FLOATING)
    //public static final int TSL2561_ADDR = 0x29; // address pin connect to GND
    //public static final int TSL2561_ADDR = 0x49; // address pin connected to VDD

    // TSL2561 registers
    public static final byte TSL2561_REG_ID = (byte) 0x8A;
    public static final byte TSL2561_REG_DATA_0 = (byte) 0x8C;
    public static final byte TSL2561_REG_DATA_1 = (byte) 0x8E;
    public static final byte TSL2561_REG_CONTROL = (byte) 0x80;

    // TSL2561 power control values
    public static final byte TSL2561_POWER_UP = (byte) 0x03;
    public static final byte TSL2561_POWER_DOWN = (byte) 0x00;

    /**
     * Program Main Entry Point
     *
     * @param args
     * @throws InterruptedException
     * @throws PlatformAlreadyAssignedException
     * @throws IOException
     * @throws UnsupportedBusNumberException
     */
    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException, IOException, UnsupportedBusNumberException {

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // explicitly assign the platform as the BananaPro platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.BANANAPRO);

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "I2C Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // get the I2C bus to communicate on
        // - I2CBus.BUS_2 uses header pin CON6:3 as SDA and header pin CON6:5 as SCL
        // - I2CBus.BUS_3 uses header pin CON6:27 as SDA and header pin CON6:28 as SCL
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_2);

        // create an I2C device for an individual device on the bus that you want to communicate with
        // in this example we will use the default address for the TSL2561 chip which is 0x39.
        I2CDevice device = i2c.getDevice(TSL2561_ADDR);

        // next, lets perform am I2C READ operation to the TSL2561 chip
        // we will read the 'ID' register from the chip to get its part number and silicon revision number
        console.println("... reading ID register from TSL2561");
        int response = device.read(TSL2561_REG_ID);
        console.println("TSL2561 ID = " + String.format("0x%02x", response) + " (should be 0x50)");

        // next we want to start taking light measurements, so we need to power up the sensor
        console.println("... powering up TSL2561");
        device.write(TSL2561_REG_CONTROL, TSL2561_POWER_UP);

        // wait while the chip collects data
        Thread.sleep(500);

        // now we will perform our first I2C READ operation to retrieve raw integration
        // results from DATA_0 and DATA_1 registers
        console.println("... reading DATA registers from TSL2561");
        int data0 = device.read(TSL2561_REG_DATA_0);
        int data1 = device.read(TSL2561_REG_DATA_1);

        // print raw integration results from DATA_0 and DATA_1 registers
        console.println("TSL2561 DATA 0 = " + String.format("0x%02x", data0));
        console.println("TSL2561 DATA 1 = " + String.format("0x%02x", data1));

        // before we exit, lets not forget to power down light sensor
        console.println("... powering down TSL2561");
        device.write(TSL2561_REG_CONTROL, TSL2561_POWER_DOWN);
    }
}
