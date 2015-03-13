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
    public void write(final byte data) throws IOException {
    	
    	final I2CDeviceImpl currentDevice = this;
    	try {
    		
	    	bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
	
					try {
						
			            int ret = bus.writeByteDirect(currentDevice, data);
			            if (ret < 0) {
			                throw new IOException("Error writing to " + makeDescription() + ". Got " + ret + ".");
			            }
			            
					} catch (IOException e) {
						throw new IOExceptionWrapperException(e);
					}
					
				}
			});
	    	
    	} catch (IOException e) {
    		throw e;
    	} catch (RuntimeException e) {
    		throw e;
    	} catch (Exception e) {
    		throw new RuntimeException(e);
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
    public void write(final byte[] buffer, final int offset, final int size) throws IOException {

    	final I2CDeviceImpl currentDevice = this;
    	try {
    		
	    	bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
	
					try {
						
			            int ret = bus.writeBytesDirect(currentDevice, size, offset, buffer);
			            if (ret < 0) {
			                throw new IOException("Error writing to " + makeDescription() + ". Got " + ret + ".");
			            }
            
					} catch (IOException e) {
						throw new IOExceptionWrapperException(e);
					}
					
				}
			});
	    	
    	} catch (IOException e) {
    		throw e;
    	} catch (RuntimeException e) {
    		throw e;
    	} catch (Exception e) {
    		throw new RuntimeException(e);
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
    public void write(final int address, final byte data) throws IOException {

    	final I2CDeviceImpl currentDevice = this;
    	try {
    		
	    	bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
	
					try {
						
			        	int ret = bus.writeByte(currentDevice, address, data);
			            if (ret < 0) {
			                throw new IOException("Error writing to " + makeDescription(address) + ". Got " + ret + ".");
			            }
			            
					} catch (IOException e) {
						throw new IOExceptionWrapperException(e);
					}
					
				}
			});
	    	
    	} catch (IOException e) {
    		throw e;
    	} catch (RuntimeException e) {
    		throw e;
    	} catch (Exception e) {
    		throw new RuntimeException(e);
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
    public void write(final int address, final byte[] buffer, final int offset, final int size) throws IOException {

    	final I2CDeviceImpl currentDevice = this;
    	try {
    		
	    	bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
	
					try {
						
			            int ret = bus.writeBytes(currentDevice, address, size, offset, buffer);
			            if (ret < 0) {
			                throw new IOException("Error writing to " + makeDescription(address) + ". Got " + ret + ".");
			            }
            
					} catch (IOException e) {
						throw new IOExceptionWrapperException(e);
					}
					
				}
			});
	    	
    	} catch (IOException e) {
    		throw e;
    	} catch (RuntimeException e) {
    		throw e;
    	} catch (Exception e) {
    		throw new RuntimeException(e);
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

    	final I2CDeviceImpl currentDevice = this;
    	final int[] result = new int[1];
    	try {
    		
	    	bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
	
					try {
						
			            int ret = bus.readByteDirect(currentDevice);
			            if (ret < 0) {
			                throw new IOException("Error reading from " + makeDescription() + ". Got " + ret + ".");
			            }
			            result[0] = ret;
            
					} catch (IOException e) {
						throw new IOExceptionWrapperException(e);
					}
					
				}
			});
	    	
	    	return result[0];
	    	
    	} catch (IOException e) {
    		throw e;
    	} catch (RuntimeException e) {
    		throw e;
    	} catch (Exception e) {
    		throw new RuntimeException(e);
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
    public int read(final byte[] buffer, final int offset, final int size) throws IOException {

    	final I2CDeviceImpl currentDevice = this;
    	final int[] result = new int[1];
    	try {
    		
	    	bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
	
					try {
						
			            // It doesn't work for some reason.
			            int ret = bus.readBytesDirect(currentDevice, size, offset, buffer);
			            if (ret < 0) {
			                throw new IOException("Error reading from " + makeDescription() + ". Got " + ret + ".");
			            }
			            result[0] = ret;
            
					} catch (IOException e) {
						throw new IOExceptionWrapperException(e);
					}
					
				}
			});
	    	
	    	return result[0];
	    	
		} catch (IOException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
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
    public int read(final int address) throws IOException {

    	final I2CDeviceImpl currentDevice = this;
    	final int[] result = new int[1];
    	try {
    		
	    	bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
	
					try {
						
			            int ret = bus.readByte(currentDevice, address);
			            if (ret < 0) {
			                throw new IOException("Error reading from " + makeDescription(address) + ". Got " + ret + ".");
			            }
			            result[0] = ret;
            
					} catch (IOException e) {
						throw new IOExceptionWrapperException(e);
					}
					
				}
			});
	    	
	    	return result[0];
	    	
		} catch (IOException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
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
    public int read(final int address, final byte[] buffer, final int offset, final int size) throws IOException {

    	final I2CDeviceImpl currentDevice = this;
    	final int[] result = new int[1];
    	try {
    		
	    	bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
	
					try {
						
			            // It doesn't work for some reason.
			            int ret = bus.readBytes(currentDevice, address, size, offset, buffer);
			            if (ret < 0) {
			                throw new IOException("Error reading from " + makeDescription(address) + ". Got " + ret + ".");
			            }
		            	result[0] = ret;
            
					} catch (IOException e) {
						throw new IOExceptionWrapperException(e);
					}
					
				}
			});
			
	    	return result[0];
	    	
		} catch (IOException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
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
    public int read(final byte[] writeBuffer, final int writeOffset,
    		final int writeSize, final byte[] readBuffer, final int readOffset,
    		final int readSize) throws IOException {

    	final I2CDeviceImpl currentDevice = this;
    	final int[] result = new int[1];
    	try {
    		
	    	bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
	
					try {
						
			            int ret = bus.writeAndReadBytesDirect(currentDevice, writeSize, writeOffset, 
			            		writeBuffer, readSize, readOffset, readBuffer);
			            if (ret < 0) {
			                throw new IOException("Error reading from " + makeDescription() + ". Got " + ret + ".");
			            }
		            	result[0] = ret;
            
					} catch (IOException e) {
						throw new IOExceptionWrapperException(e);
					}
					
				}
			});
	    	
	    	return result[0];
	    	
		} catch (IOException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
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

