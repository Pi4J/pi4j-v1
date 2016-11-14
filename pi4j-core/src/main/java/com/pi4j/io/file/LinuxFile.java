package com.pi4j.io.file;

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
import java.lang.reflect.Modifier;
import java.nio.*;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

//TODO: rename to SystemFile?

/**
 * Extends RandomAccessFile to provide access to Linux ioctl.
 */
public class LinuxFile extends RandomAccessFile {
    public LinuxFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);

        mapList = new LinkedList<ByteBuffer>();
    }

    private final LinkedList<ByteBuffer> mapList;

    public static final int wordSize = getWordSize();
    public static final int localBufferSize = 2048; //about 1 page

    public static final ThreadLocal<ByteBuffer> localDataBuffer = new ThreadLocal<ByteBuffer>();
    public static final ThreadLocal<IntBuffer> localOffsetsBuffer = new ThreadLocal<IntBuffer>();

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
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

    //TODO: instead of int array for offests, provide overload for (Array[Int], Array[AnyRef]) accepting Int or native ByteArray elements,

    /**
     * Runs an ioctl on a file descriptor. Uses special offset buffer to produce real C-like structures
     * with pointers. Advanced use only! Must be able to produce byte-perfect data structures just as
     * gcc would on this system, including struct padding and pointer size.
     *
     * The offsets array contains 2 ints for every offset needed. The first offset specifies
     * the location of the pointer in the byte array, the second offset specifies the offset
     * in the byte array that the pointer points to.
     * int[] {pointer1, dest1, pointer2, dest2, ...}
     *
     * Both NIO Buffers support position/limit, and position will be modified by this operation.
     * It is recommended to use direct ByteBuffers when possible, but this class does support thread
     * local temp buffers of 2k for copying heap-allocated buffers. An exception will be thrown otherwise.
     * JNI will generally allocate new buffers for direct JNI byte[] access anyways,
     * so the performance profile is similar.
     *
     * When assembling the structured data, use {@link LinuxFile#wordSize} to determine the size
     * in bytes needed for a pointer. Also be sure to consider GCC padding and structure alignment.
     * GCC will try a field to its word size (32b ints align at 4-byte, etc), and will align the
     * structure size with the native word size (4-byte for 32b, 8-byte for 64b).
     *
     * Provided IntBuffer offsets must use native byte order (endianness).
     *
     * <pre> TODO: fix this
     * {@code
     *    byte[] buffer = new byte[32];
     *    int dataPointer;
     *
     *    buffer[dataPointer = 0] = 0; //data pointer
     *    buffer[dataPointer + wordSize] = (byte)buffer.length; //length byte
     *
     *    int bufferStart = wordSize * 2; //align and pad
     *
     *    System.arraycopy(data, 0, buffer, bufferStart, buffer.length);
     *
     *    int[] pointers =
     *
     *    file.ioctl(0x7E, ByteBuffer.wrap(buffer),
     *      new int[] {dataPointer, bufferStart});
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
        return System.getProperty("sun.arch.data.model") == "64" ? 8 : 4;
    }

    @Override
    protected void finalize() throws Throwable {
        close();

        super.finalize();
    }

    @Override
    public synchronized void close() throws IOException {
        while(!mapList.isEmpty()) {
            munmap(mapList.getFirst());
        }

        super.close();
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

    //TODO: find safe way to handle explicit unmapping?

    //TODO: on unmap, replace mapped pointer with allocated direct buffer

    /**
     * Direct memory mapping from a file descriptor.
     * This is normally possible through the local FileChannel,
     * but NIO will try to truncate files if they don't report
     * a correct size. This will avoid that.
     *
     * TODO: use Cleaner.create() to create a finalizer for a mmap'd buffer. Disable cleaner on manual munmap.
     *
     * @param length length of desired mapping
     * @param prot protocol used for mapping
     * @param flags flags for mapping
     * @param offset offset in file for mapping
     * @return direct mapped ByteBuffer
     * @throws IOException
     */
    public ByteBuffer mmap(int length, MMAPProt prot, MMAPFlags flags, int offset) throws IOException {
        ByteBuffer bb = mmap(getFileDescriptor(), length, prot.flag, flags.flag, offset);

        if(bb == null)
            throw new LinuxFileException();

        synchronized(this) {
            mapList.add(bb);
        }

        return bb;
    }

    private static Field addressField = null;
    private static Field capacityField = null;

    static {
        try {
            addressField = Buffer.class.getDeclaredField("address");
            capacityField = Buffer.class.getDeclaredField("capacity");

            addressField.setAccessible(true);
            capacityField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public synchronized void munmap(ByteBuffer mappedBuffer) throws IOException {
        if(mapList.remove(mappedBuffer)) {
            int response = munmapDirect(mappedBuffer);

            try {
                addressField.setLong(mappedBuffer, 0);
                capacityField.setInt(mappedBuffer, 0);

                //reset mark and position to new 0 capacity
                mappedBuffer.clear();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } 

            if(response < 0)
                throw new LinuxFileException();
        }
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

    protected static native ByteBuffer mmap(int fd, int length, int prot, int flags, int offset);

    protected static native int munmapDirect(ByteBuffer mappedBuffer);

    protected static native int directIOCTLStructure(int fd, long command, ByteBuffer data, int dataOffset, IntBuffer offsetMap, int offsetMapOffset, int offsetCapacity);
}
