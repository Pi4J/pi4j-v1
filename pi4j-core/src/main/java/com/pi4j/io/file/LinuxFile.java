package com.pi4j.io.file;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  LinuxFile.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import com.pi4j.util.NativeLibraryLoader;
import jdk.internal.misc.SharedSecrets;

// TODO :: REMOVE JDK INTERNAL REFS

/**
 * Extends RandomAccessFile to provide access to Linux ioctl.
 */
@SuppressWarnings("restriction")
public class LinuxFile extends RandomAccessFile {
    public LinuxFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public static final int wordSize = getWordSize();
    public static final int localBufferSize = 2048; //about 1 page

    public static final ThreadLocal<ByteBuffer> localDataBuffer = new ThreadLocal<>();
    public static final ThreadLocal<IntBuffer> localOffsetsBuffer = new ThreadLocal<>();

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j");
    }

    /**
     * Runs an ioctl value command on a file descriptor.
     *
     * @param command ioctl command
     * @param value int ioctl value
     * @return result of operation. Zero if everything is OK, less than zero if there was an error.
     */
    public void ioctl(long command, int value) throws IOException {
        final int response = directIOCTL(getFileDescriptor(), command, value);

        if(response < 0)
            throw new LinuxFileException();
    }

    /**
     * Runs an ioctl on a file descriptor. Uses special offset buffer to produce real C-like structures
     * with pointers. Advanced use only! Must be able to produce byte-perfect data structures just as
     * gcc would on this system, including struct padding and pointer size.
     *
     * The data ByteBuffer uses the current position to determine the head point of data
     * passed to the ioctl. This is useful for appending entry-point data structures
     * at the end of the buffer, while referring to other structures/data that come before
     * them in the buffer.
     *
     * <I NEED A BETTER EXPL OF BUFFERS HERE>
     *
     * When assembling the structured data, use {@link LinuxFile#wordSize} to determine the size
     * in bytes needed for a pointer. Also be sure to consider GCC padding and structure alignment.
     * GCC will try a field to its word size (32b ints align at 4-byte, etc), and will align the
     * structure size with the native word size (4-byte for 32b, 8-byte for 64b).
     *
     * Provided IntBuffer offsets must use native byte order (endianness).
     *
     * <pre>
     * {@code
     *    <NEED BETTER EXAMPLE HERE>
     * }
     * </pre>
     *
     * DANGER: check your buffer length! The possible length varies depending on the ioctl call.
     * Overruns are very possible. ioctl tries to determine EFAULTs, but sometimes
     * you might trample JVM data if you are not careful.
     *
     * @param command ioctl command
     * @param data values in bytes for all structures, with 4 or 8 byte size holes for pointers
     * @param offsets byte offsets of pointer at given index
     * @throws IOException
     */
    public void ioctl(final long command, ByteBuffer data, IntBuffer offsets) throws IOException {
        ByteBuffer originalData = data;

        if(data == null || offsets == null)
            throw new NullPointerException("data and offsets required!");

        if(offsets.order() != ByteOrder.nativeOrder())
            throw new IllegalArgumentException("provided IntBuffer offsets ByteOrder must be native!");

        //buffers must be direct
        try {
            if(!data.isDirect()) {
                ByteBuffer newBuf = getDataBuffer(data.limit());
                int pos = data.position(); //keep position

                data.rewind();
                newBuf.clear();
                newBuf.put(data);
                newBuf.position(pos); //restore position

                data = newBuf;
            }

            if(!offsets.isDirect()) {
                IntBuffer newBuf = getOffsetsBuffer(offsets.remaining());

                newBuf.clear();
                newBuf.put(offsets);
                newBuf.flip();

                offsets = newBuf;
            }
        } catch (BufferOverflowException e) {
            throw new ScratchBufferOverrun();
        }

        if((offsets.remaining() & 1) != 0)
            throw new IllegalArgumentException("offset buffer must be even length!");

        for(int i = offsets.position() ; i < offsets.limit() ; i += 2) {
            final int ptrOffset = offsets.get(i);
            final int dataOffset = offsets.get(i + 1);

            if(dataOffset >= data.capacity() || dataOffset < 0)
                throw new IndexOutOfBoundsException("invalid data offset specified in buffer: " + dataOffset);

            if((ptrOffset + wordSize) > data.capacity() || ptrOffset < 0)
                throw new IndexOutOfBoundsException("invalid pointer offset specified in buffer: " + ptrOffset);
        }

        final int response = directIOCTLStructure(getFileDescriptor(), command, data,
                data.position(), offsets, offsets.position(), offsets.remaining());

        if(response < 0)
            throw new LinuxFileException();

        //fast forward positions
        offsets.position(offsets.limit());
        data.rewind();

        //if original data wasnt direct, copy it back in.
        if(originalData != data) {
            originalData.rewind();
            originalData.put(data);
            originalData.rewind();
        }
    }

    /**
     * Gets the real POSIX file descriptor for use by custom jni calls.
     */
    private int getFileDescriptor() throws IOException {
		final int fd = SharedSecrets.getJavaIOFileDescriptorAccess().get(getFD());

        if(fd < 1)
            throw new IOException("failed to get POSIX file descriptor!");

        return fd;
    }

    private static int getWordSize() {
        //TODO: there has to be a better way...
        final String archDataModel = System.getProperty("sun.arch.data.model");
        return "64".equals(archDataModel) ? 8 : 4;
    }

    @Override
    protected void finalize() throws Throwable {
        close();

        super.finalize();
    }

    private synchronized IntBuffer getOffsetsBuffer(int size) {
        final int byteSize = size * 4;
        IntBuffer buf = localOffsetsBuffer.get();

        if(byteSize > localBufferSize)
            throw new ScratchBufferOverrun();

        if(buf == null) {
            ByteBuffer bb = ByteBuffer.allocateDirect(localBufferSize);

            //keep native order, set before cast to IntBuffer
            bb.order(ByteOrder.nativeOrder());

            buf = bb.asIntBuffer();
            localOffsetsBuffer.set(buf);
        }

        return buf;
    }

    private synchronized ByteBuffer getDataBuffer(int size) {
        ByteBuffer buf = localDataBuffer.get();

        if(size > localBufferSize)
            throw new ScratchBufferOverrun();

        if(buf == null) {
            buf = ByteBuffer.allocateDirect(localBufferSize);
            localDataBuffer.set(buf);
        }

        return buf;
    }

    public static class ScratchBufferOverrun extends IllegalArgumentException {
		private static final long serialVersionUID = -418203522640826177L;

		public ScratchBufferOverrun() {
            super("Scratch buffer overrun! Provide direct ByteBuffer for data larger than " + localBufferSize + " bytes");
        }
    }

    public static class LinuxFileException extends IOException {
		private static final long serialVersionUID = -2581606746434701394L;
		int code;

        public LinuxFileException() {
            this(errno());
        }

        LinuxFileException(int code) {
            super(strerror(code));

            this.code = code;
        }

        /**
         * Gets the POSIX code associated with this IO error
         *
         * @return POSIX error code
         */
        public int getCode() {
            return code;
        }
    }

    public static native int errno();

    public static native String strerror(int code);

    protected static native int directIOCTL(int fd, long command, int value);

    protected static native int directIOCTLStructure(int fd, long command, ByteBuffer data, int dataOffset, IntBuffer offsetMap, int offsetMapOffset, int offsetCapacity);
}
