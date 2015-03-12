/*
 * Copyright 2014 Pi4J.
 *
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
 */

package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  SRF02DistanceSensorI2C.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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

import com.pi4j.component.sensor.DistanceSensorBase;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.io.IOException;

/**
 *
 * @author andy
 *
 *
 *  TODO:  Implement a polling thread to raise value change events!
 *
 */
public class SRF02DistanceSensorI2C extends DistanceSensorBase{

    private static final byte COMMAND_REGISTER          = 0x00;
    private static final byte COMMAND_RANGE_INCHES      = 0x50;
    private static final byte COMMAND_RANGE_CENTIMETERS = 0x51;
    private byte[]            buffer;

    public static final int DEFAULT_ADDRESS = 0x70;

    private int address = DEFAULT_ADDRESS;
    I2CDevice device = null;
    I2CBus bus = null;

    /**
     * default constructor
     *
     * @param i2c_bus_number the I2C bus that the chip is connected to
     * @param device_address the I2C device address of the chip
     * @throws IOException
     */
    public SRF02DistanceSensorI2C(int i2c_bus_number, int device_address) throws UnsupportedBusNumberException, IOException {
        this.address = device_address;
        bus = I2CFactory.getInstance(i2c_bus_number);
        device = bus.getDevice(device_address);
        buffer = new byte[10];
    }
    
    @Override
    public double getValue() {
        short result  = -1;
        boolean bSuccess = false;

        try {
            device.write(COMMAND_REGISTER, COMMAND_RANGE_CENTIMETERS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // wait for a result.
        while (!bSuccess) {
            try {
                Thread.sleep(80);

                // attempt to read value from device
                try {
                    device.read(COMMAND_REGISTER, buffer, 0, 4);
                } catch (java.io.IOException ioe) {
                    continue;
                }

                if (buffer[0] != 0xFF) {
                    bSuccess = true;
                    result = (short)((buffer[2] >> 8) + (buffer[3]&0xFF));
                } else {
                    try {
                        Thread.currentThread().sleep(10);    // takes up to 66ms after you initiate ranging so slow loop down
                    } catch (InterruptedException ie) {
                        // don't have to actually do anything with the exception except leave loop maybe
                        break;
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public void setNewAddress(int newAddress) throws IOException {
        //0xA0, 0xAA, 0xA5, 0xF2
        device.write(COMMAND_REGISTER, (byte) 0xA0);
        device.write(COMMAND_REGISTER, (byte) 0xAA);
        device.write(COMMAND_REGISTER, (byte) 0xA5);
        device.write(COMMAND_REGISTER, (byte) newAddress);
        address = newAddress;
        device = bus.getDevice(address);
    }
    
}
