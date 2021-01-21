package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Serial.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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


import java.io.Closeable;
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
 * <blockquote> This library depends on the wiringPi native system library. (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see com.pi4j.io.serial.SerialFactory
 * @see com.pi4j.io.serial.SerialDataEvent
 * @see SerialDataEventListener
 *
 * @see <a href="https://pi4j.com/">https://pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public interface Serial extends SerialDataReader, SerialDataWriter, AutoCloseable {

    /**
     * The default hardware COM port provided via the Raspberry Pi GPIO header.
     *
     * @see #open(String, int)
     */
    public static final String DEFAULT_COM_PORT = RaspberryPiSerial.DEFAULT_COM_PORT;
    public static final String FIRST_USB_COM_PORT = "/dev/ttyUSB0";
    public static final String SECOND_USB_COM_PORT = "/dev/ttyUSB1";

    // REF: https://www.raspberrypi.org/documentation/configuration/uart.md
    // added new symbolic device links included in Raspberry Pi OS.
    public static final String PRIMARY_COM_PORT = "/dev/serial0";
    public static final String SECONDARY_COM_PORT = "/dev/serial1";

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
     * @throws IOException thrown on any error.
     */
    public void open(String device, int baud, int dataBits, int parity, int stopBits, int flowControl)
            throws IOException;


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
     * @throws IOException thrown on any error.
     */
    public void open(String device, int baud) throws IOException;

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
     * @throws IOException thrown on any error.
     */
    public void open(String device, Baud baud, DataBits dataBits, Parity parity, StopBits stopBits,
                     FlowControl flowControl) throws IOException;

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
     * @throws IOException thrown on any error.
     */
    public void open(SerialConfig serialConfig) throws IOException;

    /**
     * This method is called to close a currently open open serial port.
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public void close() throws IllegalStateException, IOException;

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
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public void flush() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Discards any data in the serial receive (input) buffer.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public void discardInput() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Discards any data in the serial transmit (output) buffer.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public void discardOutput() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Discards any data in  both the serial receive and transmit buffers.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public void discardAll() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Send a BREAK signal to connected device.
     * </p>
     *
     * @param duration
     *          The length of time (milliseconds) to send the BREAK signal
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public void sendBreak(int duration) throws IllegalStateException, IOException;

    /**
     * <p>
     *     Send a BREAK signal to connected device for at least 0.25 seconds, and not more than 0.5 seconds
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public void sendBreak() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Send a constant BREAK signal to connected device. (Turn break on/off)
     *     When enabled this will send a steady stream of zero bits.
     *     When enabled, no (other) data transmitting is possible.
     * </p>
     *
     * @param enabled
     *          The enable or disable state to control the BREAK signal
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
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
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
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
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public void setDTR(boolean enabled) throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the RTS (request-to-send) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getRTS() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the DTR (data-terminal-ready) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getDTR() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the CTS (clean-to-send) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getCTS() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the DSR (data-set-ready) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getDSR() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the RI (ring-indicator) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getRI() throws IllegalStateException, IOException;

    /**
     * <p>
     *     Get the CD (carrier-detect) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
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
     * @see SerialDataEventListener
     * @see com.pi4j.io.serial.SerialDataEvent
     *
     * @param listener  A class instance that implements the SerialListener interface.
     */
    public void addListener(SerialDataEventListener... listener);

    /**
     * <p> Java consumer code can call this method to unregister itself as a listener for serial data
     * events. </p>
     *
     * @see SerialDataEventListener
     * @see com.pi4j.io.serial.SerialDataEvent
     *
     * @param listener A class instance that implements the SerialListener interface.
     */
    public void removeListener(SerialDataEventListener... listener);


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

    /**
     * This method returns the buffering state for data received from the serial device/port.
     * @return 'true' if buffering is enabled; else 'false'
     */
    public boolean isBufferingDataReceived();

    /**
     * <p>
     *     This method controls the buffering state for data received from the serial device/port.
     * </p>
     * <p>
     *   If the buffering state is enabled, then all data bytes received from the serial port will
     *   get copied into a data receive buffer.  You can use the 'getInputStream()' or and of the 'read()'
     *   methods to access this data.  The data will also be available via the 'SerialDataEvent' event.
     *   It is important to know that if you are using data buffering, the data will continue to grow
     *   in memory until your program consume it from the data reader/stream.
     * </p>
     * <p>
     *   If the buffering state is disabled, then all data bytes received from the serial port will NOT
     *   get copied into the data receive buffer, but will be included in the 'SerialDataEvent' event's
     *   data payload.  If you program does not care about or use data received from the serial port,
     *   then you should disable the data buffering state to prevent memory waste/leak.
     * </p>
     *
     * @param enabled
     *   Sets the buffering behavior state.
     *
     */
    public void setBufferingDataReceived(boolean enabled);

}
