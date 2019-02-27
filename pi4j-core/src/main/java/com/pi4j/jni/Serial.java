package com.pi4j.jni;

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
 * Copyright (C) 2012 - 2019 Pi4J
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

import com.pi4j.util.NativeLibraryLoader;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 *  THIS IS CURRENTLY A NO-IMPL STUB.
 *  THIS IS WHERE A NEW SERIAL LIBRARY IMPLEMENTATION IS PLANNED.
 */
public class Serial {

    /**
     * The default hardware COM port provided via the Raspberry Pi GPIO header.
     *
     * @see #open(String,int)
     */
    public static final String DEFAULT_COM_PORT = "/dev/ttyAMA0";
    public static final String FIRST_USB_COM_PORT = "/dev/ttyUSB0";
    public static final String SECOND_USB_COM_PORT = "/dev/ttyUSB1";

    public static int BAUD_RATE_50     = 50;
    public static int BAUD_RATE_75     = 75;
    public static int BAUD_RATE_110    = 110;
    public static int BAUD_RATE_134    = 134;
    public static int BAUD_RATE_150    = 150;
    public static int BAUD_RATE_200    = 200;
    public static int BAUD_RATE_300    = 300;
    public static int BAUD_RATE_600    = 600;
    public static int BAUD_RATE_1200   = 1200;
    public static int BAUD_RATE_1800   = 1800;
    public static int BAUD_RATE_2400   = 2400;
    public static int BAUD_RATE_4800   = 4800;
    public static int BAUD_RATE_9600   = 9600;
    public static int BAUD_RATE_19200  = 19200;
    public static int BAUD_RATE_38400  = 38400;
    public static int BAUD_RATE_57600  = 57600;
    public static int BAUD_RATE_115200 = 115200;
    public static int BAUD_RATE_230400 = 230400;

    public static int PARITY_NONE  = 0;
    public static int PARITY_ODD   = 1;
    public static int PARITY_EVEN  = 2;
    public static int PARITY_MARK  = 3;   // NOT ALL UNIX SYSTEM SUPPORT 'MARK' PARITY; THIS IS EXPERIMENTAL
    public static int PARITY_SPACE = 4;   // NOT ALL UNIX SYSTEM SUPPORT 'SPACE' PARITY; THIS IS EXPERIMENTAL

    public static int DATA_BITS_5 = 5;
    public static int DATA_BITS_6 = 6;
    public static int DATA_BITS_7 = 7;
    public static int DATA_BITS_8 = 8;

    public static int STOP_BITS_1 = 1;
    public static int STOP_BITS_2 = 2;

    public static int FLOW_CONTROL_NONE     = 0;
    public static int FLOW_CONTROL_HARDWARE = 1;
    public static int FLOW_CONTROL_SOFTWARE = 2;


    // private constructor
    private Serial() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
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
     *          The baud rate to use with the serial port. (Custom baud rate are not supported)
     * @param dataBits
     *          The data bits to use for serial communication. (5,6,7,8)
     * @param parity
     *          The parity setting to use for serial communication. (None, Event, Odd, Mark, Space)
     * @param stopBits
     *          The stop bits to use for serial communication. (1,2)
     * @param flowControl
     *          The stop bits to use for serial communication. (none, hardware, software)
     *
     * @return The return value is the file descriptor or a negative value for any error.
     *          An IOException will be thrown for all error conditions.
     */
    public synchronized static native int open(String device, int baud, int dataBits, int parity, int stopBits,
                                               int flowControl) throws IOException;

    /**
     * <p>
     * This opens and initializes the serial port/device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     *
     * The following default serial communications parameters are applied using this overloaded method instance:
     *      flow control = FLOW_CONTROL_NONE
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     * @see #FLOW_CONTROL_NONE
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
     *
     * @return The return value is the file descriptor or a negative value for any error.
     *          An IOException will be thrown for all error conditions.
     */
    public synchronized static int open(String device, int baud, int dataBits, int parity, int stopBits)
                                               throws IOException {
        return open(device, baud, dataBits, parity, stopBits, FLOW_CONTROL_NONE);
    }

    /**
     * <p>
     * This opens and initializes the serial port/device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     *
     * The following default serial communications parameters are applied using this overloaded method instance:
     *      stop bits    = STOP_BITS_1
     *      flow control = FLOW_CONTROL_NONE
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     * @see #STOP_BITS_1
     * @see #FLOW_CONTROL_NONE
     *
     * @param device
     *          The device address of the serial port to access. You can use constant
     *          'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *          GPIO header.
     * @param baud
     *          The baud rate to use with the serial port. (Custom baud rate are not supported)
     * @param dataBits
     *          The data bits to use for serial communication. (5,6,7,8)
     *
     * @return The return value is the file descriptor or a negative value for any error.
     *          An IOException will be thrown for all error conditions.
     */
    public synchronized static int open(String device, int baud, int dataBits, int parity) throws IOException {
        return open(device, baud, dataBits, parity, STOP_BITS_1, FLOW_CONTROL_NONE);
    }

    /**
     * <p>
     * This opens and initializes the serial device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     *
     * The following default serial communications parameters are applied using this overloaded method instance:
     *      parity       = PARITY_NONE
     *      stop bits    = STOP_BITS_1
     *      flow control = FLOW_CONTROL_NONE
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     * @see #PARITY_NONE
     * @see #STOP_BITS_1
     * @see #FLOW_CONTROL_NONE
     *
     * @param device
     *          The device address of the serial port to access. You can use constant
     *          'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *          GPIO header.
     * @param baud
     *          The baud rate to use with the serial port. (Custom baud rate are not supported)
     * @param dataBits
     *          The data bits to use for serial communication. (5,6,7,8)
     *
     * @return The return value is the file descriptor or a negative value for any error.
     *          An IOException will be thrown for all error conditions.
     */
    public synchronized static int open(String device, int baud, int dataBits) throws IOException {
        return open(device, baud, dataBits, PARITY_NONE, STOP_BITS_1, FLOW_CONTROL_NONE);
    }

    /**
     * <p>
     * This opens and initializes the serial device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     *
     * The following default serial communications parameters are applied using this overloaded method instance:
     *      data bits    = DATA_BITS_8
     *      parity       = PARITY_NONE
     *      stop bits    = STOP_BITS_1
     *      flow control = FLOW_CONTROL_NONE
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     * @see #DATA_BITS_8
     * @see #PARITY_NONE
     * @see #STOP_BITS_1
     * @see #FLOW_CONTROL_NONE
     *
     * @param device
     *          The device address of the serial port to access. You can use constant
     *          'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *          GPIO header.
     * @param baud
     *          The baud rate to use with the serial port. (Custom baud rate are not supported)
     *
     * @return The return value is the file descriptor or a negative value for any error.
     *          An IOException will be thrown for all error conditions.
     */
    public synchronized static int open(String device, int baud) throws IOException {
        return open(device, baud, DATA_BITS_8, PARITY_NONE, STOP_BITS_1, FLOW_CONTROL_NONE);
    }

    /**
     * <p>
     * Closes the serial port/device identified by the file descriptor.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native void close(int fd) throws IOException;

    /**
     * <p>
     *     Discards all data in the serial receive and transmit buffer.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native void discardInput(int fd) throws IOException;

    /**
     * <p>
     *     Discards all data in the serial transmit buffer.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native void discardOutput(int fd) throws IOException;

    /**
     * <p>
     *     Discards all data in the serial transmit and receive buffers.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native void discardAll(int fd) throws IOException;

    /**
     * <p>
     *     Forces (drains) all data in transmit buffers.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native void flush(int fd) throws IOException;

    /**
     * <p>
     *     Send a BREAK signal to connected device.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param duration
     *          The length of time (milliseconds) to send the BREAK signal
     */
    public synchronized static native void sendBreak(int fd, int duration) throws IOException;

    /**
     * <p>
     *     Send a BREAK signal to connected device for at least 0.25 seconds, and not more than 0.5 seconds
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static void sendBreak(int fd) throws IOException {
        sendBreak(fd, 0);
    }

    /**
     * <p>
     *     Send a constant BREAK signal to connected device. (Turn break on/off)
     *     When enabled this will send a steady stream of zero bits.
     *     When enabled, no (other) data transmitting is possible.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param enabled
     *          The enable or disable state to control the BREAK signal
     */
    public synchronized static native void setBreak(int fd, boolean enabled) throws IOException;

    /**
     * <p>
     *     Control the RTS (request-to-send) pin state.
     *     When enabled this will set the RTS pin to the HIGH state.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param enabled
     *          The enable or disable state to control the RTS pin state.
     */
    public synchronized static native void setRTS(int fd, boolean enabled) throws IOException;

    /**
     * <p>
     *     Control the DTR (data-terminal-ready) pin state.
     *     When enabled this will set the DTR pin to the HIGH state.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param enabled
     *          The enable or disable state to control the RTS pin state.
     */
    public synchronized static native void setDTR(int fd, boolean enabled) throws IOException;

    /**
     * <p>
     *     Get the RTS (request-to-send) pin state.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native boolean getRTS(int fd) throws IOException;

    /**
     * <p>
     *     Get the DTR (data-terminal-ready) pin state.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native boolean getDTR(int fd) throws IOException;

    /**
     * <p>
     *     Get the CST (clear-to-send) pin state.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native boolean getCTS(int fd) throws IOException;

    /**
     * <p>
     *     Get the DSR (data-set-ready) pin state.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native boolean getDSR(int fd) throws IOException;

    /**
     * <p>
     *     Get the RI (ring-indicator) pin state.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native boolean getRI(int fd) throws IOException;

    /**
     * <p>
     *     Get the CD (carrier-detect) pin state.
     * </p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     */
    public synchronized static native boolean getCD(int fd) throws IOException;

    // ----------------------------------------
    // READ OPERATIONS
    // ----------------------------------------

    /**
     * Returns the number of characters available for reading, or -1 for any error condition, in
     * which case errno will be set appropriately.
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     *
     * @return Returns the number of characters available for reading, or -1 for any error
     */
    public synchronized static native int available(int fd);


    /**
     * <p>Reads all available bytes from the serial port/device.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     *
     * @return Returns a byte array with the data read from the serial port.
     */
    public synchronized static native byte[] read(int fd) throws IOException;

    /**
     * <p>Reads a length of bytes from the port/serial device.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     *
     * @return Returns a byte array with the data read from the serial port.
     */
    public synchronized static native byte[] read(int fd, int length) throws IOException;

    /**
     * <p>Reads all available bytes from the serial device into a provided ByteBuffer.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param buffer
     *          The ByteBuffer object to write to.
     */
    public synchronized static void read(int fd, ByteBuffer buffer) throws IOException{
        byte[] data = read(fd);
        buffer.put(data);
    }

    /**
     * <p>Reads a length bytes from the serial port/device into a provided ByteBuffer.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     * @param buffer
     *          The ByteBuffer object to write to.
     *
     */
    public synchronized static void read(int fd, int length, ByteBuffer buffer) throws IOException{
        buffer.put(read(fd, length));
    }

    /**
     * <p>Reads all available bytes from the serial device into a provided OutputStream.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param stream
     *          The OutputStream object to write to.
     */
    public synchronized static void read(int fd, OutputStream stream) throws IOException{
        stream.write(read(fd));
    }

    /**
     * <p>Reads a length bytes from the serial port/device into a provided OutputStream.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     * @param stream
     *          The OutputStream object to write to.
     *
     */
    public synchronized static void read(int fd, int length, OutputStream stream) throws IOException{
        stream.write(read(fd, length));
    }

    /**
     * <p>Reads all available bytes from the serial port/device into a provided collection of ByteBuffer objects.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param collection
     *          The collection of CharSequence objects to append to.
     *
     */
    public synchronized static void read(int fd, Collection<ByteBuffer> collection) throws IOException{
        collection.add(ByteBuffer.wrap(read(fd)));
    }

    /**
     * <p>Reads a length of bytes from the serial port/device into a provided collection of ByteBuffer objects.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     * @param collection
     *          The collection of CharSequence objects to append to.
     *
     */
    public synchronized static void read(int fd, int length, Collection<ByteBuffer> collection) throws IOException{
        collection.add(ByteBuffer.wrap(read(fd)));
    }

    /**
     * <p>Reads all available bytes from the port/serial device.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     *
     * @return Returns a character set with the data read from the serial port.
     */
    public synchronized static CharBuffer read(int fd, Charset charset) throws IOException{
        return charset.decode(ByteBuffer.wrap(read(fd)));
    }

    /**
     * <p>Reads a length of bytes from the port/serial device.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     *
     * @return Returns a character set with the data read from the serial port.
     */
    public synchronized static CharBuffer read(int fd, int length, Charset charset) throws IOException{
        return charset.decode(ByteBuffer.wrap(read(fd, length)));
    }

    /**
     * <p>Reads all available bytes from the serial port/device into a provided Writer.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     * @param writer
     *          The Writer object to write to.
     *
     */
    public synchronized static void read(int fd, Charset charset, Writer writer) throws IOException{
        writer.write(read(fd, charset).toString());
    }

    /**
     * <p>Reads a length bytes from the serial port/device into a provided Writer.</p>
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     * @param writer
     *          The Writer object to write to.
     *
     */
    public synchronized static void read(int fd, int length, Charset charset, Writer writer) throws IOException{
        writer.write(read(fd, length, charset).toString());
    }


    // ----------------------------------------
    // WRITE OPERATIONS
    // ----------------------------------------

    /**
     * <p>Sends an array of bytes to the serial port/device identified by the given file descriptor.</p>
     *
     *
     * @param fd
     *            The file descriptor of the serial port/device.
     * @param data
     *            A ByteBuffer of data to be transmitted.
     * @param length
     *            The number of bytes from the byte array to transmit to the serial port.
     */
    private synchronized static native void write(int fd, byte[] data, long length) throws IOException;

    /**
     * <p>Sends an array of bytes to the serial port/device identified by the given file descriptor.</p>
     *
     * @param fd
     *            The file descriptor of the serial port/device.
     * @param data
     *            A ByteBuffer of data to be transmitted.
     * @param offset
     *            The starting index (inclusive) in the array to send from.
     * @param length
     *            The number of bytes from the byte array to transmit to the serial port.
     */
    public synchronized static void write(int fd, byte[] data, int offset, int length) throws IOException {

        // we make a copy of the data argument because we don't want to modify the original source data
        byte[] buffer = new byte[length];
        System.arraycopy(data, offset, buffer, 0, length);

        // write the buffer contents to the serial port via JNI native method
        write(fd, buffer, length);
    }

    /**
     * <p>Sends one of more bytes to the serial device identified by the given file descriptor.</p>
     *
     * @param fd
     *            The file descriptor of the serial port/device.
     * @param data
     *            One or more bytes (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void write(int fd, byte ... data) throws IOException {

        // write the data contents to the serial port via JNI native method
        write(fd, data, data.length);
    }

    /**
     * <p>Sends one of more bytes arrays to the serial device identified by the given file descriptor.</p>
     *
     * @param fd
     *            The file descriptor of the serial port/device.
     * @param data
     *            One or more byte arrays of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void write(int fd, byte[] ... data) throws IOException {
        for(byte[] single : data) {
            // write the data contents to the serial port via JNI native method
            write(fd, single, single.length);
        }
    }

    /**
     * Read the content of byte buffer and write the data to the serial port transmit buffer.
     * (The buffer is read from the current position up to the 'limit' value, not the 'capacity'.  You may need to
     * rewind() or flip() the byte buffer if you have just written to it.)
     *
     * @param fd
     *            The file descriptor of the serial port/device.
     * @param data
     *            A ByteBuffer of data to be transmitted.
     */
    public synchronized static void write(int fd, ByteBuffer ... data) throws IOException{

        // write each byte buffer to the serial port
        for(ByteBuffer single : data) {

            // read the byte buffer from the current position up to the limit
            byte[] payload = new byte[single.remaining()];
            single.get(payload);

            // write the data contents to the serial port via JNI native method
            write(fd, payload, payload.length);
        }
    }

    /**
     * Read content from an input stream of data and write it to the serial port transmit buffer.
     *
     * @param fd
     *          The file descriptor of the serial port/device.
     * @param input
     *          An InputStream of data to be transmitted
     */
    public synchronized static void write(int fd, InputStream input) throws IOException {

        // ensure bytes are available
        if(input.available() <= 0){
            throw new IOException("No available bytes in input stream to write to serial port.");
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int length;
        byte[] data = new byte[1024];
        while ((length = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, length);
        }
        buffer.flush();

        // write bytes to serial port
        write(fd, buffer.toByteArray(), buffer.size());
    }

    /**
     * <p>Sends an array of characters to the serial port/device identified by the given file descriptor.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           An array of chars to be decoded into bytes and transmitted.
     * @param offset
     *           The starting index (inclusive) in the array to send from.
     * @param length
     *           The number of characters from the char array to transmit to the serial port.
     */
    public synchronized static void write(int fd, Charset charset, char[] data, int offset, int length) throws IOException {

        // write the buffer contents to the serial port via JNI native method
        write(fd, charset, CharBuffer.wrap(data, offset, length));
    }

    /**
     * <p>Sends an array of characters to the serial port/device identified by the given file descriptor.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more characters (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void write(int fd, Charset charset, char ... data) throws IOException {

        // write the buffer contents to the serial port via JNI native method
        write(fd, charset, CharBuffer.wrap(data));
    }

    /**
     * <p>Sends an array of ASCII characters to the serial port/device identified by the given file descriptor.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param data
     *           One or more ASCII characters (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void write(int fd, char ... data) throws IOException {

        // write the buffer contents to the serial port via JNI native method
        write(fd, StandardCharsets.US_ASCII, CharBuffer.wrap(data));
    }

    /**
     * <p>Sends one or more CharBuffers to the serial port/device identified by the given file descriptor.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more CharBuffers (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void write(int fd, Charset charset, CharBuffer ... data) throws IllegalStateException, IOException {
        for(CharBuffer single : data) {
            write(fd, charset.encode(single));
        }
    }

    /**
     * <p>Sends one or more ASCII CharBuffers to the serial port/device identified by the given file descriptor.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param data
     *           One or more ASCII CharBuffers (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void write(int fd, CharBuffer ... data) throws IllegalStateException, IOException {
        write(fd, StandardCharsets.US_ASCII, data);
    }

    /**
     * <p>Sends one or more string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void write(int fd, Charset charset, CharSequence ... data) throws IllegalStateException, IOException {
        for(CharSequence single : data) {
            write(fd, charset.encode(CharBuffer.wrap(single)));
        }
    }

    /**
     * <p>Sends one or more ASCII string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param data
     *           One or more ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void write(int fd, CharSequence ... data) throws IllegalStateException, IOException {
        write(fd, StandardCharsets.US_ASCII, data);
    }


    /**
     * <p>Sends a collection of string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void write(int fd, Charset charset, Collection<? extends CharSequence> data) throws IllegalStateException, IOException {
        for(CharSequence single : data) {
            write(fd, charset.encode(CharBuffer.wrap(single)));
        }
    }

    /**
     * <p>Sends a collection of ASCII string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void write(int fd, Collection<? extends CharSequence> data) throws IllegalStateException, IOException {
        write(fd, StandardCharsets.US_ASCII, data);
    }


    /**
     * <p>Sends one or more string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void writeln(int fd, Charset charset, CharSequence ... data) throws IllegalStateException, IOException {
        for(CharSequence single : data) {
            write(fd, charset.encode(CharBuffer.wrap(single + "\r\n")));
        }
    }

    /**
     * <p>Sends one or more ASCII string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param data
     *           One or more ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void writeln(int fd, CharSequence ... data) throws IllegalStateException, IOException {
        writeln(fd, StandardCharsets.US_ASCII, data);
    }

    /**
     * <p>Sends a collection of string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void writeln(int fd, Charset charset, Collection<? extends CharSequence> data) throws IllegalStateException, IOException {
        for(CharSequence single : data) {
            write(fd, charset.encode(CharBuffer.wrap(single + "\r\n")));
        }
    }

    /**
     * <p>Sends a collection of ASCII string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param fd
     *           The file descriptor of the serial port/device.
     * @param data
     *           A collection of ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public synchronized static void writeln(int fd, Collection<? extends CharSequence> data) throws IllegalStateException, IOException {
        writeln(fd, StandardCharsets.US_ASCII, data);
    }

}
