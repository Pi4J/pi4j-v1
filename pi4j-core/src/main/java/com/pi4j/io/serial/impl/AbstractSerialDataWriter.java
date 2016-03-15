package com.pi4j.io.serial.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  AbstractSerialDataWriter.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import com.pi4j.io.serial.SerialDataWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public abstract class AbstractSerialDataWriter implements SerialDataWriter {

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
    public abstract void write(byte[] data, int offset, int length) throws IllegalStateException, IOException;

    /**
     * <p>Sends one of more bytes to the serial device identified by the given file descriptor.</p>
     *
     * @param data
     *            One or more bytes (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(byte ... data) throws IllegalStateException, IOException{
        write(data, 0, data.length);
    }

    /**
     * <p>Sends one of more bytes arrays to the serial device identified by the given file descriptor.</p>
     *
     * @param data
     *            One or more byte arrays of data to be transmitted. (variable-length-argument)
     */
    public void write(byte[] ... data) throws IllegalStateException, IOException{
        for(byte[] single : data) {
            write(single);
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
    public void write(ByteBuffer... data) throws IllegalStateException, IOException{
        // write each byte buffer to the serial port
        for(ByteBuffer single : data) {

            // read the byte buffer from the current position up to the limit
            byte[] payload = new byte[single.remaining()];
            single.get(payload);

            write(payload);
        }
    }

    /**
     * Read content from an input stream of data and write it to the serial port transmit buffer.
     *
     * @param input
     *          An InputStream of data to be transmitted
     */
    public void write(InputStream input) throws IllegalStateException, IOException{
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

        write(buffer.toByteArray());
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
    public void write(Charset charset, char[] data, int offset, int length) throws IllegalStateException, IOException{
        write(charset, CharBuffer.wrap(data, offset, length));
    }

    /**
     * <p>Sends an array of characters to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more characters (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, char ... data) throws IllegalStateException, IOException{
        write(charset, CharBuffer.wrap(data));
    }

    /**
     * <p>Sends an array of ASCII characters to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           One or more ASCII characters (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(char ... data) throws IllegalStateException, IOException{
        write(StandardCharsets.US_ASCII, CharBuffer.wrap(data));
    }

    /**
     * <p>Sends one or more CharBuffers to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more CharBuffers (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, CharBuffer... data) throws IllegalStateException, IOException{
        for(CharBuffer single : data) {
            write(charset.encode(single));
        }
    }

    /**
     * <p>Sends one or more ASCII CharBuffers to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           One or more ASCII CharBuffers (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(CharBuffer ... data) throws IllegalStateException, IOException{
        write(StandardCharsets.US_ASCII, data);
    }

    /**
     * <p>Sends one or more string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, CharSequence ... data) throws IllegalStateException, IOException{
        for(CharSequence single : data) {
            write(charset.encode(CharBuffer.wrap(single)));
        }
    }

    /**
     * <p>Sends one or more ASCII string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           One or more ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(CharSequence ... data) throws IllegalStateException, IOException{
        write(StandardCharsets.US_ASCII, data);
    }

    /**
     * <p>Sends a collection of string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Charset charset, Collection<? extends CharSequence> data) throws IllegalStateException, IOException{
        for(CharSequence single : data) {
            write(charset.encode(CharBuffer.wrap(single)));
        }
    }

    /**
     * <p>Sends a collection of ASCII string objects to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void write(Collection<? extends CharSequence> data) throws IllegalStateException, IOException{
        write(StandardCharsets.US_ASCII, data);
    }

    protected CharSequence appendNewLine(CharSequence data){
        String separator = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder(data);
        builder.append(separator);
        return builder.toString();
    }

    /**
     * <p>Sends one or more string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           One or more string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(Charset charset, CharSequence ... data) throws IllegalStateException, IOException{
        for(CharSequence single : data) {
            write(charset.encode(CharBuffer.wrap(appendNewLine(single))));
        }
    }

    /**
     * <p>Sends one or more ASCII string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param data
     *           One or more ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(CharSequence ... data) throws IllegalStateException, IOException{
        writeln(StandardCharsets.US_ASCII, data);
    }

    /**
     * <p>Sends a collection of string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param charset
     *           The character set to use for encoding/decoding bytes to/from text characters
     * @param data
     *           A collection of string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(Charset charset, Collection<? extends CharSequence> data) throws IllegalStateException, IOException{
        for(CharSequence single : data) {
            write(charset.encode(CharBuffer.wrap(appendNewLine(single))));
        }
    }

    /**
     * <p>Sends a collection of ASCII string objects each appended with a line terminator (CR+LF) to the serial port/device.</p>
     *
     * @param data
     *           A collection of ASCII string objects (or an array) of data to be transmitted. (variable-length-argument)
     */
    public void writeln(Collection<? extends CharSequence> data) throws IllegalStateException, IOException{
        writeln(StandardCharsets.US_ASCII, data);
    }

}
