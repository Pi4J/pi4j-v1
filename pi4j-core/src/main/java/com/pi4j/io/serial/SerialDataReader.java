package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialDataReader.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Collection;


public interface SerialDataReader {

    /**
     * Gets the number of bytes available for reading, or -1 for any error condition.
     *
     * @return Returns the number of bytes available for reading, or -1 for any error
     */
    public int available() throws IllegalStateException, IOException;


    // ----------------------------------------
    // READ OPERATIONS
    // ----------------------------------------

    /**
     * <p>discard/drain all available bytes from the serial port/device.</p>
     */
    public void discardData() throws IllegalStateException, IOException;

    /**
     * <p>Reads all available bytes from the serial port/device.</p>
     *
     * @return Returns a byte array with the data read from the serial port.
     */
    public byte[] read() throws IllegalStateException, IOException;

    /**
     * <p>Reads a length of bytes from the port/serial device.</p>
     *
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     *
     * @return Returns a byte array with the data read from the serial port.
     */
    public byte[] read(int length) throws IllegalStateException, IOException;

    /**
     * <p>Reads all available bytes from the serial device into a provided ByteBuffer.</p>
     *
     * @param buffer
     *          The ByteBuffer object to write to.
     */
    public void read(ByteBuffer buffer) throws IllegalStateException, IOException;

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
    public void read(int length, ByteBuffer buffer) throws IllegalStateException, IOException;

    /**
     * <p>Reads all available bytes from the serial device into a provided OutputStream.</p>
     *
     * @param stream
     *          The OutputStream object to write to.
     */
    public void read(OutputStream stream) throws IllegalStateException, IOException;
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
    public void read(int length, OutputStream stream) throws IllegalStateException, IOException;

    /**
     * <p>Reads all available bytes from the serial port/device into a provided collection of ByteBuffer objects.</p>
     *
     * @param collection
     *          The collection of CharSequence objects to append to.
     *
     */
    public void read(Collection<ByteBuffer> collection) throws IllegalStateException, IOException;

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
    public void read(int length, Collection<ByteBuffer> collection) throws IllegalStateException, IOException;

    /**
     * <p>Reads all available bytes from the port/serial device and returns a CharBuffer from the decoded bytes.</p>
     *
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     *
     * @return Returns a character set with the data read from the serial port.
     */
    public CharBuffer read(Charset charset) throws IllegalStateException, IOException;

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
    public CharBuffer read(int length, Charset charset) throws IllegalStateException, IOException;

    /**
     * <p>Reads all available bytes from the serial port/device into a provided Writer.</p>
     *
     * @param charset
     *          The character set to use for encoding/decoding bytes to/from text characters
     * @param writer
     *          The Writer object to write to.
     *
     */
    public void read(Charset charset, Writer writer) throws IllegalStateException, IOException;

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
    public void read(int length, Charset charset, Writer writer) throws IllegalStateException, IOException;
}
