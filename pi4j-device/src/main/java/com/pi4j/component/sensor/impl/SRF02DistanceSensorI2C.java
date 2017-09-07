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
    @SuppressWarnings("unused")
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
                        Thread.sleep(10);  // takes up to 66ms after you initiate ranging so slow loop down
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
