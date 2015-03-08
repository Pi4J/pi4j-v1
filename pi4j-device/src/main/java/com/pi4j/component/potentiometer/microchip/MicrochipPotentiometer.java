package com.pi4j.component.potentiometer.microchip;

import com.pi4j.component.potentiometer.Potentiometer;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MicrochipPotentiometer.java  
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
 * Abstract Pi4J-device for MCP45XX and MCP46XX ICs.
 * 
 * @see com.pi4j.component.potentiometer.microchip.MCP4561
 * @see com.pi4j.component.potentiometer.microchip.MCP4651
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public interface MicrochipPotentiometer extends Potentiometer {

	/**
	 * @return The channel this potentiometer is configured for
	 */
	public MicrochipPotentiometerChannel getChannel();

	/**
	 * @return Whether device is capable of non volatile wipers
	 */
	public boolean isCapableOfNonVolatileWiper();


    /**
     * @return The way non-volatile reads or writes are done
     */
    public MicrochipPotentiometerNonVolatileMode getNonVolatileMode();

	/**
	 * @return All channels supported by the underlying device
	 */
	public MicrochipPotentiometerChannel[] getSupportedChannelsByDevice();

	/**
	 * Updates the cache to the wiper's value.
	 *
	 * @return The wiper's current value
	 * @throws java.io.IOException Thrown if communication fails or device returned a malformed result
	 */
	public int updateCacheFromDevice() throws IOException;

	/**
	 * @return The device- and wiper-status
	 * @throws java.io.IOException Thrown if communication fails or device returned a malformed result
	 */
	public MicrochipPotentiometerDeviceStatus getDeviceStatus() throws IOException;

	/**
	 * @return The current terminal-configuration
	 * @throws java.io.IOException Thrown if communication fails or device returned a malformed result
	 */
	public MicrochipPotentiometerTerminalConfiguration getTerminalConfiguration() throws IOException;

	/**
	 * @param terminalConfiguration The new terminal-configuration
	 * @throws java.io.IOException Thrown if communication fails or device returned a malformed result
	 */
	public void setTerminalConfiguration(
			final MicrochipPotentiometerTerminalConfiguration terminalConfiguration) throws IOException;

	/**
	 * Enables or disables wiper-lock. See chapter 5.3.
	 *
	 * @param enabled wiper-lock for the poti's channel has to be enabled
	 * @throws java.io.IOException Thrown if communication fails or device returned a malformed result
	 */
	public void setWiperLock(final boolean enabled) throws IOException;

	/**
	 * Enables or disables write-protection for devices capable of non-volatile memory.
	 * Enabling write-protection does not only protect non-volatile wipers it also
	 * protects any other non-volatile information stored (f.e. wiper-locks).
	 *
	 * @param enabled write-protection has to be enabled
	 * @throws java.io.IOException Thrown if communication fails or device returned a malformed result
	 */
	public void setWriteProtection(final boolean enabled) throws IOException;


	/**
	 * @param channel A certain channel
	 * @return Whether the given channel is supported by the underlying device
	 */
	public boolean isChannelSupportedByDevice(final MicrochipPotentiometerChannel channel);
}
