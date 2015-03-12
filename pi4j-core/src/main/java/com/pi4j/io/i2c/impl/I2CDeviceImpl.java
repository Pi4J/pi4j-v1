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

import java.io.IOException;

import com.pi4j.io.i2c.I2CDevice;

/**
 * Implementation of i2c device. This class only holds reference to i2c bus (so it can use its handle) and
 * device address.
 * 
 * @author Daniel Sendula
 *
 */
public class I2CDeviceImpl implements I2CDevice {

    /** Reference to i2c bus */
    private I2CBusImpl bus;
    
    /** I2c device address */
    private int deviceAddress;
    
	/**
	 * @return The address for which this instance is constructed for.
	 */
    @Override
    public int getAddress() {
    	
    	return deviceAddress;
    	
    }
    
    /**
     * Constructor.
     * 
     * @param bus i2c bus
     * @param address i2c device address
     */
    public I2CDeviceImpl(I2CBusImpl bus, int address) {
        this.bus = bus;
        this.deviceAddress = address;
    }

    /**
     * This method writes one byte to i2c device. 
     * 
     * @param data byte to be written
     * 
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(byte data) throws IOException {
    	
    	try {
    		bus.lock();
    	} catch (InterruptedException e) {
    		throw new IOException(e);
    	}
    	
        try {
        	
            int ret = bus.writeByteDirect(this, data);
            if (ret < 0) {
                throw new IOException("Error writing to " + makeDescription() + ". Got " + ret + ".");
            }
            
        } finally {
            bus.unlock();
        }
        
    }

    /**
     * This method writes several bytes to the i2c device from given buffer at given offset.
     * 
     * @param buffer buffer of data to be written to the i2c device in one go
     * @param offset offset in buffer 
     * @param size number of bytes to be written 
     * 
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(byte[] buffer, int offset, int size) throws IOException {

    	try {
    		bus.lock();
    	} catch (InterruptedException e) {
    		throw new IOException(e);
    	}
    	
        try {
        	
            int ret = bus.writeBytesDirect(this, size, offset, buffer);
            if (ret < 0) {
                throw new IOException("Error writing to " + makeDescription() + ". Got " + ret + ".");
            }
            
        } finally {
            bus.unlock();
        }
        
    }

    /**
     * This method writes one byte to i2c device. 
     * 
     * @param address local address in the i2c device
     * @param data byte to be written
     * 
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(int address, byte data) throws IOException {

    	try {
    		bus.lock();
    	} catch (InterruptedException e) {
    		throw new IOException(e);
    	}
    	
        try {

        	int ret = bus.writeByte(this, address, data);
            if (ret < 0) {
                throw new IOException("Error writing to " + makeDescription(address) + ". Got " + ret + ".");
            }
            
        } finally {
            bus.unlock();
        }
        
    }

    /**
     * This method writes several bytes to the i2c device from given buffer at given offset.
     * 
     * @param address local address in the i2c device
     * @param buffer buffer of data to be written to the i2c device in one go
     * @param offset offset in buffer 
     * @param size number of bytes to be written 
     * 
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(int address, byte[] buffer, int offset, int size) throws IOException {

    	try {
    		bus.lock();
    	} catch (InterruptedException e) {
    		throw new IOException(e);
    	}
    	
        try {
        	
            int ret = bus.writeBytes(this, address, size, offset, buffer);
            if (ret < 0) {
                throw new IOException("Error writing to " + makeDescription(address) + ". Got " + ret + ".");
            }
            
        } finally {
            bus.unlock();
        }
        
    }

    /**
     * This method reads one byte from the i2c device.
     * Result is between 0 and 255 if read operation was successful, else a negative number for an error.
     *
     * @return byte value read: positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     * 
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read() throws IOException {

    	try {
    		bus.lock();
    	} catch (InterruptedException e) {
    		throw new IOException(e);
    	}
    	
        try {
        	
            int ret = bus.readByteDirect(this);
            if (ret < 0) {
                throw new IOException("Error reading from " + makeDescription() + ". Got " + ret + ".");
            }
            return ret;
            
        } finally {
            bus.unlock();
        }
        
    }

    /**
     * <p>This method reads bytes from the i2c device to given buffer at asked offset. </p>
     * 
     * <p>Note: Current implementation calls {@link #read(int)}. That means for each read byte 
     * i2c bus will send (next) address to i2c device.
     * </p>
     * 
     * @param buffer buffer of data to be read from the i2c device in one go
     * @param offset offset in buffer 
     * @param size number of bytes to be read 
     * 
     * @return number of bytes read
     * 
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(byte[] buffer, int offset, int size) throws IOException {

    	try {
    		bus.lock();
    	} catch (InterruptedException e) {
    		throw new IOException(e);
    	}
    	
        try {
        	
            // It doesn't work for some reason.
            int ret = bus.readBytesDirect(this, size, offset, buffer);
            if (ret < 0) {
                throw new IOException("Error reading from " + makeDescription() + ". Got " + ret + ".");
            }
            return ret;
            
        } finally {
            bus.unlock();
        }
        
    }

    /**
     * This method reads one byte from the i2c device.
     * Result is between 0 and 255 if read operation was successful, else a negative number for an error.
     *
     * @param address local address in the i2c device
     * @return byte value read: positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     * 
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(int address) throws IOException {

    	try {
    		bus.lock();
    	} catch (InterruptedException e) {
    		throw new IOException(e);
    	}
    	
        try {
        	
            int ret = bus.readByte(this, address);
            if (ret < 0) {
                throw new IOException("Error reading from " + makeDescription(address) + ". Got " + ret + ".");
            }
            return ret;
            
        } finally {
            bus.unlock();
        }
        
    }

    /**
     * <p>This method reads bytes from the i2c device to given buffer at asked offset. </p>
     * 
     * <p>Note: Current implementation calls {@link #read(int)}. That means for each read byte 
     * i2c bus will send (next) address to i2c device.
     * </p>
     * 
     * @param address local address in the i2c device
     * @param buffer buffer of data to be read from the i2c device in one go
     * @param offset offset in buffer 
     * @param size number of bytes to be read 
     * 
     * @return number of bytes read
     * 
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(int address, byte[] buffer, int offset, int size) throws IOException {

    	try {
    		bus.lock();
    	} catch (InterruptedException e) {
    		throw new IOException(e);
    	}
    	
        try {
        	
            // It doesn't work for some reason.
            int ret = bus.readBytes(this, address, size, offset, buffer);
            if (ret < 0) {
                throw new IOException("Error reading from " + makeDescription(address) + ". Got " + ret + ".");
            }
            return ret;
            
        } finally {
            bus.unlock();
        }
        
    }

    /**
     * This method writes and reads bytes to/from the i2c device in a single method call
     *
     * @param writeBuffer buffer of data to be written to the i2c device in one go
     * @param writeOffset offset in write buffer
     * @param writeSize number of bytes to be written from buffer
     * @param readBuffer buffer of data to be read from the i2c device in one go
     * @param readOffset offset in read buffer
     * @param readSize number of bytes to be read
     *
     * @return number of bytes read
     *
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(byte[] writeBuffer, int writeOffset, int writeSize, byte[] readBuffer, int readOffset, int readSize) throws IOException {

    	try {
    		bus.lock();
    	} catch (InterruptedException e) {
    		throw new IOException(e);
    	}
    	
        try {
        	
            int ret = bus.writeAndReadBytesDirect(this, writeSize, writeOffset, 
            		writeBuffer, readSize, readOffset, readBuffer);
            if (ret < 0) {
                throw new IOException("Error reading from " + makeDescription() + ". Got " + ret + ".");
            }
            return ret;
            
        } finally {
            bus.unlock();
        }
        
    }

    /**
     * This helper method creates a string describing bus file name and device address (in hex).
     * 
     * @return string with all details
     */
    protected String makeDescription() {
        return "I2CDevice on "
        		+ bus + " at address 0x" + Integer.toHexString(deviceAddress);
    }
    
    /**
     * This helper method creates a string describing bus file name, device address (in hex)
     * and local i2c address.
     * 
     * @param address local address in i2c device
     * @return string with all details
     */
    protected String makeDescription(int address) {
        return "I2CDevice on "
        		+ bus + " at address 0x" + Integer.toHexString(deviceAddress) 
                + " to address 0x" + Integer.toHexString(address);
    }
    
}

