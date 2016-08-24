package com.pi4j.io.file;

import com.pi4j.util.NativeLibraryLoader;
import sun.misc.SharedSecrets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.*;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Extends RandomAccessFile to provide access to Linux ioctl.
 */
public class LinuxFile extends RandomAccessFile {
    public LinuxFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);

        mapList = new LinkedList<ByteBuffer>();
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    private final LinkedList<ByteBuffer> mapList;

    public static final int wordSize = getWordSize();
    public static final int localBufferSize = 2048;

    public static ThreadLocal<ByteBuffer> localDataBuffer = new ThreadLocal<ByteBuffer>();
    public static ThreadLocal<ByteBuffer> localOffsetsBuffer = new ThreadLocal<ByteBuffer>();

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
            throw new IOCTLException("Failed to run ioctl!", response);
    }

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
     * <pre>
     * {@code
     *    byte[] buffer = new byte[32];
     *    int dataPointer;
     *
     *    buffer[dataPointer = 0] = 0; //data pointer
     *    buffer[wordSize] = (byte)buffer.length; //length byte
     *
     *    int bufferStart = wordSize * 2; //align and pad
     *
     *    System.arraycopy(data, 0, buffer, bufferStart, buffer.length);
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
    public void ioctl(long command, ByteBuffer data, IntBuffer offsets) throws IOException {
        ByteBuffer originalData = data;

        if(data == null || offsets == null)
            throw new NullPointerException("data and offsets required!");

        if(offsets.order() != ByteOrder.nativeOrder())
            throw new IllegalArgumentException("provided IntBuffer offsets ByteOrder must be native!");

        //buffers must be direct
        if(!data.isDirect()) {
            ByteBuffer newBuf = getDataBuffer(data.remaining());

            newBuf.clear();
            newBuf.put(data);
            newBuf.flip();

            data = newBuf;
        }

        if(!offsets.isDirect()) {
            ByteBuffer baseBuf = getOffsetsBuffer(offsets.remaining());

            baseBuf.clear();

            //cast *after* setting order
            IntBuffer newBuf = baseBuf.asIntBuffer();

            newBuf.clear();
            newBuf.put(offsets);
            newBuf.flip();

            offsets = newBuf;
        }

        if((offsets.remaining() & 1) != 0)
            throw new IllegalArgumentException("offset buffer must be even length!");

        for(int i = 0 ; i < offsets.remaining() ; i++) {
            final int offset = offsets.get(i + offsets.position());

            if(offset >= data.remaining() || offset < 0)
                throw new IndexOutOfBoundsException("invalid offset specified in buffer: " + offset);
        }

        final int response = directIOCTLStructure(getFileDescriptor(), command, data,
                data.position(), offsets, offsets.position(), offsets.remaining());

        if(response < 0)
            throw new IOCTLException("Failed to run ioctl!", response);

        offsets.position(offsets.limit());

        //if original data wasnt direct, copy it back in.
        if(!originalData.isDirect()) {
            originalData.put(data);
        } else {
            //otherwise fast forward data
            data.position(data.limit());
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
        return System.getProperty("sun.arch.data.model") == "64" ? 8 : 4;
    }

    @Override
    protected void finalize() throws Throwable {
        close();

        super.finalize();
    }

    @Override
    public void close() throws IOException {
        synchronized(this) {
            while(!mapList.isEmpty()) {
                ByteBuffer bb = mapList.removeFirst();

                munmap(bb);
            }
        }

        super.close();
    }

    private ByteBuffer getOffsetsBuffer(int size) {
        final int byteSize = size * 4;
        ByteBuffer buf = localOffsetsBuffer.get();

        if(byteSize > localBufferSize)
            throw new ScratchBufferOverrun();

        if(buf == null) {
            buf = ByteBuffer.allocateDirect(localBufferSize);
            localOffsetsBuffer.set(buf);
        }

        //keep native order
        buf.order(ByteOrder.nativeOrder());

        return buf;
    }

    private ByteBuffer getDataBuffer(int size) {
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
     * @param length length of desired mapping
     * @param prot protocol used for mapping
     * @param flags flags for mapping
     * @param offset offset in file for mapping
     * @return direct mapped ByteBuffer
     * @throws IOException
     */
    public synchronized ByteBuffer mmap(int length, MMAPProt prot, MMAPFlags flags, int offset) throws IOException {
        Object res = mmap(getFileDescriptor(), length, prot.flag, flags.flag, offset);

        if(res instanceof Integer) {
            int code = ((Integer)res).intValue();

            throw new IOCTLException("Failed to run mmap!", code);
        }

        ByteBuffer bb = (ByteBuffer)res;

        mapList.add(bb);

        return bb;
    }

    public synchronized void munmap(ByteBuffer mappedBuffer) throws IOException {
        if(mapList.remove(mappedBuffer)) {
            int response = munmapDirect(mappedBuffer);

            if(response < 0)
                throw new IOCTLException("Failed to run munmap!", response);
        }
    }

    private static native int directIOCTL(int fd, long command, int value);

    private static native Object mmap(int fd, int length, int prot, int flags, int offset);

    private static native int munmapDirect(ByteBuffer mappedBuffer);

    private static native int directIOCTLStructure(int fd, long command, ByteBuffer data, int dataOffset, IntBuffer offsetMap, int offsetMapOffset, int offsetCapacity);

    public static class ScratchBufferOverrun extends IllegalArgumentException {
        public ScratchBufferOverrun() {
            super("Scratch buffer overrun! Provide direct ByteBuffer for data larger than " + localBufferSize + " bytes");
        }
    }

    public static class IOCTLException extends IOException {
        int rawCode;

        /**
         * @param message Exception message
         * @param rawCode negative POSIX errno code
         */
        public IOCTLException(String message, int rawCode) {
            super(message);

            this.rawCode = Math.abs(rawCode);
        }

        /**
         * Gets the POSIX code associated with this IO error
         *
         * @return POSIX error code
         */
        public int getCode() {
            return rawCode;
        }
    }

    public enum MMAPProt {
        READ(1),
        WRITE(2),
        RW(1 | 2);

        public final int flag;

        MMAPProt(int flag) {
            this.flag = flag;
        }
    }

    public enum MMAPFlags {
        SHARED(1),
        PRIVATE(2);

        public final int flag;

        MMAPFlags(int flag) {
            this.flag = flag;
        }
    }
}
