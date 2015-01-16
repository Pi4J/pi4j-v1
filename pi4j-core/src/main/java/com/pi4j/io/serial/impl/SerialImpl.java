package com.pi4j.io.serial.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialImpl.java  
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


import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialPortException;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p> This implementation class implements the 'Serial' interface using the WiringPi Serial library.</p>
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
 * @see com.pi4j.io.serial.SerialDataEvent
 * @see com.pi4j.io.serial.SerialDataListener
 * @see com.pi4j.io.serial.SerialFactory
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SerialImpl implements Serial {

    protected int fileDescriptor = -1;
    protected final CopyOnWriteArrayList<SerialDataListener> listeners = new CopyOnWriteArrayList<>();
    protected SerialDataMonitorThread monitor;
    protected int monitorInterval = Serial.DEFAULT_MONITOR_INTERVAL;
    protected boolean isshutdown = false;

    /**
     * This method is call to open a serial port for communication. Throws SerialException on error.
     * 
     * @see #DEFAULT_COM_PORT
     * 
     * @param device The device address of the serial port to access. You can use constant
     *            'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *            GPIO header.
     * @param baudRate The baud rate to use with the serial port.
     * @throws SerialPortException Exception thrown on any error.
     */
    public void open(String device, int baudRate) throws SerialPortException {
        fileDescriptor = com.pi4j.wiringpi.Serial.serialOpen(device, baudRate);
        if (fileDescriptor == -1) {
        	throw new SerialPortException("Cannot open serial port");
        }
    }

    /**
     * This method is called to determine if the serial port is already open.
     * 
     * @see #open(String, int)
     * @return a value of 'true' is returned if the serial port is already open.
     */
    public boolean isOpen() {
        return (fileDescriptor >= 0);
    }
    
    /**
     * This method is called to determine if the serial port is already closed.
     * 
     * @see #open(String, int)
     * @return a value of 'true' is returned if the serial port is already in the closed state.
     */
    public boolean isClosed(){
        return !(isOpen());
    }
    

    /**
     * This method is called to close a currently open open serial port.
     */
    public void close() throws IllegalStateException {
    	
        // validate state
        if (isClosed()) 
    	    throw new IllegalStateException("Serial connection is not open; cannot 'close()'.");
    	
    	// close serial port now    
    	com.pi4j.wiringpi.Serial.serialClose(fileDescriptor);
	}

    /**
     * This method is called to immediately flush the serial data transmit buffer and force any
     * pending data to be sent to the serial port immediately.
     */
    public void flush() throws IllegalStateException {

        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'flush()'.");
        
        // flush data to serial port immediately
        com.pi4j.wiringpi.Serial.serialFlush(fileDescriptor);
    }

    /**
     * <p> This method will read the next character available from the serial port receive buffer.</p>
     * <p>
     * <b>NOTE: If a serial data listener has been implemented and registered with this class, then
     * this method should not be called directly. A background thread will be running to collect
     * received data from the serial port receive buffer and the received data will be available on
     * via the event.</b>
     * </p>
     * 
     * @return next available character in the serial data buffer
     */
    public char read() throws IllegalStateException {
        
        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        // attempt read byte from serial port 
        return (char) com.pi4j.wiringpi.Serial.serialGetchar(fileDescriptor);
    }

    /**
     * This method is called to submit a single character of data to the serial port transmit
     * buffer.
     * 
     * @param data  A single character to be transmitted.
     */
    public void write(char data) throws IllegalStateException {
        
        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'write(char)'.");
        
        // write character to serial port
        com.pi4j.wiringpi.Serial.serialPutchar(fileDescriptor, data);
    }

    /**
     * This method is called to submit a character array of data to the serial port transmit buffer.
     * 
     * @param data A character array of data to be transmitted.
     */
    public void write(char data[]) throws IllegalStateException {
        
        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'write(char[])'.");

        // write character array to serial port
        write(new String(data));
    }

    /**
     * This method is called to submit a single byte of data to the serial port transmit buffer.
     * 
     * @param data  A single byte to be transmitted.
     */
    public void write(byte data) throws IllegalStateException {

        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'write(byte)'.");

        // write byte to serial port        
        com.pi4j.wiringpi.Serial.serialPutchar(fileDescriptor, (char) data);
    }

    /**
     * This method is called to submit a byte array of data to the serial port transmit buffer.
     * 
     * @param data  A byte array of data to be transmitted.
     */
    public void write(byte data[]) throws IllegalStateException {
        
        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'write(byte[])'.");

        // write byte array to serial port
        for(byte b : data){
            // write byte to serial port        
            com.pi4j.wiringpi.Serial.serialPutchar(fileDescriptor, (char)b);
        }
    }

    /**
     * This method is called to submit a string of data to the serial port transmit buffer.
     * 
     * @param data A string of data to be transmitted.
     */
    public void write(String data) throws IllegalStateException {
        
        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'write(String)'.");

        // break data into packets of 1024 bytes
        int position = 0;
        while (position < data.length()) {
            int length = 1024;
            if (position + 1024 > data.length()) {
                com.pi4j.wiringpi.Serial.serialPuts(fileDescriptor, data.substring(position));
            } else {
                com.pi4j.wiringpi.Serial.serialPuts(fileDescriptor,
                                                    data.substring(position, (position + length)));
            }
            position += length;
        }
    }
    
    /**
     * This method is called to submit a string of data with trailing CR + LF characters to the
     * serial port transmit buffer.
     * 
     * @param data A string of data to be transmitted.
     */
    public void writeln(String data) throws IllegalStateException {
        
        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'writeln(String)'.");
        
        // write string with CR+LF to serial port 
        write(data + "\r\n");
    }

    /**
     * This method is called to submit a string of formatted data to the serial port transmit
     * buffer.
     * 
     * @param data A string of formatted data to be transmitted.
     * @param args  A series of arguments that can be included for the format string variable
     *            replacements.
     */
    public void write(String data, String... args) throws IllegalStateException {
        
        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'write(String data, String...args)'.");
        
        // write formatted string to serial port 
        write(String.format(data, (Object[]) args));
    }

    /**
     * This method is called to submit a string of formatted data with trailing CR + LF characters
     * to the serial port transmit buffer.
     * 
     * @param data  A string of formatted data to be transmitted.
     * @param args  A series of arguments that can be included for the format string variable
     *            replacements.
     */
    public void writeln(String data, String... args) throws IllegalStateException {
        
        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'writeln(String data, String...args)'.");
        
        // write formatted string with CR+LF to serial port         
        write(data + "\r\n", args);
    }
    
    /**
     * This method is called to determine if and how many bytes are available on the serial received
     * data buffer.
     * 
     * @return  The number of available bytes pending in the serial received buffer is returned.
     */
    public int availableBytes() throws IllegalStateException {
        
        // validate state
        if (isClosed()) 
            throw new IllegalStateException("Serial connection is not open; cannot 'availableBytes()'.");
        
        // return the number of bytes available in the serial buffer                 
        return com.pi4j.wiringpi.Serial.serialDataAvail(fileDescriptor);
    }

    /**
     * <p>Add Serial Event Listener</p>
     * 
     * <p> Java consumer code can call this method to register itself as a listener for serial data
     * events. </p>
     * 
     * @see com.pi4j.io.serial.SerialDataListener
     * @see com.pi4j.io.serial.SerialDataEvent
     * 
     * @param listener  A class instance that implements the SerialListener interface.
     */
    public synchronized void addListener(SerialDataListener... listener) {
        // add the new listener to the list of listeners
        Collections.addAll(listeners, listener);

        // if there is not a current listening monitor thread running,
        // then lets start it now
        if (monitor == null || !monitor.isAlive()) {
            monitor = new SerialDataMonitorThread(this, listeners);
            monitor.start();
        }
    }

    /**
     * <p>Remove Serial Event Listener</p>
     * 
     * <p> Java consumer code can call this method to unregister itself as a listener for serial data
     * events. </p>
     * 
     * @see com.pi4j.io.serial.SerialDataListener
     * @see com.pi4j.io.serial.SerialDataEvent
     * 
     * @param listener A class instance that implements the SerialListener interface.
     */
    public synchronized void removeListener(SerialDataListener... listener) {
        // remove the listener from the list of listeners
        for (SerialDataListener lsnr : listener) {
            listeners.remove(lsnr);
        }

        // if there are not more listeners, then exit and destroy
        // the monitor thread now
        if (listeners.isEmpty() && monitor != null) {
            monitor.shutdown();
            monitor = null;
        }
    }
    
    /**
     * This method returns TRUE if the serial interface has been shutdown.
     * 
     * @return shutdown state
     */
    @Override
    public boolean isShutdown(){
        return isshutdown;
    }

    
    /**
     * This method can be called to forcefully shutdown all 
     * serial data monitoring threads.
     */
    @Override
    public synchronized void shutdown()
    {
        // close serial port if still open
        if(isOpen())
            close();

        // prevent reentrant invocation
        if(isShutdown())
            return;
        
        // shutdown monitoring thread
        if(monitor != null)
            monitor.shutdown();
    }

    /**
     * This method returns the serial data receive monitor delay interval in milliseconds.
     * @return interval milliseconds
     */
    @Override
    public int getMonitorInterval(){
        return monitorInterval;
    }

    /**
     * This method set the serial data receive monitor delay interval in milliseconds.
     *
     * @param interval number of milliseconds
     */
    @Override
    public void setMonitorInterval(int interval){
        monitorInterval = interval;
    }
}
