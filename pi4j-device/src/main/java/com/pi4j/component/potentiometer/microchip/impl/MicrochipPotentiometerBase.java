package com.pi4j.component.potentiometer.microchip.impl;

import com.pi4j.component.ComponentBase;
import com.pi4j.component.potentiometer.microchip.*;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometerBase.java  
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
 * Abstract Pi4J-device for MCP45XX and MCP46XX ICs.
 * 
 * @see com.pi4j.component.potentiometer.microchip.MCP4561
 * @see com.pi4j.component.potentiometer.microchip.MCP4651
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public abstract class MicrochipPotentiometerBase
		extends ComponentBase implements MicrochipPotentiometer {
	
	/**
	 * An action which may be run for the volatile-wiper,
	 * the non-volatile-wiper or both.
	 * 
	 * @see MicrochipPotentiometerBase#doWiperAction(WiperAction)
	 */
	private static interface WiperAction {
		
		/**
		 * @param nonVolatile Whether to run for volatile- or non-volatile-wiper
		 * @throws IOException Thrown if communication fails or device returned a malformed result
		 */
		void run(final boolean nonVolatile) throws IOException;
		
	};
	
	/**
	 * The value which is used for address-bit if the device's package
	 * does not provide a matching address-pin (see 6.2.4)
	 */
	protected static final boolean PIN_NOT_AVAILABLE = true;
	
	/**
	 * The value which is used for devices capable of non-volatile wipers.
	 * For those devices the initial value is loaded from EEPROM. 
	 */
	protected static final int INITIALVALUE_LOADED_FROM_EEPROM = 0;
	
	/**
	 * The controller-instance
	 */
	private MicrochipPotentiometerDeviceController controller;
	
	/**
	 * The channel this instance is configured for
	 */
	private MicrochipPotentiometerChannel channel;
	
	/**
	 * Whether to use the volatile or the non-volatile wiper
	 */
	protected MicrochipPotentiometerNonVolatileMode nonVolatileMode;
	
	/**
	 * The wiper's current-value (volatile)
	 */
	private int currentValue;

	/**
	 * Builds an instance which is ready to use.
	 * 
	 * @param i2cBus The Pi4J-I2CBus to which the device is connected to
	 * @param pinA0 Whether the device's address pin A0 is high (true) or low (false)
	 * @param pinA1 Whether the device's address pin A1 (if available) is high (true) or low (false)
	 * @param pinA2 Whether the device's address pin A2 (if available) is high (true) or low (false)
	 * @param channel Which of the potentiometers provided by the device to control
	 * @param nonVolatileMode The way non-volatile reads or writes are done
	 * @param initialValueForVolatileWipers The value for devices which are not capable of non-volatile wipers
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	protected MicrochipPotentiometerBase(
            final I2CBus i2cBus,
            final boolean pinA0,
            final boolean pinA1,
            final boolean pinA2,
            final MicrochipPotentiometerChannel channel,
            final MicrochipPotentiometerNonVolatileMode nonVolatileMode,
            final int initialValueForVolatileWipers)
			throws IOException {
		
		this(i2cBus, pinA0, pinA1, pinA2, channel,
				nonVolatileMode, initialValueForVolatileWipers,
				MicrochipPotentiometerDefaultDeviceControllerFactory.getInstance());
		
	}
	
	/**
	 * Builds an instance which is ready to use.
	 * 
	 * @param i2cBus The Pi4J-I2CBus to which the device is connected to
	 * @param pinA0 Whether the device's address pin A0 is high (true) or low (false)
	 * @param pinA1 Whether the device's address pin A1 (if available) is high (true) or low (false)
	 * @param pinA2 Whether the device's address pin A2 (if available) is high (true) or low (false)
	 * @param channel Which of the potentiometers provided by the device to control
	 * @param nonVolatileMode The way non-volatile reads or writes are done
	 * @param initialValueForVolatileWipers The value for devices which are not capable of non-volatile wipers
	 * @param controllerFactory builds new controllers
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 * @see MicrochipPotentiometerDefaultDeviceControllerFactory
	 */
	protected MicrochipPotentiometerBase(
            final I2CBus i2cBus,
            final boolean pinA0,
            final boolean pinA1,
            final boolean pinA2,
            final MicrochipPotentiometerChannel channel,
            final MicrochipPotentiometerNonVolatileMode nonVolatileMode,
            final int initialValueForVolatileWipers,
            final MicrochipPotentiometerDeviceControllerFactory controllerFactory)
			throws IOException {

		// input validation
		if (i2cBus == null) {
			throw new RuntimeException("Parameter 'i2cBus' must not be null!");
		}
		if (channel == null) {
			throw new RuntimeException(
					"For building a Microchip-potentiometer "
					+ "specifying a channel is mandatory! If the device "
					+ "knows more than one potentiometer/rheostat the "
					+ "channel defines which of them is controlled "
					+ "by this object-instance");
		}
		if (!isChannelSupportedByDevice(channel)) {
			throw new RuntimeException(
					"For building a Microchip-potentiometer "
					+ "only channels supported by the underlying device "
					+ "are valid parameters!");
		}
		if (controllerFactory == null) {
			throw new RuntimeException(
					"For building a Microchip-potentiometer "
					+ "providing a controllerFactory is mandatory! "
					+ "Use 'DefaultDeviceControllerFactory."
					+ "getInstance()'.");
		}
		if (nonVolatileMode == null) {
			throw new RuntimeException(
					"For building a Microchip-potentiometer "
					+ "providing a nonVolatileMode is mandatory!.");
		}
		
		// save channel to use
		this.channel = channel;
		
		// initial non-volatile-mode
		this.nonVolatileMode = nonVolatileMode;

		// build the address
		int i2cAddress = buildI2CAddress(pinA0, pinA1, pinA2);
		
		// build the underlying Pi4J-device
		final I2CDevice i2cDevice = i2cBus.getDevice(i2cAddress);
		
		// build the underlying controller-instance
		controller = controllerFactory.getController(i2cDevice);
		
		initialize(initialValueForVolatileWipers);
		
	}
	
	/**
	 * Initializes the wiper to a defined status. For devices capable of non-volatile-wipers
	 * the non-volatile-value is loaded. For devices not capable the given value is set
	 * in the device.
	 * 
	 * @param initialValueForVolatileWipers The initial value for devices not capable
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	protected void initialize(final int initialValueForVolatileWipers) throws IOException {
		
		if (isCapableOfNonVolatileWiper()) {
			
			// the device's volatile-wiper will be set to the value stored
			// in the non-volatile memory. so for those devices the wiper's
			// current value has to be retrieved
			currentValue = controller.getValue(
                    DeviceControllerChannel.valueOf(channel), false);
			
		} else {
			
			// check boundaries
			final int newInitialValueForVolatileWipers
					= getValueAccordingBoundaries(initialValueForVolatileWipers);
			
			controller.setValue(DeviceControllerChannel.valueOf(channel),
					newInitialValueForVolatileWipers,
					MicrochipPotentiometerDeviceController.VOLATILE_WIPER);
			
			currentValue = newInitialValueForVolatileWipers;
			
		}
		
	}
	
	/**
	 * @return The channel this potentiometer is configured for
	 */
    @Override
	public MicrochipPotentiometerChannel getChannel() {
		return channel;
	}
	
	/**
	 * @return Whether device is capable of non volatile wipers
	 */
    @Override
	public abstract boolean isCapableOfNonVolatileWiper();
	
	/**
	 * @return All channels supported by the underlying device
	 */
    @Override
	public abstract MicrochipPotentiometerChannel[] getSupportedChannelsByDevice();
	
	/**
	 * @param pinA0 Whether the device's address pin A0 is high (true) or low (false)
	 * @param pinA1 Whether the device's address pin A1 (if available) is high (true) or low (false)
	 * @param pinA2 Whether the device's address pin A2 (if available) is high (true) or low (false)
	 * @return The I2C-address based on the address-pins given
	 */
	protected static int buildI2CAddress(
			final boolean pinA0,
			final boolean pinA1,
			final boolean pinA2) {
		
		// constant component
		int i2cAddress = 0b0101000;
		
		// dynamic component if device knows pin A0
		if (pinA0) {
			i2cAddress |= 0b0000001;
		}
		
		// dynamic component if device knows pin A1
		if (pinA1) {
			i2cAddress |= 0b0000010;
		}
		
		// dynamic component if device knows pin A2
		if (pinA2) {
			i2cAddress |= 0b0000100;
		}
		
		return i2cAddress;
		
	}
	
	/**
	 * @return The way non-volatile reads or writes are done
	 */
    @Override
	public MicrochipPotentiometerNonVolatileMode getNonVolatileMode() {
		
		return nonVolatileMode;
		
	}
	
	/**
	 * The visibility of this method is protected because not all
	 * devices support non-volatile wipers. Any derived class may
	 * publish this method.
	 * 
	 * @param nonVolatileMode The way non-volatile reads or writes are done
	 */
	protected void setNonVolatileMode(final MicrochipPotentiometerNonVolatileMode nonVolatileMode) {
		
		if (nonVolatileMode == null) {
			throw new RuntimeException("Setting a null-NonVolatileMode is not valid!");
		}
		
		if (!isCapableOfNonVolatileWiper()
				&& (nonVolatileMode != MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY)) {
			throw new RuntimeException("This device is not capable of non-volatile wipers."
					+ " Using another NonVolatileMode than '"
					+ MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY + "' is not valid!");
		}
		
		this.nonVolatileMode = nonVolatileMode;
		
	}

	/**
	 * Updates the cache to the wiper's value.
	 * 
	 * @return The wiper's current value
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
    @Override
	public int updateCacheFromDevice() throws IOException {
		
		currentValue = controller.getValue(DeviceControllerChannel.valueOf(channel), false);
		return currentValue;
		
	}
	
	/**
	 * The visibility of this method is protected because not all
	 * devices support non-volatile wipers. Any derived class may
	 * publish this method.
	 * 
	 * @return The non-volatile-wiper's value.
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	protected int getNonVolatileValue() throws IOException {
		
		if (!isCapableOfNonVolatileWiper()) {
			throw new RuntimeException("This device is not capable of non-volatile wipers!");
		}
		
		return controller.getValue(DeviceControllerChannel.valueOf(channel), true);
		
	}
	
	/**
	 * The wiper's value read from cache. The cache is updated on any
	 * modifying action or the method 'updateCacheFromDevice'.
	 * 
	 * @return The wipers current value
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 * @see #updateCacheFromDevice()
	 */
	@Override
	public int getCurrentValue() throws IOException {
		
		return currentValue;
		
	}
	
	/**
	 * Adjusts the given value according to boundaries (0 and getMaxValue()).
	 * 
	 * @param value The wiper's value to be set
	 * 
	 * @return A valid wiper-value
	 */
	private int getValueAccordingBoundaries(final int value) {
		
		// check boundaries
		final int newValue;
		if (value < 0) {
			newValue = 0;
		}
		else if (value > getMaxValue()) {
			newValue = getMaxValue();
		}
		else {
			newValue = value;
		}
		return newValue;

	}
	
	/**
	 * @param value The wiper's value to be set
	 * 
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	@Override
	public void setCurrentValue(final int value) throws IOException {
		
		// check boundaries
		final int newValue = getValueAccordingBoundaries(value);
		
		// set wipers according nonVolatileMode
		doWiperAction(new WiperAction() {
			
			@Override
			public void run(final boolean nonVolatile) throws IOException {
				
				controller.setValue(DeviceControllerChannel.valueOf(channel), newValue,
						nonVolatile);
				
			}
			
		});
		
		// set currentValue only if volatile-wiper is affected
		if (nonVolatileMode == MicrochipPotentiometerNonVolatileMode.NONVOLATILE_ONLY) {
			return;
		}
		
		// save current value
		currentValue = newValue;
		
	}
	
	/**
	 * Decreases the wiper's value for one step.
	 * 
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	@Override
	public void decrease() throws IOException {
		
		decrease(1);
		
	}

	/**
	 * Decreases the wiper's value for n steps.
	 * 
	 * @param steps The number of steps to decrease
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	@Override
	public void decrease(final int steps) throws IOException {
		
		if (currentValue == 0) {
			return;
		}
		if (steps < 0) {
			throw new RuntimeException("Only positive values for parameter 'steps' allowed!");
		}
		if (getNonVolatileMode() != MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY) {
			throw new RuntimeException("'decrease' is only valid for NonVolatileMode.VOLATILE_ONLY!");
		}

		// check boundaries
		final int actualSteps;
		if (steps > currentValue) {
			actualSteps = currentValue;
		} else {
			actualSteps = steps;
		}
		
		int newValue = currentValue - actualSteps;
		
		// if lower-boundary then set value in device to ensure sync
		// and for a large number of steps it is better to set a new value 
		if ((newValue == 0)
				|| (steps > 5)) {
			
			setCurrentValue(newValue);
			
		}
		// for a small number of steps use 'decrease'-method
		else {
			
			controller.decrease(DeviceControllerChannel.valueOf(channel), actualSteps);
			
			currentValue = newValue;
			
		}
		
	}
	
	/**
	 * Increases the wiper's value for one step.
	 * 
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	@Override
	public void increase() throws IOException {
		
		increase(1);
		
	}
	
	/**
	 * Increases the wiper's value for n steps.
	 * 
	 * @param steps The number of steps to increase
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	@Override
	public void increase(final int steps) throws IOException {
		
		int maxValue = getMaxValue();
		
		if (currentValue == maxValue) {
			return;
		}
		if (steps < 0) {
			throw new RuntimeException("only positive values for parameter 'steps' allowed!");
		}
		if (getNonVolatileMode() != MicrochipPotentiometerNonVolatileMode.VOLATILE_ONLY) {
			throw new RuntimeException("'increase' is only valid for NonVolatileMode.VOLATILE_ONLY!");
		}

		// check boundaries
		final int actualSteps;
		if ((steps + currentValue) > maxValue) {
			actualSteps = maxValue - currentValue;
		} else {
			actualSteps = steps;
		}
		
		int newValue = currentValue + actualSteps;
		
		// if upper-boundary then set value in device to ensure sync
		// and for a large number of steps it is better to set a new value
		if ((newValue == maxValue)
				|| (steps > 5)) {
			
			setCurrentValue(newValue);
			
		}
		// for a small number of step simply repeat 'increase'-commands
		else {
			
			controller.increase(DeviceControllerChannel.valueOf(channel), actualSteps);
			
			currentValue = newValue;
			
		}
		
	}
	
	/**
	 * @return The device- and wiper-status
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
    @Override
	public MicrochipPotentiometerDeviceStatus getDeviceStatus() throws IOException {
		
		DeviceControllerDeviceStatus deviceStatus
				= controller.getDeviceStatus();
		
		boolean wiperLockActive
				= channel == MicrochipPotentiometerChannel.A ?
						deviceStatus.isChannelALocked() : deviceStatus.isChannelBLocked();
		
		return new MicrochipPotentiometerDeviceStatusImpl(
				deviceStatus.isEepromWriteActive(),
				deviceStatus.isEepromWriteProtected(),
				channel,
				wiperLockActive);
		
	}
	
	/**
	 * @return The current terminal-configuration
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
    @Override
	public MicrochipPotentiometerTerminalConfiguration getTerminalConfiguration() throws IOException {
		
		DeviceControllerTerminalConfiguration tcon
				= controller.getTerminalConfiguration(DeviceControllerChannel.valueOf(channel));
		
		return new MicrochipPotentiometerTerminalConfiguration(channel,
				tcon.isChannelEnabled(), tcon.isPinAEnabled(),
				tcon.isPinWEnabled(), tcon.isPinBEnabled());
		
	}
	
	/**
	 * @param terminalConfiguration The new terminal-configuration
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
    @Override
	public void setTerminalConfiguration(
			final MicrochipPotentiometerTerminalConfiguration terminalConfiguration) throws IOException {
		
		if (terminalConfiguration == null) {
			throw new RuntimeException("Setting a null-terminalConfiguration is not valid!");
		}
		if (terminalConfiguration.getChannel() != channel) {
			throw new RuntimeException("Setting a terminalConfiguration with a channel "
					+ "other than the potentiometer's channel is not valid!");
		}
		
		DeviceControllerTerminalConfiguration tcon
				= new DeviceControllerTerminalConfiguration(
                DeviceControllerChannel.valueOf(channel),
				terminalConfiguration.isChannelEnabled(),
				terminalConfiguration.isPinAEnabled(),
				terminalConfiguration.isPinWEnabled(),
				terminalConfiguration.isPinBEnabled());
		
		controller.setTerminalConfiguration(tcon);
		
	}
	
	/**
	 * Enables or disables wiper-lock. See chapter 5.3.
	 * 
	 * @param enabled wiper-lock for the poti's channel has to be enabled
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
    @Override
	public void setWiperLock(final boolean enabled) throws IOException {
		
		controller.setWiperLock(DeviceControllerChannel.valueOf(channel), enabled);
		
	}
	
	/**
	 * Enables or disables write-protection for devices capable of non-volatile memory.
	 * Enabling write-protection does not only protect non-volatile wipers it also
	 * protects any other non-volatile information stored (f.e. wiper-locks).
	 * 
	 * @param enabled write-protection has to be enabled
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
    @Override
	public void setWriteProtection(final boolean enabled) throws IOException {
		
		controller.setWriteProtection(enabled);
		
	}
	
	/**
	 * Tests whether a given object equals to this object.
	 * <p>
	 * Especially to any class deriving PotentiometerImpl
	 * this test does not take properties into account which
	 * do not point to a specific device/channel.
	 * 
	 * @param obj The other object
	 * @return Whether the other object equals to this object.
	 */
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
		MicrochipPotentiometerBase other = (MicrochipPotentiometerBase) obj;
		if (channel != other.channel) {
			return false;
		}
		if (!controller.equals(other.controller)) {
			return false;
		}
		// nonVolatileMode and currentValue is not taken into account
		// because the do not point to a specific device/channel
		return true;
		
	}
	
	@Override
	public String toString() {
		
		final StringBuffer result = new StringBuffer(getClass().getName());
		result.append("{\n");
		result.append("  channel='").append(channel);
		result.append("',\n  controller='").append(controller);
		result.append("',\n  nonVolatileMode='").append(nonVolatileMode);
		result.append("',\n  currentValue='").append(currentValue);
		result.append("'\n}");
		return result.toString();
		
	}
	
	/**
	 * Runs a given 'wiperAction' for the volatile-wiper, the
	 * non-volatile-wiper or both according the current nonVolatileMode.
	 * 
	 * @param wiperAction The action to be run
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 * @see MicrochipPotentiometerBase#nonVolatileMode
	 */
	private void doWiperAction(final WiperAction wiperAction) throws IOException {
		
		// for volatile-wiper
		switch (nonVolatileMode) {
		case VOLATILE_ONLY:
		case VOLATILE_AND_NONVOLATILE:
			wiperAction.run(MicrochipPotentiometerDeviceController.VOLATILE_WIPER);
			break;
		case NONVOLATILE_ONLY:
			// do nothing
		}
		
		// for non-volatile-wiper
		switch (nonVolatileMode) {
		case NONVOLATILE_ONLY:
		case VOLATILE_AND_NONVOLATILE:
			wiperAction.run(MicrochipPotentiometerDeviceController.NONVOLATILE_WIPER);
			break;
		case VOLATILE_ONLY:
			// do nothing
		}
		
	}
	
	/**
	 * @param channel A certain channel
	 * @return Whether the given channel is supported by the underlying device
	 */
    @Override
	public boolean isChannelSupportedByDevice(final MicrochipPotentiometerChannel channel) {
		
		if (channel == null) {
			return false;
		}
		
		for (final MicrochipPotentiometerChannel supportedChannel : getSupportedChannelsByDevice()) {
			if (channel == supportedChannel) {
				return true;
			}
		}
		
		return false;
		
	}
	
}
