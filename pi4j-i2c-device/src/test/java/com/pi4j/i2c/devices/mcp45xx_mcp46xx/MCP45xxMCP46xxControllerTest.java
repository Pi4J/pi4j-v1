package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.pi4j.i2c.devices.mcp45xx_mcp46xx.MCP45xxMCP46xxController.Channel;
import com.pi4j.i2c.devices.mcp45xx_mcp46xx.MCP45xxMCP46xxController.DeviceStatus;
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
 * @see MCP45xxMCP46xxController
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class MCP45xxMCP46xxControllerTest {

	@Mock
	private I2CDevice i2cDevice;

	private MCP45xxMCP46xxController controller;
	
	@Before
	public void initialize() throws IOException {
		
		controller = new MCP45xxMCP46xxController(i2cDevice);
		
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
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());

		assertTrue("On calling 'getDeviceStatus' the method I2CDevice.read(...)"
				+ "is called with a byte-array as first argument. The length of this "
				+ "array must be at least 2 but was " + length[0], length[0] >= 2);
	
		assertEquals("Unexpected EEPROM-write-active-flag according to "
				+ "status-bits 0b1111111110101 (see 4.2.2.1)",
				Boolean.FALSE, deviceStatus1.isEepromWriteActive());
		assertEquals("Unexpected wiper0-locked-flag according to "
				+ "status-bits 0b1111111110101 (see 4.2.2.1)",
				Boolean.TRUE, deviceStatus1.isWiper1Locked());
		assertEquals("Unexpected wiper1-locked-flag according to "
				+ "status-bits 0b1111111110101 (see 4.2.2.1)",
				Boolean.FALSE, deviceStatus1.isWiper0Locked());
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
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());
		
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
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());

		// test wiper 1 - volatile

		reset(i2cDevice);
		
		// 0b0000000100000001 -> 257
		mockReadResult(i2cDevice, (byte) 0b00000001, (byte) 0b00000001, (int[]) null);
		
		currentValue = controller.getValue(Channel.B, true);
		
		assertEquals("Expected result of 'getCurrentValue(...)' as 257 "
				+ "but got " + currentValue, 257, currentValue);
		
		// test for proper write-argument -> see FIGURE 7-5 and TABLE 4-1
		verify(i2cDevice).write((byte) 0b0111100);
		// test for read was called
		verify(i2cDevice).read(any(byte[].class), anyInt(), anyInt());
		
	}
	
	@Test
	public void testIncrease() throws IOException {
		
		try {

			controller.getValue(null, true);
			fail("Got no RuntimeException on calling 'getValue(null, ...)'!");
			
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

			controller.getValue(null, true);
			fail("Got no RuntimeException on calling 'getValue(null, ...)'!");
			
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
