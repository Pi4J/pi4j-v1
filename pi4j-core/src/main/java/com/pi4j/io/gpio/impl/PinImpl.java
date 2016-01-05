package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  PinImpl.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;

public class PinImpl implements Pin {

    private final int address;
    private final String name ;
    private final String provider;
    private final EnumSet<PinPullResistance> supportedPinPullResistance;
    private final EnumSet<PinMode> supportedPinModes;

    public PinImpl(String provider, int address, String name, EnumSet<PinMode> modes, EnumSet<PinPullResistance> pullResistance) {
        this.provider = provider;
        this.address = address;
        this.name = name;
        this.supportedPinModes = modes;
        this.supportedPinPullResistance = pullResistance;
    }

    public PinImpl(String provider, int address, String name, EnumSet<PinMode> modes) {
        this(provider, address, name, modes, null);
    }
    
    @Override
    public int getAddress() {
        return address;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProvider() {
        return provider;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public EnumSet<PinMode> getSupportedPinModes() {
        if (supportedPinModes == null) {
            return EnumSet.noneOf(PinMode.class);
        }
        return supportedPinModes;
    }

    @Override
    public EnumSet<PinPullResistance> getSupportedPinPullResistance() {
        if (supportedPinPullResistance == null) {
            return EnumSet.noneOf(PinPullResistance.class);
        }
        return supportedPinPullResistance;
    }
}
