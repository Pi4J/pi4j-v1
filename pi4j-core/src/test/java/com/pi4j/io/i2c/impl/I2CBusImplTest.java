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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({ I2CBusImpl.class })
public class I2CBusImplTest {

    private static int BUSNUMBER = 1;
    @SuppressWarnings("unused")
	private static int FILEDESCRIPTOR = 4711;
    private static String FILENAME = "/dev/i2c-" + BUSNUMBER;
    private static int DEVICE_ADDRESS = 0x15;

    private static long DEFAULT_TIMEOUT = I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS.toMillis(I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT);

    private static class TestableI2CBusImpl extends I2CBusImpl {

        protected TestableI2CBusImpl(final int busNumber, String fileName, final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit) throws UnsupportedBusNumberException, IOException {
            super(busNumber, fileName, lockAquireTimeout, lockAquireTimeoutUnit);
        }

        @SuppressWarnings("unused")
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

        bus = new TestableI2CBusImpl(BUSNUMBER, FILENAME, 100, TimeUnit.MILLISECONDS);
        bus.open();

    }

    @Test
    public void testBasics() throws Exception {
        byte[] buffer = new byte[3];

        final int busNo = bus.getBusNumber();
        assertEquals("Got wrong busnumber from I2CBusImpl-instance!", BUSNUMBER, busNo);

        bus.close();

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
            bus.readBytesDirect(null, 0, 0, buffer);
            fail("calling 'readBytesDirect(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.readBytes(null, 0, 0, 0, buffer);
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
            bus.writeBytesDirect(null, 0, 0, buffer);
            fail("calling 'writeBytesDirect(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.writeBytes(null, 0, 0, 0, buffer);
            fail("calling 'writeBytes(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }
        try {
            bus.writeAndReadBytesDirect(null, 0, 0, buffer, 0, 0, buffer);
            fail("calling 'writeAndReadBytesDirect(...)' on a closed bus should throw " + "an IOException but did not!");
        } catch (IOException e) {
            // expected
        }

    }

    @Test
    public void testConcurrency() throws Exception {

        PowerMockito.whenNew(I2CDeviceImpl.class).withArguments(bus, DEVICE_ADDRESS).thenReturn(mock(I2CDeviceImpl.class));

        I2CDevice device = bus.getDevice(DEVICE_ADDRESS);
        assertNotNull("'I2CBus.getDevice(...)' did not return an device-instance", device);
        assertTrue("'I2CBus.getDevice(...)' does not return an instance of I2CDeviceImpl!", device instanceof I2CDeviceImpl);
        PowerMockito.verifyNew(I2CDeviceImpl.class, times(1)).withArguments(bus, DEVICE_ADDRESS);

        when(device.getAddress()).thenReturn(DEVICE_ADDRESS);

        I2CDeviceImpl deviceImpl = (I2CDeviceImpl) device;

        // test simple locking
        bus.open();

        long before1 = System.currentTimeMillis();
        boolean result1 = bus.runBusLockedDeviceAction(deviceImpl, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
        long time1 = System.currentTimeMillis() - before1;
        assertTrue("The Runnable given to 'I2CBus.runBusLockedDeviceAction(deviceImpl, ...)' " + "was not run!", result1);
        assertTrue("It seems that the bus was locked because running the Runnable " + "took more time than expected!", time1 < DEFAULT_TIMEOUT);

        // test second attempt

        long before2 = System.currentTimeMillis();
        boolean result2 = bus.runBusLockedDeviceAction(deviceImpl, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
        long time2 = System.currentTimeMillis() - before2;
        assertTrue("The Runnable given to 'I2CBus.runBusLockedDeviceAction(deviceImpl, ...)' " + "was not run!", result2);
        assertTrue("It seems that the bus was locked because running the Runnable " + "took more time than expected!", time2 < DEFAULT_TIMEOUT);

        // test lock-unlock and lock by another thread

        final boolean[] result3 = new boolean[] { false };
        Thread testThread1 = new Thread() {
            public void run() {
                try {
                    result3[0] = bus.runBusLockedDeviceAction(deviceImpl, new Callable<Boolean>() {
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
        boolean result4 = bus.runBusLockedDeviceAction(deviceImpl, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Thread.sleep(20);
                return true;
            }
        });
        long time3 = System.currentTimeMillis() - before3;
        assertTrue("The Runnable given to 'I2CBus.runBusLockedDeviceAction(deviceImpl, ...)' " + "was not run!", result4);
        assertTrue("It seems that the bus was not locked because running the Runnable " + "took less time than expected (" + time3 + "ms)!", time3 > 10);

        testThread1.join();
        assertTrue("The Runnable given to 'I2CBus.runBusLockedDeviceAction(deviceImpl, ...)' " + "on a separate thread was not run!", result3[0]);

        // test lock-unlock and lock by another thread - getting no lock in time

        final boolean[] result7 = new boolean[] { false };
        Thread testThread3 = new Thread() {
            public void run() {
                try {
                    result7[0] = bus.runBusLockedDeviceAction(deviceImpl, new Callable<Boolean>() {
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
            result8 = bus.runBusLockedDeviceAction(deviceImpl, new Callable<Boolean>() {
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
        assertTrue("The Runnable given to 'I2CBus.runBusLockedDeviceAction(deviceImpl, ...)' " + "was run but shouldn't!", !result8);
        assertTrue("It seems that the bus was not locked because running the Runnable " + "took less time than expected (" + time5 + "ms)!", time5 > 10);

        testThread3.join();
        assertTrue("The Runnable given to 'I2CBus.runBusLockedDeviceAction(deviceImpl, ...)' " + "on a separate thread was not run!", result7[0]);

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
                        result5[0] = bus.runBusLockedDeviceAction(deviceImpl, new Callable<Boolean>() {
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
            boolean result6 = bus.runBusLockedDeviceAction(deviceImpl, new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    Thread.sleep(20);
                    return true;
                }
            });
            long time4 = System.currentTimeMillis() - before4;
            assertTrue("The Runnable given to 'I2CBus.runBusLockedDeviceAction(deviceImpl, ...)' " + "was not run!", result6);
            assertTrue("It seems that the bus was not locked because running the Runnable " + "took less time than expected (" + time4 + "ms)!", time4 > 10);

            testThread2.join();
            assertTrue("The Runnable given to 'I2CBus.runBusLockedDeviceAction(deviceImpl, ...)' " + "on a separate thread was not run!", result5[0]);

        }

        // test custom-code using I2CDevice.read() which leads to call
        // 'runBusLockedDeviceAction' byte the custom I2CRunnable and the
        // I2CRunnable of the read/write-methods of I2CDeviceImpl

        final boolean[] results = new boolean[] { false, false };
        long before11 = System.currentTimeMillis();
        results[0] = bus.runBusLockedDeviceAction(deviceImpl, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                try {
                    long before12 = System.currentTimeMillis();
                    results[1] = bus.runBusLockedDeviceAction(deviceImpl, new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return true;
                        }
                    });
                    long time12 = System.currentTimeMillis() - before12;
                    assertTrue("The Runnable given to 'I2CBus.runBusLockedDeviceAction(deviceImpl, ...)' " + "was not run!", results[1]);
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
        assertTrue("The Runnable given to 'I2CBus.runBusLockedDeviceAction(deviceImpl, ...)' " + "was not run!", results[0]);
        assertTrue("It seems that the bus was locked because running the Runnable " + "took more time than expected!", time11 < DEFAULT_TIMEOUT);

    }

}
