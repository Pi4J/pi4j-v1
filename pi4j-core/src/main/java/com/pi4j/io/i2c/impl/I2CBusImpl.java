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

/**
 * This is implementation of i2c bus. This class keeps underlying linux file descriptor of
 * particular bus. As all reads and writes from/to i2c bus are blocked I/Os current implementation uses only 
 * one file per bus for all devices. Device implementations use this class file handle.
 * 
 * @author daniel
 *
 */
public class I2CBusImpl implements I2CBus {

    /** Singleton instance of bus 0 */
    private static I2CBus bus0 = null;

    /** Singleton instance of bus 1 */
    private static I2CBus bus1 = null;
    
    /** 
     * Factory method that returns bus implementation.
     * 
     * @param busNumber bus number
     * @return appropriate bus implementation
     * @throws IOException thrown in case there is a problem opening bus file or bus number is not 0 or 1.
     */
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
        } else {
            throw new IOException("Unknown bus number " + busNumber);
        }
        return bus;
    }

    /** File handle for this i2c bus */
    protected int fd;
    
    /** File name of this i2c bus */
    protected String filename;
    
    /**
     * Constructor of i2c bus implementation.
     * 
     * @param filename file name of device to be opened.
     * 
     * @throws IOException thrown in case that file cannot be opened
     */
    public I2CBusImpl(String filename) throws IOException {
        this.filename = filename;
        fd = I2C.i2cOpen(filename);
        if (fd < 0) {
            throw new IOException("Cannot open file handle for " + filename + " got " + fd + " back.");
        }
    }

    /**
     * Returns i2c device implementation ({@link I2CDeviceImpl}).
     * 
     * @param address address of i2c device
     * 
     * @return implementation of i2c device with given address
     * 
     * @throws IOException never in this implementation
     */
    @Override
    public I2CDevice getDevice(int address) throws IOException {
        return new I2CDeviceImpl(this, address);
    }

    /**
     * Closes this i2c bus
     * 
     * @throws IOException never in this implementation
     */
    @Override
    public void close() throws IOException {
        I2C.i2cClose(fd);
    }
}
