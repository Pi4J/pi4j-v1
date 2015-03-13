package com.pi4j.io.i2c.impl;

import java.io.IOException;

public class IOExceptionWrapperException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private IOException ioException;
	
	public IOExceptionWrapperException(final IOException ioException) {
		this.ioException = ioException;
	}
	
	public IOException getIoException() {
		return ioException;
	}

}
