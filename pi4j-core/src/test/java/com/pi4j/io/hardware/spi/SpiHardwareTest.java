package com.pi4j.io.hardware.spi;/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SpiHardwareTest.java
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

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.util.Console;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author Robert Savage
 *
 * This is a test program to perform manual hardware testing of the SPI bus and data READ/WRITE operations.
 * The test will attempt to WRITE/READ 20K data transfers on SPI (CS0).  The accompanying Arduino test code should
 * be running on an Arduino device with SPI pins (MISO/MOSI/SCLK/CE0) connected to the Raspberry Pi's SPI pins.
 */
public class SpiHardwareTest {

    public static final int TEST_ITERATIONS = 20000;

    // SPI device
    public static SpiDevice spi = null;

    // create Pi4J console wrapper/helper
    // (This is a utility class to abstract some of the boilerplate code)
    protected static final Console console = new Console();

    /**
     * Sample SPI Program
     *
     * @param args (none)
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String args[]) throws InterruptedException, IOException, NoSuchAlgorithmException {

        final MessageDigest md = MessageDigest.getInstance("MD5");
        final Random rd = new Random();
        byte[] arr = new byte[32];

        // print program title/header
        console.title("<-- The Pi4J Project -->", "SPI Hardware Test Program");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create SPI object instance for SPI for communication
        spi = SpiFactory.getInstance(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED, // 1 MHz
                SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0


        // continue running program until user exits using CTRL-C
        // or the number of test iterations has completed
        for(int index = 10; index <= TEST_ITERATIONS; index++) {

            rd.nextBytes(arr);  // fill random bytes array
            md.update(arr);     // create MD5 hash based on array of random bytes

            // create data string to transmit;
            // (NOTE) we are going to pad the string with a few spaces
            // as both prefix and suffix due to an issue in the arduino hardware test code where
            // we can't seem to get the timing of the first one or two bytes to align exactly with
            // the first transmit byte.  this is a SPI slave code issue and not an issue with
            // SPI communication on the Raspberry Pi.
            String tx = "  " + DatatypeConverter.printHexBinary(md.digest()).toUpperCase() + "  ";
            String rx = spi.write(tx, StandardCharsets.US_ASCII);

            tx = tx.trim(); // trim for comparison and pretty print
            rx = rx.trim(); // trim for comparison and pretty print
            console.println("[" + index + "] SPI TX: " + tx);
            console.print("[" + index + "] SPI RX: " + rx);

            // compare the data transmitted versus data received
            if(tx.equalsIgnoreCase(rx)){
                console.println("; <MATCH SUCCESS>");
            } else {
                console.println("; <MATCH FAILED>");
                console.println("[" + index + "] !!! FAILED while comparing TX/RX: " + tx + " != " + rx);
                System.exit(0);
                break;
            }

            // exit loop if user has aborted (CTRL-C)
            if(!console.isRunning()) break;
        }

        // done
        console.box("-----PASSED-----");
        console.emptyLine();
        console.println("Successful READ/WRITE of [" + TEST_ITERATIONS + "] iterations of SPI data exchanges/transfers.");
        console.emptyLine();
    }
}
