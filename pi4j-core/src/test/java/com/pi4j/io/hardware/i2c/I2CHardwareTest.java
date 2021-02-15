package com.pi4j.io.hardware.i2c;/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CHardwareTest.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.util.Console;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author Robert Savage
 *
 * This is a test program to perform manual hardware testing of the I2C bus and data READ/WRITE operations.
 * The test will attempt to WRITE to 255 registers on TEST device #99 on I2C BUS #1.  The accompanying Arduino
 * test code should be running on an Arduino device with I2C pins (SDA/SCL) connected to the Raspberry Pi's I2C bus.
 */
public class I2CHardwareTest {

    public static final int DEVICE_ADDRESS    = 99;
    public static final int MAX_REGISTERS     = 256;
    public static final int DATA_PAYLOAD_SIZE = 16;

    /**
     * Program Main Entry Point
     *
     * @param args
     * @throws InterruptedException
     * @throws PlatformAlreadyAssignedException
     * @throws IOException
     * @throws UnsupportedBusNumberException
     */
    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException, IOException, UnsupportedBusNumberException, NoSuchAlgorithmException {

        final MessageDigest md = MessageDigest.getInstance("MD5");
        final Random rd = new Random();

        byte[] arr = new byte[DATA_PAYLOAD_SIZE];
        byte[][] tx = new byte[MAX_REGISTERS][];
        byte[][] rx = new byte[MAX_REGISTERS][];

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "I2C Hardware Test Program");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // get the I2C bus to communicate on
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);

        // create an I2C device for an individual device on the bus that you want to communicate with
        // in this example we will use the default address for the arduino test program
        I2CDevice device = i2c.getDevice(DEVICE_ADDRESS);

        // -----------------------------------------------------------------
        // WRITE I2C DEVICE REGISTERS
        // -----------------------------------------------------------------
        console.println("... writing I2C registers...");
        for(int register = 0; register < MAX_REGISTERS; register++) {
            rd.nextBytes(arr);  // fill random bytes array
            md.update(arr);     // create MD5 hash based on array of random bytes
            tx[register] = md.digest();
            String dataString = DatatypeConverter.printHexBinary(tx[register]).toUpperCase();
            device.write(register, tx[register]);
            console.println("--> writing I2C register [" + register + "] = " + dataString);
            Thread.sleep(1);
        }

        // -----------------------------------------------------------------
        // READ I2C DEVICE REGISTERS
        // -----------------------------------------------------------------
        console.println("... reading I2C registers...");
        for(int register = 0; register < MAX_REGISTERS; register++) {
            rx[register] = new byte[DATA_PAYLOAD_SIZE];
            int result1 = device.read(register, rx[register], 0, DATA_PAYLOAD_SIZE);
            String dataString = DatatypeConverter.printHexBinary(rx[register]).toUpperCase();
            console.println("--> reading I2C register [" + register + "] = " + dataString);
            Thread.sleep(1);
        }

        // close I2C device/bus
        i2c.close();

        // -----------------------------------------------------------------
        // COMPARING I2C DEVICE REGISTERS
        // -----------------------------------------------------------------
        console.println("... comparing I2C registers...");
        for(int register = 0; register < MAX_REGISTERS; register++) {
            String txstr = DatatypeConverter.printHexBinary(tx[register]).toUpperCase();
            String rxstr = DatatypeConverter.printHexBinary(rx[register]).toUpperCase();
            if(txstr.equalsIgnoreCase(rxstr)){
                console.println("--> comparing I2C register [" + register + "] = (TX) " + txstr + "== (RX) " + rxstr);
            } else {
                console.println("!!! FAILED while comparing I2C register [" + register + "] = (TX) " + txstr + " != (RX) " + rxstr);
                console.exit();
                break;
            }
        }

        // done
        console.box("-----PASSED-----");
        console.println("Successful READ/WRITE to [" + (MAX_REGISTERS-1) + "] I2C registers.");
    }
}
