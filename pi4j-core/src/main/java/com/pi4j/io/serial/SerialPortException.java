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
 * Copyright (C) 2012 - 2013 Pi4J
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
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see #com.pi4j.io.serial.Serial
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Jakub Derda (<a
 *         href="http://www.ardeo.pl">http://www.ardeo.pl</a>)
 */
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
