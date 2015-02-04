package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: I2C Device Abstractions
 * FILENAME      :  Channel.java  
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
 * A channel an instance of MCP45XX_MCP46XX_Potentiometer can be configured for.
 * 
 * @see PotentiometerImpl
 * @author <a href="http://raspelikan.blogspot.co.at">Raspelikan</a>
 */
public enum Channel {
	
	/**
	 * Pins P0A, P0W, P0B
	 */
	A(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.A),
	
	/**
	 * Pins P1A, P1W, P1B
	 */
	B(com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel.B);
	
	/**
	 * The controller's channel 
	 */
	private com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel mcpChannel;
	
	private Channel(final com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel mcpChannel) {
		
		this.mcpChannel = mcpChannel;
		
	}
	
	public com.pi4j.i2c.devices.mcp45xx_mcp46xx.DeviceController.Channel getMcpChannel() {
		return mcpChannel;
	}
	
}