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


import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Collection;

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
public interface Serial {

    /**
     * The default hardware COM port provided via the Raspberry Pi GPIO header.
     * 
     * @see #open(String, int)
     */
    public static final String DEFAULT_COM_PORT = "/dev/ttyAMA0";

    public static final String FIRST_USB_COM_PORT = "/dev/ttyUSB0";
    public static final String SECOND_USB_COM_PORT = "/dev/ttyUSB1";

    public static final int DEFAULT_MONITOR_INTERVAL = 100; // milliseconds


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
     * @param echo
     *          Enable/disable echoing data received back to the sender.  ('false' (Disabled) by default)
     * @param flushRx
     *          Optionally flush the receive buffer when opening the serial port. ('false' by default)
     * @param flushTx
     *          Optionally flush the transmit buffer when opening the serial port. ('false' by default)
     *
     * @throws SerialPortException Exception thrown on any error.
     */
    public void open(String device, int baud, int dataBits, int parity, int stopBits,
                     int flowControl, boolean echo, boolean flushRx, boolean flushTx) throws SerialPortException;


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
    public void open(String device, int baud, int dataBits, int parity, int stopBits, int flowControl)
            throws SerialPortException;

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
     * @param echo
     *          Enable/disable echoing data received back to the sender.  ('false' (Disabled) by default)
     * @param flushRx
     *          Optionally flush the receive buffer when opening the serial port. ('false' by default)
     * @param flushTx
     *          Optionally flush the transmit buffer when opening the serial port. ('false' by default)
     *
     * @throws SerialPortException Exception thrown on any error.
     */
    public void open(String device, Baud baud, DataBits dataBits, Parity parity, StopBits stopBits,
                     FlowControl flowControl, boolean echo, boolean flushRx, boolean flushTx) throws SerialPortException;


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
     *     Discards all data in both the serial receive and transmit buffers.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     */
    public void flush() throws IllegalStateException, SerialPortException;

    /**
     * <p>
     *     Discards all data in either or both the serial receive and transmit buffers.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @param rxBuffer
     *          Flush the serial port receive buffer (input)
     * @param txBuffer
     *          Flush the serial port transmit buffer (output)
     */
    public void flush(boolean rxBuffer, boolean txBuffer) throws IllegalStateException, SerialPortException;

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
     * <p>Enable or disable ECHO of input bytes back to sender.</p>
     */
    public void echo(boolean enabled) throws SerialPortException;

    /**
     * Gets the number of bytes available for reading, or -1 for any error condition.
     *
     * @return Returns the number of bytes available for reading, or -1 for any error
     */
    public int available() throws IllegalStateException;

    /**
     * This method is called to determine if and how many bytes are available on the serial received
     * data buffer.
     * @deprecated Use 'available()' instead.
     *
     * @return The number of available bytes pending in the serial received buffer is returned.
     */
    @Deprecated
    public int availableBytes() throws IllegalStateException;



    // ----------------------------------------
    // READ OPERATIONS
    // ----------------------------------------

    /**
     * <p>Reads all available bytes from the serial port/device.</p>
     *
     * @return Returns a byte array with the data read from the serial port.
     */
    public byte[] read() throws IllegalStateException, SerialPortException;

    /**
     * <p>Reads a length of bytes from the port/serial device.</p>
     *
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     *
     * @return Returns a byte array with the data read from the serial port.
     */
    public byte[] read(int length) throws IllegalStateException, SerialPortException;

    /**
     * <p>Reads all available bytes from the serial device into a provided ByteBuffer.</p>
     *
     * @param buffer
     *          The ByteBuffer object to write to.
     */
    public void read(ByteBuffer buffer) throws IllegalStateException, SerialPortException;

    /**
     * <p>Reads a length bytes from the serial port/device into a provided ByteBuffer.</p>
     *
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     * @param buffer
     *          The ByteBuffer object to write to.
     *
     */
    public void read(int length, ByteBuffer buffer) throws IllegalStateException, SerialPortException;

    /**
     * <p>Reads all available bytes from the serial device into a provided OutputStream.</p>
     *
     * @param stream
     *          The OutputStream object to write to.
     */
    public void read(OutputStream stream) throws IllegalStateException, SerialPortException;
    /**
     * <p>Reads a length bytes from the serial port/device into a provided OutputStream.</p>
     *
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     * @param stream
     *          The OutputStream object to write to.
     *
     */
    public void read(int length, OutputStream stream) throws IllegalStateException, SerialPortException;

    /**
     * <p>Reads all available bytes from the serial port/device into a provided collection of ByteBuffer objects.</p>
     *
     * @param collection
     *          The collection of CharSequence objects to append to.
     *
     */
    public void read(Collection<ByteBuffer> collection) throws IllegalStateException, SerialPortException;

    /**
     * <p>Reads a length of bytes from the serial port/device into a provided collection of ByteBuffer objects.</p>
     *
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     * @param collection
     *          The collection of CharSequence objects to append to.
     *
     */
    public void read(int length, Collection<ByteBuffer> collection) throws IllegalStateException, SerialPortException;

    /**
     * <p>Reads all available bytes from the port/serial device and returns a CharBuffer from the decoded bytes.</p>
     *
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     *
     * @return Returns a character set with the data read from the serial port.
     */
    public CharBuffer read(Charset charset) throws IllegalStateException, SerialPortException;

    /**
     * <p>Reads a length of bytes from the port/serial device and returns a CharBuffer from the decoded bytes.</p>
     *
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     *
     * @return Returns a character set with the data read from the serial port.
     */
    public CharBuffer read(int length, Charset charset) throws IllegalStateException, SerialPortException;

    /**
     * <p>Reads all available bytes from the serial port/device into a provided Writer.</p>
     *
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     * @param writer
     *          The Writer object to write to.
     *
     */
    public void read(Charset charset, Writer writer) throws IllegalStateException, SerialPortException;

    /**
     * <p>Reads a length bytes from the serial port/device into a provided Writer.</p>
     *
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     * @param writer
     *          The Writer object to write to.
     *
     */
    public void read(int length, Charset charset, Writer writer) throws IllegalStateException, SerialPortException;


    // ----------------------------------------
    // WRITE OPERATIONS
    // ----------------------------------------

    /**
     * <p>Sends an array of bytes to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *            A ByteBuffer of data to be transmitted.
     * @param offset
     *            The starting index (inclusive) in the array to send from.
     * @param length
     *            The number of bytes from the byte array to transmit to the serial port.
     */
    public void write(byte[] data, int offset, int length) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends one of more bytes to the serial device identified by the given file descriptor.</p>
     *
     * @param data
     *            One or more bytes (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(byte ... data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends one of more bytes arrays to the serial device identified by the given file descriptor.</p>
     *
     * @param data
     *            One or more byte arrays of data to be transmitted. (variable-length-argument)
     */
    public void write(byte[] ... data) throws IllegalStateException, SerialPortException;

    /**
     * Read the content of byte buffer and write the data to the serial port transmit buffer.
     * (The buffer is read from the current position up to the 'limit' value, not the 'capacity'.  You may need to
     * rewind() or flip() the byte buffer if you have just written to it.)
     *
     * @param data
     *            A ByteBuffer of data to be transmitted.
     */
    public void write(ByteBuffer ... data) throws IllegalStateException, SerialPortException;

    /**
     * Read content from an input stream of data and write it to the serial port transmit buffer.
     *
     * @param input
     *          An InputStream of data to be transmitted
     */
    public void write(InputStream input) throws IllegalStateException, SerialPortException;
    /**
     * <p>Sends an array of characters to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           An array of chars to be decoded into bytes and transmitted.
     * @param offset
     *           The starting index (inclusive) in the array to send from.
     * @param length
     *           The number of characters from the char array to transmit to the serial port.
     */
    public void write(Charset charset, char[] data, int offset, int length) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends an array of characters to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more characters (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, char ... data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends an array of ASCII characters to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           One or more ASCII characters (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(char ... data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends one or more CharBuffers to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more CharBuffers (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, CharBuffer ... data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends one or more ASCII CharBuffers to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           One or more ASCII CharBuffers (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(CharBuffer ... data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends one or more string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, CharSequence ... data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends one or more ASCII string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           One or more ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(CharSequence ... data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends a collection of string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, Collection<? extends CharSequence> data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends a collection of ASCII string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Collection<? extends CharSequence> data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends one or more string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(Charset charset, CharSequence ... data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends one or more ASCII string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param data
     *           One or more ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(CharSequence ... data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends a collection of string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(Charset charset, Collection<? extends CharSequence> data) throws IllegalStateException, SerialPortException;

    /**
     * <p>Sends a collection of ASCII string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param data
     *           A collection of ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(Collection<? extends CharSequence> data) throws IllegalStateException, SerialPortException;



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
        
    /**
     * This method returns TRUE if the serial interface has been shutdown.
     * 
     * @return shutdown state
     */
    public boolean isShutdown();
    
    
    /**
     * This method can be called to forcefully shutdown all serial data monitoring threads.
     */
    public void shutdown();

    /**
     * This method returns the serial device file descriptor
     * @return fileDescriptor file descriptor
     */
    public int getFileDescriptor();
    
    /**
     * This method returns the serial data receive monitor delay interval in milliseconds.
     * @return interval milliseconds
     */
    public int getMonitorInterval();

    /**
     * This method set the serial data receive monitor delay interval in milliseconds.
     *
     * @param interval number of milliseconds
     */
    public void setMonitorInterval(int interval);
}
