package com.pi4j.i2c.devices.mcp45xx_mcp46xx;

import java.io.IOException;

import com.pi4j.io.i2c.I2CDevice;

public interface MCP45xxMCP46xxControllerFactory {

	MCP45xxMCP46xxController getController(final I2CDevice i2cDevice)
			throws IOException ;
	
}
