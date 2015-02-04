package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel;
import com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.DeviceStatus;
import com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.TerminalConfiguration;
import com.pi4j.io.i2c.I2CDevice;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: I2C Device Abstractions
 * FILENAME      :  MCP45xxMCP46xxControllerTest.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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

/**
 * Test for controller for MCP45XX and MCP46XX ICs.
 * 
 * @see DeviceController
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class DeviceControllerTest {

	@Mock
	private I2CDevice i2cDevice;

	private DeviceController controller;
	
	@Before
	public void initialize() throws IOException {
		
		controller = new DeviceController(i2cDevice);
		
	}
	
	@Test
	public void testGetDeviceStatus() throws IOException {
		
		// test io-exception
		
		try {
			
			controller.getDeviceStatus();
			
			fail("Excpected IOException (but there is no) because "
					+ "I2CDevice-mock is not initialized yet!");
			
		} catch (IOException e) {
			// expected
		}
		
		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice).write((byte) 0b1011100);
		// test for write was called only once
		verify(i2cDevice).write(anyByte());
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());

		// test success
		
		reset(i2cDevice);

		final int[] length = new int[1];
		
		// 0b1111111110101 -> Status-bits (see 4.2.2.1)
		mockReadResult(i2cDevice, (byte) 0b00011111, (byte) 0b11110101, length);
		
		DeviceStatus deviceStatus1 = controller.getDeviceStatus();

		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice).write((byte) 0b1011100);
		// test for write was called only once
		verify(i2cDevice).write(anyByte());
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());

		assertTrue("On calling 'getDeviceStatus' the method I2CDevice.read(...)"
				+ "is called with a byte-array as first argument. The length of this "
				+ "array must be at least 2 but was " + length[0], length[0] >= 2);
	
		assertEquals("Unexpected EEPROM-write-active-flag according to "
				+ "status-bits 0b1111111110101 (see 4.2.2.1)",
				Boolean.FALSE, deviceStatus1.isEepromWriteActive());
		assertEquals("Unexpected channel-B-locked-flag according to "
				+ "status-bits 0b1111111110101 (see 4.2.2.1)",
				Boolean.TRUE, deviceStatus1.isChannelBLocked());
		assertEquals("Unexpected channel-A-locked-flag according to "
				+ "status-bits 0b1111111110101 (see 4.2.2.1)",
				Boolean.FALSE, deviceStatus1.isChannelALocked());
		assertEquals("Unexpected EEPROM-write-protected-flag according to "
				+ "status-bits 0b1111111110101 (see 4.2.2.1)",
				Boolean.TRUE, deviceStatus1.isEepromWriteProtected());
		
		// test wrong answer from device
		
		reset(i2cDevice);
		
		// 0b0000000000000000 -> malformed result!
		mockReadResult(i2cDevice, (byte) 0b00000000, (byte) 0b000000000, length);
		
		try {
			
			controller.getDeviceStatus();
			
			fail("Excpected IOException (but there is no) because "
					+ "I2CDevice-mock returns malformed device-status!");
			
		} catch (IOException e) {
			// expected
		}
		
		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice).write((byte) 0b1011100);
		// test for write was called only once
		verify(i2cDevice).write(anyByte());
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());
		
		assertTrue("On calling 'getDeviceStatus' the method I2CDevice.read(...)"
				+ "is called with a byte-array as first argument. The length of this "
				+ "array must be at least 2 but was " + length[0], length[0] >= 2);
		
	}
	
	@Test
	public void testGetValue() throws IOException {
		
		try {

			controller.getValue(null, true);
			fail("Got no RuntimeException on calling 'getValue(null, ...)'!");
			
		} catch (RuntimeException e) {
			// expected
		}
		
		// test wiper 0 - volatile

		// 0b0000000000000000 -> 0
		mockReadResult(i2cDevice, (byte) 0b00000000, (byte) 0b00000000, (int[]) null);
		
		int currentValue = controller.getValue(Channel.A, false);
		
		assertEquals("Expected result of 'getCurrentValue(...)' as 0 "
				+ "but got " + currentValue, 0, currentValue);
		
		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice).write((byte) 0b0001100);
		// test for write was called only once
		verify(i2cDevice).write(anyByte());
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());

		// test wiper 1 - volatile

		reset(i2cDevice);
		
		// 0b0000000010000000 -> 128
		mockReadResult(i2cDevice, (byte) 0b00000000, (byte) 0b10000000, (int[]) null);

		currentValue = controller.getValue(Channel.B, false);

		assertEquals("Expected result of 'getCurrentValue(...)' as 128 "
				+ "but got " + currentValue, 128, currentValue);

		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice).write((byte) 0b0011100);
		// test for write was called only once
		verify(i2cDevice).write(anyByte());
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());
			
		// test wiper 0 - non-volatile

		reset(i2cDevice);

		// 0b0000000100000001 -> 257
		mockReadResult(i2cDevice, (byte) 0b00000001, (byte) 0b00000001, (int[]) null);
		
		currentValue = controller.getValue(Channel.A, true);
		
		assertEquals("Expected result of 'getCurrentValue(...)' as 257 "
				+ "but got " + currentValue, 257, currentValue);
		
		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice).write((byte) 0b0101100);
		// test for write was called only once
		verify(i2cDevice).write(anyByte());
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());

		// test wiper 1 - non-volatile

		reset(i2cDevice);
		
		// 0b0000000100000001 -> 257
		mockReadResult(i2cDevice, (byte) 0b00000001, (byte) 0b00000001, (int[]) null);
		
		currentValue = controller.getValue(Channel.B, true);
		
		assertEquals("Expected result of 'getCurrentValue(...)' as 257 "
				+ "but got " + currentValue, 257, currentValue);
		
		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice).write((byte) 0b0111100);
		// test for write was called only once
		verify(i2cDevice).write(anyByte());
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());
		
	}
	
	@Test
	public void testSetValue() throws IOException {
		
		try {

			controller.setValue(null, 1, true);
			fail("Got no RuntimeException on calling 'setValue(null, ...)'!");
			
		} catch (RuntimeException e) {
			// expected
		}
		try {

			controller.setValue(Channel.A, -1, true);
			fail("Got no RuntimeException on calling 'setValue(...)' using a negative value!");
			
		} catch (RuntimeException e) {
			// expected
		}
		
		// test wiper 0 - volatile
		
		controller.setValue(Channel.A, 0, false);
		
		// test for proper write-argument -> see FIGURE 7-2 and TABLE 4-1
		verify(i2cDevice).write(new byte[] { (byte) 0b0000000, (byte) 0b00000000 }, 0, 2);
		// test for write was called only once
		verify(i2cDevice).write(any(byte[].class), anyInt(), anyInt());
		
		// test wiper 1 - volatile

		reset(i2cDevice);
		
		controller.setValue(Channel.B, 1, false);
		
		// test for proper write-argument -> see FIGURE 7-2 and TABLE 4-1
		verify(i2cDevice).write(new byte[] { (byte) 0b0010000, (byte) 0b00000001 }, 0, 2);
		// 'write' is called on time
		verify(i2cDevice).write(any(byte[].class), anyInt(), anyInt());

		// test wiper 0 - non-volatile

		reset(i2cDevice);
		
		controller.setValue(Channel.A, 255, true);
		
		// test for proper write-argument -> see FIGURE 7-2 and TABLE 4-1
		verify(i2cDevice).write(new byte[] { (byte) 0b0100000, (byte) 0b11111111 }, 0, 2);
		// 'write' is called on time
		verify(i2cDevice).write(any(byte[].class), anyInt(), anyInt());

		// test wiper 1 - non-volatile

		reset(i2cDevice);
		
		controller.setValue(Channel.B, 256, true);
		
		// test for proper write-argument -> see FIGURE 7-2 and TABLE 4-1
		verify(i2cDevice).write(new byte[] { (byte) 0b0110001, (byte) 0b00000000 }, 0, 2);
		// 'write' is called on time
		verify(i2cDevice).write(any(byte[].class), anyInt(), anyInt());
		
	}
	
	@Test
	public void testIncrease() throws IOException {
		
		try {

			controller.increase(null, 1);
			fail("Got no RuntimeException on calling 'increase(null, ...)'!");
			
		} catch (RuntimeException e) {
			// expected
		}
		
		// zero-step increase
		
		controller.increase(Channel.A, 0);
		
		// 'write' called zero times
		verify(i2cDevice, times(0)).write(any(byte[].class), anyInt(), anyInt());
		
		// one-step increase
		
		reset(i2cDevice);
		
		controller.increase(Channel.A, 1);
		
		// test for proper write-argument -> see FIGURE 7-7 and TABLE 4-1
		verify(i2cDevice).write(new byte[] { (byte) 0b0000100 }, 0, 1);
		// 'write' called on time
		verify(i2cDevice).write(any(byte[].class), anyInt(), anyInt());

		// three-step increase
		
		reset(i2cDevice);
		
		controller.increase(Channel.B, 3);
		
		// test for proper write-argument -> see FIGURE 7-7 and TABLE 4-1
		verify(i2cDevice).write(new byte[] { (byte) 0b0010100,
				(byte) 0b0010100, (byte) 0b0010100 }, 0, 3);
		// 'write' called on time
		verify(i2cDevice).write(any(byte[].class), anyInt(), anyInt());
		
		// negative-steps increase
		
		reset(i2cDevice);
		
		controller.increase(Channel.A, -1);
		
		// test for proper write-argument -> see FIGURE 7-7 and TABLE 4-1
		verify(i2cDevice).write(new byte[] { (byte) 0b0001000 }, 0, 1);
		// 'write' called on time
		verify(i2cDevice).write(any(byte[].class), anyInt(), anyInt());
		
	}

	@Test
	public void testDecrease() throws IOException {
		
		try {

			controller.decrease(null, 1);
			fail("Got no RuntimeException on calling 'decrease(null, ...)'!");
			
		} catch (RuntimeException e) {
			// expected
		}
		
		// zero-step decrease
		
		controller.increase(Channel.A, 0);
		
		// 'write' called zero times
		verify(i2cDevice, times(0)).write(any(byte[].class), anyInt(), anyInt());

		// one-step decrease
		
		reset(i2cDevice);
		
		controller.decrease(Channel.A, 1);
		
		// test for proper write-argument -> see FIGURE 7-7 and TABLE 4-1
		verify(i2cDevice).write(new byte[] { (byte) 0b0001000 }, 0, 1);
		// 'write' called on time
		verify(i2cDevice).write(any(byte[].class), anyInt(), anyInt());

		// three-step decrease
		
		reset(i2cDevice);
		
		controller.decrease(Channel.B, 3);
		
		// test for proper write-argument -> see FIGURE 7-7 and TABLE 4-1
		verify(i2cDevice).write(new byte[] { (byte) 0b0011000,
				(byte) 0b0011000, (byte) 0b0011000 }, 0, 3);
		// 'write' called on time
		verify(i2cDevice).write(any(byte[].class), anyInt(), anyInt());
		
		// negative-steps decrease
		
		reset(i2cDevice);
		
		controller.decrease(Channel.A, -1);
		
		// test for proper write-argument -> see FIGURE 7-7 and TABLE 4-1
		verify(i2cDevice).write(new byte[] { (byte) 0b0000100 }, 0, 1);
		// 'write' called on time
		verify(i2cDevice).write(any(byte[].class), anyInt(), anyInt());
		
	}
	
	@Test
	public void testGetTerminalConfiguration() throws IOException {
		
		try {
			
			controller.getTerminalConfiguration(null);
			fail("Got no RuntimeException on calling 'getTerminalConfiguration(null)'!");
		
		} catch (RuntimeException e) {
			// expected
		}
		
		// 0b0111000011 -> TCON-bits (see 4.2.2.2)
		mockReadResult(i2cDevice, (byte) 0, (byte) 0b11100011, null);
		
		TerminalConfiguration tconA = controller.getTerminalConfiguration(Channel.A);

		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice).write((byte) 0b1001100);
		// test for write was called only once
		verify(i2cDevice).write(anyByte());
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());
		
		assertNotNull("Calling 'getTerminalConfiguration(Channel.A)' did return null!",
				tconA);
		assertEquals("Result of 'getTerminalConfiguration(Channel.A)' did not return '"
				+ "Channel.A' on calling 'getChannel()'!", Channel.A, tconA.getChannel());
		assertEquals("According to mocked read-result the channel should be disabled!",
				false, tconA.isChannelEnabled());
		assertEquals("According to mocked read-result the pin A should be disabled!",
				false, tconA.isPinAEnabled());
		assertEquals("According to mocked read-result the pin W should be enabled!",
				true, tconA.isPinWEnabled());
		assertEquals("According to mocked read-result the pin B should be enabled!",
				true, tconA.isPinBEnabled());

		TerminalConfiguration tconB = controller.getTerminalConfiguration(Channel.B);

		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice, times(2)).write((byte) 0b1001100);
		// test for write was called only once
		verify(i2cDevice, times(2)).write(anyByte());
		// test for read was called
		verify(i2cDevice, times(2)).read(any(byte[].class), anyInt(), anyInt());
		
		assertNotNull("Calling 'getTerminalConfiguration(Channel.B)' did return null!",
				tconB);
		assertEquals("Result of 'getTerminalConfiguration(Channel.B)' did not return '"
				+ "Channel.A' on calling 'getChannel()'!", Channel.B, tconB.getChannel());
		assertEquals("According to mocked read-result the channel should be enabled!",
				true, tconB.isChannelEnabled());
		assertEquals("According to mocked read-result the pin A should be enabled!",
				true, tconB.isPinAEnabled());
		assertEquals("According to mocked read-result the pin W should be enabled!",
				true, tconB.isPinWEnabled());
		assertEquals("According to mocked read-result the pin B should be disabled!",
				false, tconB.isPinBEnabled());
		
	}
	
	@Test
	public void testSetTerminalConfiguration() throws IOException {
		
		try {
			
			controller.setTerminalConfiguration(null);
			fail("Got no RuntimeException on calling 'setTerminalConfiguration(null)'!");
		
		} catch (RuntimeException e) {
			// expected
		}
		try {
			
			controller.setTerminalConfiguration(new TerminalConfiguration(
					null, false, false, false, false));
			fail("Got no RuntimeException on calling 'setTerminalConfiguration(tcon)' "
					+ " where tcon.getChannel() is null!");
		
		} catch (RuntimeException e) {
			// expected
		}
		
		// 0b0101010101 -> TCON-bits (see 4.2.2.2)
		mockReadResult(i2cDevice, (byte) 0, (byte) 0b0101010101, null);
		
		TerminalConfiguration tconA = new TerminalConfiguration(Channel.A,
				true, true, true, false);
		controller.setTerminalConfiguration(tconA);
		
		// reading current configuration:
		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice).write((byte) 0b1001100);
		// test for write was called only once
		verify(i2cDevice).write(anyByte());
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());
		
		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		// The first four bits of the second byte are the same four bits of
		// the mocked read-result. Those four bits represent the configuration
		// of wiper1. This test only modifies wiper0, so only the last four
		// bits have to be according to 'tconA'.
		verify(i2cDevice).write(new byte[] { (byte) 0b1000000, (byte) 0b01011110 }, 0, 2);

		TerminalConfiguration tconB = new TerminalConfiguration(Channel.B,
				false, false, false, true);
		controller.setTerminalConfiguration(tconB);
		
		// reading current configuration:
		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice, times(2)).write((byte) 0b1001100);
		// test for write was called only once
		verify(i2cDevice, times(2)).write(anyByte());
		// test for read was called
		verify(i2cDevice, times(2)).read(any(byte[].class), anyInt(), anyInt());
		
		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		// The last four bits of the second byte are the same four bits of
		// the mocked read-result. Those four bits represent the configuration
		// of wiper0. This test only modifies wiper1, so only the last four
		// bits have to be according to 'tconB'.
		verify(i2cDevice).write(new byte[] { (byte) 0b1000000, (byte) 0b00010101 }, 0, 2);
		
	}
	
	/**
	 * @param i2cDevice I2CDevice-mock
	 * @param first first byte of result
	 * @param second second byte of result
	 * @param The length of the buffer given to the read-method
	 * @throws IOException necessary because I2CDevice.read(...) throws an IOException
	 */
	private void mockReadResult(final I2CDevice i2cDevice,
			final byte first, final byte second, final int[] length) throws IOException {
		
		final int[] tmp;
		if (length == null) {
			tmp = new int[1];
		} else {
			tmp = length;
		}
		when(i2cDevice.read(any(byte[].class), anyInt(), anyInt()))
				.then(new Answer<Integer>() {
					 @Override
					public Integer answer(InvocationOnMock invocation)
							throws Throwable {
						
						// save given length for checking
						 tmp[0] = (int) invocation.getArguments()[2];
						
						if (tmp[0] >= 2) {
							
							byte[] buf = (byte[]) invocation.getArguments()[0];
							int offset = (int) invocation.getArguments()[1];
							
							// 0b1111111110101 -> Status-bits (see 4.2.2.1)
							buf[offset] = first;
							buf[offset + 1] = second;
							
						}
						
						return tmp[0] < 2 ? tmp[0] : 2;
						
					}
				});
		
	}
	
}
