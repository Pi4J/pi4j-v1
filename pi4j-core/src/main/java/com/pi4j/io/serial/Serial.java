package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Serial.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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
 * <p>This interface provides a set of functions for 'Serial' communication.</p>
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
 * @see #SerialFactory
 * @see #SerialDataEvent
 * @see #SerialDataListener
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public interface Serial {

    /**
     * The default hardware COM port provided via the Raspberry Pi GPIO header.
     * 
     * @see #serialOpen(String,int)
     */
    public static final String DEFAULT_COM_PORT = "/dev/ttyAMA0";

    /**
     * This method is call to open a serial port for communication.
     * 
     * @see #DEFAULT_COM_PORT
     * 
     * @param device The device address of the serial port to access. You can use constant
     *            'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *            GPIO header.
     * @param baudRate The baud rate to use with the serial port.
     *            
     * @return The return value is the file descriptor.
     * @throws SerialPortException Exception thrown on any error.
     */
    void open(String device, int baudRate) throws SerialPortException;

    /**
     * This method is called to close a currently open open serial port.
     */
    void close() throws IllegalStateException;

    /**
     * This method is called to determine if the serial port is already open.
     * 
     * @see #open(String, int)
     * @return a value of 'true' is returned if the serial port is already open.
     */
    boolean isOpen();

    /**
     * This method is called to determine if the serial port is already closed.
     * 
     * @see #open(String, int)
     * @return a value of 'true' is returned if the serial port is already in the closed state.
     */
    boolean isClosed();
    
    /**
     * This method is called to immediately flush the serial data transmit buffer and force any
     * pending data to be sent to the serial port immediately.
     */
    void flush() throws IllegalStateException;

    /**
     * <p>This method will read the next character available from the serial port receive buffer.</p>
     * <p>
     * <b>NOTE: If a serial data listener has been implemented and registered with this class, then
     * this method should not be called directly. A background thread will be running to collect
     * received data from the serial port receive buffer and the received data will be available on
     * via the event.</b>
     * </p>
     * 
     * @return next available character in the serial data buffer
     */
    char read() throws IllegalStateException;

    /**
     * This method is called to submit a single character of data to the serial port transmit
     * buffer.
     * 
     * @param data A single character to be transmitted.
     */
    void write(char data) throws IllegalStateException;

    /**
     * This method is called to submit a character array of data to the serial port transmit buffer.
     * 
     * @param data  A character array of data to be transmitted.
     */
    void write(char data[]) throws IllegalStateException;

    /**
     * This method is called to submit a single byte of data to the serial port transmit buffer.
     * 
     * @param data  A single byte to be transmitted.
     */
    void write(byte data) throws IllegalStateException;

    /**
     * This method is called to submit a byte array of data to the serial port transmit buffer.
     * 
     * @param data A byte array of data to be transmitted.
     */
    void write(byte data[]) throws IllegalStateException;

    /**
     * This method is called to submit a string of data to the serial port transmit buffer.
     * 
     * @param data  A string of data to be transmitted.
     */
    void write(String data) throws IllegalStateException;

    /**
     * This method is called to submit a string of data with trailing CR + LF characters to the
     * serial port transmit buffer.
     * 
     * @param data  A string of data to be transmitted.
     */
    void writeln(String data) throws IllegalStateException;

    /**
     * This method is called to submit a string of formatted data to the serial port transmit
     * buffer.
     * 
     * @param data  A string of formatted data to be transmitted.
     * @param args  A series of arguments that can be included for the format string variable
     *            replacements.
     */
    void write(String data, String... args) throws IllegalStateException;

    /**
     * This method is called to submit a string of formatted data with trailing CR + LF characters
     * to the serial port transmit buffer.
     * 
     * @param data  A string of formatted data to be transmitted.
     * @param args  A series of arguments that can be included for the format string variable
     *            replacements.
     */
    void writeln(String data, String... args) throws IllegalStateException;

    /**
     * This method is called to determine if and how many bytes are available on the serial received
     * data buffer.
     * 
     * @return The number of available bytes pending in the serial received buffer is returned.
     */
    int availableBytes() throws IllegalStateException;

    /**
     * <p>
     * Java consumer code can call this method to register itself as a listener for serial data
     * events.
     * </p>
     * 
     * @see #SerialListener
     * @see #SerialDataEvent
     * 
     * @param listener  A class instance that implements the SerialListener interface.
     */
    void addListener(SerialDataListener... listener);

    /**
     * <p> Java consumer code can call this method to unregister itself as a listener for serial data
     * events. </p>
     * 
     * @see #SerialListener
     * @see #SerialDataEvent
     * 
     * @param listener A class instance that implements the SerialListener interface.
     */
    void removeListener(SerialDataListener... listener);
        
    /**
     * This method returns TRUE if the serial interface has been shutdown.
     * 
     * @return shutdown state
     */    
    boolean isShutdown();
    
    
    /**
     * This method can be called to forcefully shutdown all serial data monitoring threads.
     */    
    void shutdown();
}
