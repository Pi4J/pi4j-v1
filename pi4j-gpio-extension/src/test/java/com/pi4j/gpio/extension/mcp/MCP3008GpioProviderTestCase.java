package com.pi4j.gpio.extension.mcp;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP3008GpioProviderTestCase.java  
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

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MCP3008GpioProviderTestCase {

	private MCP3008GpioProvider mcpP3008Provider;

	@Mocked
	private SpiDevice spiDevice;
	@Mocked
	private SpiFactory spiFactory;

	private SpiChannel spiChannel = SpiChannel.CS0;
	private Pin inputPin = MCP3008Pin.CH0;

	@Before
	public void setup() throws IOException {
		new NonStrictExpectations() {
			{
				SpiFactory.getInstance(spiChannel);
				result = spiDevice;

				spiDevice.write(anyShort, anyShort, anyShort);
				result = new short[] { 0, 0b01, 0b11111111 };
			}
		};

		mcpP3008Provider = new MCP3008GpioProvider(spiChannel);
	}

	@Test
	public void testGetValueReturnsInvalidIfReadFails() throws IOException {
		new NonStrictExpectations() {
			{
				spiDevice.write(anyShort, anyShort, anyShort);
				result = new IOException("Some fake error");
			}
		};
		double result;
		try {
			result = mcpP3008Provider.getValue(inputPin);
		} catch (Exception e) {
			fail("No exception expected here, but got " + e);
			e.printStackTrace();
			return;
		}

		assertTrue(result < 0);
	}

	@Test
	public void testReadReturnsValid() {
		double result = mcpP3008Provider.getValue(inputPin);
		assertEquals(511, result, 0.001);
	}

	@Test(expected = IOException.class)
	public void testExceptionThrownDuringInitThrowsException() throws IOException {
		new NonStrictExpectations() {
			{
				SpiFactory.getInstance(spiChannel);
				result = new IOException("Some fake error");
			}
		};
		new MCP3008GpioProvider(spiChannel);
	}
}
