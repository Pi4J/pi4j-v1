package com.pi4j.io.gpio;

import java.util.EnumSet;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  PinState.java  
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
 * Pin edge definition.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public enum PinState {

    LOW(0, "LOW"), 
    HIGH(1, "HIGH"); 

    private final int value;
    private final String name;

    private PinState(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public boolean isHigh() {
        return (this == HIGH);
    }

    public boolean isLow() {
        return (this == LOW);
    }
    
    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;        
    }    
    
    public static PinState getState(int state) {
        for (PinState item : PinState.values()) {
            if (item.getValue() == state) {
                return item;
            }
        }
        return null;
    }

    public static PinState getInverseState(PinState state) {
        return (state == HIGH ? LOW : HIGH);
    }
    
    public static PinState getState(boolean state) {
        return (state ? PinState.HIGH : PinState.LOW);
    }

    public static PinState[] allStates() {
        return PinState.values();
    }    
    
    public static EnumSet<PinState> all() {
        return EnumSet.of(PinState.HIGH, PinState.LOW);
    }        
}
