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
	 * Base class for runnables used for writing data.
	 * 
	 * @see WriteByteRunnable
	 * @see WriteBytesRunnable
	 */
	private static abstract class I2CDeviceImplRunnable<T> implements Callable<T> {

		protected I2CDeviceImpl owner;
		
		public I2CDeviceImplRunnable(final I2CDeviceImpl owner) {
			this.owner = owner;
		}
		
	}

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
    private I2CBusImpl getBus() {
    	return bus;
    }
        
    /**
     * Writes one byte to the bus.
     *
     * @see I2CDeviceImpl#write(byte)
     * @see I2CDeviceImpl#write(int, byte)
     */
	private static class WriteByteRunnable extends I2CDeviceImplRunnable<Void> {
		
		private int localAddress;
		
		private byte data;
		
		/**
		 * @param owner The runnable's owner
		 * @param data The byte to be written
		 */
		public WriteByteRunnable(final I2CDeviceImpl owner, final byte data) {
			this(owner, -1, data);
		}

		/**
		 * @param owner The runnable's owner
		 * @param localAddress The register address where the byte has to be written to
		 * @param data The byte to be written
		 */
		public WriteByteRunnable(final I2CDeviceImpl owner, 
				final int localAddress, final byte data) {
			super(owner);
			this.data = data;
			this.localAddress = localAddress;
		}
		
		@Override
		public Void call() throws Exception {

            final int ret;
            if (localAddress == -1) {
            	ret = owner.getBus().writeByteDirect(owner, data);
            } else {
            	ret = owner.getBus().writeByte(owner, localAddress, data);
            }
            
            if (ret < 0) {
            	final String desc;
            	if (localAddress == -1) {
            		desc = owner.makeDescription();
            	} else {
            		desc = owner.makeDescription(localAddress);
            	}
                throw new IOException("Error writing to " + desc + ". Got '" + ret + "'.");
            }
            
            return null;
			
		}
		
	}
	
    /**
     * Writes n bytes to the bus.
     *
     * @see I2CDeviceImpl#write(byte[], int, int)
     * @see I2CDeviceImpl#write(int, byte[], int, int)
     */
	private static class WriteBytesRunnable extends I2CDeviceImplRunnable<Void> {
		
		private int localAddress;
		
		private byte[] data;
		
		private int offset;
		
		private int size;
		
		/**
		 * @param owner The runnable's owner
		 * @param data The bytes to be written
		 * @param offset The offset where the bytes start within the data-array
		 * @param size The number of bytes to be written
		 */
		public WriteBytesRunnable(final I2CDeviceImpl owner, final byte[] data,
				final int offset, final int size) {
			this(owner, -1, data, offset, size);
		}

		/**
		 * @param owner The runnable's owner
		 * @param localAddress The register address where the byte has to be written to
		 * @param data The bytes to be written
		 * @param offset The offset where the bytes start within the data-array
		 * @param size The number of bytes to be written
		 */
		public WriteBytesRunnable(final I2CDeviceImpl owner, 
				final int localAddress, final byte[] data,
				final int offset, final int size) {
			super(owner);
			this.localAddress = localAddress;
			this.data = data;
			this.offset = offset;
			this.size = size;
		}
		
		@Override
		public Void call() throws Exception {
				
            final int ret;
            if (localAddress == -1) {
            	ret = owner.getBus().writeBytesDirect(owner, size, offset, data);
            } else {
            	ret = owner.getBus().writeBytes(owner, localAddress, size, offset, data);
            }
            
            if (ret < 0) {
            	final String desc;
            	if (localAddress == -1) {
            		desc = owner.makeDescription();
            	} else {
            		desc = owner.makeDescription(localAddress);
            	}
                throw new IOException("Error writing to " + desc + ". Got '" + ret + "'.");
            }
	            
			return null;
			
		}
		
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
    	
    	bus.runActionOnExclusivLockedBus(
    			new WriteByteRunnable(this, data));
    	
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

    	bus.runActionOnExclusivLockedBus(
    			new WriteBytesRunnable(this, data, offset, size));
        
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

    	bus.runActionOnExclusivLockedBus(
    			new WriteByteRunnable(this, address, data));
        
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

    	bus.runActionOnExclusivLockedBus(
    			new WriteBytesRunnable(this, address, data, offset, size));
        
    }

    /**
     * Reads one byte from the bus.
     * 
     * @see I2CDeviceImpl#read()
     * @see I2CDeviceImpl#read(int)
     */
    private static class ReadByteRunnable extends I2CDeviceImplRunnable<Integer> {
    	
		private int localAddress;
    	
		/**
		 * @param owner The runnable's owner
		 */
		public ReadByteRunnable(final I2CDeviceImpl owner) {
			this(owner, -1);
		}
		
		/**
		 * @param owner The runnable's owner
		 * @param localAddress The register address where the byte has to be read from
		 */
		public ReadByteRunnable(final I2CDeviceImpl owner, final int localAddress) {
			super(owner);
			this.localAddress = localAddress;
		}
		
		@Override
		public Integer call() throws Exception {
			
			final Integer result;
				
            if (localAddress == -1) {
            	result = owner.getBus().readByteDirect(owner);
            } else {
            	result = owner.getBus().readByte(owner, localAddress);
            }
            
            if (result < 0) {
            	final String desc;
            	if (localAddress == -1) {
            		desc = owner.makeDescription();
            	} else {
            		desc = owner.makeDescription(localAddress);
            	}
                throw new IOException("Error reading from " + desc + ". Got '" + result + "'.");
            }

            return result;
			
		}
    	
    }

    /**
     * Reads n bytes from the bus.
     * 
     * @see I2CDeviceImpl#read(byte[], int, int)
     * @see I2CDeviceImpl#read(int, byte[], int, int)
     * @see I2CDeviceImpl#read(byte[], int, int, byte[], int, int)
     */
    private static class ReadBytesRunnable extends I2CDeviceImplRunnable<Integer> {
    	
		private int localAddress;
		
		private byte[] data;
		
		private int offset;
		
		private int size;
		
		private byte[] writeData;
		
		private int writeOffset;
		
		private int writeSize;
    	
		/**
		 * @param owner The runnable's owner
		 * @param data The buffer where the bytes read have to been stored
		 * @param offset The offset where the bytes start within the data-array
		 * @param size The number of bytes may be read at once
		 */
		public ReadBytesRunnable(final I2CDeviceImpl owner, final byte[] data,
				final int offset, final int size) {
			this(owner, -1, data, offset, size);
		}
		
		/**
		 * @param owner The runnable's owner
		 * @param localAddress The register address where the byte has to be read from
		 * @param data The buffer where the bytes read have to been stored
		 * @param offset The offset where the bytes start within the data-array
		 * @param size The number of bytes may be read at once
		 */
		public ReadBytesRunnable(final I2CDeviceImpl owner, final int localAddress,
				final byte[] data, final int offset, final int size) {
			super(owner);
			this.localAddress = localAddress;
			this.data = data;
			this.offset = offset;
			this.size = size;
		}

		/**
		 * @param owner The runnable's owner
		 * @param writeData The bytes to be written prior to reading
		 * @param writeOffset The offset where the bytes start within the writeData-array
		 * @param writeSize The number of bytes to be written
		 * @param data The buffer where the bytes read have to been stored
		 * @param offset The offset where the bytes start within the data-array
		 * @param size The number of bytes may be read at once
		 */
		public ReadBytesRunnable(final I2CDeviceImpl owner,
					final byte[] writeData, final int writeOffset, final int writeSize,
					final byte[] data, final int offset, final int size) {
			this(owner, -1, data, offset, size);
			this.writeData = writeData;
			this.writeOffset = writeOffset;
			this.writeSize = writeSize;
		}
		
		@Override
		public Integer call() throws Exception {
			
			final Integer result;
				
			if (localAddress != -1) {
    			result = owner.getBus().readBytes(owner, localAddress, size, offset, data);
			} else  if (writeData == null) {
				result = owner.getBus().readBytesDirect(owner, size, offset, data);
			} else {
				result = owner.getBus().writeAndReadBytesDirect(owner,
						writeSize, writeOffset, writeData, size, offset, data);
			}
            
            if (result < 0) {
            	final String desc;
            	if (localAddress == -1) {
            		desc = owner.makeDescription();
            	} else {
            		desc = owner.makeDescription(localAddress);
            	}
                throw new IOException("Error reading from " + desc + ". Got '" + result + "'.");
            }
    
			return result;
			
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

    	return bus.runActionOnExclusivLockedBus(
    			new ReadByteRunnable(this));
        
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

    	return bus.runActionOnExclusivLockedBus(
    			new ReadBytesRunnable(this, data, offset, size));
        
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

    	return bus.runActionOnExclusivLockedBus(
    			new ReadByteRunnable(this, address));
        
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

    	return bus.runActionOnExclusivLockedBus(
    			new ReadBytesRunnable(this, address, data, offset, size));
        
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

    	return bus.runActionOnExclusivLockedBus(
    			new ReadBytesRunnable(this, writeData, writeOffset, writeSize,
    					readData, readOffset, readSize));
    	
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

