package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: I2C Device Abstractions
 * FILENAME      :  TerminalConfiguration.java  
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
 * The device-status concerning the channel this
 * instance of MCP45XX_MCP46XX_Potentiometer is configured for.
 * 
 * @see PotentiometerImpl
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public class TerminalConfiguration {

	private Channel channel;
	private boolean channelEnabled;
	private boolean pinAEnabled;
	private boolean pinWEnabled;
	private boolean pinBEnabled;
	
	public TerminalConfiguration(Channel channel,
			boolean channelEnabled, boolean pinAEnabled,
			boolean pinWEnabled, boolean pinBEnabled) {
		this.channel = channel;
		this.channelEnabled = channelEnabled;
		this.pinAEnabled = pinAEnabled;
		this.pinWEnabled = pinWEnabled;
		this.pinBEnabled = pinBEnabled;
	}

	public Channel getChannel() {
		return channel;
	}
	
	/**
	 * @return Whether the entire channel is enabled or disabled
	 */
	public boolean isChannelEnabled() {
		return channelEnabled;
	}

	/**
	 * @return If channel is enabled, whether pin A is enabled
	 */
	public boolean isPinAEnabled() {
		return pinAEnabled;
	}

	/**
	 * @return If channel is enabled, whether pin W is enabled
	 */
	public boolean isPinWEnabled() {
		return pinWEnabled;
	}

	/**
	 * @return If channel is enabled, whether pin B is enabled
	 */
	public boolean isPinBEnabled() {
		return pinBEnabled;
	}
	
}
