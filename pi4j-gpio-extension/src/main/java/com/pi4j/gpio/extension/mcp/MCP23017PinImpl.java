package com.pi4j.gpio.extension.mcp;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP23017PinImpl.java  
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
