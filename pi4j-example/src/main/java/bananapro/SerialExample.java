package bananapro;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  SerialExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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


import com.pi4j.io.serial.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

import java.io.IOException;
import java.util.Date;

/**
 * This example code demonstrates how to perform serial communications using the BananaPro.
 * (see 'BananaProSerial' for constant definitions for BananaPro Serial Port addresses.)
 *
 * @author Robert Savage
 */
public class SerialExample {

    public static void main(String args[]) throws InterruptedException, IOException, PlatformAlreadyAssignedException {

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // explicitly assign the platform as the BananaPro platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.BANANAPRO);

        System.out.println("<--Pi4J--> Serial Communication Example ... started.");
        System.out.println(" ... connect using settings: 115200, 8, N, 1.");
        System.out.println(" ... data received on serial port should be displayed below.");

        // create an instance of the serial communications class
        final Serial serial = SerialFactory.createInstance();

        // create and register the serial data listener
        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {

                // NOTE! - It is extremely important to read the data received from the
                // serial port.  If it does not get read from the receive buffer, the
                // buffer will continue to grow and consume memory.

                // print out the data received to the console
                try {
                    System.out.println("[HEX DATA]   " + event.getHexByteString());
                    System.out.println("[ASCII DATA] " + event.getAsciiString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            // see 'BananaProSerial' for constant definitions for BananaPro Serial Port addresses.
            String serialPort = BananaProSerial.DEFAULT_COM_PORT; // TX is located at CON6-08; RX is located at CON6-10

            // optionally allow a CLI argument to override the serial port address.
            if(args.length > 0){
                serialPort = args[0];
            }

            // open the default serial port provided on the GPIO header
            serial.open(serialPort, Baud._115200, DataBits._8, Parity.NONE, StopBits._1, FlowControl.NONE);

            // continuous loop to keep the program running until the user terminates the program
            while(true) {
                try {
                    // write a formatted string to the serial transmit buffer
                    serial.write("CURRENT TIME: %s", new Date().toString());

                    // write a individual bytes to the serial transmit buffer
                    serial.write((byte) 13);
                    serial.write((byte) 10);

                    // write a simple string to the serial transmit buffer
                    serial.write("Second Line");

                    // write a individual characters to the serial transmit buffer
                    serial.write('\r');
                    serial.write('\n');

                    // write a string terminating with CR+LF to the serial transmit buffer
                    serial.writeln("Third Line");
                }
                catch(IllegalStateException ex){
                    ex.printStackTrace();
                }

                // wait 1 second before continuing
                Thread.sleep(1000);
            }

        }
        catch(IOException ex) {
            System.out.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
            return;
        }
    }
}

