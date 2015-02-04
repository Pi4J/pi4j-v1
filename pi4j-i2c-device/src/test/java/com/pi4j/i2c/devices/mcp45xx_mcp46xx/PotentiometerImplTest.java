package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pi4j.i2c.devices.mcp45xx_mcp46xx.PotentiometerImpl.NonVolatileMode;
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
 * @see PotentiometerImpl
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class PotentiometerImplTest {
	
	private static int INITIAL_VALUE_A = 0;
	private static int INITIAL_VALUE_B = 100;

	@Mock
	private I2CDevice i2cDevice;
	
	@Mock
	private I2CBus i2cBus;
	
	@Mock
	private DeviceController controller;
	
	@Mock
	private DeviceControllerFactory controllerFactory;
	
	private TestableMCP45xxMCP46xxPotentiometer potiA;

	private TestableMCP45xxMCP46xxPotentiometer potiB;

	class TestableMCP45xxMCP46xxPotentiometer
			extends PotentiometerImpl {

		private boolean capableOfNonVolatileWiper;
		
		TestableMCP45xxMCP46xxPotentiometer(Channel channel,
				boolean capableOfNonVolatileWiper, int initialValue)
				throws IOException {
			super(i2cBus, false, false, false, channel,
					NonVolatileMode.VOLATILE_ONLY, initialValue, controllerFactory);
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
		
	}
	
	@Before
	public void initialize() throws IOException {
		
		when(i2cBus.getDevice(anyInt()))
				.thenReturn(i2cDevice);
		
		when(controllerFactory.getController(any(I2CDevice.class)))
				.thenReturn(controller);
		
		potiA = new TestableMCP45xxMCP46xxPotentiometer(
				Channel.A, true, INITIAL_VALUE_A);
		
		potiB = new TestableMCP45xxMCP46xxPotentiometer(
				Channel.B, false, INITIAL_VALUE_B);
		
	}
	
	@Test
	public void testDeviceStatus() throws IOException {

		boolean WRITE_ACTIVE = false;
		boolean WRITE_PROTECTED = true;
		boolean WIPER0_LOCKED = false;
		boolean WIPER1_LOCKED = true;
		
		// prepare controller-status mock
		
		DeviceController.DeviceStatus deviceStatusMock
				= mock(DeviceController.DeviceStatus.class);
		when(deviceStatusMock.isEepromWriteActive()).thenReturn(WRITE_ACTIVE);
		when(deviceStatusMock.isEepromWriteProtected()).thenReturn(WRITE_PROTECTED);
		when(deviceStatusMock.isChannelALocked()).thenReturn(WIPER0_LOCKED);
		when(deviceStatusMock.isChannelBLocked()).thenReturn(WIPER1_LOCKED);
		when(controller.getDeviceStatus())
				.thenReturn(deviceStatusMock);
		
		// test poti of channel A
		
		DeviceStatus deviceStatusA = potiA.getDeviceStatus();
		
		assertNotNull("Method 'getDeviceStatus()' returned null but "
				+ "expected a proper 'DeviceStatus'-instance!", deviceStatusA);
		assertEquals("Got unexpected write-active-flag",
				WRITE_ACTIVE, deviceStatusA.isEepromWriteActive());
		assertEquals("Got unexpected write-protected-flag",
				WRITE_PROTECTED, deviceStatusA.isEepromWriteProtected());
		assertEquals("Got unexpected write-locked-flag",
				WIPER0_LOCKED, deviceStatusA.isWiperLockActive());
		
		// test poti of channel B
		
		DeviceStatus deviceStatusB = potiB.getDeviceStatus();
		
		assertNotNull("Method 'getDeviceStatus()' returned null but "
				+ "expected a proper 'DeviceStatus'-instance!", deviceStatusB);
		assertEquals("Got unexpected write-active-flag",
				WRITE_ACTIVE, deviceStatusB.isEepromWriteActive());
		assertEquals("Got unexpected write-protected-flag",
				WRITE_PROTECTED, deviceStatusB.isEepromWriteProtected());
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
	public void testNonVolatileMode() {
		
		// null-test
		
		try {
			
			potiB.setNonVolatileMode(null);
			fail("Got no RuntimeException on calling 'setNonVolatileMode(null)'!");
			
		} catch (RuntimeException e) {
			// expected
		}
		
		// unsupported modes (potiB is not capable of non-volatile wipers)
		
		try {
			
			potiB.setNonVolatileMode(NonVolatileMode.NONVOLATILE_ONLY);
			fail("Got no RuntimeException on calling 'setNonVolatileMode(NONVOLATILE_ONLY)'!");
			
		} catch (RuntimeException e) {
			// expected
		}
		try {
			
			potiB.setNonVolatileMode(NonVolatileMode.VOLATILE_AND_NONVOLATILE);
			fail("Got no RuntimeException on calling 'setNonVolatileMode(VOLATILE_AND_NONVOLATILE)'!");
			
		} catch (RuntimeException e) {
			// expected
		}
		
		// supported modes

		potiA.setNonVolatileMode(NonVolatileMode.NONVOLATILE_ONLY);
		NonVolatileMode nonVolatileMode1 = potiA.getNonVolatileMode();
		
		assertEquals("After calling 'setNonVolatileMode(NONVOLATILE_ONLY)' the method "
				+ "'getNonVolatileMode()' does not return NONVOLATILE_ONLY!",
				NonVolatileMode.NONVOLATILE_ONLY, nonVolatileMode1);
			
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_ONLY);
		NonVolatileMode nonVolatileMode2 = potiA.getNonVolatileMode();
		
		assertEquals("After calling 'setNonVolatileMode(VOLATILE_ONLY)' the method "
				+ "'getNonVolatileMode()' does not return VOLATILE_ONLY!",
				NonVolatileMode.VOLATILE_ONLY, nonVolatileMode2);
		
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_AND_NONVOLATILE);
		NonVolatileMode nonVolatileMode3 = potiA.getNonVolatileMode();
		
		assertEquals("After calling 'setNonVolatileMode(VOLATILE_AND_NONVOLATILE)' the method "
				+ "'getNonVolatileMode()' does not return VOLATILE_AND_NONVOLATILE!",
				NonVolatileMode.VOLATILE_AND_NONVOLATILE, nonVolatileMode3);
			
		potiB.setNonVolatileMode(NonVolatileMode.VOLATILE_ONLY);
		NonVolatileMode nonVolatileMode4 = potiB.getNonVolatileMode();
		
		assertEquals("After calling 'setNonVolatileMode(VOLATILE_ONLY)' the method "
				+ "'getNonVolatileMode()' does not return VOLATILE_ONLY!",
				NonVolatileMode.VOLATILE_ONLY, nonVolatileMode4);
			
	}
	
	@Test
	public void testSetCurrentValue() throws IOException {
		
		// test RAM_ONLY
		
		reset(controller);
		
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_ONLY);
		potiA.setCurrentValue(50);
		
		// controller 'setValue' used to set value '50' on channel 'A' for volatile-wiper
		verify(controller).setValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A,
				50, false);
		// controller 'setValue' only used one time
		verify(controller).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt(), anyBoolean());
		
		int currentValue1 = potiA.getCurrentValue();
		assertEquals("Expected to get 50, previously set, on calling 'getCurrentValue()'!",
				50, currentValue1);
		
		// test EEPROM_ONLY
		
		reset(controller);
		
		potiA.setNonVolatileMode(NonVolatileMode.NONVOLATILE_ONLY);
		potiA.setCurrentValue(60);
		
		// controller 'setValue' used to set '60' on channel 'A' for non-volatile-wiper
		verify(controller).setValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A,
				60, true);
		// controller 'setValue' only used one time
		verify(controller).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt(), anyBoolean());
		
		int currentValue2 = potiA.getCurrentValue();
		assertEquals("Expected to get 50, since NonVolatileMode is NONVOLATILE_ONLY, "
				+ "on calling 'getCurrentValue()'!",
				50, currentValue2);
		
		// test RAM_AND_EEPROM
		
		reset(controller);
		
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_AND_NONVOLATILE);
		potiA.setCurrentValue(70);
		
		// controller 'setValue' used to set '70' on channel 'A' for non-volatile-wiper
		verify(controller).setValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A,
				70, true);
		// controller 'setValue' used to set '70' on channel 'A' for volatile-wiper
		verify(controller).setValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A,
				70, false);
		// controller 'setValue' used two times
		verify(controller, times(2)).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt(), anyBoolean());
		
		int currentValue3 = potiA.getCurrentValue();
		assertEquals("Expected to get 70, previously set, on calling 'getCurrentValue()'!",
				70, currentValue3);

		// test value below lower boundary
		
		reset(controller);
		
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_ONLY);
		potiA.setCurrentValue(-50);
		
		// controller 'setValue' used to set '0' on channel 'A' for volatile-wiper
		verify(controller).setValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A,
				0, false);
		// controller 'setValue' used one time
		verify(controller).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt(), anyBoolean());
		
		int currentValue4 = potiA.getCurrentValue();
		assertEquals("Expected to get 0, previously set, on calling 'getCurrentValue()'!",
				0, currentValue4);
		
		// test value above upper boundary
		
		reset(controller);
		
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_ONLY);
		potiA.setCurrentValue(400);
		
		// controller 'setValue' used to set '256' on channel 'A' for volatile-wiper
		verify(controller).setValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A,
				256, false);
		// controller 'setValue' used on time
		verify(controller).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt(), anyBoolean());
		
		int currentValue5 = potiA.getCurrentValue();
		assertEquals("Expected to get 256, previously set, on calling 'getCurrentValue()'!",
				256, currentValue5);
		
	}
	
	@Test
	public void testIncrease() throws IOException {
		
		// wrong parameters
		
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_AND_NONVOLATILE);
		try {
			
			potiA.increase();
			
		} catch (RuntimeException e) {
			// expected because only VOLATILE_ONLY is supported
		}
		potiA.setNonVolatileMode(NonVolatileMode.NONVOLATILE_ONLY);
		try {
			
			potiA.increase();
			
		} catch (RuntimeException e) {
			// expected because only VOLATILE_ONLY is supported
		}
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_ONLY);
		try {
			
			potiA.increase(-10);
			
		} catch (RuntimeException e) {
			// expected because only positive values are allowed
		}
		
		// success
		
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_ONLY);
		potiA.setCurrentValue(240);

		reset(controller);
		
		potiA.increase();
		
		// controller 'increase' used with '1' step on channel 'A' for volatile-wiper
		verify(controller).increase(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A, 1);
		// controller 'increase' used one time
		verify(controller).increase(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt());
		
		int currentValue1 = potiA.getCurrentValue();
		assertEquals("Expected to get 241 on calling 'getCurrentValue()'!",
				241, currentValue1);
		
		reset(controller);
		
		potiA.increase(2);
		
		// controller 'increase' used with '2' steps on channel 'A' for volatile-wiper
		verify(controller).increase(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A, 2);
		// controller 'increase' used on time
		verify(controller).increase(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt());
		
		int currentValue2 = potiA.getCurrentValue();
		assertEquals("Expected to get 243 on calling 'getCurrentValue()'!",
				243, currentValue2);
		
		reset(controller);
		
		potiA.increase(10);
		
		// controller 'setValue' used to set '253' on channel 'A' for volatile-wiper
		// instead of increase because for more than 5 steps using 'setValue' is "cheaper"
		verify(controller).setValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A,
				253, false);
		// controller 'setValue' used on time
		verify(controller).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt(), anyBoolean());
		// controller 'increase' is not used
		verify(controller, times(0)).increase(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt());

		reset(controller);

		potiA.increase(10);
		
		// controller 'setValue' used to set '256' on channel 'A' for volatile-wiper
		// instead of increase because this hits the upper boundary
		verify(controller).setValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A,
				256, false);
		// controller 'setValue' used on time
		verify(controller).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt(), anyBoolean());
		// controller 'increase' is not used
		verify(controller, times(0)).increase(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt());
		
		int currentValue3 = potiA.getCurrentValue();
		assertEquals("Expected to get 256 on calling 'getCurrentValue()'!",
				256, currentValue3);

		reset(controller);
		
		potiA.increase();
		
		verify(controller, times(0)).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt(), anyBoolean());
		verify(controller, times(0)).increase(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt());
		
		int currentValue4 = potiA.getCurrentValue();
		assertEquals("Expected to get 256 on calling 'getCurrentValue()'!",
				256, currentValue4);

	}
	
	@Test
	public void testDecrease() throws IOException {
		
		// wrong parameters
		
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_AND_NONVOLATILE);
		try {
			
			potiA.decrease();
			
		} catch (RuntimeException e) {
			// expected because only VOLATILE_ONLY is supported
		}
		potiA.setNonVolatileMode(NonVolatileMode.NONVOLATILE_ONLY);
		try {
			
			potiA.decrease();
			
		} catch (RuntimeException e) {
			// expected because only VOLATILE_ONLY is supported
		}
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_ONLY);
		try {
			
			potiA.decrease(-10);
			
		} catch (RuntimeException e) {
			// expected because only positive values are allowed
		}
		
		// success
		
		potiA.setNonVolatileMode(NonVolatileMode.VOLATILE_ONLY);
		potiA.setCurrentValue(10);

		reset(controller);
		
		potiA.decrease();
		
		verify(controller).decrease(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A, 1);
		verify(controller).decrease(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt());
		
		int currentValue1 = potiA.getCurrentValue();
		assertEquals("Expected to get 9 on calling 'getCurrentValue()'!",
				9, currentValue1);
		
		reset(controller);
		
		potiA.decrease(2);
		
		verify(controller).decrease(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A, 2);
		verify(controller).decrease(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt());
		
		int currentValue2 = potiA.getCurrentValue();
		assertEquals("Expected to get 7 on calling 'getCurrentValue()'!",
				7, currentValue2);
		
		reset(controller);
		
		potiA.decrease(20);
		
		verify(controller).setValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A,
				0, false);
		verify(controller).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt(), anyBoolean());
		verify(controller, times(0)).increase(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt());
		
		int currentValue3 = potiA.getCurrentValue();
		assertEquals("Expected to get 0 on calling 'getCurrentValue()'!",
				0, currentValue3);

		reset(controller);
		
		potiA.decrease();
		
		verify(controller, times(0)).setValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt(), anyBoolean());
		verify(controller, times(0)).increase(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyInt());
		
		int currentValue4 = potiA.getCurrentValue();
		assertEquals("Expected to get 0 on calling 'getCurrentValue()'!",
				0, currentValue4);
		
	}
	
	@Test
	public void testUpdateCacheFromDevice() throws IOException {
		
		potiA.setCurrentValue(50);
		int currentValue1 = potiA.getCurrentValue();
		assertEquals("Precondition for test fails: 'getCurrentValue()' should "
				+ "return 50!", 50, currentValue1);
		
		reset(controller);
		
		when(controller.getValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				eq(false))).thenReturn(40);
		when(controller.getValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				eq(true))).thenReturn(70);
		
		int currentValue2 = potiA.updateCacheFromDevice();
		
		verify(controller).getValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A, false);
		verify(controller).getValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
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
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyBoolean());
		
		when(controller.getValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				eq(false))).thenReturn(40);
		when(controller.getValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				eq(true))).thenReturn(70);
		
		int nonVolatileValue = potiA.getNonVolatileValue();

		verify(controller).getValue(
				com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A, true);
		verify(controller).getValue(
				any(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.class),
				anyBoolean());
		
		assertEquals("Did not get non-volatile-value on calling 'getNonVolatileValue()'",
				70, nonVolatileValue);
		
	}
	
}
