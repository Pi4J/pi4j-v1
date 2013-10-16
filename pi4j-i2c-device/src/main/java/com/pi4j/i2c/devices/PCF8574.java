package com.pi4j.i2c.devices;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: I2C Device Abstractions
 * FILENAME      :  PCF8574.java  
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

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.impl.I2CBusImpl;


public class PCF8574 {

    private I2CDevice device;
    private int lastRead;
    
    public PCF8574(I2CBus bus) throws IOException {
        this.device = bus.getDevice(0x27);
    }

    public void write(byte b) throws IOException {
        device.write((byte)b);
    }
    
    public int read() throws IOException {
        lastRead = device.read();
        return lastRead;
    }

    public boolean isSet(int bit) {
        return (1 >> bit & lastRead) != 0;
    }

    public static void main(String[] args) throws Exception {
        I2CBus bus = I2CBusImpl.getBus(0);

        int i = 0;
        PCF8574 io = new PCF8574(bus);
        while (true) {
            io.write((byte)-1);
            Thread.sleep(100);
            i = io.read();
            System.out.print(" x   " + Integer.toHexString(i) + "\r");
    
            Thread.sleep(1000);
    
            io.write((byte)0);
            Thread.sleep(100);
            
            i = io.read();
            System.out.print(" 0   " + Integer.toHexString(i) + "\r");
            
            Thread.sleep(1000);
            
        }
    }
}
