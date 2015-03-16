package com.pi4j.i2c.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CDeviceImplTest.java  
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.impl.I2CBusImpl;
import com.pi4j.io.i2c.impl.I2CDeviceImpl;
import com.pi4j.io.i2c.impl.IOExceptionWrapperException;

@RunWith(MockitoJUnitRunner.class)
public class I2CDeviceImplTest {
	
	private static final int ADDRESS = 0x28;
	
	private static final int LOCALADDRESS = 0x02;
	
	private static final byte READ_FIRSTBYTE = 77;
	private static final byte READ_SECONDBYTE = 78;
	private static final byte WRITE_FIRSTBYTE = 47;
	private static final byte WRITE_SECONDBYTE = 11;
	
	private Answer<Integer> readAnswer = new Answer<Integer>() {
				@Override
				public Integer answer(InvocationOnMock invocation)
						throws Throwable {
					Object[] args = invocation.getArguments();
					int size = (Integer) args[args.length - 3];
					int offset = (Integer) args[args.length - 2];
					byte[] buffer = (byte[]) args[args.length - 1];
					if (size < 2) {
						throw new IOException(
								"Expected a size greater than one but got '" + size + "'!");
					}
					if (offset < 0) {
						throw new IOException(
								"Expected an non-negative offset but got '" + offset + "'!");
					}
					if (buffer == null) {
						throw new IOException("Got null-buffer!");
					}
					if (buffer.length < (offset + size)) {
						throw new IOException(
								"Expected a buffer greater than 'offset + size' (="
								+ (offset + size) + ") but got '" + buffer.length + "'");
					}
					buffer[offset] = READ_FIRSTBYTE;
					buffer[offset + 1] = READ_SECONDBYTE;
					return 2;
				}
			};
			
	private Answer<Integer> writeAndReadAnswer = new Answer<Integer>() {
		@Override
		public Integer answer(InvocationOnMock invocation)
				throws Throwable {
			Object[] args = invocation.getArguments();
			
			int writeSize = (Integer) args[args.length - 6];
			int writeOffset = (Integer) args[args.length - 5];
			byte[] writeData = (byte[]) args[args.length - 4];
			if (writeSize < 2) {
				throw new IOException(
						"Expected a writeSize greater than one but got '" + writeSize + "'!");
			}
			if (writeOffset < 0) {
				throw new IOException(
						"Expected an non-negative writeOffset but got '" + writeOffset + "'!");
			}
			if (writeData == null) {
				throw new IOException("Got null-writeData!");
			}
			if (writeData.length < (writeOffset + writeSize)) {
				throw new IOException(
						"Expected a buffer greater than 'offset + size' (="
						+ (writeOffset + writeSize) + ") but got '" + writeData.length + "'");
			}
			byte firstByte = writeData[writeOffset];
			if (firstByte != WRITE_FIRSTBYTE) {
				throw new IOException("Expected to get '" + WRITE_FIRSTBYTE
						+ "' at writeData[" + writeOffset + "] but got '" + firstByte);
			}
			byte secondByte = writeData[writeOffset + 1];
			if (secondByte != WRITE_SECONDBYTE) {
				throw new IOException("Expected to get '" + WRITE_SECONDBYTE
						+ "' at writeData[" + (writeOffset + 1) + "] but got '" + secondByte);
			}
			
			int size = (Integer) args[args.length - 3];
			int offset = (Integer) args[args.length - 2];
			byte[] buffer = (byte[]) args[args.length - 1];
			if (size < 2) {
				throw new IOException(
						"Expected a size greater than one but got '" + size + "'!");
			}
			if (offset < 0) {
				throw new IOException(
						"Expected an non-negative offset but got '" + offset + "'!");
			}
			if (buffer == null) {
				throw new IOException("Got null-buffer!");
			}
			if (buffer.length < (offset + size)) {
				throw new IOException(
						"Expected a buffer greater than 'offset + size' (="
						+ (offset + size) + ") but got '" + buffer.length + "'");
			}
			buffer[offset] = READ_FIRSTBYTE;
			buffer[offset + 1] = READ_SECONDBYTE;
			return 2;
		}
	};
	
	private Answer<Object> writeAnswer = new Answer<Object>() {
		@Override
		public Integer answer(InvocationOnMock invocation)
				throws Throwable {
			Object[] args = invocation.getArguments();
			
			int writeSize = (Integer) args[args.length - 3];
			int writeOffset = (Integer) args[args.length - 2];
			byte[] writeData = (byte[]) args[args.length - 1];
			if (writeSize < 2) {
				throw new IOException(
						"Expected a writeSize greater than one but got '" + writeSize + "'!");
			}
			if (writeOffset < 0) {
				throw new IOException(
						"Expected an non-negative writeOffset but got '" + writeOffset + "'!");
			}
			if (writeData == null) {
				throw new IOException("Got null-writeData!");
			}
			if (writeData.length < (writeOffset + writeSize)) {
				throw new IOException(
						"Expected a buffer greater than 'offset + size' (="
						+ (writeOffset + writeSize) + ") but got '" + writeData.length + "'");
			}
			
			return null; // void
		}
		
	};
	
	@Mock		
	private I2CBusImpl bus;
	
	private I2CDeviceImpl device;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {

		// simple run runnable and return result. the original method
		// adds locking but we want to test I2CDeviceImpl not I2CBusImpl!
		when(bus.runActionOnExclusivLockedBus(any(I2CBus.I2CRunnable.class)))
				.thenAnswer(new Answer<Object>() {
					@Override
					public Object answer(InvocationOnMock invocation)
							throws Throwable {
						I2CBus.I2CRunnable<Object> action = (I2CBus.I2CRunnable<Object>)
								invocation.getArguments()[0];
						try {
							action.run();
						} catch (IOExceptionWrapperException e) {
							throw e.getIOException();
						}
						return action.getResult();
					}
				});
		
		device = new I2CDeviceImpl(bus, ADDRESS);
		
	}
	
	@Test
	public void testBasics() throws IOException {
		
		int address = device.getAddress();
		assertEquals("'getAddress()' returns another address that the "
				+ "device was constructed with", ADDRESS, address);
		
	}
	
	@Test
	public void testReading() throws IOException {

		// read a byte
		
		int readResult1 = 50;
		when(bus.readByteDirect(eq(device))).thenReturn(readResult1);
		int read1 = device.read();
		assertEquals("'read()' does not return the expected value!",
				readResult1, read1);
		
		when(bus.readByteDirect(eq(device))).thenReturn(-1000);
		try {
			device.read();
			fail("'read() should throw an exception but didn't!");
		} catch (IOException e) {
			String msg = e.getMessage();
			assertTrue("Expected message of IOException to contain error-code -1000 "
					+ "given by underlying bus but got '"
					+ msg + "'", msg.contains("-1000"));
		}
		
		// read a byte from register
		
		int readResult2 = 51;
		when(bus.readByte(eq(device), eq(LOCALADDRESS))).thenReturn(readResult2);
		int read2 = device.read(LOCALADDRESS);
		assertEquals("'read(int)' does not return the expected value!",
				readResult2, read2);
		
		when(bus.readByte(eq(device), eq(LOCALADDRESS))).thenReturn(-1000);
		try {
			device.read(LOCALADDRESS);
			fail("'read(int) should throw an exception but didn't!");
		} catch (IOException e) {
			String msg = e.getMessage();
			assertTrue("Expected message of IOException to contain error-code -1000 "
					+ "given by underlying bus but got '"
					+ msg + "'", msg.contains("-1000"));
		}
		
		// read n bytes
		
		when(bus.readBytesDirect(eq(device), anyInt(), anyInt(), any(byte[].class)))
				.thenAnswer(readAnswer);
		
		byte[] buffer1 = new byte[2];
		int read3 = device.read(buffer1, 0, 2);
		assertEquals("Expected to read one byte but got a different number of bytes!", 
				2, read3);
		assertEquals("Unexpected bytes in buffer!", buffer1[0], READ_FIRSTBYTE);
		assertEquals("Unexpected bytes in buffer!", buffer1[1], READ_SECONDBYTE);

		byte[] buffer2 = new byte[3];
		int read4 = device.read(buffer2, 1, 2);
		assertEquals("Expected to read one byte but got a different number of bytes!", 
				2, read4);
		assertEquals("Unexpected bytes in buffer!", buffer2[1], READ_FIRSTBYTE);
		assertEquals("Unexpected bytes in buffer!", buffer2[2], READ_SECONDBYTE);
		
		try {
			device.read(new byte[1], 0, 1);
			fail("Expected 'read(...)' to throw an exception but got none!");
		} catch (IOException e) {
			// expected
		}
		try {
			device.read(new byte[2], 1, 2);
			fail("Expected 'read(...)' to throw an exception but got none!");
		} catch (IOException e) {
			// expected
		}
		
		when(bus.readBytesDirect(eq(device),
				anyInt(), anyInt(), any(byte[].class))).thenReturn(-1000);
		try {
			device.read(new byte[2], 0, 2);
			fail("'read(int) should throw an exception but didn't!");
		} catch (IOException e) {
			String msg = e.getMessage();
			assertTrue("Expected message of IOException to contain error-code -1000 "
					+ "given by underlying bus but got '"
					+ msg + "'", msg.contains("-1000"));
		}
		
		// read n bytes from register
		
		when(bus.readBytes(eq(device), eq(LOCALADDRESS),
				anyInt(), anyInt(), any(byte[].class))).thenAnswer(readAnswer);

		byte[] buffer3 = new byte[2];
		int read5 = device.read(LOCALADDRESS, buffer3, 0, 2);
		assertEquals("Expected to read one byte but got a different number of bytes!", 
				2, read5);
		assertEquals("Unexpected bytes in buffer!", buffer3[0], READ_FIRSTBYTE);
		assertEquals("Unexpected bytes in buffer!", buffer3[1], READ_SECONDBYTE);

		byte[] buffer4 = new byte[3];
		int read6 = device.read(LOCALADDRESS, buffer4, 1, 2);
		assertEquals("Expected to read one byte but got a different number of bytes!", 
				2, read6);
		assertEquals("Unexpected bytes in buffer!", buffer4[1], READ_FIRSTBYTE);
		assertEquals("Unexpected bytes in buffer!", buffer4[2], READ_SECONDBYTE);
		
		try {
			device.read(LOCALADDRESS, new byte[1], 0, 1);
			fail("Expected 'read(...)' to throw an exception but got none!");
		} catch (IOException e) {
			// expected
		}
		try {
			device.read(LOCALADDRESS, new byte[2], 1, 2);
			fail("Expected 'read(...)' to throw an exception but got none!");
		} catch (IOException e) {
			// expected
		}
		
		when(bus.readBytes(eq(device), eq(LOCALADDRESS),
				anyInt(), anyInt(), any(byte[].class))).thenReturn(-1000);
		try {
			device.read(LOCALADDRESS, new byte[2], 0, 2);
			fail("'read(int) should throw an exception but didn't!");
		} catch (IOException e) {
			String msg = e.getMessage();
			assertTrue("Expected message of IOException to contain error-code -1000 "
					+ "given by underlying bus but got '"
					+ msg + "'", msg.contains("-1000"));
		}
		
		// write and read bytes
		
		byte[] dataToBeWritten = new byte[] { WRITE_FIRSTBYTE, WRITE_SECONDBYTE };
		when(bus.writeAndReadBytesDirect(eq(device), anyInt(), anyInt(), any(byte[].class),
				anyInt(), anyInt(), any(byte[].class))).thenAnswer(writeAndReadAnswer);
		
		byte[] buffer5 = new byte[2];
		int read7 = device.read(dataToBeWritten, 0, 2, buffer5, 0, 2);
		assertEquals("Expected to read one byte but got a different number of bytes!", 
				2, read7);
		assertEquals("Unexpected bytes in buffer!", buffer5[0], READ_FIRSTBYTE);
		assertEquals("Unexpected bytes in buffer!", buffer5[1], READ_SECONDBYTE);

		byte[] buffer6 = new byte[3];
		int read8 = device.read(dataToBeWritten, 0, 2, buffer6, 1, 2);
		assertEquals("Expected to read one byte but got a different number of bytes!", 
				2, read8);
		assertEquals("Unexpected bytes in buffer!", buffer6[1], READ_FIRSTBYTE);
		assertEquals("Unexpected bytes in buffer!", buffer6[2], READ_SECONDBYTE);
		
		try {
			device.read(new byte[1], 0, 1);
			fail("Expected 'read(...)' to throw an exception but got none!");
		} catch (IOException e) {
			// expected
		}
		try {
			device.read(new byte[2], 1, 2);
			fail("Expected 'read(...)' to throw an exception but got none!");
		} catch (IOException e) {
			// expected
		}
		
	}

	@Test
	public void testWriting() throws IOException {
		
		// write one byte
		
		when(bus.writeByteDirect(eq(device), anyByte()))
				.thenReturn(-1000);
		when(bus.writeByteDirect(eq(device), eq(WRITE_FIRSTBYTE)))
				.thenReturn(0);
		device.write(WRITE_FIRSTBYTE);
		
		try {
			device.write(WRITE_SECONDBYTE);
			fail("'write(byte) should throw an exception but didn't!");
		} catch (IOException e) {
			String msg = e.getMessage();
			assertTrue("Expected message of IOException to contain error-code -1000 "
					+ "given by underlying bus but got '"
					+ msg + "'", msg.contains("-1000"));
		}
		
		// write one byte to register
		
		when(bus.writeByte(eq(device), eq(LOCALADDRESS), anyByte()))
				.thenReturn(-1000);
		when(bus.writeByte(eq(device), eq(LOCALADDRESS), eq(WRITE_FIRSTBYTE)))
				.thenReturn(0);
		device.write(LOCALADDRESS, WRITE_FIRSTBYTE);
		
		try {
			device.write(LOCALADDRESS, WRITE_SECONDBYTE);
			fail("'write(byte) should throw an exception but didn't!");
		} catch (IOException e) {
			String msg = e.getMessage();
			assertTrue("Expected message of IOException to contain error-code -1000 "
					+ "given by underlying bus but got '"
					+ msg + "'", msg.contains("-1000"));
		}
		
		// write n bytes
		
		byte[] dataToBeWritten = new byte[] { WRITE_FIRSTBYTE, WRITE_SECONDBYTE };
		when(bus.writeBytesDirect(eq(device), anyInt(), anyInt(), any(byte[].class)))
				.thenReturn(-1000);
		when(bus.writeBytesDirect(eq(device), eq(2), anyInt(), any(byte[].class)))
				.thenAnswer(writeAnswer);
		device.write(dataToBeWritten, 0, dataToBeWritten.length);
		
		try {
			device.write(new byte[1], 0, 1);
			fail("Expected 'write(...)' to throw an exception but got none!");
		} catch (IOException e) {
			// expected
		}
		try {
			device.write(new byte[2], 1, 2);
			fail("Expected 'write(...)' to throw an exception but got none!");
		} catch (IOException e) {
			// expected
		}
		
		try {
			device.write(new byte[1], 0, 1);
			fail("'write(byte[], int, int) should throw an exception but didn't!");
		} catch (IOException e) {
			String msg = e.getMessage();
			assertTrue("Expected message of IOException to contain error-code -1000 "
					+ "given by underlying bus but got '"
					+ msg + "'", msg.contains("-1000"));
		}
		
		// write n bytes to register
		
		when(bus.writeBytes(eq(device), eq(LOCALADDRESS), anyInt(), anyInt(), any(byte[].class)))
				.thenReturn(-1000);
		when(bus.writeBytes(eq(device), eq(LOCALADDRESS), eq(2), anyInt(), any(byte[].class)))
				.thenAnswer(writeAnswer);
		device.write(LOCALADDRESS, dataToBeWritten, 0, dataToBeWritten.length);
		
		try {
			device.write(LOCALADDRESS, new byte[1], 0, 1);
			fail("Expected 'write(...)' to throw an exception but got none!");
		} catch (IOException e) {
			// expected
		}
		try {
			device.write(LOCALADDRESS, new byte[2], 1, 2);
			fail("Expected 'write(...)' to throw an exception but got none!");
		} catch (IOException e) {
			// expected
		}
		
		try {
			device.write(LOCALADDRESS, new byte[1], 0, 1);
			fail("'write(int, byte[], int, int) should throw an exception but didn't!");
		} catch (IOException e) {
			String msg = e.getMessage();
			assertTrue("Expected message of IOException to contain error-code -1000 "
					+ "given by underlying bus but got '"
					+ msg + "'", msg.contains("-1000"));
		}
		
	}
	
}
