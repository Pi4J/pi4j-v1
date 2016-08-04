package com.pi4j.io.i2c.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CBusImplTest.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.jni.I2C;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ I2C.class, I2CBusImpl.class })
public class I2CBusImplTest {

    private static int BUSNUMBER = 1;
    private static int FILEDESCRIPTOR = 4711;
    private static String FILENAME = "/dev/i2c-1";
    private static int DEVICE_ADDRESS = 0x15;

    private static long DEFAULT_TIMEOUT = I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS.toMillis(I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT);

    private static class TestableI2CBusImpl extends I2CBusImpl {

        protected TestableI2CBusImpl(final int busNumber, String fileName, final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit) throws UnsupportedBusNumberException, IOException {
            super(busNumber, fileName, lockAquireTimeout, lockAquireTimeoutUnit);
        }

        protected String getFilenameForBusnumber(int busNumber) throws UnsupportedBusNumberException {
            if (busNumber != BUSNUMBER) {
                throw new UnsupportedBusNumberException();
            }

            return FILENAME;
        }

    }

    private I2CBusImpl bus;

    @BeforeClass
    public static void warn() {

        System.out.println("The message 'Unable to load [libpi4j.so] using path: [/lib/libpi4j.so]' " + "including stacktrace to 'java.lang.UnsatisfiedLinkError' may be ignored safely! "
                + "This happens during initialization of mocking native classes (com.pi4j.jni.I2C).");

    }

    @Before
    public void setUp() throws Exception {

        PowerMockito.mockStatic(I2C.class);
        when(I2C.i2cOpen(anyString())).thenReturn(FILEDESCRIPTOR);
        when(I2C.i2cClose(anyInt())).thenReturn(0);

        bus = new TestableI2CBusImpl(BUSNUMBER, FILENAME, 100, TimeUnit.MILLISECONDS);
        bus.open();

    }

    @Test
    public void testBasics() throws Exception {

        I2CFactory.getInstance(BUSNUMBER);

        // test that I2C.i2cOpen was called during setup

        verifyStatic(times(2));
        I2C.i2cOpen(eq(FILENAME));

        // test for busnumber

        final int busNo = bus.getBusNumber();
        assertEquals("Got wrong busnumber from I2CBusImpl-instance!", BUSNUMBER, busNo);

        // test that I2C.i2cClose is called on I2CBus.close()

        bus.close();
        verifyStatic(times(1));
        I2C.i2cClose(eq(FILEDESCRIPTOR));

        // test for IOExceptions on using a bus already closed

        try {
            bus.readByteDirect(null);
            fail("calling 'readByteDirect(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.readByte(null, 0);
            fail("calling 'readByte(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.readBytesDirect(null, 0, 0, null);
            fail("calling 'readBytesDirect(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.readBytes(null, 0, 0, 0, null);
            fail("calling 'readBytes(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.writeByteDirect(null, Byte.MIN_VALUE);
            fail("calling 'writeByteDirect(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.writeByte(null, 0, Byte.MIN_VALUE);
            fail("calling 'writeByte(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.writeBytesDirect(null, 0, 0, null);
            fail("calling 'writeBytesDirect(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.writeBytes(null, 0, 0, 0, null);
            fail("calling 'writeBytes(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.writeAndReadBytesDirect(null, 0, 0, null, 0, 0, null);
            fail("calling 'writeAndReadBytesDirect(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }

    }

    @Test
    public void testDeviceInteractions() throws Exception {

        // test that retrieving a device calls the I2CDeviceImpl-constructor

        PowerMockito.whenNew(I2CDeviceImpl.class).withArguments(bus, DEVICE_ADDRESS).thenReturn(mock(I2CDeviceImpl.class));

        I2CDevice device = bus.getDevice(DEVICE_ADDRESS);
        assertNotNull("'I2CBus.getDevice(...)' did not return an device-instance", device);
        assertTrue("'I2CBus.getDevice(...)' does not return an instance of I2CDeviceImpl!", device instanceof I2CDeviceImpl);
        PowerMockito.verifyNew(I2CDeviceImpl.class, times(1)).withArguments(bus, DEVICE_ADDRESS);

        when(device.getAddress()).thenReturn(DEVICE_ADDRESS);

        I2CDeviceImpl deviceImpl = (I2CDeviceImpl) device;

        // test for required lock

//        try {
//            bus.readByteDirect(deviceImpl);
//            fail("Calling 'readByteDirect' did not throw an RuntimeException although bus was not locked!");
//        } catch (RuntimeException e) {
//            // expected
//        }
//        try {
//            bus.readBytesDirect(deviceImpl, 0, 0, null);
//            fail("Calling 'readBytesDirect' did not throw an RuntimeException although bus was not locked!");
//        } catch (RuntimeException e) {
//            // expected
//        }
//        try {
//            bus.readByte(deviceImpl, 4711);
//            fail("Calling 'readByteDirect' did not throw an RuntimeException although bus was not locked!");
//        } catch (RuntimeException e) {
//            // expected
//        }
//        try {
//            bus.readBytes(deviceImpl, 4711, 0, 0, null);
//            fail("Calling 'readBytes' did not throw an RuntimeException although bus was not locked!");
//        } catch (RuntimeException e) {
//            // expected
//        }
//        try {
//            bus.writeByteDirect(deviceImpl, (byte) 0);
//            fail("Calling 'writeByteDirect' did not throw an RuntimeException although bus was not locked!");
//        } catch (RuntimeException e) {
//            // expected
//        }
//        try {
//            bus.writeBytesDirect(deviceImpl, 0, 0, null);
//            fail("Calling 'writeBytesDirect' did not throw an RuntimeException although bus was not locked!");
//        } catch (RuntimeException e) {
//            // expected
//        }
//        try {
//            bus.writeByte(deviceImpl, 4711, (byte) 0);
//            fail("Calling 'writeByte' did not throw an RuntimeException although bus was not locked!");
//        } catch (RuntimeException e) {
//            // expected
//        }
//        try {
//            bus.writeBytes(deviceImpl, 4711, 0, 0, null);
//            fail("Calling 'writeBytes' did not throw an RuntimeException although bus was not locked!");
//        } catch (RuntimeException e) {
//            // expected
//        }
//        try {
//            bus.writeAndReadBytesDirect(deviceImpl, 0, 0, null, 0, 0, null);
//            fail("Calling 'writeAndReadBytesDirect' did not throw an RuntimeException although bus was not locked!");
//        } catch (RuntimeException e) {
//            // expected
//        }

        // test read-methods

        final Class<?> busClass = bus.getClass().getSuperclass();
//        final Method lock = busClass.getDeclaredMethod("lock");
//        lock.setAccessible(true);
//        lock.invoke(bus);

        when(I2C.i2cSlaveSelect(anyInt(), anyInt())).thenReturn(0);

        int byteToRead = 123;
        when(I2C.i2cReadByteDirect(anyInt())).thenReturn(byteToRead);
        int readByteDirect = bus.readByteDirect(deviceImpl);
        assertEquals("Unexpected result from 'I2CBusImpl.readByteDirect(...)'", byteToRead, readByteDirect);

        int localAddress = 815;
        when(I2C.i2cReadByte(anyInt(), eq(localAddress))).thenReturn(byteToRead);
        int readByte = bus.readByte(deviceImpl, localAddress);
        assertEquals("Unexpected result from 'I2CBusImpl.readByte(...)'", byteToRead, readByte);

        byte[] buffer = new byte[2];
        when(I2C.i2cReadBytes(anyInt(), eq(localAddress), eq(buffer.length), eq(0), eq(buffer))).thenReturn(buffer.length);
        int readBytes = bus.readBytes(deviceImpl, localAddress, buffer.length, 0, buffer);
        assertEquals("Unexpected result from 'I2CBusImpl.readBytes(...)'", buffer.length, readBytes);

        // test write-methods

        byte toBeWritten = 13;
        when(I2C.i2cWriteByteDirect(anyInt(), eq(toBeWritten))).thenReturn(10);
        int writeByteDirect = bus.writeByteDirect(deviceImpl, toBeWritten);
        assertEquals("Unexpected result from 'writeByteDirect(...)'", 10, writeByteDirect);

        when(I2C.i2cWriteByte(anyInt(), eq(localAddress), eq(toBeWritten))).thenReturn(10);
        int writeByte = bus.writeByte(deviceImpl, localAddress, toBeWritten);
        assertEquals("Unexpected result from 'writeByte(...)'", 10, writeByte);

        byte[] toBeWrittenBuffer = new byte[] { 47, 11 };
        when(I2C.i2cWriteBytesDirect(anyInt(), eq(2), eq(0), any(byte[].class))).thenReturn(10);
        int writeBytesDirect = bus.writeBytesDirect(deviceImpl, 2, 0, toBeWrittenBuffer);
        assertEquals("Unexpected result from 'writeBytesDirect(...)'", 10, writeBytesDirect);

        when(I2C.i2cWriteBytes(anyInt(), eq(localAddress), eq(2), eq(0), any(byte[].class))).thenReturn(10);
        int writeBytes = bus.writeBytes(deviceImpl, localAddress, 2, 0, toBeWrittenBuffer);
        assertEquals("Unexpected result from 'writeBytes(...)'", 10, writeBytes);

        when(I2C.i2cWriteAndReadBytes(anyInt(), eq(2), eq(0), any(byte[].class), eq(2), eq(0), any(byte[].class))).thenReturn(10);
        int result = bus.writeAndReadBytesDirect(deviceImpl, 2, 0, toBeWrittenBuffer, 2, 0, buffer);
        assertEquals("Unexpected result from 'writeAndReadBytesDirect(...)'", 10, result);

        // test read- and write-methods with null-device argument

        try {
            bus.readByteDirect(null);
            fail("calling 'readByteDirect(null, ...)' throw " + "a NullPointerException but did not!");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            bus.readByte(null, 0);
            fail("calling 'readByte(null, ...)' throw " + "a NullPointerException but did not!");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            bus.readBytesDirect(null, 0, 0, null);
            fail("calling 'readBytesDirect(null, ...)' throw " + "a NullPointerException but did not!");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            bus.readBytes(null, 0, 0, 0, null);
            fail("calling 'readBytes(null, ...)' throw " + "a NullPointerException but did not!");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            bus.writeByteDirect(null, Byte.MIN_VALUE);
            fail("calling 'writeByteDirect(null, ...)' throw " + "a NullPointerException but did not!");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            bus.writeByte(null, 0, Byte.MIN_VALUE);
            fail("calling 'writeByte(null, ...)' throw " + "a NullPointerException but did not!");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            bus.writeBytesDirect(null, 0, 0, null);
            fail("calling 'writeBytesDirect(null, ...)' throw " + "a NullPointerException but did not!");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            bus.writeBytes(null, 0, 0, 0, null);
            fail("calling 'writeBytesDirect(null, ...)' throw " + "a NullPointerException but did not!");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            bus.writeAndReadBytesDirect(null, 0, 0, null, 0, 0, null);
            fail("calling 'writeAndReadBytesDirect(null, ...)' throw " + "a NullPointerException but did not!");
        } catch (NullPointerException e) {
            // expected
        }
//
//        final Method unlock = busClass.getDeclaredMethod("unlock");
//        unlock.setAccessible(true);
//        unlock.invoke(bus);

    }

    @Test
    public void testConcurrency() throws Exception {

        // test simple locking

        long before1 = System.currentTimeMillis();
        boolean result1 = bus.runActionOnExclusivLockedBus(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
        long time1 = System.currentTimeMillis() - before1;
        assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' " + "was not run!", result1);
        assertTrue("It seems that the bus was locked because running the Runnable " + "took more time than expected!", time1 < DEFAULT_TIMEOUT);

        // test second attempt

        long before2 = System.currentTimeMillis();
        boolean result2 = bus.runActionOnExclusivLockedBus(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
        long time2 = System.currentTimeMillis() - before2;
        assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' " + "was not run!", result2);
        assertTrue("It seems that the bus was locked because running the Runnable " + "took more time than expected!", time2 < DEFAULT_TIMEOUT);

        // test lock-unlock and lock by another thread

        final boolean[] result3 = new boolean[] { false };
        Thread testThread1 = new Thread() {
            public void run() {
                try {
                    result3[0] = bus.runActionOnExclusivLockedBus(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                // never mind
                            }
                            return true;
                        }
                    });
                } catch (Throwable e) {
                    // expected
                }
            };
        };
        testThread1.start();

        Thread.sleep(10);
        long before3 = System.currentTimeMillis();
        boolean result4 = bus.runActionOnExclusivLockedBus(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Thread.sleep(20);
                return true;
            }
        });
        long time3 = System.currentTimeMillis() - before3;
        assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' " + "was not run!", result4);
        assertTrue("It seems that the bus was not locked because running the Runnable " + "took less time than expected (" + time3 + "ms)!", time3 > 10);

        testThread1.join();
        assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' " + "on a separat thread was not run!", result3[0]);

        // test lock-unlock and lock by another thread - getting no lock in time

        final boolean[] result7 = new boolean[] { false };
        Thread testThread3 = new Thread() {
            public void run() {
                try {
                    result7[0] = bus.runActionOnExclusivLockedBus(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                // never mind
                            }
                            return true;
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
        boolean result8 = false;
        try {
            result8 = bus.runActionOnExclusivLockedBus(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    Thread.sleep(20);
                    return true;
                }
            });
        } catch (Exception e) {
            // expected
        }
        long time5 = System.currentTimeMillis() - before5;
        assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' " + "was run but shouldn't!", !result8);
        assertTrue("It seems that the bus was not locked because running the Runnable " + "took less time than expected (" + time5 + "ms)!", time5 > 10);

        testThread3.join();
        assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' " + "on a separat thread was not run!", result7[0]);

        // test lock-unlock and lock by another thread using random delays

        final Random rnd = new Random();

        System.out.println("Testing concurrency 0/50");
        for (int i = 1; i <= 50; ++i) {

            if (i % 10 == 0) {
                System.out.println("Testing concurrency " + i + "/50");
            }

            final boolean[] result5 = new boolean[] { false };
            Thread testThread2 = new Thread() {
                public void run() {
                    try {
                        result5[0] = bus.runActionOnExclusivLockedBus(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                try {
                                    long tts = rnd.nextInt(50) + 20;
                                    Thread.sleep(tts);
                                } catch (InterruptedException e) {
                                    // never mind
                                }
                                return true;
                            }
                        });
                    } catch (Throwable e) {
                        // expected
                    }
                };
            };
            testThread2.start();

            Thread.sleep(10);
            long before4 = System.currentTimeMillis();
            boolean result6 = bus.runActionOnExclusivLockedBus(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    Thread.sleep(20);
                    return true;
                }
            });
            long time4 = System.currentTimeMillis() - before4;
            assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' " + "was not run!", result6);
            assertTrue("It seems that the bus was not locked because running the Runnable " + "took less time than expected (" + time4 + "ms)!", time4 > 10);

            testThread2.join();
            assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' " + "on a separat thread was not run!", result5[0]);

        }

        // test custom-code using I2CDevice.read() which leads to call
        // 'runActionOnExclusivLockedBus' byte the custom I2CRunnable and the
        // I2CRunnable of the read/write-methods of I2CDeviceImpl

        final boolean[] results = new boolean[] { false, false };
        long before11 = System.currentTimeMillis();
        results[0] = bus.runActionOnExclusivLockedBus(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                try {
                    long before12 = System.currentTimeMillis();
                    results[1] = bus.runActionOnExclusivLockedBus(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return true;
                        }
                    });
                    long time12 = System.currentTimeMillis() - before12;
                    assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' " + "was not run!", results[1]);
                    assertTrue("It seems that the bus was locked because running the Runnable " + "took more time than expected!", time12 < DEFAULT_TIMEOUT);
                } catch (IOException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return true;

            }
        });
        long time11 = System.currentTimeMillis() - before11;
        assertTrue("The Runnable given to 'I2CBus.runActionOnExclusivLockedBus(...)' " + "was not run!", results[0]);
        assertTrue("It seems that the bus was locked because running the Runnable " + "took more time than expected!", time11 < DEFAULT_TIMEOUT);

    }

}
