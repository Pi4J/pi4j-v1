package com.pi4j.io.i2c.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CDeviceImpl.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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

import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.jni.I2C;

public class I2CDeviceImpl implements I2CDevice {

    private I2CBusImpl bus;
    private int deviceAddress;
    
    public I2CDeviceImpl(I2CBusImpl bus, int address) {
        this.bus = bus;
        this.deviceAddress = address;
    }

    @Override
    public void write(int address, byte data) throws IOException {
        int ret = I2C.i2cWriteByte(bus.fd, deviceAddress, address, data);
        if (ret < 0) {
            throw new IOException("Error writing to " + makeDescription(address) + ". Got " + ret + ".");
        }
    }

    @Override
    public void write(int address, byte[] buffer, int offset, int size) throws IOException {
        
        int ret = I2C.i2cWriteBytes(bus.fd, deviceAddress, address, size, offset, buffer);
        if (ret < 0) {
            throw new IOException("Error writing to " + makeDescription(address) + ". Got " + ret + ".");
        }
    }

    @Override
    public int read(int address) throws IOException {
        int ret = I2C.i2cReadByte(bus.fd, deviceAddress, address);
        if (ret < 0) {
            throw new IOException("Error reading from " + makeDescription(address) + ". Got " + ret + ".");
        }
        return ret;
    }

    @Override
    public int read(int address, byte[] buffer, int offset, int size) throws IOException {
        // It doesn't work for some reason. 
//        int ret = I2C.i2cReadBytes(bus.fd, deviceAddress, address, size, offset, buffer);
//        if (ret < 0) {
//            throw new IOException("Error reading from " + makeDescription(address) + ". Got " + ret + ".");
//        }
//        return ret;
        for (int i = 0; i < size; i++) {
            int b = read(address + i);
            if (b < 0) {
                if (i == 0) {
                    throw new IOException("Error reading from " + makeDescription(address) + ". Got " + b + ".");
                } else {
                    return i;
                }
            }
            if (b > 127) { b = b - 256; }
            buffer[i + offset] = (byte)b;
        }
        return size;
    }
    
    protected String makeDescription(int address) {
        return bus.filename + " at address 0x" + Integer.toHexString(deviceAddress) 
                + " to address 0x" + Integer.toHexString(address);
    }
    
}
