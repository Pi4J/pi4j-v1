package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialDataEvent.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.EventObject;

/**
 * <p> This class provides the serial data event object.</p>
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
 * @see SerialDataEventListener
 * @see com.pi4j.io.serial.SerialDataReader
 * @see com.pi4j.io.serial.SerialFactory
 *
 * @see <a href="https://www.pi4j.com/">https://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SerialDataEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private final Serial serial;
    private byte[] cachedData = null;

    /**
     * Default event constructor.
     */
    public SerialDataEvent(Serial serial) {
        super(serial);
        this.serial = serial;
    }

    /**
     * Default event constructor.
     */
    public SerialDataEvent(Serial serial, byte[] data) {
        this(serial);
        this.cachedData = data;
    }

    /**
     * Get the serial interface instance
     *
     * @return serial interface
     */
    public Serial getSerial(){
        return this.serial;
    }

    /**
     * Get an instance of the serial data reader
     *
     * @return serial data reader interface
     */
    public SerialDataReader getReader(){
        return this.serial;
    }

    /**
     * Get the number of bytes available in the serial data
     *
     * @return number of bytes available
     * @throws IOException
     */
    public int length() throws IOException {
        if(cachedData != null){
            return cachedData.length;
        }
        return getReader().available();
    }

    /**
     * Get all the bytes (byte-array) available in the serial data receive buffer
     *
     * @return byte array containing all bytes available in serial data receive buffer
     * @throws IOException
     */
    public byte[] getBytes() throws IOException {
        if(cachedData == null){
            if(getReader().available() > 0) {
                cachedData = getReader().read();
            }
            else{
                cachedData = new byte[0];
            }
        }
        return cachedData;
    }

    /**
     * Get all the bytes (byte-buffer) available in the serial data receive buffer
     *
     * @return ByteBuffer containing all bytes available in serial data receive buffer
     * @throws IOException
     */
    public ByteBuffer getByteBuffer() throws IOException {
        return ByteBuffer.wrap(getBytes());
    }

    /**
     * Get a string representation of the bytes available in the serial data receive buffer
     *
     * @param charset the character-set used to construct the string from the underlying byte array
     * @return string of data from serial data receive buffer
     * @throws IOException
     */
    public String getString(Charset charset) throws IOException {
        return getCharBuffer(charset).toString();
    }

    /**
     * Get an ASCII string representation of the bytes available in the serial data receive buffer
     *
     * @return ASCII string of data from serial data receive buffer
     * @throws IOException
     */
    public String getAsciiString() throws IOException {
        return getCharBuffer(StandardCharsets.US_ASCII).toString();
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Get a HEX string representation of the bytes available in the serial data receive buffer
     *
     * @return HEX string of comma separated data bytes from serial data receive buffer
     * @throws IOException
     */
    public String getHexByteString() throws IOException {
        return getHexByteString(null, ",", null);
    }

    /**
     * Get a HEX string representation of the bytes available in the serial data receive buffer
     *
     * @param prefix optional prefix string to append before each data byte
     * @param separator  optional separator string to append in between each data byte sequence
     * @param suffix optional suffix string to append after each data byte
     * @return HEX string of data bytes from serial data receive buffer
     * @throws IOException
     */
    public String getHexByteString(CharSequence prefix, CharSequence separator, CharSequence suffix) throws IOException {
        byte data[] = getBytes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++)
        {
            if(i > 0) sb.append(separator);
            int v = data[i] & 0xff;
            if(prefix != null) sb.append(prefix);
            sb.append(hexArray[v >> 4]);
            sb.append(hexArray[v & 0xf]);
            if(suffix != null) sb.append(suffix);
        }
        return sb.toString();
    }

    /**
     * Get a character buffer of the bytes available in the serial data receive buffer
     *
     * @param charset the character-set used to construct the character buffer from the underlying byte array
     * @return CharBuffer of data from serial data receive buffer
     * @throws IOException
     */
    public CharBuffer getCharBuffer(Charset charset) throws IOException {
        return charset.decode(getByteBuffer());
    }

    /**
     * <p>discard/drain all available bytes from the serial data receive buffer</p>
     */
    public void discardData() throws IllegalStateException, IOException{
        getReader().discardData();
    }

}
