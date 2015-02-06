package com.pi4j.i2c.devices.microchip.potentiometers.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.pi4j.i2c.devices.microchip.potentiometers.impl.Channel;
import com.pi4j.i2c.devices.microchip.potentiometers.impl.DeviceControllerChannel;
import com.pi4j.i2c.devices.microchip.potentiometers.impl.DeviceControllerTerminalConfiguration;
import com.pi4j.i2c.devices.microchip.potentiometers.impl.DeviceStatus;
import com.pi4j.i2c.devices.microchip.potentiometers.impl.TerminalConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class MiscTest {

	@Test
	public void testChannel() {
		
		final String toStringA = Channel.A.toString();
		
		assertNotNull("result of 'Channel.A.toString()' is null!", toStringA);
		assertEquals("Unexpected result from calling 'Channel.A.toString()'!",
				"com.pi4j.i2c.devices.microchip.potentiometers.impl.Channel.A", toStringA);
		
		final String toStringB = Channel.B.toString();
		
		assertNotNull("result of 'Channel.B.toString()' is null!", toStringB);
		assertEquals("Unexpected result from calling 'Channel.B.toString()'!",
				"com.pi4j.i2c.devices.microchip.potentiometers.impl.Channel.B", toStringB);

	}
	
	@Test
	public void testDeviceStatus() {
		
		try {
			
			new DeviceStatus(false, true, null, true);
			fail("Got no RuntimeException on constructing "
					+ "a DeviceStatus using a null-wiperLockChannel");
			
		} catch (RuntimeException e) {
			// expected
		}
		
		final DeviceStatus deviceStatus = new DeviceStatus(
				false, true, Channel.A, true);
		
		// toString()
		
		final String toString = deviceStatus.toString();
		
		assertNotNull("result of 'toString()' is null!", toString);
		assertEquals("Unexpected result from calling 'toString()'!",
				"com.pi4j.i2c.devices.microchip.potentiometers.impl.DeviceStatus{\n"
				+ "  eepromWriteActive='false',\n"
				+ "  eepromWriteProtected='true',\n"
				+ "  wiperLockChannel='com.pi4j.i2c.devices.microchip.potentiometers.impl.Channel.A',\n"
				+ "  wiperLockActive='true'\n}", toString);
		
		// equals(...)

		final DeviceStatus copyOfDeviceStatus = new DeviceStatus(
				false, true, Channel.A, true);
		final DeviceStatus other1 = new DeviceStatus(
				true, true, Channel.A, true);
		final DeviceStatus other2 = new DeviceStatus(
				false, false, Channel.A, true);
		final DeviceStatus other3 = new DeviceStatus(
				false, true, Channel.B, true);
		final DeviceStatus other4 = new DeviceStatus(
				false, true, Channel.A, false);
		
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
			
			new TerminalConfiguration(null, false, true, false, true);
			fail("Got no RuntimeException on constructing "
					+ "a TerminalConfiguration using a null-channel");
			
		} catch (RuntimeException e) {
			// expected
		}
		
		final TerminalConfiguration tcon = new TerminalConfiguration(
				Channel.A, false, true, false, true);
		
		// toString()
		
		final String toString = tcon.toString();
		
		assertNotNull("result of 'toString()' is null!", toString);
		assertEquals("Unexpected result from calling 'toString()'!",
				"com.pi4j.i2c.devices.microchip.potentiometers.impl.TerminalConfiguration{\n"
				+ "  channel='com.pi4j.i2c.devices.microchip.potentiometers.impl.Channel.A',\n"
				+ "  channelEnabled='false',\n"
				+ "  pinAEnabled='true',\n"
				+ "  pinWEnabled='false',\n"
				+ "  pinBEnabled='true'\n}", toString);
		
		// equals(...)

		final TerminalConfiguration copyOfTerminalConfiguration
				= new TerminalConfiguration(
						Channel.A, false, true, false, true);
		final TerminalConfiguration other1 = new TerminalConfiguration(
				Channel.B, false, true, false, true);
		final TerminalConfiguration other2 = new TerminalConfiguration(
				Channel.A, true, true, false, true);
		final TerminalConfiguration other3 = new TerminalConfiguration(
				Channel.A, false, false, false, true);
		final TerminalConfiguration other4 = new TerminalConfiguration(
				Channel.A, false, true, true, true);
		final TerminalConfiguration other5 = new TerminalConfiguration(
				Channel.A, false, true, false, false);
		
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
