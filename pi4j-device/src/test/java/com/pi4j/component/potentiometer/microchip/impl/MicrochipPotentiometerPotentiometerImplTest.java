package com.pi4j.component.potentiometer.microchip.impl;

import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerChannel;
import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerDeviceStatus;
import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerNonVolatileMode;
import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerTerminalConfiguration;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerPotentiometerImplTest.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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

/**
 * Test for abstract Pi4J-device for MCP45XX and MCP46XX ICs.
 *
 * @see com.pi4j.component.potentiometer.microchip.impl.MicrochipPotentiometerBase
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class MicrochipPotentiometerPotentiometerImplTest {

	private static int INITIAL_VALUE_A = 0;
	private static int INITIAL_VALUE_B = 100;

	@Mock
	private I2CDevice i2cDevice;

	@Mock
	private I2CBus i2cBus;

	@Mock
	private MicrochipPotentiometerDeviceController controller;

	@Mock
	private MicrochipPotentiometerDeviceControllerFactory controllerFactory;

	private TestableMCP45xxMCP46xxPotentiometer potiA;

	private TestableMCP45xxMCP46xxPotentiometer potiB;

	class TestableMCP45xxMCP46xxPotentiometer
			extends MicrochipPotentiometerBase {

		private boolean capableOfNonVolatileWiper;

		TestableMCP45xxMCP46xxPotentiometer(MicrochipPotentiometerChannel channel,
				boolean capableOfNonVolatileWiper, int initialValue)
				throws IOException {
			super(i2cBus, false, false, false, channel,
					MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, initialValue, controllerFactory);
			this.capableOfNonVolatileWiper = capableOfNonVolatileWiper;
		}

		@Override
		public boolean isCapableOfNonVolatileWiper() {
			return capableOfNonVolatileWiper;
		}

		@Override
		public int getMaxValue() {
			return 256;
		}

		@Override
		public boolean isRheostat() {
			return false;
		}

		@Override
		public MicrochipPotentiometerChannel[] getSupportedChannelsByDevice() {
			return new MicrochipPotentiometerChannel[] { MicrochipPotentiometerChannel.A, MicrochipPotentiometerChannel.B };
		}

	}

	@Before
	public void initialize() throws IOException {

		when(i2cBus.getDevice(anyInt()))
				.thenReturn(i2cDevice);

		when(controllerFactory.getController(any(I2CDevice.class)))
				.thenReturn(controller);

		potiA = new TestableMCP45xxMCP46xxPotentiometer(
				MicrochipPotentiometerChannel.A, true, INITIAL_VALUE_A);

		potiB = new TestableMCP45xxMCP46xxPotentiometer(
				MicrochipPotentiometerChannel.B, false, INITIAL_VALUE_B);

	}

	@Test
	public void testDeviceStatus() throws IOException {

		boolean WRITE_ACTIVE = false;
		boolean WRITE_PROTECTED = true;
		boolean WIPER0_LOCKED = false;
		boolean WIPER1_LOCKED = true;

		// prepare controller-status mock

        DeviceControllerDeviceStatus deviceStatusMock
				= mock(DeviceControllerDeviceStatus.class);
		when(deviceStatusMock.isEepromWriteActive()).thenReturn(WRITE_ACTIVE);
		when(deviceStatusMock.isEepromWriteProtected()).thenReturn(WRITE_PROTECTED);
		when(deviceStatusMock.isChannelALocked()).thenReturn(WIPER0_LOCKED);
		when(deviceStatusMock.isChannelBLocked()).thenReturn(WIPER1_LOCKED);
		when(controller.getDeviceStatus())
				.thenReturn(deviceStatusMock);

		// test poti of channel A

		MicrochipPotentiometerDeviceStatus deviceStatusA = potiA.getDeviceStatus();

		assertNotNull("Method 'getDeviceStatus()' returned null but "
				+ "expected a proper 'DeviceStatus'-instance!", deviceStatusA);
		assertEquals("Got unexpected write-active-flag",
				WRITE_ACTIVE, deviceStatusA.isEepromWriteActive());
		assertEquals("Got unexpected write-protected-flag",
				WRITE_PROTECTED, deviceStatusA.isEepromWriteProtected());
		assertEquals("Got wrong channel in device-status",
				potiA.getChannel(), deviceStatusA.getWiperLockChannel());
		assertEquals("Got unexpected write-locked-flag",
				WIPER0_LOCKED, deviceStatusA.isWiperLockActive());

		// test poti of channel B

		MicrochipPotentiometerDeviceStatus deviceStatusB = potiB.getDeviceStatus();

		assertNotNull("Method 'getDeviceStatus()' returned null but "
				+ "expected a proper 'DeviceStatus'-instance!", deviceStatusB);
		assertEquals("Got unexpected write-active-flag",
				WRITE_ACTIVE, deviceStatusB.isEepromWriteActive());
		assertEquals("Got unexpected write-protected-flag",
				WRITE_PROTECTED, deviceStatusB.isEepromWriteProtected());
		assertEquals("Got wrong channel in device-status",
				potiB.getChannel(), deviceStatusB.getWiperLockChannel());
		assertEquals("Got unexpected write-locked-flag",
				WIPER1_LOCKED, deviceStatusB.isWiperLockActive());

	}

	@Test
	public void testGetCurrentValue() throws IOException {

		// test simple calls

		int potiAValue = potiA.getCurrentValue();
		assertEquals("Expected to get initial-value '"
				+ INITIAL_VALUE_A + "' on calling 'getCurrentValue()' "
				+ "after building an object-instance",
				INITIAL_VALUE_A, potiAValue);

		int potiBValue = potiB.getCurrentValue();
		assertEquals("Expected to get initial-value '"
				+ INITIAL_VALUE_B + "' on calling 'getCurrentValue()' "
				+ "after building an object-instance",
				INITIAL_VALUE_B, potiBValue);

		// repeatable calls

		int potiBValue1 = potiB.getCurrentValue();
		assertEquals("Expected to get initial-value '"
				+ INITIAL_VALUE_B + "' on calling 'getCurrentValue()' "
				+ "after building an object-instance",
				INITIAL_VALUE_B, potiBValue1);

		int potiBValue2 = potiB.getCurrentValue();
		assertEquals("Expected to get the same value on calling '"
				+ "getCurrentValue()' for the second time as returned "
				+ "at the first time!", potiBValue1, potiBValue2);

	}

	@Test
	public void testMicrochipPotentiometerNonVolatileMode() {

		// null-test

		try {

			potiB.setNonVolatileMode(null);
			fail("Got no RuntimeException on calling 'setNonVolatileMode(null)'!");

		} catch (RuntimeException e) {
			// expected
		}

		// unsupported modes (potiB is not capable of non-volatile wipers)

		try {

			potiB.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.NONVOLATILE_ONLY);
			fail("Got no RuntimeException on calling 'setNonVolatileMode(NONVOLATILE_ONLY)'!");

		} catch (RuntimeException e) {
			// expected
		}
		try {

			potiB.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_AND_NONVOLATILE);
			fail("Got no RuntimeException on calling 'setNonVolatileMode(VOLATILE_AND_NONVOLATILE)'!");

		} catch (RuntimeException e) {
			// expected
		}

		// supported modes

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.NONVOLATILE_ONLY);
		MicrochipPotentiometerNonVolatileMode nonVolatileMode1 = potiA.getNonVolatileMode();

		assertEquals("After calling 'setNonVolatileMode(NONVOLATILE_ONLY)' the method "
				+ "'getNonVolatileMode()' does not return NONVOLATILE_ONLY!",
				MicrochipPotentiometerNonVolatileMode.NONVOLATILE_ONLY, nonVolatileMode1);

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY);
		MicrochipPotentiometerNonVolatileMode nonVolatileMode2 = potiA.getNonVolatileMode();

		assertEquals("After calling 'setNonVolatileMode(VOLATILE_ONLY)' the method "
				+ "'getNonVolatileMode()' does not return VOLATILE_ONLY!",
				MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, nonVolatileMode2);

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_AND_NONVOLATILE);
		MicrochipPotentiometerNonVolatileMode nonVolatileMode3 = potiA.getNonVolatileMode();

		assertEquals("After calling 'setNonVolatileMode(VOLATILE_AND_NONVOLATILE)' the method "
				+ "'getNonVolatileMode()' does not return VOLATILE_AND_NONVOLATILE!",
				MicrochipPotentiometerNonVolatileMode.VOLATILE_AND_NONVOLATILE, nonVolatileMode3);

		potiB.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY);
		MicrochipPotentiometerNonVolatileMode nonVolatileMode4 = potiB.getNonVolatileMode();

		assertEquals("After calling 'setNonVolatileMode(VOLATILE_ONLY)' the method "
				+ "'getNonVolatileMode()' does not return VOLATILE_ONLY!",
				MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, nonVolatileMode4);

	}

	@Test
	public void testSetCurrentValue() throws IOException {

		// test RAM_ONLY

		reset(controller);

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY);
		potiA.setCurrentValue(50);

		// controller 'setValue' used to set value '50' on channel 'A' for volatile-wiper
		verify(controller).setValue(
                DeviceControllerChannel.A,
				50, false);
		// controller 'setValue' only used one time
		verify(controller).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());

		int currentValue1 = potiA.getCurrentValue();
		assertEquals("Expected to get 50, previously set, on calling 'getCurrentValue()'!",
				50, currentValue1);

		// test EEPROM_ONLY

		reset(controller);

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.NONVOLATILE_ONLY);
        potiA.setCurrentValue(60);

		// controller 'setValue' used to set '60' on channel 'A' for non-volatile-wiper
		verify(controller).setValue(
                DeviceControllerChannel.A,
				60, true);
		// controller 'setValue' only used one time
		verify(controller).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());

		int currentValue2 = potiA.getCurrentValue();
		assertEquals("Expected to get 50, since MicrochipPotentiometerNonVolatileMode is NONVOLATILE_ONLY, "
				+ "on calling 'getCurrentValue()'!",
				50, currentValue2);

		// test RAM_AND_EEPROM

		reset(controller);

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_AND_NONVOLATILE);
        potiA.setCurrentValue(70);

		// controller 'setValue' used to set '70' on channel 'A' for non-volatile-wiper
		verify(controller).setValue(
                DeviceControllerChannel.A,
				70, true);
		// controller 'setValue' used to set '70' on channel 'A' for volatile-wiper
		verify(controller).setValue(
                DeviceControllerChannel.A,
				70, false);
		// controller 'setValue' used two times
		verify(controller, times(2)).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());

		int currentValue3 = potiA.getCurrentValue();
		assertEquals("Expected to get 70, previously set, on calling 'getCurrentValue()'!",
				70, currentValue3);

		// test value below lower boundary

		reset(controller);

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY);
        potiA.setCurrentValue(-50);

		// controller 'setValue' used to set '0' on channel 'A' for volatile-wiper
		verify(controller).setValue(
                DeviceControllerChannel.A,
				0, false);
		// controller 'setValue' used one time
		verify(controller).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());

		int currentValue4 = potiA.getCurrentValue();
		assertEquals("Expected to get 0, previously set, on calling 'getCurrentValue()'!",
				0, currentValue4);

		// test value above upper boundary

		reset(controller);

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY);
        potiA.setCurrentValue(400);

		// controller 'setValue' used to set '256' on channel 'A' for volatile-wiper
		verify(controller).setValue(
                DeviceControllerChannel.A,
				256, false);
		// controller 'setValue' used on time
		verify(controller).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());

		int currentValue5 = potiA.getCurrentValue();
		assertEquals("Expected to get 256, previously set, on calling 'getCurrentValue()'!",
				256, currentValue5);

	}

	@Test
	public void testIncrease() throws IOException {

		// wrong parameters

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_AND_NONVOLATILE);
        try {

            potiA.increase();

		} catch (RuntimeException e) {
			// expected because only VOLATILE_ONLY is supported
		}
		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.NONVOLATILE_ONLY);
        try {

            potiA.increase();

		} catch (RuntimeException e) {
			// expected because only VOLATILE_ONLY is supported
		}
		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY);
        try {

            potiA.increase(-10);

		} catch (RuntimeException e) {
			// expected because only positive values are allowed
		}

		// success

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY);
        potiA.setCurrentValue(240);

		reset(controller);

		potiA.increase();

		// controller 'increase' used with '1' step on channel 'A' for volatile-wiper
		verify(controller).increase(
                DeviceControllerChannel.A, 1);
		// controller 'increase' used one time
		verify(controller).increase(
				any(DeviceControllerChannel.class),
				anyInt());

		int currentValue1 = potiA.getCurrentValue();
		assertEquals("Expected to get 241 on calling 'getCurrentValue()'!",
				241, currentValue1);

		reset(controller);

		potiA.increase(2);

		// controller 'increase' used with '2' steps on channel 'A' for volatile-wiper
		verify(controller).increase(
                DeviceControllerChannel.A, 2);
		// controller 'increase' used on time
		verify(controller).increase(
				any(DeviceControllerChannel.class),
				anyInt());

		int currentValue2 = potiA.getCurrentValue();
		assertEquals("Expected to get 243 on calling 'getCurrentValue()'!",
				243, currentValue2);

		reset(controller);

		potiA.increase(10);

		// controller 'setValue' used to set '253' on channel 'A' for volatile-wiper
		// instead of increase because for more than 5 steps using 'setValue' is "cheaper"
		verify(controller).setValue(
                DeviceControllerChannel.A,
				253, false);
		// controller 'setValue' used on time
		verify(controller).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());
		// controller 'increase' is not used
		verify(controller, times(0)).increase(
				any(DeviceControllerChannel.class),
				anyInt());

		reset(controller);

		potiA.increase(10);

		// controller 'setValue' used to set '256' on channel 'A' for volatile-wiper
		// instead of increase because this hits the upper boundary
		verify(controller).setValue(
                DeviceControllerChannel.A,
				256, false);
		// controller 'setValue' used on time
		verify(controller).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());
		// controller 'increase' is not used
		verify(controller, times(0)).increase(
				any(DeviceControllerChannel.class),
				anyInt());

		int currentValue3 = potiA.getCurrentValue();
		assertEquals("Expected to get 256 on calling 'getCurrentValue()'!",
				256, currentValue3);

		reset(controller);

		potiA.increase();

		verify(controller, times(0)).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());
		verify(controller, times(0)).increase(
				any(DeviceControllerChannel.class),
				anyInt());

		int currentValue4 = potiA.getCurrentValue();
		assertEquals("Expected to get 256 on calling 'getCurrentValue()'!",
				256, currentValue4);

	}

	@Test
	public void testDecrease() throws IOException {

		// wrong parameters

		potiA.setCurrentValue(10);

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_AND_NONVOLATILE);
        try {

            potiA.decrease();

		} catch (RuntimeException e) {
			// expected because only VOLATILE_ONLY is supported
		}
		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.NONVOLATILE_ONLY);
        try {

            potiA.decrease(10);

		} catch (RuntimeException e) {
			// expected because only VOLATILE_ONLY is supported
		}
		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY);
        try {

            potiA.decrease(-10);

		} catch (RuntimeException e) {
			// expected because only positive values are allowed
		}

		// success

		potiA.setNonVolatileMode(MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY);

        reset(controller);

		potiA.decrease();

		verify(controller).decrease(
                DeviceControllerChannel.A, 1);
		verify(controller).decrease(
				any(DeviceControllerChannel.class),
				anyInt());

		int currentValue1 = potiA.getCurrentValue();
		assertEquals("Expected to get 9 on calling 'getCurrentValue()'!",
				9, currentValue1);

		reset(controller);

		potiA.decrease(2);

		verify(controller).decrease(
                DeviceControllerChannel.A, 2);
		verify(controller).decrease(
				any(DeviceControllerChannel.class),
				anyInt());

		int currentValue2 = potiA.getCurrentValue();
		assertEquals("Expected to get 7 on calling 'getCurrentValue()'!",
				7, currentValue2);

		reset(controller);

		potiA.decrease(6);

		verify(controller).setValue(
                DeviceControllerChannel.A,
				1, false);
		verify(controller).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());
		verify(controller, times(0)).increase(
				any(DeviceControllerChannel.class),
				anyInt());

		int currentValue3 = potiA.getCurrentValue();
		assertEquals("Expected to get 1 on calling 'getCurrentValue()'!",
				1, currentValue3);

		reset(controller);

		potiA.decrease(20);

		verify(controller).setValue(
                DeviceControllerChannel.A,
				0, false);
		verify(controller).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());
		verify(controller, times(0)).increase(
				any(DeviceControllerChannel.class),
				anyInt());

		int currentValue4 = potiA.getCurrentValue();
		assertEquals("Expected to get 0 on calling 'getCurrentValue()'!",
				0, currentValue4);

		reset(controller);

		potiA.decrease();

		verify(controller, times(0)).setValue(
				any(DeviceControllerChannel.class),
				anyInt(), anyBoolean());
		verify(controller, times(0)).increase(
				any(DeviceControllerChannel.class),
				anyInt());

		int currentValue5 = potiA.getCurrentValue();
		assertEquals("Expected to get 0 on calling 'getCurrentValue()'!",
				0, currentValue5);

	}

	@Test
	public void testUpdateCacheFromDevice() throws IOException {

		potiA.setCurrentValue(50);
		int currentValue1 = potiA.getCurrentValue();
		assertEquals("Precondition for test fails: 'getCurrentValue()' should "
				+ "return 50!", 50, currentValue1);

		reset(controller);

		when(controller.getValue(
				any(DeviceControllerChannel.class),
				eq(false))).thenReturn(40);
		when(controller.getValue(
				any(DeviceControllerChannel.class),
				eq(true))).thenReturn(70);

		int currentValue2 = potiA.updateCacheFromDevice();

		verify(controller).getValue(
                DeviceControllerChannel.A, false);
		verify(controller).getValue(
				any(DeviceControllerChannel.class),
				anyBoolean());

		assertEquals("Did not get updated value by method 'updateCacheFromDevice()'",
				40, currentValue2);

		int currentValue3 = potiA.getCurrentValue();
		assertEquals("'getCurrentValue()' did not return updated value after "
				+ "calling 'updateCacheFromDevice()'", currentValue2, currentValue3);

	}

	@Test
	public void testGetNonVolatileValue() throws IOException {

		try {

			potiB.getNonVolatileValue();
			fail("Expected 'getNonVolatileValue()' to throw RuntimeException "
					+ "because potiB is not capable of non-volatile wipers!");

		} catch (RuntimeException e) {
			// expected since potiB is not capable of non-volatile wipers
		}

		verify(controller, times(0)).getValue(
				any(DeviceControllerChannel.class),
				anyBoolean());

		when(controller.getValue(
				any(DeviceControllerChannel.class),
				eq(false))).thenReturn(40);
		when(controller.getValue(
				any(DeviceControllerChannel.class),
				eq(true))).thenReturn(70);

		int nonVolatileValue = potiA.getNonVolatileValue();

		verify(controller).getValue(
				DeviceControllerChannel.A, true);
		verify(controller).getValue(
				any(DeviceControllerChannel.class),
				anyBoolean());

		assertEquals("Did not get non-volatile-value on calling 'getNonVolatileValue()'",
				70, nonVolatileValue);

	}

	@Test
	public void testGetTerminalConfiguration() throws IOException {

		// channel A poti

		final DeviceControllerTerminalConfiguration mockedTconA =
				new DeviceControllerTerminalConfiguration(
						DeviceControllerChannel.A,
						true, false, true, false);
		when(controller.getTerminalConfiguration(
				eq(DeviceControllerChannel.A))).thenReturn(mockedTconA);

		MicrochipPotentiometerTerminalConfiguration tconA = potiA.getTerminalConfiguration();

		assertNotNull("'getTerminalConfiguration()' return null but expected a "
				+ "properly filled object!", tconA);
		assertNotNull("'getTerminalConfiguration()' returned an object which "
				+ "method 'getChannel() returns null and not the poti's "
				+ "channel!", tconA.getChannel());
		assertEquals("'getTerminalConfiguration()' returned a configuration "
				+ "not matching the poti's channel!", MicrochipPotentiometerChannel.A, tconA.getChannel());
		assertEquals("'getTerminalConfiguration()' returned a configuration "
				+ "not matching the mocked controllers configuration 'channelEnabled'!",
				mockedTconA.isChannelEnabled(), tconA.isChannelEnabled());
		assertEquals("'getTerminalConfiguration()' returned a configuration "
				+ "not matching the mocked controllers configuration 'pinAEnabled'!",
				mockedTconA.isPinAEnabled(), tconA.isPinAEnabled());
		assertEquals("'getTerminalConfiguration()' returned a configuration "
				+ "not matching the mocked controllers configuration 'pinWEnabled'!",
				mockedTconA.isPinWEnabled(), tconA.isPinWEnabled());
		assertEquals("'getTerminalConfiguration()' returned a configuration "
				+ "not matching the mocked controllers configuration 'pinBEnabled'!",
				mockedTconA.isPinBEnabled(), tconA.isPinBEnabled());

		// channel B poti

		final DeviceControllerTerminalConfiguration mockedTconB =
				new DeviceControllerTerminalConfiguration(
						DeviceControllerChannel.B,
						false, true, false, true);
		when(controller.getTerminalConfiguration(
				eq(DeviceControllerChannel.B))).thenReturn(mockedTconB);

		MicrochipPotentiometerTerminalConfiguration tconB = potiB.getTerminalConfiguration();

		assertNotNull("'getTerminalConfiguration()' return null but expected a "
				+ "properly filled object!", tconB);
		assertNotNull("'getTerminalConfiguration()' returned an object which "
				+ "method 'getChannel() returns null and not the poti's "
				+ "channel!", tconB.getChannel());
		assertEquals("'getTerminalConfiguration()' returned a configuration "
				+ "not matching the poti's channel!", MicrochipPotentiometerChannel.B, tconB.getChannel());
		assertEquals("'getTerminalConfiguration()' returned a configuration "
				+ "not matching the mocked controllers configuration 'channelEnabled'!",
				mockedTconB.isChannelEnabled(), tconB.isChannelEnabled());
		assertEquals("'getTerminalConfiguration()' returned a configuration "
				+ "not matching the mocked controllers configuration 'pinAEnabled'!",
				mockedTconB.isPinAEnabled(), tconB.isPinAEnabled());
		assertEquals("'getTerminalConfiguration()' returned a configuration "
				+ "not matching the mocked controllers configuration 'pinWEnabled'!",
				mockedTconB.isPinWEnabled(), tconB.isPinWEnabled());
		assertEquals("'getTerminalConfiguration()' returned a configuration "
				+ "not matching the mocked controllers configuration 'pinBEnabled'!",
				mockedTconB.isPinBEnabled(), tconB.isPinBEnabled());

	}

	@Test
	public void testSetTerminalConfiguration() throws IOException {

		try {

			potiA.setTerminalConfiguration(null);
			fail("Expected 'setTerminalConfiguration(null)' to throw RuntimeException!");

		} catch (RuntimeException e) {
			// expected
		}
		try {

			potiA.setTerminalConfiguration(new MicrochipPotentiometerTerminalConfiguration(
					MicrochipPotentiometerChannel.B, true, true, true, true));
			fail("Expected 'setTerminalConfiguration(...)' to throw RuntimeException "
					+ "because the given configuration's channel does not match "
					+ "the potentiometers channel!");

		} catch (RuntimeException e) {
			// expected
		}

		// test poti A

		potiA.setTerminalConfiguration(new MicrochipPotentiometerTerminalConfiguration(
				MicrochipPotentiometerChannel.A, true, false, true, false));

		verify(controller).setTerminalConfiguration(new DeviceControllerTerminalConfiguration(
				DeviceControllerChannel.A, true, false, true, false));
		verify(controller).setTerminalConfiguration(any(DeviceControllerTerminalConfiguration.class));


	}

	@Test
	public void testSetWriteProtection() throws IOException {

		// enable lock

		potiA.setWriteProtection(true);

		verify(controller).setWriteProtection(true);
		verify(controller).setWriteProtection(anyBoolean());

		// disable lock

		reset(controller);

		potiA.setWriteProtection(false);

		verify(controller).setWriteProtection(false);
		verify(controller).setWriteProtection(anyBoolean());

	}

	@Test
	public void testSetWiperLock() throws IOException {

		// enable lock for poti A

		potiA.setWiperLock(true);

		verify(controller).setWiperLock(DeviceControllerChannel.A, true);
		verify(controller).setWiperLock(any(DeviceControllerChannel.class), anyBoolean());

		// disable lock for poti A

		reset(controller);

		potiA.setWiperLock(false);

		verify(controller).setWiperLock(DeviceControllerChannel.A, false);
		verify(controller).setWiperLock(any(DeviceControllerChannel.class), anyBoolean());

		// disable lock for poti B

		reset(controller);

		potiB.setWiperLock(false);

		verify(controller).setWiperLock(DeviceControllerChannel.B, false);
		verify(controller).setWiperLock(any(DeviceControllerChannel.class), anyBoolean());

		// disable lock for poti B

		reset(controller);

		potiB.setWiperLock(false);

		verify(controller).setWiperLock(DeviceControllerChannel.B, false);
		verify(controller).setWiperLock(any(DeviceControllerChannel.class), anyBoolean());

	}

	@Test
	public void testIsChannelSupportedByDevice() {

		boolean supported1 = potiA.isChannelSupportedByDevice(null);
		assertFalse("'isChannelSupported(null) must be false but wasn't!", supported1);

		boolean supported2 = potiA.isChannelSupportedByDevice(MicrochipPotentiometerChannel.A);
		assertTrue("'isChannelSupported(Channel.A) must be true but wasn't!", supported2);

		boolean supported3 = potiA.isChannelSupportedByDevice(MicrochipPotentiometerChannel.C);
		assertFalse("'isChannelSupported(Channel.C) must be false but wasn't!", supported3);

	}

}
