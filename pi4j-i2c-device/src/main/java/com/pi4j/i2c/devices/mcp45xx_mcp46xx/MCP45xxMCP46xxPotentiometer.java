package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

import java.io.IOException;

import com.pi4j.device.DeviceBase;
import com.pi4j.device.potentiometer.DigitalPotentiometer;
import com.pi4j.i2c.devices.MCP4561;
import com.pi4j.i2c.devices.MCP4651;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: I2C Device Abstractions
 * FILENAME      :  MCP45xxMCP46xxPotentiometer.java  
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
 * Abstract Pi4J-device for MCP45XX and MCP46XX ICs.
 * 
 * @see MCP4561
 * @see MCP4651
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public abstract class MCP45xxMCP46xxPotentiometer
		extends DeviceBase implements DigitalPotentiometer {
	
	/**
	 * The way non-volatile reads or writes are done
	 */
	public static enum NonVolatileMode {
		
		/**
		 * read and write to volatile-wiper
		 */
		VOLATILE_ONLY,
		
		/**
		 * read and write to non-volatile-wiper
		 */
		NONVOLATILE_ONLY,
		
		/**
		 * read and write to both volatile- and non-volatile-wipers
		 */
		VOLATILE_AND_NONVOLATILE
		
	};
	
	/**
	 * An action which may be run for the volatile-wiper,
	 * the non-volatile-wiper or both.
	 * 
	 * @see MCP45xxMCP46xxPotentiometer#doWiperAction(WiperAction)
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
	private MCP45xxMCP46xxController controller;
	
	/**
	 * The channel this instance is configured for
	 */
	private Channel channel;
	
	/**
	 * Whether to use the volatile or the non-volatile wiper
	 */
	protected NonVolatileMode nonVolatileMode;
	
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
	 * @param initialValueForVolatileWiper The value for devices which are not capable of non-volatile wipers
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	protected MCP45xxMCP46xxPotentiometer(
			final I2CBus i2cBus,
			final boolean pinA0,
			final boolean pinA1,
			final boolean pinA2,
			final Channel channel,
			final NonVolatileMode nonVolatileMode,
			final int initialValueForVolatileWipers)
			throws IOException {
		
		this(i2cBus, pinA0, pinA1, pinA2, channel,
				nonVolatileMode, initialValueForVolatileWipers,
				DefaultMCP45xxMCP46xxControllerFactory.getInstance());
		
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
	 * @param initialValueForVolatileWiper The value for devices which are not capable of non-volatile wipers
	 * @param controllerFactory builds new controllers
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 * @see DefaultMCP45xxMCP46xxControllerFactory
	 */
	protected MCP45xxMCP46xxPotentiometer(
			final I2CBus i2cBus,
			final boolean pinA0,
			final boolean pinA1,
			final boolean pinA2,
			final Channel channel,
			final NonVolatileMode nonVolatileMode,
			final int initialValueForVolatileWipers,
			final MCP45xxMCP46xxControllerFactory controllerFactory)
			throws IOException {

		// input validation
		if (i2cBus == null) {
			throw new RuntimeException("Parameter 'i2cBus' must not be null!");
		}
		if (channel == null) {
			throw new RuntimeException(
					"For building a MCP45xxMCP46xxPotentiometer "
					+ "specifying a channel is mandetory! If the device "
					+ "knows more than one potentiometer/rheostat the "
					+ "channel defines which of them is controlled "
					+ "by this object-instance");
		}
		if (controllerFactory == null) {
			throw new RuntimeException(
					"For building a MCP45xxMCP46xxPotentiometer "
					+ "providing a controllerFactory is mandetory! "
					+ "Use 'DefaultMCP45xxMCP46xxControllerFactory."
					+ "getInstance()'.");
		}
		if (nonVolatileMode == null) {
			throw new RuntimeException(
					"For building a MCP45xxMCP46xxPotentiometer "
					+ "providing a nonVolatileMode is mandetory!.");
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
	 * @param initialValueForVolatileWiper The initial value for devices not capable
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	protected void initialize(final int initialValueForVolatileWipers) throws IOException {
		
		if (isCapableOfNonVolatileWiper()) {
			
			// the device's volatile-wiper will be set to the value stored
			// in the non-volatile memory. so for those devices the wiper's
			// current value has to be retrieved
			currentValue = controller.getValue(channel.getMcpChannel(), false);
			
		} else {
			
			controller.setValue(channel.getMcpChannel(),
					initialValueForVolatileWipers,
					MCP45xxMCP46xxController.VOLATILE_WIPER);
			
			currentValue = initialValueForVolatileWipers;
			
		}
		
	}
	
	/**
	 * @return Whether device is capable of non volatile wipers
	 */
	public abstract boolean isCapableOfNonVolatileWiper();
	
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
		int i2cAddress = 0b01010000;
		
		// dynamic component if device knows pin A0
		if (pinA0) {
			i2cAddress |= 0b00000010;
		}
		
		// dynamic component if device knows pin A1
		if (pinA1) {
			i2cAddress |= 0b00000100;
		}
		
		// dynamic component if device knows pin A2
		if (pinA2) {
			i2cAddress |= 0b00001000;
		}
		
		return i2cAddress;
		
	}
	
	/**
	 * @return The way non-volatile reads or writes are done
	 */
	public NonVolatileMode getNonVolatileMode() {
		
		return nonVolatileMode;
		
	}
	
	/**
	 * The visibility of this method is protected because not all
	 * devices support non-volatile wipers. Any derived class may
	 * publish this method.
	 * 
	 * @param nonVolatileMode The way non-volatile reads or writes are done
	 */
	protected void setNonVolatileMode(final NonVolatileMode nonVolatileMode) {
		
		if (nonVolatileMode == null) {
			throw new RuntimeException("Setting a null-NonVolatileMode is not valid!");
		}
		
		if (!isCapableOfNonVolatileWiper()
				&& (nonVolatileMode != NonVolatileMode.VOLATILE_ONLY)) {
			throw new RuntimeException("This device is not capable of non-volatile wipers."
					+ " Using another NonVolatileMode than '"
					+ NonVolatileMode.VOLATILE_ONLY + "' is not valid!");
		}
		
		this.nonVolatileMode = nonVolatileMode;
		
	}

	/**
	 * Updates the cache to the wiper's value.
	 * 
	 * @return The wiper's current value
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public int updateCacheFromDevice() throws IOException {
		
		currentValue = controller.getValue(channel.getMcpChannel(), false);
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
		
		return controller.getValue(channel.getMcpChannel(), true);
		
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
	 * @param value The wiper's value to be set
	 * 
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	@Override
	public void setCurrentValue(final int value) throws IOException {
		
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
		
		// set wipers according nonVolatileMode
		doWiperAction(new WiperAction() {
			
			@Override
			public void run(final boolean nonVolatile) throws IOException {
				
				controller.setValue(channel.getMcpChannel(), newValue,
						nonVolatile);
				
			}
			
		});
		
		// set currentValue only if volatile-wiper is affected
		if (nonVolatileMode == NonVolatileMode.NONVOLATILE_ONLY) {
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
			throw new RuntimeException("Only possitive values for parameter 'steps' allowed!");
		}
		if (getNonVolatileMode() != NonVolatileMode.VOLATILE_ONLY) {
			throw new RuntimeException("'decrease' is only valid for NonVolatileMode.RAM_ONLY!");
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
			
			controller.decrease(channel.getMcpChannel(),
					actualSteps, false);
			
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
			throw new RuntimeException("only possitive values for parameter 'steps' allowed!");
		}
		if (getNonVolatileMode() != NonVolatileMode.VOLATILE_ONLY) {
			throw new RuntimeException("'increase' is only valid for NonVolatileMode.RAM_ONLY!");
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
			
			controller.increase(channel.getMcpChannel(),
					actualSteps, false);
			
			currentValue = newValue;
			
		}
		
	}
	
	/**
	 * @return The device- and wiper-status
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 */
	public DeviceStatus getDeviceStatus() throws IOException {
		
		com.pi4j.i2c.devices.mcp45xx_mcp46xx.MCP45xxMCP46xxController.DeviceStatus deviceStatus
				= controller.getDeviceStatus();
		
		boolean wiperLockActive
				= channel == Channel.A ?
						deviceStatus.isWiper0Locked() : deviceStatus.isWiper1Locked();
		
		return new DeviceStatus(
				deviceStatus.isEepromWriteActive(),
				deviceStatus.isEepromWriteProtected(),
				wiperLockActive);
		
	}
	
	/**
	 * Runs a given 'wiperAction' for the volatile-wiper, the
	 * non-volatile-wiper or both according the current nonVolatileMode.
	 * 
	 * @param wiperAction The action to be run
	 * @throws IOException Thrown if communication fails or device returned a malformed result
	 * @see MCP45xxMCP46xxPotentiometer#nonVolatileMode
	 */
	private void doWiperAction(final WiperAction wiperAction) throws IOException {
		
		if (wiperAction == null) {
			throw new RuntimeException(
					"null-wiperAction parameter is not supported");
		}
		
		// for volatile-wiper
		switch (nonVolatileMode) {
		case VOLATILE_ONLY:
		case VOLATILE_AND_NONVOLATILE:
			wiperAction.run(MCP45xxMCP46xxController.VOLATILE_WIPER);
			break;
		case NONVOLATILE_ONLY:
			// do nothing
		}
		
		// for non-volatile-wiper
		switch (nonVolatileMode) {
		case NONVOLATILE_ONLY:
		case VOLATILE_AND_NONVOLATILE:
			wiperAction.run(MCP45xxMCP46xxController.NONVOLATILE_WIPER);
			break;
		case VOLATILE_ONLY:
			// do nothing
		}
		
	}
	
}
