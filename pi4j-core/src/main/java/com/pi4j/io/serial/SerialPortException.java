package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialPortException.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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


/**
 * <p> This class represents Exception that might occur in Serial interface.</p>
 *
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see com.pi4j.io.serial.Serial
 *
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Jakub Derda (<a
 *         href="http://www.ardeo.pl">http://www.ardeo.pl</a>)
 */
@SuppressWarnings("unused")
public class SerialPortException extends RuntimeException {

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default no argument constructor.
	 */

	public SerialPortException() {
		super();
	}

	/**
	 * Constructor with description.
	 *
	 * @param message Description of error that occured.
	 */
    public SerialPortException(String message) {
		super(message);
	}


	/**
	 * Constructor with cause.
	 *
	 * @param cause Cause of SerialException.
	 */
    public SerialPortException(Throwable cause) {
		super(cause);
	}
}
