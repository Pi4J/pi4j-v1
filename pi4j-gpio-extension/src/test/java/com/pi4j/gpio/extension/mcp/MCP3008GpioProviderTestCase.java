package com.pi4j.gpio.extension.mcp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

public class MCP3008GpioProviderTestCase {

	private MCP3008GpioProvider mcpP3008Provider;

	@Mocked
	private SpiDevice spiDevice;
	@Mocked
	private SpiFactory spiFactory;

	private SpiChannel spiChannel = SpiChannel.CS0;
	private Pin inputPin = MCP3008Pin.CH0.pin;

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
