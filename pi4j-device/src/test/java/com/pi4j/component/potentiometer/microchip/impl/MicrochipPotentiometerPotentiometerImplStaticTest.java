package com.pi4j.component.potentiometer.microchip.impl;

import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerChannel;
import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerNonVolatileMode;
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
 * FILENAME      :  MicrochipPotentiometerPotentiometerImplStaticTest.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
public class MicrochipPotentiometerPotentiometerImplStaticTest {

	@Mock
	private I2CDevice i2cDevice;

	@Mock
	private I2CBus i2cBus;

	@Mock
	private MicrochipPotentiometerDeviceController controller;

	@Mock
	private MicrochipPotentiometerDeviceControllerFactory controllerFactory;

	/**
	 * publishes some internals for testing purposes
	 */
	static class TestablePotentiometer
			extends MicrochipPotentiometerBase {

		private boolean capableOfNonVolatileWiper = false;

		TestablePotentiometer(I2CBus i2cBus, boolean pinA0,
				boolean pinA1, boolean pinA2, MicrochipPotentiometerChannel channel,
                MicrochipPotentiometerNonVolatileMode nonVolatileMode,
				MicrochipPotentiometerDeviceControllerFactory controllerFactory)
				throws IOException {

			super(i2cBus, pinA0, pinA1, pinA2, channel,
					nonVolatileMode, 0, controllerFactory);

		}

		public TestablePotentiometer(I2CBus i2cBus,
				MicrochipPotentiometerChannel channel, MicrochipPotentiometerNonVolatileMode nonVolatileMode,
				int initialValueForVolatileWipers)
				throws IOException {

			super(i2cBus, false, false, false, channel, nonVolatileMode,
					initialValueForVolatileWipers);

		}

		public void initialize(final int initialValueForVolatileWipers) throws IOException {
			super.initialize(initialValueForVolatileWipers);
		}

		@Override
		public boolean isCapableOfNonVolatileWiper() {
			return capableOfNonVolatileWiper;
		}

		public void setCapableOfNonVolatileWiper(
				boolean capableOfNonVolatileWiper) {
			this.capableOfNonVolatileWiper = capableOfNonVolatileWiper;
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

	}

	@Test
	public void testCreation() throws IOException {

		// wrong parameters

		try {
			new TestablePotentiometer(null,
					false, false, false, MicrochipPotentiometerChannel.A,
                    MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, controllerFactory);
			fail("Got no RuntimeException on constructing "
					+ "a PotentiometerImpl using a null-I2CBus");
		} catch (RuntimeException e) {
			// expected expection
		}

		try {
			new TestablePotentiometer(i2cBus,
					false, false, false, null,
                    MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, controllerFactory);
			fail("Got no RuntimeException on constructing "
					+ "a PotentiometerImpl using a null-Channel");
		} catch (RuntimeException e) {
			// expected expection
		}

		try {
			new TestablePotentiometer(i2cBus,
					false, false, false, MicrochipPotentiometerChannel.C,
                    MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, controllerFactory);
			fail("Got no RuntimeException on constructing "
					+ "a PotentiometerImpl using a not supported channel");
		} catch (RuntimeException e) {
			// expected expection
		}

		try {
			new TestablePotentiometer(i2cBus,
					false, false, false, MicrochipPotentiometerChannel.A,
					null, controllerFactory);
			fail("Got no RuntimeException on constructing "
					+ "a PotentiometerImpl using a null-NonVolatileMode");
		} catch (RuntimeException e) {
			// expected expection
		}

		try {
			new TestablePotentiometer(i2cBus,
					false, false, false, MicrochipPotentiometerChannel.A, MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, null);
			fail("Got no RuntimeException on constructing "
					+ "a PotentiometerImpl using a null-controllerFactory");
		} catch (RuntimeException e) {
			// expected expection
		}

		// correct parameters

		new TestablePotentiometer(i2cBus,
				false, false, false, MicrochipPotentiometerChannel.A, MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY,
				controllerFactory);

		new TestablePotentiometer(i2cBus, MicrochipPotentiometerChannel.B,
                MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, 127);

	}

	@Test
	public void testBuildI2CAddress() throws IOException {

		int address1 = MicrochipPotentiometerBase.buildI2CAddress(false, false, false);
		assertEquals("'buildI2CAddress(false, false, false)' "
				+ "does not return '0b0101000'", 0b0101000, address1);

		int address2 = MicrochipPotentiometerBase.buildI2CAddress(true, false, false);
		assertEquals("'buildI2CAddress(true, false, false)' "
				+ "does not return '0b0101001'", 0b0101001, address2);

		int address3 = MicrochipPotentiometerBase.buildI2CAddress(true, true, false);
		assertEquals("'buildI2CAddress(true, true, false)' "
				+ "does not return '0b0101011'", 0b0101011, address3);

		int address4 = MicrochipPotentiometerBase.buildI2CAddress(true, true, true);
		assertEquals("'buildI2CAddress(true, true, true)' "
				+ "does not return '0b0101111'", 0b0101111, address4);

	}

	@Test
	public void testInitialization() throws IOException {

		final TestablePotentiometer poti
				= new TestablePotentiometer(i2cBus,
					false, false, false, MicrochipPotentiometerChannel.A, MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY,
					controllerFactory);

		reset(controller);

		poti.setCapableOfNonVolatileWiper(true);
		poti.initialize(0);

		// called with expected parameters
		verify(controller).getValue(
                DeviceControllerChannel.A
				, false);
		// only called with expected parameters
		verify(controller, times(1)).getValue(
				any(DeviceControllerChannel.class)
				, anyBoolean());
		// never called since non-volatile-wiper is true
		verify(controller, times(0)).setValue(
				any(DeviceControllerChannel.class)
				, anyInt(), anyBoolean());

		reset(controller);

		poti.setCapableOfNonVolatileWiper(false);
		poti.initialize(120);

		// called with expected parameters
		verify(controller).setValue(
                DeviceControllerChannel.A
				, 120, false);
		// only called with expected parameters
		verify(controller, times(1)).setValue(
				any(DeviceControllerChannel.class)
				, anyInt(), anyBoolean());
		// never called since non-volatile-wiper is true
		verify(controller, times(0)).getValue(
                DeviceControllerChannel.A
				, true);

	}

	@Test
	public void testToString() throws IOException {

		when(controller.toString()).thenReturn("ControllerMock");

		final String toString = new TestablePotentiometer(i2cBus, false, false, false,
				MicrochipPotentiometerChannel.A, MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, controllerFactory).toString();

		assertNotNull("result of 'toString()' is null!", toString);
		assertEquals("Unexpected result from calling 'toString'!",
				"com.pi4j.component.potentiometer.microchip.impl.MicrochipPotentiometerPotentiometerImplStaticTest$TestablePotentiometer{\n"
				+ "  channel='com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerChannel.A',\n"
				+ "  controller='ControllerMock',\n"
				+ "  nonVolatileMode='VOLATILE_ONLY',\n"
				+ "  currentValue='0'\n}",
				toString);

	}

	@Test
	public void testEquals() throws IOException {

		final TestablePotentiometer poti = new TestablePotentiometer(i2cBus, false, false, false,
				MicrochipPotentiometerChannel.A, MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, controllerFactory);
		final TestablePotentiometer copyOfPoti = new TestablePotentiometer(i2cBus, false, false, false,
				MicrochipPotentiometerChannel.A, MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, controllerFactory);

		final TestablePotentiometer other1 = new TestablePotentiometer(i2cBus, false, false, false,
				MicrochipPotentiometerChannel.B, MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, controllerFactory);
		final TestablePotentiometer other2 = new TestablePotentiometer(i2cBus, false, false, false,
				MicrochipPotentiometerChannel.A, MicrochipPotentiometerNonVolatileMode.NONVOLATILE_ONLY, controllerFactory);
		final TestablePotentiometer other3 = new TestablePotentiometer(i2cBus, false, false, false,
				MicrochipPotentiometerChannel.A, MicrochipPotentiometerNonVolatileMode.NONVOLATILE_ONLY, controllerFactory);
		other3.setCurrentValue(127);

		controller = mock(MicrochipPotentiometerDeviceController.class);
		when(controllerFactory.getController(any(I2CDevice.class)))
				.thenReturn(controller);
		final TestablePotentiometer other4 = new TestablePotentiometer(i2cBus, false, false, false,
				MicrochipPotentiometerChannel.A, MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY, controllerFactory);

		assertNotEquals("'poti.equals(null)' returns true!",
				poti, null);
		assertEquals("'poti.equals(poti) returns false!",
				poti, poti);
		assertNotEquals("'poti.equals(\"Test\")' returns true!",
				poti, "Test");
		assertEquals("'poti.equals(copyOfPoti)' returns false!",
				poti, copyOfPoti);
		assertNotEquals("'poti.equals(other1)' returns true!",
				poti, other1);
		assertEquals("'poti.equals(other2)' returns false!",
				poti, other2);
		assertEquals("'poti.equals(other3)' returns false!",
				poti, other3);
		assertNotEquals("'poti.equals(other4)' returns true!",
				poti, other4);

	}

}
