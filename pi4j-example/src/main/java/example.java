/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  example.java  
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

import com.pi4j.wiringpi.Spi;

public class example {

    // SPI operations
    public static short INIT_CMD = (short) 0xD0;
    public static short SPI_CHANNEL = 0x0;
    
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
        // http://wiringpi.com/reference/spi-library/
        //
        // see the link below for the data sheet on the MCP23S17 chip:
        // http://ww1.microchip.com/downloads/en/devicedoc/21952b.pdf
        
        System.out.println("<--Pi4J--> SPI test program using MCP3002 AtoD Chip");
                
        // setup SPI for communication
        int fd = Spi.wiringPiSPISetup(SPI_CHANNEL, 1000000);;
        if (fd <= -1) {
            System.out.println(" ==>> SPI SETUP FAILED");
            return;
        }
        
        // infinite loop
        while(true) {
            
            read();
            Thread.sleep(1000);
        }
    }

    public static void read(){
        
        // send test ASCII message
        short packet[] = new short[2];
        packet[0] = INIT_CMD;  // address short
        //packet[0] = (short)(INIT_CMD | (SPI_CHANNEL<<5));
        packet[1] = 0x00;  // dummy
           
        System.out.println("-----------------------------------------------");
        System.out.println("[TX] " + shortsToHex(packet));
        Spi.wiringPiSPIDataRW(SPI_CHANNEL, packet);        
        System.out.println("[RX] " + shortsToHex(packet));
        System.out.println("-----------------------------------------------");
        
        //System.out.println(( (packet[0]<<7) | (packet[1]>>1) ) & 0x3FF);
        System.out.println( ((packet[0]<<8)|packet[1]) & 0x3FF );
        
    }
    
    
    public static String shortsToBinary(short[] shorts) {
        StringBuilder sb = new StringBuilder();
        int v;
        for ( int j = 0; j < shorts.length; j++ ) {
            v = shorts[j];
            sb.append(Integer.toBinaryString(v));
        }
        return sb.toString();
    }    

    public static String shortsToHex(short[] shorts) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[shorts.length * 2];
        int v;
        for ( int j = 0; j < shorts.length; j++ ) {
            v = shorts[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }    
}
