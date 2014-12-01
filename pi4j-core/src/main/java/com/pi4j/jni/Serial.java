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

import com.pi4j.util.NativeLibraryLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
     * This opens and initializes the serial device and sets the baud rate. It sets the port into
     * raw mode (character at a time and no translations), and sets the read timeout to 10 seconds.
     * The return value is the file descriptor or -1 for any error, in which case errno will be set
     * as appropriate.
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     *
     * @param device The device address of the serial port to access. You can use constant
     *            'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *            GPIO header.
     * @param baud The baud rate to use with the serial port.
     * @return The return value is the file descriptor or -1 for any error, in which case errno will
     *         be set as appropriate.
     */
    public synchronized static native int open(String device, int baud, int dataBits, int parity, int stopBits,
                                               int flowControl, boolean echo, boolean flushRx, boolean flushTx);

    public synchronized static int open(String device, int baud, int dataBits, int parity, int stopBits,
                                               int flowControl, boolean echo){
        return open(device, baud, dataBits, parity, stopBits, flowControl, echo, false, false);
    }

    public synchronized static int open(String device, int baud, int dataBits, int parity, int stopBits,
                                        int flowControl){
        return open(device, baud, dataBits, parity, stopBits, flowControl, false, false, false);
    }

    public synchronized static int open(String device, int baud, int dataBits, int parity, int stopBits){
        return open(device, baud, dataBits, parity, stopBits, FLOW_CONTROL_NONE, false, false, false);
    }

    public synchronized static int open(String device, int baud, int dataBits, int parity){
        return open(device, baud, dataBits, parity, STOP_BITS_1, FLOW_CONTROL_NONE, false, false, false);
    }

    public synchronized static int open(String device, int baud, int dataBits){
        return open(device, baud, dataBits, PARITY_NONE, STOP_BITS_1, FLOW_CONTROL_NONE, false, false, false);
    }

    public synchronized static int open(String device, int baud){
        return open(device, baud, DATA_BITS_8, PARITY_NONE, STOP_BITS_1, FLOW_CONTROL_NONE, false, false, false);
    }

    /**
     * <p>
     * Closes the device identified by the file descriptor given.
     * </p>
     *
     * @param fd <p>
     *            The file descriptor of the serial port.
     *            </p>
     */
    public synchronized static native void close(int fd);

    /**
     * <p>
     *     This discards all data received, or waiting to be send down the given device.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @param fd The file descriptor of the serial port.
     */
    public synchronized static native void flush(int fd);

    /**
     * <p>
     *     This discards all data waiting to be send down the given device. (in Transmit buffer)
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @param fd The file descriptor of the serial port.
     */
    public synchronized static native void flushTx(int fd);

    /**
     * <p>
     *     This discards all data received. (in Receive buffer)
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @param fd The file descriptor of the serial port.
     */
    public synchronized static native void flushRx(int fd);

    /**
     * <p>
     *     Send a BREAK signal to connected device.
     * </p>
     *
     * @param fd The file descriptor of the serial port.
     * @param duration The length of time to send the BREAK signal
     */
    public synchronized static native void sendBreak(int fd, int duration);

    /**
     * <p>
     *     Send a BREAK signal to connected device for at least 0.25 seconds, and not more than 0.5 seconds
     * </p>
     *
     * @param fd The file descriptor of the serial port.
     */
    public synchronized static void sendBreak(int fd){
        sendBreak(fd, 0);
    }

    /**
     * <p>Sends an array of bytes to the serial device identified by the given file descriptor.</p>
     *
     * @param fd The file descriptor of the serial port.
     * @param data The byte array to transmit to the serial port.
     * @param length The number of bytes from the byte array to transmit to the serial port.
     */
    public synchronized static native void write(int fd, byte[] data, int length);

    /**
     * <p>Sends one of more bytes to the serial device identified by the given file descriptor.</p>
     *
     * @param fd The file descriptor of the serial port.
     * @param data The byte array to transmit to the serial port.
     */
    public synchronized static void write(int fd, byte ... data){
        write(fd, data, data.length);
    }

    /**
     * <p>Sends an array of bytes to the serial device identified by the given file descriptor.</p>
     *
     * @param fd The file descriptor of the serial port.
     * @param data The byte array to transmit to the serial port.
     * @param start The starting index (inclusive) in the array to send from.
     * @param length The number of bytes from the byte array to transmit to the serial port.
     */
    public synchronized static void write(int fd, byte[] data, int start, int length) {

        // we make a copy of the data argument because we don't want to modify the original source data
        byte[] buffer = new byte[length];
        System.arraycopy(data, start, buffer, 0, length);
        write(fd, buffer, length);
    }

    /**
     * This method is called to submit a byte buffer of data to the serial port transmit buffer.

     * @param fd
     *            The file descriptor of the serial port.
     * @param data
     *            A ByteBuffer of data to be transmitted.
     */
    public synchronized static void write(int fd, ByteBuffer data) throws IOException{
        write(fd, data.array());
    }

    /**
     * This method is called to submit an input stream of data to the serial port transmit buffer.
     *
     * @param fd
     *            The file descriptor of the serial port.
     * @param input
     *          input stream to read from to get bytes to write to be transmitted
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
        write(fd, buffer.toByteArray());
    }

    /**
     * This method is called to submit a string of data to the serial port transmit buffer.
     *
     * @param fd
     *            The file descriptor of the serial port.
     * @param data
     *            A string of data to be transmitted.
     * @param charset
     *            character encoding for bytes in string
     */
    public synchronized static void write(int fd, Charset charset, String data) throws IllegalStateException{
        byte[] buffer = data.getBytes(charset);
        write(fd, buffer);
    }

    /**
     * This method is called to submit a string of data to the serial port transmit buffer.
     *
     * @param fd
     *            The file descriptor of the serial port.
     * @param data
     *            A string of data to be transmitted.
     * @param charset
     *            character encoding for bytes in string
     */
    public synchronized static void write(int fd, String charset, String data) throws IllegalStateException{
        write(fd, Charset.forName(charset), data);
    }

    /**
     * This method is called to submit an ASCII string of data to the serial port transmit buffer.
     *
     * @param fd
     *            The file descriptor of the serial port.
     * @param data
     *            A string of data to be transmitted.
     */
    public synchronized static void write(int fd, String data) throws IllegalStateException{
        write(fd, StandardCharsets.US_ASCII, data);
    }

    /**
     * Returns the number of characters available for reading, or -1 for any error condition, in
     * which case errno will be set appropriately.
     *
     * @param fd The file descriptor of the serial port.
     * @return Returns the number of characters available for reading, or -1 for any error
     *         condition, in which case errno will be set appropriately.
     */
    public synchronized static native int available(int fd);

    /**
     * <p>Returns the length of bytes available on the serial device.</p>
     *
     * @param fd The file descriptor of the serial port.
     * @param length The number of bytes to get from the serial port.  This number must not be higher than the number of available bytes.
     * @return Returns the length of byte available on the serial device.
     */
    public synchronized static native byte[] read(int fd, int length);

    /**
     * <p>Returns available bytes available on the serial device.</p>
     *
     * @param fd The file descriptor of the serial port.
     * @return Returns the length of byte available on the serial device.
     */
    public synchronized static native byte[] readAll(int fd);

    /**
     * <p>Enable or disable ECHO of input bytes back to sender.</p>
     *
     * @param fd The file descriptor of the serial port.
     */
    public synchronized static native byte[] echo(int fd, boolean enabled);

}
