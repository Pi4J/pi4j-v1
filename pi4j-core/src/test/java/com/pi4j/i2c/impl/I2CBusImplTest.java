package com.pi4j.i2c.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.impl.I2CBusImpl;
import com.pi4j.io.i2c.impl.I2CDeviceImpl;
import com.pi4j.jni.I2C;

@RunWith(PowerMockRunner.class)
@PrepareForTest({I2C.class, I2CBusImpl.class})
public class I2CBusImplTest {

	private static int BUSNUMBER = 1;
	private static int FILEDESCRIPTOR = 4711;
	private static String FILENAME = "/dev/null";
	private static int DEVICE_ADDRESS = 0x15;
	
	private static class TestableI2CBusImpl extends I2CBusImpl {
		
		protected TestableI2CBusImpl(final int busNumber,
	    		final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit)
				throws UnsupportedBusNumberException, IOException {
			super(busNumber, lockAquireTimeout, lockAquireTimeoutUnit);
		}
		
		@Override
		protected String getFilenameForBusnumber(int busNumber)
				throws UnsupportedBusNumberException {
			
			if (busNumber != BUSNUMBER) {
				throw new UnsupportedBusNumberException();
			}
			
			return FILENAME;
			
		}
		
	}
	
	private I2CBusImpl bus;
	
	@Before
	public void setUp() throws Exception {
		
		PowerMockito.mockStatic(I2C.class);
		when(I2C.i2cOpen(anyString())).thenReturn(FILEDESCRIPTOR);
		when(I2C.i2cClose(anyInt())).thenReturn(0);
		
		bus = new TestableI2CBusImpl(BUSNUMBER, 100, TimeUnit.MILLISECONDS);

	}
	
	@Test
	public void testBasics() throws Exception {
		
		// test that I2C.i2cOpen was called during setup
		
		verifyStatic(times(1)); I2C.i2cOpen(eq(FILENAME));
		
		// test for busnumber
		
		final int busNo = bus.getBusNumber();
		assertEquals("Got wrong busnumber from I2CBusImpl-instance!", BUSNUMBER, busNo);
		
		// test that I2C.i2cClose is called on I2CBus.close()
		
		bus.close();
		verifyStatic(times(1)); I2C.i2cClose(eq(FILEDESCRIPTOR));
		
		// test for IOExceptions on using a bus already closed
		
		try {
			bus.readByteDirect(null);
			fail("calling 'readByteDirect(...)' on a closed bus should throw "
					+ "an IOException but did not!");
		} catch (IOException e) {
			// expected
		}
		try {
			bus.readByte(null, 0);
			fail("calling 'readByte(...)' on a closed bus should throw "
					+ "an IOException but did not!");
		} catch (IOException e) {
			// expected
		}
		try {
			bus.readBytesDirect(null, 0, 0, null);
			fail("calling 'readBytesDirect(...)' on a closed bus should throw "
					+ "an IOException but did not!");
		} catch (IOException e) {
			// expected
		}
		try {
			bus.readBytes(null, 0, 0, 0, null);
			fail("calling 'readBytes(...)' on a closed bus should throw "
					+ "an IOException but did not!");
		} catch (IOException e) {
			// expected
		}
		try {
			bus.writeByteDirect(null, Byte.MIN_VALUE);
			fail("calling 'writeByteDirect(...)' on a closed bus should throw "
					+ "an IOException but did not!");
		} catch (IOException e) {
			// expected
		}
		try {
			bus.writeByte(null, 0, Byte.MIN_VALUE);
			fail("calling 'writeByte(...)' on a closed bus should throw "
					+ "an IOException but did not!");
		} catch (IOException e) {
			// expected
		}
		try {
			bus.writeBytesDirect(null, 0, 0, null);
			fail("calling 'writeBytesDirect(...)' on a closed bus should throw "
					+ "an IOException but did not!");
		} catch (IOException e) {
			// expected
		}
		try {
			bus.writeBytes(null, 0, 0, 0, null);
			fail("calling 'writeBytes(...)' on a closed bus should throw "
					+ "an IOException but did not!");
		} catch (IOException e) {
			// expected
		}
		try {
			bus.writeAndReadBytesDirect(null, 0, 0, null, 0, 0, null);
			fail("calling 'writeAndReadBytesDirect(...)' on a closed bus should throw "
					+ "an IOException but did not!");
		} catch (IOException e) {
			// expected
		}
		try {
			bus.lock();
			fail("calling 'lock(...)' on a closed bus should throw "
					+ "an RuntimeException but did not!");
		} catch (RuntimeException e) {
			// expected
		}
		try {
			bus.unlock();
			fail("calling 'unlock(...)' on a closed bus should throw "
					+ "an RuntimeException but did not!");
		} catch (RuntimeException e) {
			// expected
		}
		
	}
	
	@Test
	public void testDeviceInteractions() throws Exception {
		
		// test that retrieving a device calls the I2CDeviceImpl-constructor
		
		PowerMockito.whenNew(I2CDeviceImpl.class).withArguments(
				bus, DEVICE_ADDRESS).thenReturn(mock(I2CDeviceImpl.class));
		
		I2CDevice device = bus.getDevice(DEVICE_ADDRESS);
		assertNotNull(
				"'I2CBus.getDevice(...)' did not return an device-instance", device);
		assertTrue("'I2CBus.getDevice(...)' does not return an instance of I2CDeviceImpl!",
				device instanceof I2CDeviceImpl);
		PowerMockito.verifyNew(I2CDeviceImpl.class, times(1))
				.withArguments(bus, DEVICE_ADDRESS);
		
		when(device.getAddress()).thenReturn(DEVICE_ADDRESS);
		
		// test read-methods
		
		I2CDeviceImpl deviceImpl = (I2CDeviceImpl) device;
		
		int byteToRead = 123;
		when(I2C.i2cReadByteDirect(anyInt(), eq(DEVICE_ADDRESS))).thenReturn(byteToRead);
		int readByteDirect = bus.readByteDirect(deviceImpl);
		assertEquals("Unexpected result from 'I2CBusImpl.readByteDirect(...)'",
				byteToRead, readByteDirect);
		
		int localAddress = 815;
		when(I2C.i2cReadByte(anyInt(), eq(DEVICE_ADDRESS), eq(localAddress)))
				.thenReturn(byteToRead);
		int readByte = bus.readByte(deviceImpl, localAddress);
		assertEquals("Unexpected result from 'I2CBusImpl.readByte(...)'",
				byteToRead, readByte);
		
		byte[] buffer = new byte[2];
		when(I2C.i2cReadBytes(anyInt(), eq(DEVICE_ADDRESS), 
				eq(localAddress), eq(buffer.length), eq(0), eq(buffer)))
				.thenReturn(buffer.length);
		int readBytes = bus.readBytes(deviceImpl, localAddress, buffer.length, 0, buffer);
		assertEquals("Unexpected result from 'I2CBusImpl.readBytes(...)'",
				buffer.length, readBytes);
		
		// test write-methods
		
		byte toBeWritten = 13;
		when(I2C.i2cWriteByteDirect(anyInt(), eq(DEVICE_ADDRESS), eq(toBeWritten)))
				.thenReturn(10);
		int writeByteDirect = bus.writeByteDirect(deviceImpl, toBeWritten);
		assertEquals("Unexpected result from 'writeByteDirect(...)'",
				10, writeByteDirect);

		when(I2C.i2cWriteByte(anyInt(), eq(DEVICE_ADDRESS), eq(localAddress), eq(toBeWritten)))
				.thenReturn(10);
		int writeByte = bus.writeByte(deviceImpl, localAddress, toBeWritten);
		assertEquals("Unexpected result from 'writeByte(...)'",
				10, writeByte);

		byte[] toBeWrittenBuffer = new byte[] { 47, 11 };
		when(I2C.i2cWriteBytesDirect(anyInt(), eq(DEVICE_ADDRESS), eq(2),
				eq(0), any(byte[].class))).thenReturn(10);
		int writeBytesDirect = bus.writeBytesDirect(deviceImpl, 2, 0, toBeWrittenBuffer);
		assertEquals("Unexpected result from 'writeBytesDirect(...)'",
				10, writeBytesDirect);
		
		when(I2C.i2cWriteBytes(anyInt(), eq(DEVICE_ADDRESS), eq(localAddress), eq(2),
				eq(0), any(byte[].class))).thenReturn(10);
		int writeBytes = bus.writeBytes(deviceImpl, localAddress, 2, 0, toBeWrittenBuffer);
		assertEquals("Unexpected result from 'writeBytes(...)'",
				10, writeBytes);
		
		when(I2C.i2cWriteAndReadBytes(anyInt(), eq(DEVICE_ADDRESS), eq(2), eq(0),
				any(byte[].class), eq(2), eq(0), any(byte[].class)))
				.thenReturn(10);
		int result = bus.writeAndReadBytesDirect(deviceImpl, 2, 0, toBeWrittenBuffer, 2, 0, buffer);
		assertEquals("Unexpected result from 'writeAndReadBytesDirect(...)'",
				10, result);

		// test read- and write-methods with null-device argument
		
		try {
			bus.readByteDirect(null);
			fail("calling 'readByteDirect(null, ...)' throw "
					+ "a NullPointerException but did not!");
		} catch (NullPointerException e) {
			// expected
		}
		try {
			bus.readByte(null, 0);
			fail("calling 'readByte(null, ...)' throw "
					+ "a NullPointerException but did not!");
		} catch (NullPointerException e) {
			// expected
		}
		try {
			bus.readBytesDirect(null, 0, 0, null);
			fail("calling 'readBytesDirect(null, ...)' throw "
					+ "a NullPointerException but did not!");
		} catch (NullPointerException e) {
			// expected
		}
		try {
			bus.readBytes(null, 0, 0, 0, null);
			fail("calling 'readBytes(null, ...)' throw "
					+ "a NullPointerException but did not!");
		} catch (NullPointerException e) {
			// expected
		}
		try {
			bus.writeByteDirect(null, Byte.MIN_VALUE);
			fail("calling 'writeByteDirect(null, ...)' throw "
					+ "a NullPointerException but did not!");
		} catch (NullPointerException e) {
			// expected
		}
		try {
			bus.writeByte(null, 0, Byte.MIN_VALUE);
			fail("calling 'writeByte(null, ...)' throw "
					+ "a NullPointerException but did not!");
		} catch (NullPointerException e) {
			// expected
		}
		try {
			bus.writeBytesDirect(null, 0, 0, null);
			fail("calling 'writeBytesDirect(null, ...)' throw "
					+ "a NullPointerException but did not!");
		} catch (NullPointerException e) {
			// expected
		}
		try {
			bus.writeBytes(null, 0, 0, 0, null);
			fail("calling 'writeBytesDirect(null, ...)' throw "
					+ "a NullPointerException but did not!");
		} catch (NullPointerException e) {
			// expected
		}
		try {
			bus.writeAndReadBytesDirect(null, 0, 0, null, 0, 0, null);
			fail("calling 'writeAndReadBytesDirect(null, ...)' throw "
					+ "a NullPointerException but did not!");
		} catch (NullPointerException e) {
			// expected
		}
				
	}
	
	@Test
	public void testConcurrency() throws Exception {
		
		// test locking
		
		bus.lock();
		
		try {
			bus.lock();
			fail("calling 'lock()' on the same thread for the second time did "
					+ "not throw a RuntimeException!");
		} catch (RuntimeException e) {
			// expected
		}
		
		final boolean[] lockDidNotBlock = new boolean[] { false };
		long now = System.currentTimeMillis();
		Thread testThread1 = new Thread() {
			public void run() {
				try {
					bus.lock();
					lockDidNotBlock[0] = true;
				} catch (InterruptedException e) {
					// expected
				}
			};
		};
		testThread1.start();
		testThread1.join();
		long diff = System.currentTimeMillis() - now;
		
		assertTrue("second attempt of calling 'lock()' did not throw an InterruptedException",
				!lockDidNotBlock[0]);
		assertTrue("second attempt of calling 'lock()' did return before "
				+ "configured timeout of 1 second!", diff > 100);

		// test unlocking
		
		final boolean[] unlockDidNotThrow = new boolean[] { false };
		Thread testThread2 = new Thread() {
			public void run() {
				try {
					bus.unlock();
					unlockDidNotThrow[0] = true;
				} catch (RuntimeException e) {
					// expected
				}
			};
		};
		testThread2.start();
		testThread2.join();

		assertTrue("calling 'unlock()' on a different thread did not throw a RuntimeException",
				!unlockDidNotThrow[0]);

		bus.unlock();
		
		try {
			bus.unlock();
			fail("calling 'unlock()' on the same thread for the second time did "
					+ "not throw a RuntimeException!");
		} catch (RuntimeException e) {
			// expected
		}
		
		// test lock-unlock and lock by another thread
		
		final Random rnd = new Random();

		System.out.println("Testing concurrency 0/50");
		for (int i = 1; i <= 50; ++i) {
			
			if (i % 10 == 0) {
				System.out.println("Testing concurrency "
					+ i + "/50");
			}
			
			bus.lock();
			
			final boolean[] lockDidThrow = new boolean[] { false, false };
			Thread testThread3 = new Thread() {
				public void run() {
					try {
						bus.lock();
					} catch (Exception e) {
						lockDidThrow[0] = true;
						e.printStackTrace();
					}
					try {
						Thread.sleep(rnd.nextInt(10));
					} catch (Exception e) {
						// never mind
					}
					try {
						bus.unlock();
					} catch (Exception e) {
						lockDidThrow[1] = true;
						e.printStackTrace();
					}
				};
			};
			testThread3.start();
			
			Thread.sleep(rnd.nextInt(90));
			
			bus.unlock();
			
			testThread3.join();
			
			assertTrue("calling 'lock()' on a different thread did throw an exception "
					+ "although the lock was released by the main-thread!",
					!lockDidThrow[0]);
			assertTrue("calling 'unlock()' on a different thread did throw an exception "
					+ "although the lock was owned by the other thread!",
					!lockDidThrow[1]);
			
		}
		
	}

}
