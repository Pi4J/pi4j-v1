package com.pi4j.io.i2c.impl;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;

/**
 * Used to wrap IOExceptions throw within {@link I2CBus.I2CRunnable#run()}.
 * 
 */
public class IOExceptionWrapperException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private IOException ioException;
	
	public IOExceptionWrapperException(final IOException ioException) {
		this.ioException = ioException;
	}
	
	public IOException getIOException() {
		return ioException;
	}

}
