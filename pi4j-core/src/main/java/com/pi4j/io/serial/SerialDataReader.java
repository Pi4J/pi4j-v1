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
