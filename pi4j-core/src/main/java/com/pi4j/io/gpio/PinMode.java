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
 * Copyright (C) 2012 - 2014 Pi4J
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
 * Pin edge definition.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
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
