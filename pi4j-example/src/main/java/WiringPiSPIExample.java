/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiSPIExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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

import com.pi4j.wiringpi.Spi;

public class WiringPiSPIExample {

    // SPI operations
    public static byte WRITE_CMD = 0x40;
    public static byte READ_CMD  = 0x41;
    
    @SuppressWarnings("unused")
    public static void main(String args[]) throws InterruptedException {
        
        // 
        // This SPI example is using the WiringPi native library to communicate with 
        // the SPI hardware interface connected to a MCP23S17 I/O Expander.
        //
        // Please note the following command are required to enable the SPI driver on
        // your Raspberry Pi:
        // >  sudo modprobe spi_bcm2708
        // >  sudo chown `id -u`.`id -g` /dev/spidev0.*
        //
        // this source code was adapted from:
        // https://github.com/thomasmacpherson/piface/blob/master/python/piface/pfio.py
        //
        // see this blog post for additional details on SPI and WiringPi
        // https://projects.drogon.net/understanding-spi-on-the-raspberry-pi/
        //
        // see the link below for the data sheet on the MCP23S17 chip:
        // http://ww1.microchip.com/downloads/en/devicedoc/21952b.pdf
        
        System.out.println("<--Pi4J--> SPI test program using MCP23S17 I/O Expander Chip");
                
        // configuration
        byte IODIRA = 0x00; // I/O direction A
        byte IODIRB = 0x01; // I/O direction B
        byte IOCON  = 0x0A; // I/O config
        byte GPIOA  = 0x12; // port A
        byte GPIOB  = 0x13; // port B
        byte GPPUA  = 0x0C; // port A pullups
        byte GPPUB  = 0x0D; // port B pullups
        byte OUTPUT_PORT = GPIOA;
        byte INPUT_PORT  = GPIOB;
        byte INPUT_PULLUPS = GPPUB;        

        // setup SPI for communication
        int fd = Spi.wiringPiSPISetup(0, 10000000);;
        if (fd <= -1) {
            System.out.println(" ==>> SPI SETUP FAILED");
            return;
        }
        
        // initialize
        write(IOCON,  0x08);  // enable hardware addressing
        write(GPIOA,  0x00);  // set port A off
        write(IODIRA, 0);     // set port A as outputs
        write(IODIRB, 0xFF);  // set port B as inputs
        write(GPPUB,  0xFF);  // set port B pullups on
        
        int pins = 1;

        // infinite loop
        while(true) {
            
            // shift the bit to the left in the A register
            // this will cause the next LED to light up and 
            // the current LED to turn off.
            if(pins >= 255)
                pins=1;
            write(GPIOA,  (byte)pins);  
            pins = pins << 1;
            Thread.sleep(1000);
            
            // read for input changes 
            //read(INPUT_PORT);
        }
    }
    
    public static void write(byte register, int data){
        
        // send test ASCII message
        byte packet[] = new byte[3];
        packet[0] = WRITE_CMD;  // address byte
        packet[1] = register;  // register byte
        packet[2] = (byte)data;  // data byte
           
        System.out.println("-----------------------------------------------");
        System.out.println("[TX] " + bytesToHex(packet));
        Spi.wiringPiSPIDataRW(0, packet, 3);        
        System.out.println("[RX] " + bytesToHex(packet));
        System.out.println("-----------------------------------------------");
    }

    public static void read(byte register){
        
        // send test ASCII message
        byte packet[] = new byte[3];
        packet[0] = READ_CMD;    // address byte
        packet[1] = register;    // register byte
        packet[2] = 0b00000000;  // data byte
           
        System.out.println("-----------------------------------------------");
        System.out.println("[TX] " + bytesToHex(packet));
        Spi.wiringPiSPIDataRW(0, packet, 3);        
        System.out.println("[RX] " + bytesToHex(packet));
        System.out.println("-----------------------------------------------");
    }
    
    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }    
}
