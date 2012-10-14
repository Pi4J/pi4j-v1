package com.pi4j.io.i2c.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CBusImpl.java  
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

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.jni.I2C;

public class I2CBusImpl implements I2CBus {

    private static I2CBus bus0 = null;    
    private static I2CBus bus1 = null;
    
    public static I2CBus getBus(int busNumber) throws IOException {
        I2CBus bus = null;
        if (busNumber == 0) {
            bus = bus0;
            if (bus == null) {
                bus = new I2CBusImpl("/dev/i2c-0");
                bus0 = bus;
            }
        } else if (busNumber == 1) {
            bus = bus1;
            if (bus == null) {
                bus = new I2CBusImpl("/dev/i2c-1");
                bus1 = bus;
            }
        }
        return bus;
    }

    protected int fd;
    protected String filename;
    
    public I2CBusImpl(String filename) throws IOException {
        this.filename = filename;
        fd = I2C.i2cOpen(filename);
        if (fd < 0) {
            throw new IOException("Cannot open file handle for " + filename + " got " + fd + " back.");
        }
    }

    @Override
    public I2CDevice getDevice(int address) throws IOException {
        return new I2CDeviceImpl(this, address);
    }
    
    public void close() throws IOException {
        I2C.i2cClose(fd);
    }
}
