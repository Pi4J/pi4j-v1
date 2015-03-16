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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.jni.I2C;

/**
 * This is implementation of i2c bus. This class keeps underlying linux file descriptor of
 * particular bus. As all reads and writes from/to i2c bus are blocked I/Os current implementation uses only 
 * one file per bus for all devices. Device implementations use this class file handle.
 * 
 * Hint: For concurrency-locking the methods lock() and unlock() are provided. This requires
 * that there is exactly one I2CBus-instance per bus-number what is guaranteed by the I2CFactory class.  
 * The locking is done by I2CDeviceImpl by using those methods. The reason for this is to
 * enable other locking-strategies than the simple "lock before and release after access"-strategy.  
 * 
 * @author Daniel Sendula, refactored by <a href="http://raspelikan.blogspot.co.at">RasPelikan</a>
 *
 */
public abstract class I2CBusImpl implements I2CBus {

	public static class UnsupportedBusNumberException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public UnsupportedBusNumberException() {
			super();
		}
	}

    private static final Logger logger = Logger.getLogger(I2CBusImpl.class.getCanonicalName());
    
    /** Singletons */
    private static Map<Integer, I2CBus> busSingletons = new HashMap<>();
    
    /** to lock the creation/destruction of the bus singletons */
    protected final static Lock singletonPerBusLock = new ReentrantLock( true );
    
    /** 
     * Factory method that returns bus implementation.
     * 
     * @param newInstanceCandidate if no bus has been created yet, this instance is used
     * @param busNumber bus number
     * @param lockAquireTimeout maximal amount of time to wait for an i2c operation to get exclusive access to the bus
     * @param lockAquireTimeoutUnit units of lockAquireTimeout
     * @return appropriate bus implementation
     * @throws IOException thrown in case there is a problem opening bus file or bus number is not 0 or 1.
     */
    protected static I2CBus getBus(I2CBusImpl newInstanceCandidate, int busNumber,
    		long lockAquireTimeout, TimeUnit lockAquireTimeoutUnit)
    				throws UnsupportedBusNumberException, IOException {
    	
        final I2CBus bus;
        
        InterruptedException lockException = null;
        boolean locked = false;
        try {
	        if (singletonPerBusLock.tryLock()
	        		|| singletonPerBusLock.tryLock(lockAquireTimeout, lockAquireTimeoutUnit)) {
	        	singletonPerBusLock.lock();
	        	locked = true;
	        }
        } catch (InterruptedException e) {
        	lockException = e;
        }
        if (! locked) {
        	throw new RuntimeException("Could not abtain lock to build new bus!", lockException);
        }
        
        try {

        	final I2CBus i2cBus = busSingletons.get(busNumber);
        	
        	if (i2cBus == null) {
        		
        		bus = newInstanceCandidate;
        		busSingletons.put(busNumber, bus);
        		
        	} else {
        		bus = i2cBus;
        	}
        	
        } finally {
        	
        	try {
        		singletonPerBusLock.unlock();
        	} catch (Throwable e) {
        		logger.log(Level.WARNING,
        				"Unlocking 'singletonPerBusLock' throws an exception", e);
        	}
        	
        }
        
        return bus;
        
    }

    /** File handle for this i2c bus */
    protected int fd = -1;
    
    /** File name of this i2c bus */
    protected String filename;
    
    /** Used to identifiy the i2c bus within Pi4J **/
    protected int busNumber;
    
    protected long lockAquireTimeout;
    
    protected TimeUnit lockAquireTimeoutUnit;
    
    private final ReentrantLock accessLock = new ReentrantLock( true );
    /**
     * @param busNumber used to identifiy the i2c bus within Pi4J
     * @result filename file name of device to be opened.
     */
    protected abstract String getFilenameForBusnumber(
    		final int busNumber) throws UnsupportedBusNumberException;
    
    /**
     * Constructor of i2c bus implementation.
     * 
     * @param busNumber used to identifiy the i2c bus within Pi4J
     * @throws IOException thrown in case that file cannot be opened
     */
    protected I2CBusImpl(final int busNumber,
    		final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit)
    				throws UnsupportedBusNumberException, IOException {
    	
        this.filename = getFilenameForBusnumber(busNumber);
        this.busNumber = busNumber;
        
        if (lockAquireTimeout < 0) {
        	this.lockAquireTimeout = I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT;
        } else {
        	this.lockAquireTimeout = lockAquireTimeout;
        }
        if (lockAquireTimeoutUnit == null) {
        	this.lockAquireTimeoutUnit = I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS;
        } else {
        	this.lockAquireTimeoutUnit = lockAquireTimeoutUnit;
        }
        
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
    
    @Override
    public <T> T runActionOnExclusivLockedBus(final I2CRunnable<T> action)
    		throws IOException {
    	
    	if (action == null) {
    		throw new RuntimeException("Parameter 'action' is mandatory!");
    	}
    	
		testWhetherBusHasAlreadyBeenClosed();
    	
		boolean locked = false;
    	try {
    		
	    	lock();
	    	locked = true;
	    	
    		action.run();
	    
    	} catch (InterruptedException e) {
    		
    		logger.log(Level.FINER, "Failed locking I2CBusImpl-"
    				+ busNumber, e);
    		throw new RuntimeException("Could not abtain an access-lock!", e);
    		
    	} catch (IOExceptionWrapperException e) { // unwrap IOExceptionWrapperException
    		throw e.getIOException();
    	} finally {
    		
    		if (locked) {
	    		try {
	    			unlock();
	    		} catch (Throwable e) {
	    			logger.log(Level.WARNING, "Could not unlock access-lock!", e);
	    		}
    		}
    		
    	}
    	
    	return action.getResult();
    	
    }

	private void unlock() {
		
		logger.log(Level.FINEST, "Will try to lock I2CBusImpl-{0}", busNumber);
		
		accessLock.unlock();
		
		logger.log(Level.FINER, "I2CBusImpl-{0} unlocked", busNumber);
		
	}

	private void lock() throws InterruptedException {
		
		logger.log(Level.FINEST, "Will try to lock I2CBusImpl-{0}", busNumber);
		
		// "or" means barging on a fair lock
		if (accessLock.tryLock(lockAquireTimeout, lockAquireTimeoutUnit)) {
			logger.log(Level.FINER, "I2CBusImpl-{0} locked", busNumber);
		} else {
			throw new InterruptedException("timeout for lock acquisition elapsed");
		}
		
	}
    
    /**
     * Closes this i2c bus
     * 
     * @throws IOException never in this implementation
     */
    @Override
    public void close() throws IOException {
    	
    	testWhetherBusHasAlreadyBeenClosed();
    	
        singletonPerBusLock.lock();
        try {
        	
	        I2C.i2cClose(fd);
	        fd = -1;
	        
        } finally {
        	
	        /* after closing the fd, we must "forget" the singleton bus instance, otherwise further request to this bus will
	         * always fail
	         */
        	try {
        		busSingletons.remove(busNumber);
        	} catch (Throwable e) {
        		logger.log(Level.WARNING,
        				"Error on removing bus '"
        				+ busNumber
        				+ "' from the pool of busses!", e);
        	}
        	
        	try {
        		singletonPerBusLock.unlock();
        	} catch (Throwable e) {
        		logger.log(Level.WARNING,
        				"Unlocking 'singletonPerBusLock' throws an exception", e);
        	}
        	
        }
        
    }
    
    public int readByteDirect(final I2CDeviceImpl device) throws IOException {
    	
    	testWhetherBusHasAlreadyBeenClosed();

    	if (device == null) {
    		throw new NullPointerException("Parameter 'device' is mandatory!");
    	}

    	return I2C.i2cReadByteDirect(fd, device.getAddress());
    	
    }
    
    public int readBytesDirect(final I2CDeviceImpl device, final int size, final int offset,
    		final byte[] buffer) throws IOException {
    	
    	testWhetherBusHasAlreadyBeenClosed();

    	if (device == null) {
    		throw new NullPointerException("Parameter 'device' is mandatory!");
    	}

    	return I2C.i2cReadBytesDirect(fd, device.getAddress(), size, offset, buffer);
    	
    }
    
    public int readByte(final I2CDeviceImpl device, final int localAddress) throws IOException {

    	testWhetherBusHasAlreadyBeenClosed();

    	if (device == null) {
    		throw new NullPointerException("Parameter 'device' is mandatory!");
    	}

    	return I2C.i2cReadByte(fd, device.getAddress(), localAddress);

    }
    
    public int readBytes(final I2CDeviceImpl device, final int localAddress,
    		final int size, final int offset, final byte[] buffer) throws IOException {

    	testWhetherBusHasAlreadyBeenClosed();

    	if (device == null) {
    		throw new NullPointerException("Parameter 'device' is mandatory!");
    	}

    	return I2C.i2cReadBytes(fd, device.getAddress(), localAddress,
    			size, offset, buffer);

    }
    
    public int writeByteDirect(final I2CDeviceImpl device, final byte data) throws IOException {
    	
    	testWhetherBusHasAlreadyBeenClosed();

    	if (device == null) {
    		throw new NullPointerException("Parameter 'device' is mandatory!");
    	}

    	return I2C.i2cWriteByteDirect(data, device.getAddress(), data);
    	
    }
    
    public int writeBytesDirect(final I2CDeviceImpl device, final int size,
    		final int offset, final byte[] buffer) throws IOException {
    	
    	testWhetherBusHasAlreadyBeenClosed();

    	if (device == null) {
    		throw new NullPointerException("Parameter 'device' is mandatory!");
    	}

    	return I2C.i2cWriteBytesDirect(fd, device.getAddress(), size, offset, buffer);
    	
    }
    
    public int writeByte(final I2CDeviceImpl device, final int localAddress, final byte data) throws IOException {
    	
    	testWhetherBusHasAlreadyBeenClosed();

    	if (device == null) {
    		throw new NullPointerException("Parameter 'device' is mandatory!");
    	}

    	return I2C.i2cWriteByte(fd, device.getAddress(), localAddress, data);
    	
    }
    
    public int writeBytes(final I2CDeviceImpl device, final int localAddress,
    		final int size, final int offset, final byte[] buffer) throws IOException {
    	
    	testWhetherBusHasAlreadyBeenClosed();

    	if (device == null) {
    		throw new NullPointerException("Parameter 'device' is mandatory!");
    	}

    	return I2C.i2cWriteBytes(fd, device.getAddress(), localAddress, size, offset, buffer);

    }
    
    public int writeAndReadBytesDirect(final I2CDeviceImpl device,
    		final int writeSize, final int writeOffset, final byte[] writeBuffer,
    		final int readSize, final int readOffset, final byte[] readBuffer) throws IOException {
    	
    	testWhetherBusHasAlreadyBeenClosed();

    	if (device == null) {
    		throw new NullPointerException("Parameter 'device' is mandatory!");
    	}
    	
    	return I2C.i2cWriteAndReadBytes(fd, device.getAddress(),
    			writeSize, writeOffset, writeBuffer,
    			readSize, readOffset, readBuffer);
    	
    }
    
    private void testWhetherBusHasAlreadyBeenClosed() throws IOException {
    	
    	if (fd == -1) {
    		throw new IOException(toString()
    				+ " has already been closed! A new bus has to be aquired.");
    	}
    	
    }
    
	@Override
	public int getBusNumber() {
		return busNumber;
	}
	
	@Override
	public String toString() {
		return "I2CBus '" + busNumber + "' ('" + filename + "')";
	}
	
}
