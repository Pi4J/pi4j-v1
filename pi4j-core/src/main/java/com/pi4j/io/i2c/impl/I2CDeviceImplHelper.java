package com.pi4j.io.i2c.impl;

import java.io.IOException;
import java.util.concurrent.Callable;

public class I2CDeviceImplHelper {

	/**
	 * Base class for runnables used for writing data.
	 * 
	 * @see WriteByteCallable
	 * @see I2CDeviceImplHelper.WriteBytesCallable
	 */
	static abstract class I2CDeviceImplCallable<T> implements Callable<T> {
	
		protected I2CDeviceImpl owner;
		
		protected int localAddress;
				
		public I2CDeviceImplCallable(final I2CDeviceImpl owner, final int localAddress) {
			this.owner = owner;
			this.localAddress = localAddress;
		}
		
		protected void handleErrorResults(final int ret, final boolean written) throws Exception {
			
	        if (ret < 0) {
	        	final String desc;
	        	if (localAddress == -1) {
	        		desc = owner.makeDescription();
	        	} else {
	        		desc = owner.makeDescription(localAddress);
	        	}
	        	if (written) {
	        		throw new IOException("Error writing to " + desc + ". Got '" + ret + "'.");
	        	} else {
	        		throw new IOException("Error reading from " + desc + ". Got '" + ret + "'.");
	        	}
	        }
			
		}
		
	}

	/**
	 * Writes one byte to the bus.
	 *
	 * @see I2CDeviceImpl#write(byte)
	 * @see I2CDeviceImpl#write(int, byte)
	 */
	static class WriteByteCallable extends I2CDeviceImplHelper.I2CDeviceImplCallable<Void> {
		
		private byte data;
		
		/**
		 * @param owner The runnable's owner
		 * @param data The byte to be written
		 */
		public WriteByteCallable(final I2CDeviceImpl owner, final byte data) {
			this(owner, -1, data);
		}
	
		/**
		 * @param owner The runnable's owner
		 * @param localAddress The register address where the byte has to be written to
		 * @param data The byte to be written
		 */
		public WriteByteCallable(final I2CDeviceImpl owner, 
				final int localAddress, final byte data) {
			super(owner, localAddress);
			this.data = data;
		}
		
		@Override
		public Void call() throws Exception {
	
	        final int ret;
	        if (localAddress == -1) {
	        	ret = owner.getBus().writeByteDirect(owner, data);
	        } else {
	        	ret = owner.getBus().writeByte(owner, localAddress, data);
	        }
	
	        handleErrorResults(ret, true);
	        
	        return null;
			
		}
		
	}

	/**
	 * Writes n bytes to the bus.
	 *
	 * @see I2CDeviceImpl#write(byte[], int, int)
	 * @see I2CDeviceImpl#write(int, byte[], int, int)
	 */
	static class WriteBytesCallable extends I2CDeviceImplCallable<Void> {
		
		private byte[] data;
		
		private int offset;
		
		private int size;
		
		/**
		 * @param owner The runnable's owner
		 * @param data The bytes to be written
		 * @param offset The offset where the bytes start within the data-array
		 * @param size The number of bytes to be written
		 */
		public WriteBytesCallable(final I2CDeviceImpl owner, final byte[] data,
				final int offset, final int size) {
			this(owner, -1, data, offset, size);
		}
	
		/**
		 * @param owner The runnable's owner
		 * @param localAddress The register address where the byte has to be written to
		 * @param data The bytes to be written
		 * @param offset The offset where the bytes start within the data-array
		 * @param size The number of bytes to be written
		 */
		public WriteBytesCallable(final I2CDeviceImpl owner, 
				final int localAddress, final byte[] data,
				final int offset, final int size) {
			super(owner, localAddress);
			this.data = data;
			this.offset = offset;
			this.size = size;
		}
		
		@Override
		public Void call() throws Exception {
				
	        final int ret;
	        if (localAddress == -1) {
	        	ret = owner.getBus().writeBytesDirect(owner, size, offset, data);
	        } else {
	        	ret = owner.getBus().writeBytes(owner, localAddress, size, offset, data);
	        }
	        
	        handleErrorResults(ret, true);
	            
			return null;
			
		}
		
	}

	/**
	 * Reads one byte from the bus.
	 * 
	 * @see I2CDeviceImpl#read()
	 * @see I2CDeviceImpl#read(int)
	 */
	static class ReadByteCallable extends I2CDeviceImplCallable<Integer> {
		
		public ReadByteCallable(I2CDeviceImpl owner) {
			this(owner, -1);
		}
		
		public ReadByteCallable(I2CDeviceImpl owner, int localAddress) {
			super(owner, localAddress);
		}
	
		@Override
		public Integer call() throws Exception {
			
			final Integer result;
				
	        if (localAddress == -1) {
	        	result = owner.getBus().readByteDirect(owner);
	        } else {
	        	result = owner.getBus().readByte(owner, localAddress);
	        }
	
	        handleErrorResults(result, false);
	
	        return result;
			
		}
		
	}

	/**
	 * Reads n bytes from the bus.
	 * 
	 * @see I2CDeviceImpl#read(byte[], int, int)
	 * @see I2CDeviceImpl#read(int, byte[], int, int)
	 * @see I2CDeviceImpl#read(byte[], int, int, byte[], int, int)
	 */
	static class ReadBytesCallable extends I2CDeviceImplCallable<Integer> {
		
		private byte[] data;
		
		private int offset;
		
		private int size;
		
		private byte[] writeData;
		
		private int writeOffset;
		
		private int writeSize;
		
		/**
		 * @param owner The runnable's owner
		 * @param data The buffer where the bytes read have to been stored
		 * @param offset The offset where the bytes start within the data-array
		 * @param size The number of bytes may be read at once
		 */
		public ReadBytesCallable(final I2CDeviceImpl owner, final byte[] data,
				final int offset, final int size) {
			this(owner, -1, data, offset, size);
		}
		
		/**
		 * @param owner The runnable's owner
		 * @param localAddress The register address where the byte has to be read from
		 * @param data The buffer where the bytes read have to been stored
		 * @param offset The offset where the bytes start within the data-array
		 * @param size The number of bytes may be read at once
		 */
		public ReadBytesCallable(final I2CDeviceImpl owner, final int localAddress,
				final byte[] data, final int offset, final int size) {
			super(owner, localAddress);
			this.data = data;
			this.offset = offset;
			this.size = size;
		}
	
		/**
		 * @param owner The runnable's owner
		 * @param writeData The bytes to be written prior to reading
		 * @param writeOffset The offset where the bytes start within the writeData-array
		 * @param writeSize The number of bytes to be written
		 * @param data The buffer where the bytes read have to been stored
		 * @param offset The offset where the bytes start within the data-array
		 * @param size The number of bytes may be read at once
		 */
		public ReadBytesCallable(final I2CDeviceImpl owner,
					final byte[] writeData, final int writeOffset, final int writeSize,
					final byte[] data, final int offset, final int size) {
			this(owner, -1, data, offset, size);
			this.writeData = writeData;
			this.writeOffset = writeOffset;
			this.writeSize = writeSize;
		}
		
		@Override
		public Integer call() throws Exception {
			
			final Integer result;
				
			if (localAddress != -1) {
				result = owner.getBus().readBytes(owner, localAddress, size, offset, data);
			} else  if (writeData == null) {
				result = owner.getBus().readBytesDirect(owner, size, offset, data);
			} else {
				result = owner.getBus().writeAndReadBytesDirect(owner,
						writeSize, writeOffset, writeData, size, offset, data);
			}
	        
	        handleErrorResults(result, false);
	
			return result;
			
		}
		
	}

}
