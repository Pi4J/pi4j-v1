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

		mcpP3008Provider = new MCP3008GpioProviderOld(spiChannel);
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
			result = mcpP3008Provider.getImmediateValue(inputPin);
		} catch (Exception e) {
			fail("No exception expected here, but got " + e);
			e.printStackTrace();
			return;
		}

		assertTrue(result < 0);
	}

//	@Test
//	public void testReadReturnsValid() throws IOException {
//		double result = mcpP3008Provider.getImmediateValue(inputPin);
//		assertEquals(511, result, 0.001);
//	}
//
//	@Test(expected = IOException.class)
//	public void testExceptionThrownDuringInitThrowsException() throws IOException {
//		new NonStrictExpectations() {
//			{
//				SpiFactory.getInstance(spiChannel);
//				result = new IOException("Some fake error");
//			}
//		};
//		new MCP3008GpioProviderOld(spiChannel);
//	}
}
