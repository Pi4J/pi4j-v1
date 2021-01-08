package com.pi4j.io.serial.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialByteBuffer.java
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
import java.io.InputStream;
import java.nio.BufferOverflowException;

/**
 * This class implements a dynamic expanding byte buffer to
 * accommodate new data received from the serial port
 *
 * Adapted from sources at:
 * http://ostermiller.org/utils/src/CircularByteBuffer.java.html
 * Stephen Ostermiller http://ostermiller.org/contact.pl?regarding=Java+Utilities
 *
 */
public class SerialByteBuffer {

    public static int DEFAULT_BUFFER_SCALE_FACTOR = 2;
    public static int DEFAULT_INITIAL_BUFFER_SIZE = 4096;

    private InputStream stream = new SerialByteBufferInputStream();
    private volatile int writeIndex = 0;
    private volatile int readIndex = 0;
    private byte[] buffer;

    public SerialByteBuffer(){
        // initialize buffer with default capacity
        this(DEFAULT_INITIAL_BUFFER_SIZE);
    }

    public SerialByteBuffer(int initialCapacity){
        // initialize buffer with user provided capacity
        buffer = new byte[initialCapacity];
    }

    public synchronized void clear(){
        // reset read and write index pointers
        readIndex = writeIndex = 0;
    }

    public InputStream getInputStream(){
        // return the input stream
        return stream;
    }

    public synchronized int capacity(){
        // return the buffer's total capacity
        return buffer.length;
    }

    public synchronized int remaining(){
        // return the number of (unused) bytes still available in the current buffer's capacity
        if (writeIndex < readIndex)
            return (readIndex - writeIndex - 1);
        return ((buffer.length - 1) - (writeIndex - readIndex));
    }

    public synchronized int available(){
        // return the number of bytes that are ready to be read
        if (readIndex <= writeIndex)
            return (writeIndex - readIndex);
        return (buffer.length - (readIndex - writeIndex));
    }

    private void resize(int length) {

        int min_capacity = buffer.length + length;
        int new_capacity = buffer.length;

        // double the capacity until the buffer is large enough to accommodate the new demand
        while (new_capacity < min_capacity) {
            new_capacity *= DEFAULT_BUFFER_SCALE_FACTOR;
        }

        // create a new buffer that can hold the newly determined
        // capacity and copy the bytes from the old buffer into the new buffer
        byte[] new_buffer = new byte[new_capacity];
        System.arraycopy(buffer, readIndex, new_buffer, 0, writeIndex);

        // update pointers
        buffer = new_buffer; // old buffer should get garbage collected
        readIndex = 0;
        writeIndex = available();
    }

    public  void write(byte[] data) throws IOException, BufferOverflowException {
        write(data, 0, data.length);
    }

    public  void write(byte[] data, int offset, int length) throws IOException {
        while (length > 0) {
            int remaining_space = remaining();
            if(remaining_space < length) {
                resize(length);
            }
            int realLen = Math.min(length, remaining_space);
            int firstLen = Math.min(realLen, buffer.length - writeIndex);
            int secondLen = Math.min(realLen - firstLen, buffer.length - readIndex - 1);
            int written = firstLen + secondLen;
            if (firstLen > 0) {
                System.arraycopy(data, offset, buffer, writeIndex, firstLen);
            }
            if (secondLen > 0) {
                System.arraycopy(data, offset + firstLen, buffer, 0, secondLen);
                writeIndex = secondLen;
            } else {
                writeIndex += written;
            }
            if (writeIndex == buffer.length) {
                writeIndex = 0;
            }
            offset += written;
            length -= written;
        }
        if (length > 0) {
            try {
                Thread.sleep(100);
            } catch (Exception x) {
                throw new IOException("Waiting for available space in buffer interrupted.");
            }
        }
    }

    protected class SerialByteBufferInputStream extends InputStream {

        @Override
        public int available() throws IOException {
            synchronized (SerialByteBuffer.this){
                return (SerialByteBuffer.this.available());
            }
        }

        @Override
        public int read() throws IOException {
            while (true){
                synchronized (SerialByteBuffer.this){
                    int available = SerialByteBuffer.this.available();
                    if (available > 0){
                        int result = buffer[readIndex] & 0xff; // we only care about fist 8 bits
                        readIndex++; // increment read index position
                        // if the read index reaches the maximum buffer capacity, then rollover to zero index
                        if (readIndex == buffer.length)
                            readIndex = 0;
                        return result;
                    }
                }
                try {
                    Thread.sleep(100);
                } catch(Exception x){
                    throw new IOException("Blocking read operation interrupted.");
                }
            }
        }

        @Override
        public int read(byte[] data) throws IOException {
            return read(data, 0, data.length);
        }

        @Override
        public int read(byte[] data, int off, int len) throws IOException {
            while (true){
                synchronized (SerialByteBuffer.this){
                    int available = SerialByteBuffer.this.available();
                    if (available > 0){
                        int length = Math.min(len, available);
                        int firstLen = Math.min(length, buffer.length - readIndex);
                        int secondLen = length - firstLen;
                        System.arraycopy(buffer, readIndex, data, off, firstLen);
                        if (secondLen > 0){
                            System.arraycopy(buffer, 0, data, off+firstLen,  secondLen);
                            readIndex = secondLen;
                        } else {
                            readIndex += length;
                        }
                        if (readIndex == buffer.length) {
                            readIndex = 0;
                        }

                        return length;
                    }
                }
                try {
                    Thread.sleep(100);
                } catch(Exception x){
                    throw new IOException("Blocking read operation interrupted.");
                }
            }
        }

        @Override
        public long skip(long n) throws IOException, IllegalArgumentException {
            while (true){
                synchronized (SerialByteBuffer.this){
                    int available = SerialByteBuffer.this.available();
                    if (available > 0){
                        int length = Math.min((int)n, available);
                        int firstLen = Math.min(length, buffer.length - readIndex);
                        int secondLen = length - firstLen;
                        if (secondLen > 0){
                            readIndex = secondLen;
                        } else {
                            readIndex += length;
                        }
                        if (readIndex == buffer.length) {
                            readIndex = 0;
                        }
                        return length;
                    }
                }
                try {
                    Thread.sleep(100);
                } catch(Exception x){
                    throw new IOException("Blocking read operation interrupted.");
                }
            }
        }
    }
}
