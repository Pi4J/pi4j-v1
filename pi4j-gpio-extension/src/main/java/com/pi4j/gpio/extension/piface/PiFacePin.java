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
