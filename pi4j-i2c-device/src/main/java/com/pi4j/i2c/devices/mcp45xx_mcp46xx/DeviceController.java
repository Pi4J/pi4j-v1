package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

import java.io.IOException;
import java.util.Arrays;

import com.pi4j.io.i2c.I2CDevice;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: I2C Device Abstractions
 * FILENAME      :  MCP45xxMCP46xxController.java  
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
 * Ported from <a href="http://blog.stibrany.com/?p=9">Stibro's code blog</a>.
 * 
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public class DeviceController {
	
	// for 'nonVolatile' parameters
	
	public static final boolean VOLATILE_WIPER = false;
	public static final boolean NONVOLATILE_WIPER = true;
	
	// register memory addresses (see TABLE 4-1)
	
	static final byte MEMADDR_WIPER0 = 0x00;
	static final byte MEMADDR_WIPER1 = 0x01;
	static final byte MEMADDR_WIPER0_NV = 0x02;
	static final byte MEMADDR_WIPER1_NV = 0x03;
	private static final byte MEMADDR_TCON = 0x04; // terminal control
	private static final byte MEMADDR_STATUS = 0x05;
	private static final byte MEMADDR_WRITEPROTECTION = 0x0F;
	
	// commands
	
	private static final byte CMD_WRITE = (0x00 << 2);
	private static final byte CMD_INC = (0x01 << 2);
	private static final byte CMD_DEC = (0x02 << 2);
	private static final byte CMD_READ = (0x03 << 2);
	
	// terminal control register bits
	
	// general call not yet supported by this implementation
	// private static final int TCON_GCEN = (1 << 8);
	static final int TCON_RH0HW = (1 << 3);
	static final int TCON_RH0A = (1 << 2);
	static final int TCON_RH0W = (1 << 1);
	static final int TCON_RH0B = (1 << 0);
	static final int TCON_RH1HW = (1 << 7);
	static final int TCON_RH1A = (1 << 6);
	static final int TCON_RH1W = (1 << 5);
	static final int TCON_RH1B = (1 << 4);
	
	// status-bits (see 4.2.2.1)
	
	private static final int STATUS_RESERVED_MASK = 0b1111111110000;
	private static final int STATUS_RESERVED_VALUE = 0b1111111110000;
	private static final int STATUS_EEPROM_WRITEACTIVE_BIT = 0b1000;
	private static final int STATUS_WIPERLOCK1_BIT = 0b0100;
	private static final int STATUS_WIPERLOCK0_BIT = 0b0010;
	private static final int STATUS_EEPROM_WRITEPROTECTION_BIT = 0b0001;
	
	/**
	 * the underlying Pi4J-device
	 */
	private I2CDevice i2cDevice;
	
	/**
	 * Builds an instance which is ready to use.
	 * 
	 * @param i2cDevice The Pi4J-I2CDevice to which the instance is connected to
	 * @throws IOException throw 
	 */
	DeviceController(final I2CDevice i2cDevice) throws IOException {
		
		// input validation
		if (i2cDevice == null) {
			throw new RuntimeException("Parameter 'i2cDevice' must not be null!");
		}

		this.i2cDevice = i2cDevice;

	}
	
	/**
	 * Returns the status of the device according EEPROM and WiperLocks.
	 * 
	 * @return The device's status
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public DeviceControllerDeviceStatus getDeviceStatus() throws IOException {

		// get status from device
		int deviceStatus = read(MEMADDR_STATUS);
		
		// check formal criterias
		int reservedValue = deviceStatus & STATUS_RESERVED_MASK;
		if (reservedValue != STATUS_RESERVED_VALUE) {
			throw new IOException(
					"status-bits 4 to 8 must be 1 according to documentation chapter 4.2.2.1. got '"
					+ Integer.toString(reservedValue, 2)
					+ "'!");
		}
		
		// build the result
		boolean eepromWriteActive
				= (deviceStatus & STATUS_EEPROM_WRITEACTIVE_BIT) > 0;
		boolean eepromWriteProtection
		 		= (deviceStatus & STATUS_EEPROM_WRITEPROTECTION_BIT) > 0;
		boolean wiperLock0
				= (deviceStatus & STATUS_WIPERLOCK0_BIT) > 0;
		boolean wiperLock1
				= (deviceStatus & STATUS_WIPERLOCK1_BIT) > 0;
		
		return new DeviceControllerDeviceStatus(
				eepromWriteActive, eepromWriteProtection,
				wiperLock0, wiperLock1);
		
	}
	
	/**
	 * Increments the volatile wiper for the given number steps.
	 * 
	 * @param channel Which wiper
	 * @param steps The number of steps
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public void increase(final DeviceControllerChannel channel, final int steps)
			throws IOException {
		
		if (channel == null) {
			throw new RuntimeException("null-channel is not allowed. For devices "
					+ "knowing just one wiper Channel.A is mandatory for "
					+ "parameter 'channel'");
		}
		
		// decrease only works on volatile-wiper
		byte memAddr = channel.getVolatileMemoryAddress();
		
		increaseOrDecrease(memAddr, true, steps);
		
	}
	
	/**
	 * Decrements the volatile wiper for the given number steps.
	 * 
	 * @param channel Which wiper
	 * @param steps The number of steps
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public void decrease(final DeviceControllerChannel channel, final int steps)
			throws IOException {

		if (channel == null) {
			throw new RuntimeException("null-channel is not allowed. For devices "
					+ "knowing just one wiper Channel.A is mandatory for "
					+ "parameter 'channel'");
		}
		
		// decrease only works on volatile-wiper
		byte memAddr = channel.getVolatileMemoryAddress();
		
		increaseOrDecrease(memAddr, false, steps);

	}
	
	/**
	 * Receives the current wiper's value from the device.
	 * 
	 * @param channel Which wiper
	 * @param nonVolatile volatile or non-volatile value
	 * @return The wiper's value
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public int getValue(final DeviceControllerChannel channel, final boolean nonVolatile)
			throws IOException {
		
		if (channel == null) {
			throw new RuntimeException("null-channel is not allowed. For devices "
					+ "knowing just one wiper Channel.A is mandatory for "
					+ "parameter 'channel'");
		}
		
		// choose proper memory address (see TABLE 4-1)
		byte memAddr = nonVolatile ?
				channel.getNonVolatileMemoryAddress()
				: channel.getVolatileMemoryAddress();
		
		// read current value
		int currentValue = read(memAddr);
		
		return currentValue;
		
	}
	
	/**
	 * Sets the wiper's value in the device.
	 * 
	 * @param channel Which wiper
	 * @param value The wiper's value
	 * @param nonVolatile volatile or non-volatile value
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public void setValue(final DeviceControllerChannel channel, final int value,
			final boolean nonVolatile) throws IOException {
		
		if (channel == null) {
			throw new RuntimeException("null-channel is not allowed. For devices "
					+ "knowing just one wiper Channel.A is mandatory for "
					+ "parameter 'channel'");
		}
		if (value < 0) {
			throw new RuntimeException("only positive values are allowed! Got value '"
					+ value + "' for writing to channel '"
					+ channel.name() + "'");
		}
		
		// choose proper memory address (see TABLE 4-1)
		byte memAddr = nonVolatile ?
				channel.getNonVolatileMemoryAddress()
				: channel.getVolatileMemoryAddress();
		
		// write the value to the device
		write(memAddr, value);
		
	}
	
	/**
	 * Fetches the terminal-configuration from the device for a certain channel.
	 * 
	 * @param channel The channel
	 * @return The current terminal-configuration
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public DeviceControllerTerminalConfiguration getTerminalConfiguration(
			final DeviceControllerChannel channel) throws IOException {
		
		if (channel == null) {
			throw new RuntimeException("null-channel is not allowed. For devices "
					+ "knowing just one wiper Channel.A is mandatory for "
					+ "parameter 'channel'");
		}
		
		// read configuration from device
		int tcon = read(MEMADDR_TCON);
		
		// build result
		boolean channelEnabled = (tcon & channel.getHardwareConfigControlBit()) > 0;
		boolean pinAEnabled = (tcon & channel.getTerminalAConnectControlBit()) > 0;
		boolean pinWEnabled = (tcon & channel.getWiperConnectControlBit()) > 0;
		boolean pinBEnabled = (tcon & channel.getTerminalBConnectControlBit()) > 0;
		
		return new DeviceControllerTerminalConfiguration(channel, channelEnabled,
				pinAEnabled, pinWEnabled, pinBEnabled);
		
	}
	
	/**
	 * Sets the given terminal-configuration to the device.
	 * 
	 * @param config A terminal-configuration for a certain channel.
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public void setTerminalConfiguration(final DeviceControllerTerminalConfiguration config)
			throws IOException {
		
		if (config == null) {
			throw new RuntimeException("null-config is not allowed!");
		}
		final DeviceControllerChannel channel = config.getChannel();
		if (channel == null) {
			throw new RuntimeException("null-channel is not allowed. For devices "
					+ "knowing just one wiper Channel.A is mandatory for "
					+ "parameter 'channel'");
		}
		
		// read current configuration from device
		int tcon = read(MEMADDR_TCON);
		
		// modify configuration
		tcon = setBit(tcon, channel.getHardwareConfigControlBit(),
				config.isChannelEnabled());
		tcon = setBit(tcon, channel.getTerminalAConnectControlBit(),
				config.isPinAEnabled());
		tcon = setBit(tcon, channel.getWiperConnectControlBit(),
				config.isPinWEnabled());
		tcon = setBit(tcon, channel.getTerminalBConnectControlBit(),
				config.isPinBEnabled());
		
		// write new configuration to device
		write(MEMADDR_TCON, tcon);
		
	}
	
	/**
	 * Enables or disables a wiper's lock.
	 * <p>
	 * Hint: This will only work using the &quot;High Volate Command&quot; (see 3.1).
	 * 
	 * @param enabled Whether to enable the wiper's lock
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public void setWiperLock(final DeviceControllerChannel channel,
			final boolean locked) throws IOException {
		
		if (channel == null) {
			throw new RuntimeException("null-channel is not allowed. For devices "
					+ "knowing just one wiper Channel.A is mandatory for "
					+ "parameter 'channel'");
		}
		
		byte memAddr = channel.getNonVolatileMemoryAddress();
		
		// increasing or decreasing on non-volatile wipers
		// enables or disables WiperLock (see 7.8)
		increaseOrDecrease(memAddr, locked, 1);
		
	}
	
	/**
	 * Enables or disables the device's write-protection.
	 * <p>
	 * Hint: This will only work using the &quot;High Volate Command&quot; (see 3.1).
	 * 
	 * @param enabled Whether to enable write-protection
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public void setWriteProtection(final boolean enabled) throws IOException {
		
		// increasing or decreasing on WP-memory-address
		// enables or disables write-protection (see 7.8)
		increaseOrDecrease(MEMADDR_WRITEPROTECTION, enabled, 1);
		
	}
	
	/**
	 * Sets or clears a bit in the given memory (integer).
	 * 
	 * @param mem The memory to modify
	 * @param mask The mask which defines to bit to be set/cleared
	 * @param value Whether to set the bit (true) or clear the bit (false)
	 * @return The modified memory
	 */
	private int setBit(final int mem, final int mask, final boolean value) {
		
		final int result;
		if (value) {
			result = mem | mask;  // set bit by using OR
		} else {
			result = mem & ~mask; // clear bit by using AND with the inverted mask
		}
		return result;
		
	}
	
	/**
	 * Reads two bytes from the devices at the given memory-address.
	 * 
	 * @param memAddr The memory-address to read from
	 * @return The two bytes read
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	private int read(final byte memAddr) throws IOException {
		
		// ask device for reading data - see FIGURE 7-5
		byte cmd = (byte) ((memAddr << 4) | CMD_READ);
		i2cDevice.write(cmd);
		
		// read two bytes
		byte[] buf = new byte[2];
		int read = i2cDevice.read(buf, 0, 2);
		if (read != 2) {
			throw new IOException("Expected to read two bytes but got: " + read);
		}
		
		// transform signed byte to unsigned byte stored as int
		int first = buf[0] & 0xFF;
		int second = buf[1] & 0xFF;
		
		// interpret two bytes as one integer
		return (first << 8) | second;
		
	}
	
	/**
	 * Writes 9 bits of the given value to the device.
	 * 
	 * @param memAddr The memory-address to write to
	 * @param value The value to be written
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	private void write(final byte memAddr, final int value) throws IOException {
		
		// bit 8 of value
		byte firstBit = (byte) ((value >> 8) & 0x000001);
		
		// ask device for setting a value - see FIGURE 7-2
		byte cmd = (byte) ((memAddr << 4) | CMD_WRITE | firstBit);
		
		// 7 bits of value
		byte data = (byte) (value & 0x00FF);
		
		// write sequence of cmd and data to the device
		byte[] sequence = new byte[] { cmd, data };
		i2cDevice.write(sequence, 0, sequence.length);
		
	}
	
	/**
	 * Writes n (steps) bytes to the device holding the wiper's address
	 * and the increment or decrement command.
	 * 
	 * @param memAddr The wiper's address
	 * @param increase Whether to increment the wiper
	 * @param steps The number of steps the wiper has to be incremented/decremented
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	private void increaseOrDecrease(final byte memAddr,
			final boolean increase, final int steps) throws IOException {
		
		// 0 steps means 'do nothing'
		if (steps == 0) {
			return;
		}
		
		// negative steps means to decrease on 'increase' or the increase on 'decrease'
		final int actualSteps;
		final boolean actualIncrease;
		if (steps < 0) {
			actualIncrease = !increase;
			actualSteps = Math.abs(steps);
		} else {
			actualIncrease = increase;
			actualSteps = steps;
		}
		
		// ask device for increasing or decrease - see FIGURE 7-7
		byte cmd = (byte) ((memAddr << 4) | (actualIncrease ? CMD_INC : CMD_DEC));
		
		// build sequence of commands (one for each step)
		byte[] sequence = new byte[actualSteps];
		Arrays.fill(sequence, cmd);
		
		// write sequence to the device
		i2cDevice.write(sequence, 0, sequence.length);
		
	}
	
	@Override
	public String toString() {

		final StringBuffer result = new StringBuffer(getClass().getName());
		result.append("{\n");
		result.append("  i2cDevice='").append(i2cDevice);
		result.append("'\n}");
		return result.toString();

	}
	
	@Override
	public boolean equals(final Object obj) {
		
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		DeviceController other = (DeviceController) obj;
		if (!i2cDevice.equals(other.i2cDevice)) {
			return false;
		}
		return true;
		
	}

}
