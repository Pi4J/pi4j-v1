package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pi4j.i2c.devices.mcp45xx_mcp46xx.MCP45xxMCP46xxPotentiometer.NonVolatileMode;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: I2C Device Abstractions
 * FILENAME      :  MCP45xxMCP46xxPotentiometerTest.java  
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
 * Test for abstract Pi4J-device for MCP45XX and MCP46XX ICs.
 * 
 * @see MCP45xxMCP46xxPotentiometer
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class MCP45xxMCP46xxPotentiometerStaticTest {
	
	@Mock
	private I2CDevice i2cDevice;
	
	@Mock
	private I2CBus i2cBus;
	
	@Mock
	private MCP45xxMCP46xxController controller;
	
	@Mock
	private MCP45xxMCP46xxControllerFactory controllerFactory;

	/**
	 * publishes some internals for testing purposes
	 */
	static class TestableMCP45xxMCP46xxPotentiometer
			extends MCP45xxMCP46xxPotentiometer {

		private boolean capableOfNonVolatileWiper = false;
		
		TestableMCP45xxMCP46xxPotentiometer(I2CBus i2cBus, boolean pinA0,
				boolean pinA1, boolean pinA2, Channel channel,
				NonVolatileMode nonVolatileMode,
				MCP45xxMCP46xxControllerFactory controllerFactory)
				throws IOException {
			super(i2cBus, pinA0, pinA1, pinA2, channel,
					nonVolatileMode, 0, controllerFactory);
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
			new TestableMCP45xxMCP46xxPotentiometer(null,
					false, false, false, Channel.A,
					NonVolatileMode.VOLATILE_ONLY, controllerFactory);
			fail("Got no RuntimeException on constructing "
					+ "a MCP45xxMCP46xxPotentiometer using a null-I2CBus");
		} catch (RuntimeException e) {
			// expected expection
		}
		
		try {
			new TestableMCP45xxMCP46xxPotentiometer(i2cBus,
					false, false, false, null,
					NonVolatileMode.VOLATILE_ONLY, controllerFactory);
			fail("Got no RuntimeException on constructing "
					+ "a MCP45xxMCP46xxPotentiometer using a null-Channel");
		} catch (RuntimeException e) {
			// expected expection
		}

		try {
			new TestableMCP45xxMCP46xxPotentiometer(i2cBus,
					false, false, false, Channel.A,
					null, controllerFactory);
			fail("Got no RuntimeException on constructing "
					+ "a MCP45xxMCP46xxPotentiometer using a null-NonVolatileMode");
		} catch (RuntimeException e) {
			// expected expection
		}
		
		try {
			new TestableMCP45xxMCP46xxPotentiometer(i2cBus,
					false, false, false, Channel.A, NonVolatileMode.VOLATILE_ONLY, null);
			fail("Got no RuntimeException on constructing "
					+ "a MCP45xxMCP46xxPotentiometer using a null-controllerFactory");
		} catch (RuntimeException e) {
			// expected expection
		}

		// correct parameters
		
		new TestableMCP45xxMCP46xxPotentiometer(i2cBus,
				false, false, false, Channel.A, NonVolatileMode.VOLATILE_ONLY,
				controllerFactory);
		
	}
	
	@Test
	public void testBuildI2CAddress() throws IOException {
		
		int address1 = MCP45xxMCP46xxPotentiometer.buildI2CAddress(false, false, false);
		assertEquals("'buildI2CAddress(false, false, false)' "
				+ "does not return '0b01010000'", 0b01010000, address1);

		int address2 = MCP45xxMCP46xxPotentiometer.buildI2CAddress(true, false, false);
		assertEquals("'buildI2CAddress(true, false, false)' "
				+ "does not return '0b01010010'", 0b01010010, address2);
		
		int address3 = MCP45xxMCP46xxPotentiometer.buildI2CAddress(true, true, false);
		assertEquals("'buildI2CAddress(true, true, false)' "
				+ "does not return '0b01010110'", 0b01010110, address3);

		int address4 = MCP45xxMCP46xxPotentiometer.buildI2CAddress(true, true, true);
		assertEquals("'buildI2CAddress(true, true, true)' "
				+ "does not return '0b01011110'", 0b01011110, address4);
		
	}
	
	@Test
	public void testInitialization() throws IOException {
		
		final TestableMCP45xxMCP46xxPotentiometer poti
				= new TestableMCP45xxMCP46xxPotentiometer(i2cBus,
					false, false, false, Channel.A, NonVolatileMode.VOLATILE_ONLY,
					controllerFactory);
		
		reset(controller);
		
		poti.setCapableOfNonVolatileWiper(true);
		poti.initialize(0);
		
		// called with expected parameters
		verify(controller).getValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.MCP45xxMCP46xxController.Channel.A
				, true);
		// only called with expected parameters
		verify(controller, times(1)).getValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.MCP45xxMCP46xxController.Channel.class)
				, anyBoolean());
		// never called since non-volatile-wiper is true
		verify(controller, times(0)).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.MCP45xxMCP46xxController.Channel.class)
				, anyInt(), anyBoolean());
		
		reset(controller);
		
		poti.setCapableOfNonVolatileWiper(false);
		poti.initialize(120);
		
		// called with expected parameters
		verify(controller).setValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.MCP45xxMCP46xxController.Channel.A
				, 120, false);
		// only called with expected parameters
		verify(controller, times(1)).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.MCP45xxMCP46xxController.Channel.class)
				, anyInt(), anyBoolean());
		// never called since non-volatile-wiper is true
		verify(controller, times(0)).getValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.MCP45xxMCP46xxController.Channel.A
				, true);

	}
	
}
