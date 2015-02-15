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


import com.pi4j.io.serial.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
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
                     int flowControl, boolean echo, boolean flushRx, boolean flushTx) throws SerialPortException{
        try {
            fileDescriptor = com.pi4j.jni.Serial.open(device, baud, dataBits, parity, stopBits, flowControl, echo, flushRx, flushTx);
        } catch (IOException e) {
            throw new SerialPortException("Cannot open serial port: " + e.getMessage());
        }
        if (fileDescriptor == -1) {
            throw new SerialPortException("Cannot open serial port");
        }
    }

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
            throws SerialPortException{
        // open the serial port with NO ECHO and NO (forced) BUFFER FLUSH
        open(device, baud, dataBits, parity, stopBits, flowControl, false, false, false);
    }

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
    public void open(String device, int baud) throws SerialPortException{
        // open the serial port with config settings of "8N1" and no flow control
        open(device,
             baud,
             com.pi4j.jni.Serial.DATA_BITS_8,
             com.pi4j.jni.Serial.PARITY_NONE,
             com.pi4j.jni.Serial.STOP_BITS_1,
             com.pi4j.jni.Serial.FLOW_CONTROL_NONE);
    }

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
                FlowControl flowControl, boolean echo, boolean flushRx, boolean flushTx) throws SerialPortException{

        // open the serial port with NO ECHO and NO (forced) BUFFER FLUSH
        open(device, baud.getValue(), dataBits.getValue(), parity.getIndex(),
                stopBits.getValue(), flowControl.getIndex(), echo, flushRx, flushTx);
    }


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
                     FlowControl flowControl) throws SerialPortException{
        // open the serial port with NO ECHO and NO (forced) BUFFER FLUSH
        open(device, baud.getValue(), dataBits.getValue(), parity.getIndex(),
                stopBits.getValue(), flowControl.getIndex(), false, false, false);
    }

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
    public void open(SerialConfig serialConfig) throws SerialPortException{
        // open the serial port with config settings
        open(serialConfig.device(),
             serialConfig.baud().getValue(),
             serialConfig.dataBits().getValue(),
             serialConfig.parity().getIndex(),
             serialConfig.stopBits().getValue(),
             serialConfig.flowControl().getIndex());
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
        try {
            com.pi4j.jni.Serial.close(fileDescriptor);
        } catch (IOException e) {
            throw new SerialPortException("Cannot close serial port: " + e.getMessage());
        }
        fileDescriptor = -1;
	}


    /**
     * <p>
     *     Discards all data in both the serial receive and transmit buffers.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     */
    public void flush() throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'flush()'.");

        // flush data to serial port immediately
        try {
            com.pi4j.jni.Serial.flush(fileDescriptor);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

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
    public void flush(boolean rxBuffer, boolean txBuffer) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'flush()'.");

        // flush data to serial port immediately
        try {
            com.pi4j.jni.Serial.flush(fileDescriptor, rxBuffer, txBuffer);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>
     *     Send a BREAK signal to connected device.
     * </p>
     *
     * @param duration
     *          The length of time (milliseconds) to send the BREAK signal
     */
    public void sendBreak(int duration) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'sendBreak()'.");

        // send BREAK signal to serial port immediately
        try {
            com.pi4j.jni.Serial.sendBreak(fileDescriptor, duration);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>
     *     Send a BREAK signal to connected device for at least 0.25 seconds, and not more than 0.5 seconds
     * </p>
     *
     */
    public void sendBreak() throws IllegalStateException, SerialPortException{
        sendBreak(0);
    }

    /**
     * <p>Enable or disable ECHO of input bytes back to sender.</p>
     */
    public void echo(boolean enabled) throws SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'sendBreak()'.");

        // update ECHO state on serial port immediately
        try {
            com.pi4j.jni.Serial.echo(fileDescriptor, enabled);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * Gets the number of bytes available for reading, or -1 for any error condition.
     *
     * @return Returns the number of bytes available for reading, or -1 for any error
     */
    public int available() throws IllegalStateException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'available()'.");

        // get the number of available bytes in the serial port's receive buffer
        return com.pi4j.jni.Serial.available(fileDescriptor);
    }

    /**
     * This method is called to determine if and how many bytes are available on the serial received
     * data buffer.
     * @deprecated Use 'available()' instead.
     *
     * @return The number of available bytes pending in the serial received buffer is returned.
     */
    @Deprecated
    public int availableBytes() throws IllegalStateException{
        return available();
    }


    // ----------------------------------------
    // READ OPERATIONS
    // ----------------------------------------

    /**
     * <p>Reads all available bytes from the serial port/device.</p>
     *
     * @return Returns a byte array with the data read from the serial port.
     */
    public byte[] read() throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            return com.pi4j.jni.Serial.read(fileDescriptor);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Reads a length of bytes from the port/serial device.</p>
     *
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     *
     * @return Returns a byte array with the data read from the serial port.
     */
    public byte[] read(int length) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            return com.pi4j.jni.Serial.read(fileDescriptor, length);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Reads all available bytes from the serial device into a provided ByteBuffer.</p>
     *
     * @param buffer
     *          The ByteBuffer object to write to.
     */
    public void read(ByteBuffer buffer) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            com.pi4j.jni.Serial.read(fileDescriptor, buffer);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

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
    public void read(int length, ByteBuffer buffer) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            com.pi4j.jni.Serial.read(fileDescriptor, length, buffer);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Reads all available bytes from the serial device into a provided OutputStream.</p>
     *
     * @param stream
     *          The OutputStream object to write to.
     */
    public void read(OutputStream stream) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            com.pi4j.jni.Serial.read(fileDescriptor, stream);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

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
    public void read(int length, OutputStream stream) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            com.pi4j.jni.Serial.read(fileDescriptor, length, stream);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Reads all available bytes from the serial port/device into a provided collection of ByteBuffer objects.</p>
     *
     * @param collection
     *          The collection of CharSequence objects to append to.
     *
     */
    public void read(Collection<ByteBuffer> collection) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            com.pi4j.jni.Serial.read(fileDescriptor, collection);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

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
    public void read(int length, Collection<ByteBuffer> collection) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            com.pi4j.jni.Serial.read(fileDescriptor, length, collection);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Reads all available bytes from the port/serial device and returns a CharBuffer from the decoded bytes.</p>
     *
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     *
     * @return Returns a character set with the data read from the serial port.
     */
    public CharBuffer read(Charset charset) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            return com.pi4j.jni.Serial.read(fileDescriptor, charset);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

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
    public CharBuffer read(int length, Charset charset) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            return com.pi4j.jni.Serial.read(fileDescriptor, length, charset);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Reads all available bytes from the serial port/device into a provided Writer.</p>
     *
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     * @param writer
     *          The Writer object to write to.
     *
     */
    public void read(Charset charset, Writer writer) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            com.pi4j.jni.Serial.read(fileDescriptor, charset, writer);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

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
    public void read(int length, Charset charset, Writer writer)  throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        try {
            // read serial data from receive buffer
            com.pi4j.jni.Serial.read(fileDescriptor, charset, writer);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }



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
    public void write(byte[] data, int offset, int length) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, data, offset, length);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends one of more bytes to the serial device identified by the given file descriptor.</p>
     *
     * @param data
     *            One or more bytes (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(byte ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends one of more bytes arrays to the serial device identified by the given file descriptor.</p>
     *
     * @param data
     *            One or more byte arrays of data to be transmitted. (variable-length-argument)
     */
    public void write(byte[] ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * Read the content of byte buffer and write the data to the serial port transmit buffer.
     * (The buffer is read from the current position up to the 'limit' value, not the 'capacity'.  You may need to
     * rewind() or flip() the byte buffer if you have just written to it.)
     *
     * @param data
     *            A ByteBuffer of data to be transmitted.
     */
    public void write(ByteBuffer ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * Read content from an input stream of data and write it to the serial port transmit buffer.
     *
     * @param input
     *          An InputStream of data to be transmitted
     */
    public void write(InputStream input) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, input);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

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
    public void write(Charset charset, char[] data, int offset, int length) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, charset, data, offset, length);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends an array of characters to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more characters (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, char ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, charset, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends an array of ASCII characters to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           One or more ASCII characters (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(char ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends one or more CharBuffers to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more CharBuffers (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, CharBuffer ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, charset, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends one or more ASCII CharBuffers to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           One or more ASCII CharBuffers (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(CharBuffer ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends one or more string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, CharSequence ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, charset, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends one or more ASCII string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           One or more ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(CharSequence ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends a collection of string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, Collection<? extends CharSequence> data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, charset, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends a collection of ASCII string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Collection<? extends CharSequence> data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.write(fileDescriptor, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends one or more string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(Charset charset, CharSequence ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'writeln()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.writeln(fileDescriptor, charset, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends one or more ASCII string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param data
     *           One or more ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(CharSequence ... data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'writeln()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.writeln(fileDescriptor, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends a collection of string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(Charset charset, Collection<? extends CharSequence> data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'writeln()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.writeln(fileDescriptor, charset, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }

    /**
     * <p>Sends a collection of ASCII string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param data
     *           A collection of ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(Collection<? extends CharSequence> data) throws IllegalStateException, SerialPortException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'writeln()'.");

        try {
            // write serial data to transmit buffer
            com.pi4j.jni.Serial.writeln(fileDescriptor, data);
        } catch (IOException e) {
            throw new SerialPortException(e);
        }
    }



    // ----------------------------------------
    // EVENT OPERATIONS
    // ----------------------------------------

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
