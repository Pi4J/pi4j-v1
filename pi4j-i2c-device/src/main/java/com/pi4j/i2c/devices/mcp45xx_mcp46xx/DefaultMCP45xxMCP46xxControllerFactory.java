package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

import java.io.IOException;

import com.pi4j.io.i2c.I2CDevice;

public class DefaultMCP45xxMCP46xxControllerFactory
		implements MCP45xxMCP46xxControllerFactory {

	private static final MCP45xxMCP46xxControllerFactory defaultFactory
			= new DefaultMCP45xxMCP46xxControllerFactory();
	
	public static MCP45xxMCP46xxControllerFactory getInstance() {
		
		return defaultFactory;
		
	}
	
	@Override
	public MCP45xxMCP46xxController getController(I2CDevice i2cDevice)
			throws IOException {
		
		return new MCP45xxMCP46xxController(i2cDevice);
		
	}
	
}
