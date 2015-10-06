package com.pi4j.component.potentiometer.microchip.impl;

import com.pi4j.io.i2c.I2CDevice;

import java.io.IOException;
import java.util.Arrays;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerDeviceController.java  
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

/**
 * Ported from <a href="http://blog.stibrany.com/?p=9">Stibro's code blog</a>.
 * 
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public class MicrochipPotentiometerDeviceController {
	
	// for 'nonVolatile' parameters
	
	public static final boolean VOLATILE_WIPER = false;
	public static final boolean NONVOLATILE_WIPER = true;
	
	// register memory addresses (see TABLE 4-1)
	
	static final byte MEMADDR_WIPER0 = 0x00;
	static final byte MEMADDR_WIPER1 = 0x01;
	static final byte MEMADDR_WIPER0_NV = 0x02;
	static final byte MEMADDR_WIPER1_NV = 0x03;
	static final byte MEMADDR_TCON01 = 0x04; // terminal control for wiper 0 and 1
	private static final byte MEMADDR_STATUS = 0x05;
	static final byte MEMADDR_WIPER2 = 0x06;
	static final byte MEMADDR_WIPER3 = 0x07;
	static final byte MEMADDR_WIPER2_NV = 0x08;
	static final byte MEMADDR_WIPER3_NV = 0x09;
	static final byte MEMADDR_TCON23 = 0x04; // terminal control for wiper 2 and 3
	private static final byte MEMADDR_WRITEPROTECTION = 0x0F;
	
	// commands
	
	private static final byte CMD_WRITE = (0x00 << 2);
	private static final byte CMD_INC = (0x01 << 2);
	private static final byte CMD_DEC = (0x02 << 2);
	private static final byte CMD_READ = (0x03 << 2);
	
	// terminal control register bits
	
	// general call not yet supported by this implementation
	// private static final int TCON_GCEN = (1 << 8);
	static final int TCON_RH02HW = (1 << 3); // for wiper 0 and 2
	static final int TCON_RH02A = (1 << 2);  // for wiper 0 and 2
	static final int TCON_RH02W = (1 << 1);  // for wiper 0 and 2
	static final int TCON_RH02B = (1 << 0);  // for wiper 0 and 2
	static final int TCON_RH13HW = (1 << 7); // for wiper 1 and 3
	static final int TCON_RH13A = (1 << 6);  // for wiper 1 and 3
	static final int TCON_RH13W = (1 << 5);  // for wiper 1 and 3
	static final int TCON_RH13B = (1 << 4);  // for wiper 1 and 3
	
	// status-bits (see 4.2.2.1)
	
	private static final int STATUS_RESERVED_MASK = 0b0000111110000;
	private static final int STATUS_RESERVED_VALUE = 0b0000111110000;
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
	public MicrochipPotentiometerDeviceController(final I2CDevice i2cDevice) throws IOException {
		
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
		int tcon = read(channel.getTerminalControllAddress());
		
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
		
		byte memAddr = config.getChannel().getTerminalControllAddress();
		
		// read current configuration from device
		int tcon = read(memAddr);
		
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
		write(memAddr, tcon);
		
	}
	
	/**
	 * Enables or disables a wiper's lock.
	 * <p>
	 * Hint: This will only work using the &quot;High Volate Command&quot; (see 3.1).
	 * 
	 * @param channel Which wiper
	 * @param locked Whether to enable the wiper's lock
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
		byte[] cmd = new byte[] { (byte) ((memAddr << 4) | CMD_READ) };
		
		// read two bytes
		byte[] buf = new byte[2];
		int read = i2cDevice.read(cmd, 0, cmd.length,
				buf, 0, buf.length);
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
		MicrochipPotentiometerDeviceController other = (MicrochipPotentiometerDeviceController) obj;
		if (!i2cDevice.equals(other.i2cDevice)) {
			return false;
		}
		return true;
		
	}

}
