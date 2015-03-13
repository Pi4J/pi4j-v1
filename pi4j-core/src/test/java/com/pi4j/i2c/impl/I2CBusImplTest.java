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
import com.pi4j.io.i2c.I2CFactory;
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
		
		long DEFAULT_TIMEOUT = I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS.toMillis(
				I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT);
		
		// test simple locking
		
		final boolean[] run1 = new boolean[] { false };
		long before1 = System.currentTimeMillis();
		bus.runActionOnExclusivLockedBus(new Runnable() {
			@Override
			public void run() {
				run1[0] = true;
			}
		});
		long time1 = System.currentTimeMillis() - before1;
		assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' "
				+ "was not run!", run1[0]);
		assertTrue("It seems that the bus was locked because running the Runnable "
				+ "took more time than expected!", time1 < DEFAULT_TIMEOUT);
		
		// test second attempt
		
		final boolean[] run2 = new boolean[] { false };
		long before2 = System.currentTimeMillis();
		bus.runActionOnExclusivLockedBus(new Runnable() {
			@Override
			public void run() {
				run2[0] = true;
			}
		});
		long time2 = System.currentTimeMillis() - before2;
		assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' "
				+ "was not run!", run2[0]);
		assertTrue("It seems that the bus was locked because running the Runnable "
				+ "took more time than expected!", time2 < DEFAULT_TIMEOUT);
		
		// test lock-unlock and lock by another thread
		
		final boolean[] run3 = new boolean[] { false };
		Thread testThread1 = new Thread() {
			public void run() {
				try {
					bus.runActionOnExclusivLockedBus(new Runnable() {
						@Override
						public void run() {
							run3[0] = true;
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// never mind
							}
						}
					});
				} catch (Throwable e) {
					// expected
				}
			};
		};
		testThread1.start();
		
		Thread.sleep(10);
		final boolean[] run4 = new boolean[] { false };
		long before3 = System.currentTimeMillis();
		bus.runActionOnExclusivLockedBus(new Runnable() {
			@Override
			public void run() {
				run4[0] = true;
			}
		});
		long time3 = System.currentTimeMillis() - before3;
		assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' "
				+ "was not run!", run4[0]);
		assertTrue("It seems that the bus was not locked because running the Runnable "
				+ "took less time than expected (" + time3 + "ms)!", time3 > 10);
		
		testThread1.join();
		assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' "
				+ "on a separat thread was not run!", run3[0]);

		// test lock-unlock and lock by another thread - getting no lock in time
		
		final boolean[] run7 = new boolean[] { false };
		Thread testThread3 = new Thread() {
			public void run() {
				try {
					bus.runActionOnExclusivLockedBus(new Runnable() {
						@Override
						public void run() {
							run7[0] = true;
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								// never mind
							}
						}
					});
				} catch (Throwable e) {
					// expected
				}
			};
		};
		testThread3.start();
		
		Thread.sleep(10);
		long before5 = System.currentTimeMillis();
		final boolean[] run8 = new boolean[] { false };
		try {
			bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
					run8[0] = true;
				}
			});
			run8[0] = true;
		} catch (Exception e) {
			// expected
		}
		long time5 = System.currentTimeMillis() - before5;
		assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' "
				+ "was run but shouldn't!", !run8[0]);
		assertTrue("It seems that the bus was not locked because running the Runnable "
				+ "took less time than expected (" + time5 + "ms)!", time5 > 10);
		
		testThread3.join();
		assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' "
				+ "on a separat thread was not run!", run7[0]);

		// test lock-unlock and lock by another thread using random delays
		
		final Random rnd = new Random();

		System.out.println("Testing concurrency 0/50");
		for (int i = 1; i <= 50; ++i) {
			
			if (i % 10 == 0) {
				System.out.println("Testing concurrency "
					+ i + "/50");
			}
			
			final boolean[] run5 = new boolean[] { false };
			Thread testThread2 = new Thread() {
				public void run() {
					try {
						bus.runActionOnExclusivLockedBus(new Runnable() {
							@Override
							public void run() {
								run5[0] = true;
								try {
									long tts = rnd.nextInt(50) + 20;
									Thread.sleep(tts);
								} catch (InterruptedException e) {
									// never mind
								}
							}
						});
					} catch (Throwable e) {
						// expected
					}
				};
			};
			testThread2.start();
			
			Thread.sleep(10);
			final boolean[] run6 = new boolean[] { false };
			long before4 = System.currentTimeMillis();
			bus.runActionOnExclusivLockedBus(new Runnable() {
				@Override
				public void run() {
					run6[0] = true;
				}
			});
			long time4 = System.currentTimeMillis() - before4;
			assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' "
					+ "was not run!", run6[0]);
			assertTrue("It seems that the bus was not locked because running the Runnable "
					+ "took less time than expected (" + time4 + "ms)!", time4 > 10);
			
			testThread2.join();
			assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' "
					+ "on a separat thread was not run!", run5[0]);
			
		}
		
	}

}
