package com.pi4j.io.gpio;

import java.util.EnumSet;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  PinMode.java  
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

/**
 * Pin edge definition.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public enum PinMode {

    DIGITAL_INPUT(0, "input", PinDirection.IN), 
    DIGITAL_OUTPUT(1, "output", PinDirection.OUT),
    PWM_OUTPUT(2, "pwm_output", PinDirection.OUT),
    ANALOG_INPUT(3, "analog_input", PinDirection.IN), 
    ANALOG_OUTPUT(4, "analog_output", PinDirection.OUT);

    private final int value;
    private final String name;
    private final PinDirection direction;

    private PinMode(int value, String name, PinDirection direction) {
        this.value = value;
        this.name = name;
        this.direction = direction;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public PinDirection getDirection() {
        return direction;
    }
    
    @Override
    public String toString() {
        return name.toUpperCase();        
    }    
    
    public static EnumSet<PinMode> allDigital() {
        return EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT);
    }    

    public static EnumSet<PinMode> allAnalog() {
        return EnumSet.of(PinMode.ANALOG_INPUT, PinMode.ANALOG_OUTPUT);
    }    

    public static EnumSet<PinMode> all() {
        return EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, 
                          PinMode.ANALOG_INPUT, PinMode.ANALOG_OUTPUT,
                          PinMode.PWM_OUTPUT);
    }    

    public static EnumSet<PinMode> allInputs() {
        return EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.ANALOG_INPUT);
    }    

    public static EnumSet<PinMode> allOutput() {
        return EnumSet.of(PinMode.DIGITAL_OUTPUT, 
                          PinMode.ANALOG_OUTPUT,
                          PinMode.PWM_OUTPUT);
    }        
}
