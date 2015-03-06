package com.pi4j.component.potentiometer.impl.microchip;

import com.pi4j.io.i2c.I2CDevice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerDeviceControllerStaticTest.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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
 * @see com.pi4j.component.potentiometer.impl.microchip.MicrochipPotentiometerDeviceController
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class MicrochipPotentiometerDeviceControllerStaticTest {

	@Mock
	private I2CDevice i2cDevice;

	@Test
	public void testCreation() throws IOException {
		
		// wrong parameter
		
		try {
			
			new MicrochipPotentiometerDeviceController(null);
			fail("Got no RuntimeException on constructing "
					+ "a DeviceController using a null-i2cDevice");
			
		} catch (RuntimeException e) {
			// expected expection
		}

		// correct parameter
		
		new MicrochipPotentiometerDeviceController(i2cDevice);

	}
	
	@Test
	public void testToString() throws IOException {
		
		when(i2cDevice.toString()).thenReturn("I2CDeviceMock");
		
		String toString = new MicrochipPotentiometerDeviceController(i2cDevice).toString();
		
		assertNotNull("result of 'toString()' is null!", toString);
		assertEquals("Unexpected result from calling 'toString'!",
				"com.pi4j.component.potentiometer.impl.microchip.MicrochipPotentiometerDeviceController{\n"
				+ "  i2cDevice='I2CDeviceMock'\n}",
				toString);
		
	}
	
	@Test
	public void testEquals() throws IOException {
		
		final MicrochipPotentiometerDeviceController deviceController = new MicrochipPotentiometerDeviceController(i2cDevice);
		final MicrochipPotentiometerDeviceController copyDeviceController = new MicrochipPotentiometerDeviceController(i2cDevice);

		final I2CDevice otherI2cDevice = mock(I2CDevice.class);
		final MicrochipPotentiometerDeviceController otherDeviceController = new MicrochipPotentiometerDeviceController(otherI2cDevice);
		
		assertNotEquals("'dc.equals(null)' returns true!",
				deviceController, null);
		assertEquals("'dc.equals(dc) returns false!",
				deviceController, deviceController);
		assertNotEquals("'dc.equals(\"Test\")' returns true!",
				deviceController, "Test");
		assertEquals("'dc.equals(copyOfDc)' returns false!",
				deviceController, copyDeviceController);
		assertNotEquals("'dc.equals(otherDc)' returns true!",
				deviceController, otherDeviceController);
		
	}
	
}
