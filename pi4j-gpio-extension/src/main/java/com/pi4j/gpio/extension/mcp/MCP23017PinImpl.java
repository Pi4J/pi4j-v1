package com.pi4j.gpio.extension.mcp;

import java.util.EnumSet;

import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.impl.PinImpl;

public class MCP23017PinImpl extends PinImpl {

	private int i2cAddress;
	
    public MCP23017PinImpl(String provider, int address, int i2cAddress,String name,
			EnumSet<PinMode> modes, EnumSet<PinPullResistance> pullResistance,
			EnumSet<PinEdge> pinEdges) {

    	super(provider, address, name, modes, pullResistance, pinEdges);
    	this.i2cAddress = i2cAddress;
    	
	}

	public MCP23017PinImpl(String provider, int address, int i2cAddress, String name,
			EnumSet<PinMode> modes, EnumSet<PinPullResistance> pullResistance) {
		
		super(provider, address, name, modes, pullResistance);
    	this.i2cAddress = i2cAddress;
    	
	}

	public MCP23017PinImpl(String provider, int address, int i2cAddress, String name,
			EnumSet<PinMode> modes) {
		
		super(provider, address, name, modes);
    	this.i2cAddress = i2cAddress;

	}

	public int getI2cAddress() {
		return i2cAddress;
	}
	
}
