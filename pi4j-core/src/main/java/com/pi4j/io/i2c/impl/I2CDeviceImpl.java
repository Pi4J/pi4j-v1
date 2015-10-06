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

import java.io.IOException;
import java.util.concurrent.Callable;

import com.pi4j.io.i2c.I2CDevice;

/**
 * Implementation of i2c device. This class only holds reference to i2c bus (so it can use its handle) and
 * device address.
 * 
 * @author Daniel Sendula, refactored by <a href="http://raspelikan.blogspot.co.at">RasPelikan</a>
 *
 */
public class I2CDeviceImpl implements I2CDevice {

	/**
     * Reference to i2c bus
     */
    private I2CBusImpl bus;
    
    /**
     * I2c device address
     */
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
     * Used by WriteRunnables and ReadRunnables.
     * 
     * @return The bus associated with this I2CDeviceImpl instance.
     */
    I2CBusImpl getBus() {
    	return bus;
    }
    
    /**
     * This method writes one byte to i2c device. 
     * 
     * @param data byte to be written
     * 
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(final byte data) throws IOException {
    	
    	final I2CDeviceImpl device = this;
    	bus.runActionOnExclusivLockedBus(
    			new Callable<Void>() {
    				@Override
    				public Void call() throws Exception {
    					int ret = getBus().writeByteDirect(device, data);
    					if (ret < 0) {
    						throw new IOException(
    								"Error writing to "
    								+ device.makeDescription()
    								+ ". Got '"
    								+ ret
    								+ "'.");
    								
    					}
    					return null;
    				}
				});
    	
    }

    /**
     * This method writes several bytes to the i2c device from given buffer at given offset.
     * 
     * @param data buffer of data to be written to the i2c device in one go
     * @param offset offset in buffer 
     * @param size number of bytes to be written 
     * 
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(final byte[] data, final int offset, final int size) throws IOException {

    	final I2CDeviceImpl device = this;
    	bus.runActionOnExclusivLockedBus(
    			new Callable<Void>() {
    				@Override
    				public Void call() throws Exception {
    					int ret = getBus().writeBytesDirect(device, size, offset, data);
    					if (ret < 0) {
    						throw new IOException(
    								"Error writing to "
    								+ device.makeDescription()
    								+ ". Got '"
    								+ ret
    								+ "'.");
    								
    					}
    					return null;
    				}
				});
        
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
    public void write(final int address, final byte data) throws IOException {

    	final I2CDeviceImpl device = this;
    	bus.runActionOnExclusivLockedBus(
    			new Callable<Void>() {
    				@Override
    				public Void call() throws Exception {
    					int ret = getBus().writeByte(device, address, data);
    					if (ret < 0) {
    						throw new IOException(
    								"Error writing to "
    								+ device.makeDescription(address)
    								+ ". Got '"
    								+ ret
    								+ "'.");
    								
    					}
    					return null;
    				}
				});
        
    }

    /**
     * This method writes several bytes to the i2c device from given buffer at given offset.
     * 
     * @param address local address in the i2c device
     * @param data buffer of data to be written to the i2c device in one go
     * @param offset offset in buffer 
     * @param size number of bytes to be written 
     * 
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(final int address, final byte[] data, final int offset, final int size) throws IOException {

    	final I2CDeviceImpl device = this;
    	bus.runActionOnExclusivLockedBus(
    			new Callable<Void>() {
    				@Override
    				public Void call() throws Exception {
    					int ret = getBus().writeBytes(device, address, size, offset, data);
    					if (ret < 0) {
    						throw new IOException(
    								"Error writing to "
    								+ device.makeDescription(address)
    								+ ". Got '"
    								+ ret
    								+ "'.");
    								
    					}
    					return null;
    				}
				});
        
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

    	final I2CDeviceImpl device = this;
    	return bus.runActionOnExclusivLockedBus(
    			new Callable<Integer>() {
    				@Override
    				public Integer call() throws Exception {
    					int ret = getBus().readByteDirect(device);
    					if (ret < 0) {
    						throw new IOException(
    								"Error reading from "
    								+ device.makeDescription()
    								+ ". Got '"
    								+ ret
    								+ "'.");
    								
    					}
    					return ret;
    				}
				});
        
    }

    /**
     * <p>This method reads bytes from the i2c device to given buffer at asked offset. </p>
     * 
     * <p>Note: Current implementation calls {@link #read(int)}. That means for each read byte 
     * i2c bus will send (next) address to i2c device.
     * </p>
     * 
     * @param data buffer of data to be read from the i2c device in one go
     * @param offset offset in buffer 
     * @param size number of bytes to be read 
     * 
     * @return number of bytes read
     * 
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(final byte[] data, final int offset, final int size) throws IOException {

    	final I2CDeviceImpl device = this;
    	return bus.runActionOnExclusivLockedBus(
    			new Callable<Integer>() {
    				@Override
    				public Integer call() throws Exception {
    					int ret = getBus().readBytesDirect(device, size, offset, data);
    					if (ret < 0) {
    						throw new IOException(
    								"Error reading from "
    								+ device.makeDescription()
    								+ ". Got '"
    								+ ret
    								+ "'.");
    								
    					}
    					return ret;
    				}
				});
        
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
    public int read(final int address) throws IOException {

    	final I2CDeviceImpl device = this;
    	return bus.runActionOnExclusivLockedBus(
    			new Callable<Integer>() {
    				@Override
    				public Integer call() throws Exception {
    					int ret = getBus().readByte(device, address);
    					if (ret < 0) {
    						throw new IOException(
    								"Error reading from "
    								+ device.makeDescription(address)
    								+ ". Got '"
    								+ ret
    								+ "'.");
    								
    					}
    					return ret;
    				}
				});
        
    }

    /**
     * <p>This method reads bytes from the i2c device to given buffer at asked offset. </p>
     * 
     * <p>Note: Current implementation calls {@link #read(int)}. That means for each read byte 
     * i2c bus will send (next) address to i2c device.
     * </p>
     * 
     * @param address local address in the i2c device
     * @param data buffer of data to be read from the i2c device in one go
     * @param offset offset in buffer 
     * @param size number of bytes to be read 
     * 
     * @return number of bytes read
     * 
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(final int address, final byte[] data, final int offset, final int size) throws IOException {

    	final I2CDeviceImpl device = this;
    	return bus.runActionOnExclusivLockedBus(
    			new Callable<Integer>() {
    				@Override
    				public Integer call() throws Exception {
    					int ret = getBus().readBytes(device, address, size, offset, data);
    					if (ret < 0) {
    						throw new IOException(
    								"Error reading from "
    								+ device.makeDescription(address)
    								+ ". Got '"
    								+ ret
    								+ "'.");
    								
    					}
    					return ret;
    				}
				});
        
    }

    /**
     * This method writes and reads bytes to/from the i2c device in a single method call
     *
     * @param writeData buffer of data to be written to the i2c device in one go
     * @param writeOffset offset in write buffer
     * @param writeSize number of bytes to be written from buffer
     * @param readData buffer of data to be read from the i2c device in one go
     * @param readOffset offset in read buffer
     * @param readSize number of bytes to be read
     *
     * @return number of bytes read
     *
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(final byte[] writeData, final int writeOffset,
    		final int writeSize, final byte[] readData, final int readOffset,
    		final int readSize) throws IOException {

    	final I2CDeviceImpl device = this;
    	return bus.runActionOnExclusivLockedBus(
    			new Callable<Integer>() {
    				@Override
    				public Integer call() throws Exception {
    					int ret = getBus().writeAndReadBytesDirect(device, writeSize, writeOffset,
    							writeData, readSize, readOffset, readData);
    					if (ret < 0) {
    						throw new IOException(
    								"Error reading from "
    								+ device.makeDescription()
    								+ ". Got '"
    								+ ret
    								+ "'.");
    								
    					}
    					return ret;
    				}
				});
    	
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

