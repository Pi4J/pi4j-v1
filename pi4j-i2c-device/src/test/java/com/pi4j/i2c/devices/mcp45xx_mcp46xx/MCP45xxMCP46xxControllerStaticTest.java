package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
public class MCP45xxMCP46xxControllerStaticTest {

	@Mock
	private I2CDevice i2cDevice;

	@Test
	public void testCreation() throws IOException {
		
		// wrong parameter
		
		try {
			new MCP45xxMCP46xxController(null);
			fail("Got no RuntimeException on constructing "
					+ "a MCP45xxMCP46xxController using a null-i2cDevice");
		} catch (RuntimeException e) {
			// expected expection
		}

		// correct parameter
		
		new MCP45xxMCP46xxController(i2cDevice);

	}
	
}
