package com.pi4j.gpio.extension.piface;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  PiFacePin.java  
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

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.impl.PinImpl;

/**
 * Pi-Face pin definitions.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class PiFacePin  {

    // OUTPUTS (Digital Output Pins)
    public static final Pin OUTPUT_00 = createDigitalOutputPin(1,  "OUTPUT 1 (RELAY 1)");
    public static final Pin OUTPUT_01 = createDigitalOutputPin(2,  "OUTPUT 2 (RELAY 2)");
    public static final Pin OUTPUT_02 = createDigitalOutputPin(4,  "OUTPUT 3");
    public static final Pin OUTPUT_03 = createDigitalOutputPin(8,  "OUTPUT 4");
    public static final Pin OUTPUT_04 = createDigitalOutputPin(16, "OUTPUT 5");
    public static final Pin OUTPUT_05 = createDigitalOutputPin(32, "OUTPUT 5");
    public static final Pin OUTPUT_06 = createDigitalOutputPin(64, "OUTPUT 6");
    public static final Pin OUTPUT_07 = createDigitalOutputPin(128,"OUTPUT 8");
    public static Pin[] OUTPUTS = { OUTPUT_00, OUTPUT_01, OUTPUT_02, OUTPUT_03, OUTPUT_04, OUTPUT_05, OUTPUT_06, OUTPUT_07 };    

    // INPUTS (Digital Input Pins)
    public static final Pin INPUT_00 = createDigitalInputPin(1001, "INPUT 1 (SWITCH 1)");
    public static final Pin INPUT_01 = createDigitalInputPin(1002, "INPUT 2 (SWITCH 2)");
    public static final Pin INPUT_02 = createDigitalInputPin(1004, "INPUT 3 (SWITCH 3)");
    public static final Pin INPUT_03 = createDigitalInputPin(1008, "INPUT 4 (SWITCH 4)");
    public static final Pin INPUT_04 = createDigitalInputPin(1016, "INPUT 5");
    public static final Pin INPUT_05 = createDigitalInputPin(1032, "INPUT 6");
    public static final Pin INPUT_06 = createDigitalInputPin(1064, "INPUT 7");
    public static final Pin INPUT_07 = createDigitalInputPin(1128, "INPUT 8");    
    public static Pin[] INPUTS = { INPUT_00, INPUT_01, INPUT_02, INPUT_03, INPUT_04, INPUT_05, INPUT_06, INPUT_07 };
    
    private static Pin createDigitalOutputPin(int address, String name) {
        return new PinImpl(PiFaceGpioProvider.NAME, address, name, 
                    EnumSet.of(PinMode.DIGITAL_OUTPUT),
                    EnumSet.of(PinPullResistance.OFF));
    }
        
    private static Pin createDigitalInputPin(int address, String name) {
        return new PinImpl(PiFaceGpioProvider.NAME, address, name, 
                    EnumSet.of(PinMode.DIGITAL_INPUT),
                    EnumSet.of(PinPullResistance.PULL_UP));
    }       
}
