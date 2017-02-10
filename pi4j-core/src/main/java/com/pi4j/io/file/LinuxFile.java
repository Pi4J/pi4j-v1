package com.pi4j.io.file;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  LinuxFile.java
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

import com.pi4j.util.NativeLibraryLoader;

import sun.misc.Cleaner;
import sun.misc.SharedSecrets;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.nio.*;
import java.util.Objects;

/**
 * Extends RandomAccessFile to provide access to Linux ioctl.
 */
public class LinuxFile extends RandomAccessFile {
    public LinuxFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public static final int wordSize = getWordSize();
    public static final int localBufferSize = 2048; //about 1 page

    public static final ThreadLocal<ByteBuffer> localDataBuffer = new ThreadLocal<>();
    public static final ThreadLocal<IntBuffer> localOffsetsBuffer = new ThreadLocal<>();

    private static final Constructor directByteBufferConstructor;

    private static final Field addressField;
    private static final Field capacityField;
    private static final Field cleanerField;

    static {
        try {
            // Load the platform library
            NativeLibraryLoader.load("libpi4j.so");

            Class dbb = Class.forName("java.nio.DirectByteBuffer");

            addressField = Buffer.class.getDeclaredField("address");
            capacityField = Buffer.class.getDeclaredField("capacity");
            cleanerField = Buffer.class.getDeclaredField("cleaner");
            directByteBufferConstructor = dbb.getDeclaredConstructor(
                    new Class[] { int.class, long.class, FileDescriptor.class, Runnable.class });

            addressField.setAccessible(true);
            capacityField.setAccessible(true);
            cleanerField.setAccessible(true);
            directByteBufferConstructor.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new InternalError();
        } catch (ClassNotFoundException e) {
            throw new InternalError();
        } catch (NoSuchMethodException e) {
            throw new InternalError();
        }
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

    /**
     * Direct memory mapping from a file descriptor.
     * This is normally possible through the local FileChannel,
     * but NIO will try to truncate files if they don't report
     * a correct size. This will avoid that.
     *
     *
     * @param length length of desired mapping
     * @param prot protocol used for mapping
     * @param flags flags for mapping
     * @param offset offset in file for mapping
     * @return direct mapped ByteBuffer
     * @throws IOException
     */
    public ByteBuffer mmap(int length, MMAPProt prot, MMAPFlags flags, int offset) throws IOException {
        long pointer = mmap(getFileDescriptor(), length, prot.flag, flags.flag, offset);

        if(pointer == -1)
            throw new LinuxFileException();

        return newMappedByteBuffer(length, pointer, () -> {
            munmapDirect(pointer, length);
        });
    }

    public static void munmap(ByteBuffer mappedBuffer) throws IOException {
        if(!mappedBuffer.isDirect())
            throw new IllegalArgumentException("Must be a mapped direct buffer");

        try {
            long address = addressField.getLong(mappedBuffer);
            int capacity = capacityField.getInt(mappedBuffer);

            if(address == 0 || capacity == 0) return;

            //reset address field first
            addressField.setLong(mappedBuffer, 0);
            capacityField.setInt(mappedBuffer, 0);

            //reset mark and position to new 0 capacity
            mappedBuffer.clear();

            //clean object so it doesnt clean on collection
            ((Cleaner)cleanerField.get(mappedBuffer)).clean();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new InternalError();
        }
    }

    private MappedByteBuffer newMappedByteBuffer(int size, long addr, Runnable unmapper) throws IOException
    {
        MappedByteBuffer dbb;
        try {
            dbb = (MappedByteBuffer)directByteBufferConstructor.newInstance(
                    new Object[] { new Integer(size), new Long(addr), this.getFD(), unmapper });
        } catch (InstantiationException e) {
            throw new InternalError();
        } catch (IllegalAccessException e) {
            throw new InternalError();
        } catch (InvocationTargetException e) {
            throw new InternalError();
        }
        return dbb;
    }

    public static class ScratchBufferOverrun extends IllegalArgumentException {
        public ScratchBufferOverrun() {
            super("Scratch buffer overrun! Provide direct ByteBuffer for data larger than " + localBufferSize + " bytes");
        }
    }

    public static class LinuxFileException extends IOException {
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

    public enum MMAPProt {
        NONE(0),
        READ(1),
        WRITE(2),
        EXEC(4),
        RW(READ.flag | WRITE.flag),
        RX(READ.flag | EXEC.flag),
        RWX(READ.flag | WRITE.flag | EXEC.flag),
        WX(WRITE.flag | EXEC.flag);

        public final int flag;

        MMAPProt(int flag) {
            this.flag = flag;
        }
    }

    public enum MMAPFlags {
        SHARED(1),
        PRIVATE(2),
        SHARED_PRIVATE(SHARED.flag | PRIVATE.flag);

        public final int flag;

        MMAPFlags(int flag) {
            this.flag = flag;
        }
    }

    public static native int errno();

    public static native String strerror(int code);

    protected static native int directIOCTL(int fd, long command, int value);

    protected static native long mmap(int fd, int length, int prot, int flags, int offset);

    protected static native int munmapDirect(long address, long capacity);

    protected static native int directIOCTLStructure(int fd, long command, ByteBuffer data, int dataOffset, IntBuffer offsetMap, int offsetMapOffset, int offsetCapacity);
}
