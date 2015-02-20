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
import java.io.InputStream;
import java.io.OutputStream;

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
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 * 
 * @see com.pi4j.io.serial.SerialFactory
 * @see com.pi4j.io.serial.SerialDataEvent
 * @see com.pi4j.io.serial.SerialDataListener
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public interface Serial extends SerialDataReader, SerialDataWriter {

    /**
     * The default hardware COM port provided via the Raspberry Pi GPIO header.
     * 
     * @see #open(String, int)
     */
    public static final String DEFAULT_COM_PORT = "/dev/ttyAMA0";
    public static final String FIRST_USB_COM_PORT = "/dev/ttyUSB0";
    public static final String SECOND_USB_COM_PORT = "/dev/ttyUSB1";


    /**
     * <p>
     * This opens and initializes the serial port/device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     *
     * @param device
     *          The device address of the serial port to access. You can use constant
     *          'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *          GPIO header.
     * @param baud
     *          The baud rate to use with the serial port. (Custom baud rate are not supported)
     * @param dataBits
     *          The data bits to use for serial communication. (5,6,7,8)
     * @param parity
     *          The parity setting to use for serial communication. (None, Event, Odd, Mark, Space)
     * @param stopBits
     *          The stop bits to use for serial communication. (1,2)
     * @param flowControl
     *          The flow control option to use for serial communication. (none, hardware, software)
     *
     * @throws SerialPortException Exception thrown on any error.
     */
    public void open(String device, int baud, int dataBits, int parity, int stopBits, int flowControl)
            throws SerialPortException;


    /**
     * <p>
     * This opens and initializes the serial port/device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     *
     * This method will use the following default serial configuration parameters:
     *  - DATA BITS    = 8
     *  - PARITY       = NONE
     *  - STOP BITS    = 1
     *  - FLOW CONTROL = NONE
     *
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     *
     * @param device
     *          The device address of the serial port to access. You can use constant
     *          'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *          GPIO header.
     * @param baud
     *          The baud rate to use with the serial port.
     *
     * @throws SerialPortException Exception thrown on any error.
     */
    public void open(String device, int baud) throws SerialPortException;

    /**
     * <p>
     * This opens and initializes the serial port/device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     *
     * @param device
     *          The device address of the serial port to access. You can use constant
     *          'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *          GPIO header.
     * @param baud
     *          The baud rate to use with the serial port.
     * @param dataBits
     *          The data bits to use for serial communication. (5,6,7,8)
     * @param parity
     *          The parity setting to use for serial communication. (None, Event, Odd, Mark, Space)
     * @param stopBits
     *          The stop bits to use for serial communication. (1,2)
     * @param flowControl
     *          The flow control option to use for serial communication. (none, hardware, software)
     *
     * @throws SerialPortException Exception thrown on any error.
     */
    public void open(String device, Baud baud, DataBits dataBits, Parity parity, StopBits stopBits,
                     FlowControl flowControl) throws SerialPortException;

    /**
     * <p>
     * This opens and initializes the serial port/device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     *
     * @param serialConfig
     *          A serial configuration object that contains the device, baud rate, data bits, parity,
     *          stop bits, and flow control settings.
     *
     * @throws SerialPortException Exception thrown on any error.
     */
    public void open(SerialConfig serialConfig) throws SerialPortException;

    /**
     * This method is called to close a currently open open serial port.
     */
    public void close() throws IllegalStateException;

    /**
     * This method is called to determine if the serial port is already open.
     * 
     * @see #open(String, int)
     * @return a value of 'true' is returned if the serial port is already open.
     */
    public boolean isOpen();

    /**
     * This method is called to determine if the serial port is already closed.
     * 
     * @see #open(String, int)
     * @return a value of 'true' is returned if the serial port is already in the closed state.
     */
    public boolean isClosed();

    /**
     * <p>
     *     Forces the transmission of any remaining data in the serial port transmit buffer.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     */
    public void flush() throws IllegalStateException, SerialPortException;

    /**
     * <p>
     *     Discards any data in the serial receive (input) buffer.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     */
    public void discardInput() throws IllegalStateException, SerialPortException;

    /**
     * <p>
     *     Discards any data in the serial transmit (output) buffer.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     */
    public void discardOutput() throws IllegalStateException, SerialPortException;

    /**
     * <p>
     *     Discards any data in  both the serial receive and transmit buffers.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     */
    public void discardAll() throws IllegalStateException, SerialPortException;

    /**
     * <p>
     *     Send a BREAK signal to connected device.
     * </p>
     *
     * @param duration
     *          The length of time (milliseconds) to send the BREAK signal
     */
    public void sendBreak(int duration) throws IllegalStateException, SerialPortException;

    /**
     * <p>
     *     Send a BREAK signal to connected device for at least 0.25 seconds, and not more than 0.5 seconds
     * </p>
     *
     */
    public void sendBreak() throws IllegalStateException, SerialPortException;

    /**
     * <p>
     *     Send a constant BREAK signal to connected device. (Turn break on/off)
     *     When enabled this will send a steady stream of zero bits.
     *     When enabled, no (other) data transmitting is possible.
     * </p>
     *
     * @param enabled
     *          The enable or disable state to control the BREAK signal
     */
    public void setBreak(boolean enabled) throws IllegalStateException, IOException;

    /**
     * <p>
     *     Control the RTS (request-to-send) pin state.
     *     When enabled this will set the RTS pin to the HIGH state.
     * </p>
     *
     * @param enabled
     *          The enable or disable state to control the RTS pin state.
     */
    public void setRTS(boolean enabled) throws IllegalStateException, IOException;

    /**
     * <p>
     *     Control the DTR (data-terminal-ready) pin state.
     *     When enabled this will set the DTR pin to the HIGH state.
     * </p>
     *
     * @param enabled
     *          The enable or disable state to control the RTS pin state.
     */
    public void setDTR(boolean enabled) throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the RTS (request-to-send) pin state.
     * </p>
     */
    public boolean getRTS() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the DTR (data-terminal-ready) pin state.
     * </p>
     */
    public boolean getDTR() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the CTS (clean-to-send) pin state.
     * </p>
     */
    public boolean getCTS() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the DSR (data-set-ready) pin state.
     * </p>
     */
    public boolean getDSR() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the RI (ring-indicator) pin state.
     * </p>
     */
    public boolean getRI() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the CD (carrier-detect) pin state.
     * </p>
     */
    public boolean getCD() throws IllegalStateException, IOException;


    // ----------------------------------------
    // EVENT OPERATIONS
    // ----------------------------------------

    /**
     * <p>
     * Java consumer code can call this method to register itself as a listener for serial data
     * events.
     * </p>
     * 
     * @see com.pi4j.io.serial.SerialDataListener
     * @see com.pi4j.io.serial.SerialDataEvent
     * 
     * @param listener  A class instance that implements the SerialListener interface.
     */
    public void addListener(SerialDataListener... listener);

    /**
     * <p> Java consumer code can call this method to unregister itself as a listener for serial data
     * events. </p>
     * 
     * @see com.pi4j.io.serial.SerialDataListener
     * @see com.pi4j.io.serial.SerialDataEvent
     * 
     * @param listener A class instance that implements the SerialListener interface.
     */
    public void removeListener(SerialDataListener... listener);


    // ----------------------------------------
    // FILE OPERATIONS
    // ----------------------------------------

    /**
     * This method returns the serial device file descriptor
     * @return fileDescriptor file descriptor
     */
    public int getFileDescriptor();

    /**
     * This method returns the input data stream for the serial port's receive buffer
     * @return InputStream input stream
     */
    public InputStream getInputStream();

    /**
     * This method returns the output data stream for the serial port's transmit buffer
     * @return OutputStream output stream
     */
    public OutputStream getOutputStream();
}
