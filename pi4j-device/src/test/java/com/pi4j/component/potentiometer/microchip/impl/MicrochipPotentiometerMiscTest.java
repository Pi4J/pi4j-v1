package com.pi4j.component.potentiometer.microchip.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerMiscTest.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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

import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerChannel;
import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerDeviceStatus;
import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerTerminalConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MicrochipPotentiometerMiscTest {

	@Test
	public void testChannel() {

		final String toStringA = MicrochipPotentiometerChannel.A.toString();

		assertNotNull("result of 'MicrochipPotentiometerChannel.A.toString()' is null!", toStringA);
		assertEquals("Unexpected result from calling 'MicrochipPotentiometerChannel.A.toString()'!",
				"com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerChannel.A", toStringA);

		final String toStringB = MicrochipPotentiometerChannel.B.toString();

		assertNotNull("result of 'Channel.B.toString()' is null!", toStringB);
		assertEquals("Unexpected result from calling 'MicrochipPotentiometerChannel.B.toString()'!",
				"com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerChannel.B", toStringB);

	}

	@Test
	public void testDeviceStatus() {

		try {

			new MicrochipPotentiometerDeviceStatusImpl(false, true, null, true);
			fail("Got no RuntimeException on constructing "
					+ "a DeviceStatus using a null-wiperLockChannel");

		} catch (RuntimeException e) {
			// expected
		}

		final MicrochipPotentiometerDeviceStatus deviceStatus = new MicrochipPotentiometerDeviceStatusImpl(
				false, true, MicrochipPotentiometerChannel.A, true);

		// toString()

		final String toString = deviceStatus.toString();

		assertNotNull("result of 'toString()' is null!", toString);
		assertEquals("Unexpected result from calling 'toString()'!",
				"com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerDeviceStatus{\n"
				+ "  eepromWriteActive='false',\n"
				+ "  eepromWriteProtected='true',\n"
				+ "  wiperLockChannel='com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerChannel.A',\n"
				+ "  wiperLockActive='true'\n}", toString);

		// equals(...)

		final MicrochipPotentiometerDeviceStatus copyOfDeviceStatus = new MicrochipPotentiometerDeviceStatusImpl(
				false, true, MicrochipPotentiometerChannel.A, true);
		final MicrochipPotentiometerDeviceStatus other1 = new MicrochipPotentiometerDeviceStatusImpl(
				true, true, MicrochipPotentiometerChannel.A, true);
		final MicrochipPotentiometerDeviceStatus other2 = new MicrochipPotentiometerDeviceStatusImpl(
				false, false, MicrochipPotentiometerChannel.A, true);
		final MicrochipPotentiometerDeviceStatus other3 = new MicrochipPotentiometerDeviceStatusImpl(
				false, true, MicrochipPotentiometerChannel.B, true);
		final MicrochipPotentiometerDeviceStatus other4 = new MicrochipPotentiometerDeviceStatusImpl(
				false, true, MicrochipPotentiometerChannel.A, false);

		assertNotEquals("'deviceStatus.equals(null)' returns true!",
				deviceStatus, null);
		assertEquals("'deviceStatus.equals(deviceStatus) returns false!",
				deviceStatus, deviceStatus);
		assertNotEquals("'deviceStatus.equals(\"Test\")' returns true!",
				deviceStatus, "Test");
		assertEquals("'deviceStatus.equals(copyOfDc)' returns false!",
				deviceStatus, copyOfDeviceStatus);
		assertNotEquals("'deviceStatus.equals(other1)' returns true!",
				deviceStatus, other1);
		assertNotEquals("'deviceStatus.equals(other2)' returns true!",
				deviceStatus, other2);
		assertNotEquals("'deviceStatus.equals(other3)' returns true!",
				deviceStatus, other3);
		assertNotEquals("'deviceStatus.equals(other4)' returns true!",
				deviceStatus, other4);

	}

	@Test
	public void testTerminalConfiguration() {

		try {

			new MicrochipPotentiometerTerminalConfiguration(null, false, true, false, true);
			fail("Got no RuntimeException on constructing "
					+ "a TerminalConfiguration using a null-channel");

		} catch (RuntimeException e) {
			// expected
		}

		final MicrochipPotentiometerTerminalConfiguration tcon = new MicrochipPotentiometerTerminalConfiguration(
				MicrochipPotentiometerChannel.A, false, true, false, true);

		// toString()

		final String toString = tcon.toString();

		assertNotNull("result of 'toString()' is null!", toString);
		assertEquals("Unexpected result from calling 'toString()'!",
				"com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerTerminalConfiguration{\n"
				+ "  channel='com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerChannel.A',\n"
				+ "  channelEnabled='false',\n"
				+ "  pinAEnabled='true',\n"
				+ "  pinWEnabled='false',\n"
				+ "  pinBEnabled='true'\n}", toString);

		// equals(...)

		final MicrochipPotentiometerTerminalConfiguration copyOfTerminalConfiguration
				= new MicrochipPotentiometerTerminalConfiguration(
						MicrochipPotentiometerChannel.A, false, true, false, true);
		final MicrochipPotentiometerTerminalConfiguration other1 = new MicrochipPotentiometerTerminalConfiguration(
				MicrochipPotentiometerChannel.B, false, true, false, true);
		final MicrochipPotentiometerTerminalConfiguration other2 = new MicrochipPotentiometerTerminalConfiguration(
				MicrochipPotentiometerChannel.A, true, true, false, true);
		final MicrochipPotentiometerTerminalConfiguration other3 = new MicrochipPotentiometerTerminalConfiguration(
				MicrochipPotentiometerChannel.A, false, false, false, true);
		final MicrochipPotentiometerTerminalConfiguration other4 = new MicrochipPotentiometerTerminalConfiguration(
				MicrochipPotentiometerChannel.A, false, true, true, true);
		final MicrochipPotentiometerTerminalConfiguration other5 = new MicrochipPotentiometerTerminalConfiguration(
				MicrochipPotentiometerChannel.A, false, true, false, false);

		assertNotEquals("'tcon.equals(null)' returns true!",
				tcon, null);
		assertEquals("'tcon.equals(tcon) returns false!",
				tcon, tcon);
		assertNotEquals("'tcon.equals(\"Test\")' returns true!",
				tcon, "Test");
		assertEquals("'tcon.equals(copyOfTerminalConfiguration)' returns false!",
				tcon, copyOfTerminalConfiguration);
		assertNotEquals("'tcon.equals(other1)' returns true!",
				tcon, other1);
		assertNotEquals("'tcon.equals(other2)' returns true!",
				tcon, other2);
		assertNotEquals("'tcon.equals(other3)' returns true!",
				tcon, other3);
		assertNotEquals("'tcon.equals(other4)' returns true!",
				tcon, other4);
		assertNotEquals("'tcon.equals(other5)' returns true!",
				tcon, other5);

	}

	@Test
	public void testDeviceControllerTerminalConfiguration() {

		final DeviceControllerTerminalConfiguration tcon
				= new DeviceControllerTerminalConfiguration(
                DeviceControllerChannel.A, false, true, false, true);

		final DeviceControllerTerminalConfiguration copyOfTerminalConfiguration
				= new DeviceControllerTerminalConfiguration(
                DeviceControllerChannel.A, false, true, false, true);
		final DeviceControllerTerminalConfiguration other1
				= new DeviceControllerTerminalConfiguration(
                DeviceControllerChannel.B, false, true, false, true);
		final DeviceControllerTerminalConfiguration other2
				= new DeviceControllerTerminalConfiguration(
                DeviceControllerChannel.A, true, true, false, true);
		final DeviceControllerTerminalConfiguration other3
				= new DeviceControllerTerminalConfiguration(
                DeviceControllerChannel.A, false, false, false, true);
		final DeviceControllerTerminalConfiguration other4
				= new DeviceControllerTerminalConfiguration(
                DeviceControllerChannel.A, false, true, true, true);
		final DeviceControllerTerminalConfiguration other5
				= new DeviceControllerTerminalConfiguration(
                DeviceControllerChannel.A, false, true, false, false);

		assertNotEquals("'tcon.equals(null)' returns true!",
				tcon, null);
		assertEquals("'tcon.equals(tcon) returns false!",
				tcon, tcon);
		assertNotEquals("'tcon.equals(\"Test\")' returns true!",
				tcon, "Test");
		assertEquals("'tcon.equals(copyOfTerminalConfiguration)' returns false!",
				tcon, copyOfTerminalConfiguration);
		assertNotEquals("'tcon.equals(other1)' returns true!",
				tcon, other1);
		assertNotEquals("'tcon.equals(other2)' returns true!",
				tcon, other2);
		assertNotEquals("'tcon.equals(other3)' returns true!",
				tcon, other3);
		assertNotEquals("'tcon.equals(other4)' returns true!",
				tcon, other4);
		assertNotEquals("'tcon.equals(other5)' returns true!",
				tcon, other5);

	}

}
