package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  RaspiPin.java  
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


import com.pi4j.io.gpio.impl.PinImpl;

import java.util.EnumSet;

/**
 * Raspberry Pi pin definitions.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class RaspiPin  {
    
    public static final Pin GPIO_00 = createDigitalPin(0, "GPIO 0"); 
    public static final Pin GPIO_01 = createDigitalAndPwmPin(1, "GPIO 1"); // PIN 1 supports PWM output
    public static final Pin GPIO_02 = createDigitalPin(2, "GPIO 2"); 
    public static final Pin GPIO_03 = createDigitalPin(3, "GPIO 3"); 
    public static final Pin GPIO_04 = createDigitalPin(4, "GPIO 4"); 
    public static final Pin GPIO_05 = createDigitalPin(5, "GPIO 5"); 
    public static final Pin GPIO_06 = createDigitalPin(6, "GPIO 6"); 
    public static final Pin GPIO_07 = createDigitalPin(7, "GPIO 7"); 
    public static final Pin GPIO_08 = createDigitalPin(8, "GPIO 8"); 
    public static final Pin GPIO_09 = createDigitalPin(9, "GPIO 9"); 
    public static final Pin GPIO_10 = createDigitalPin(10, "GPIO 10"); 
    public static final Pin GPIO_11 = createDigitalPin(11, "GPIO 11"); 
    public static final Pin GPIO_12 = createDigitalPin(12, "GPIO 12"); 
    public static final Pin GPIO_13 = createDigitalPin(13, "GPIO 13"); 
    public static final Pin GPIO_14 = createDigitalPin(14, "GPIO 14"); 
    public static final Pin GPIO_15 = createDigitalPin(15, "GPIO 15"); 
    public static final Pin GPIO_16 = createDigitalPin(16, "GPIO 16");

    // the following GPIO pins are only available on the Raspbery Pi Model A, B (revision 2.0), B+ and Compute Module
    public static final Pin GPIO_17 = createDigitalPin(17, "GPIO 17"); // requires B rev2 or newer model (P5 header)
    public static final Pin GPIO_18 = createDigitalPin(18, "GPIO 18"); // requires B rev2 or newer model (P5 header)
    public static final Pin GPIO_19 = createDigitalPin(19, "GPIO 19"); // requires B rev2 or newer model (P5 header)
    public static final Pin GPIO_20 = createDigitalPin(20, "GPIO 20"); // requires B rev2 or newer model (P5 header)

    // the following GPIO pins are only available on the Raspbery Pi Model B+ and Compute Module
    public static final Pin GPIO_21 = createDigitalPin(21, "GPIO 21"); // requires B+ or newer model (40 pin header)
    public static final Pin GPIO_22 = createDigitalPin(22, "GPIO 22"); // requires B+ or newer model (40 pin header)
    public static final Pin GPIO_23 = createDigitalPin(23, "GPIO 23"); // requires B+ or newer model (40 pin header)
    public static final Pin GPIO_24 = createDigitalPin(24, "GPIO 24"); // requires B+ or newer model (40 pin header)
    public static final Pin GPIO_25 = createDigitalPin(25, "GPIO 25"); // requires B+ or newer model (40 pin header)
    public static final Pin GPIO_26 = createDigitalPin(26, "GPIO 26"); // requires B+ or newer model (40 pin header)
    public static final Pin GPIO_27 = createDigitalPin(27, "GPIO 27"); // requires B+ or newer model (40 pin header)
    public static final Pin GPIO_28 = createDigitalPin(28, "GPIO 28"); // requires B+ or newer model (40 pin header)
    public static final Pin GPIO_29 = createDigitalPin(29, "GPIO 29"); // requires B+ or newer model (40 pin header)

    // the following GPIO pins are only available on the Raspbery Pi Compute Module
    public static final Pin GPIO_30 = createDigitalPin(30, "GPIO 30"); // requires Compute Module
    public static final Pin GPIO_31 = createDigitalPin(31, "GPIO 31"); // requires Compute Module
    public static final Pin GPIO_32 = createDigitalPin(32, "GPIO 32"); // requires Compute Module
    public static final Pin GPIO_33 = createDigitalPin(33, "GPIO 33"); // requires Compute Module
    public static final Pin GPIO_34 = createDigitalPin(34, "GPIO 34"); // requires Compute Module
    public static final Pin GPIO_35 = createDigitalPin(35, "GPIO 35"); // requires Compute Module
    public static final Pin GPIO_36 = createDigitalPin(36, "GPIO 36"); // requires Compute Module
    public static final Pin GPIO_37 = createDigitalPin(37, "GPIO 37"); // requires Compute Module
    public static final Pin GPIO_38 = createDigitalPin(38, "GPIO 38"); // requires Compute Module
    public static final Pin GPIO_39 = createDigitalPin(39, "GPIO 39"); // requires Compute Module
    public static final Pin GPIO_40 = createDigitalPin(40, "GPIO 40"); // requires Compute Module
    public static final Pin GPIO_41 = createDigitalPin(41, "GPIO 41"); // requires Compute Module
    public static final Pin GPIO_42 = createDigitalPin(42, "GPIO 42"); // requires Compute Module
    public static final Pin GPIO_43 = createDigitalPin(43, "GPIO 43"); // requires Compute Module
    public static final Pin GPIO_44 = createDigitalPin(44, "GPIO 44"); // requires Compute Module
    public static final Pin GPIO_45 = createDigitalPin(45, "GPIO 45"); // requires Compute Module

    private static Pin createDigitalPin(int address, String name) {
        return new PinImpl(RaspiGpioProvider.NAME, address, name, 
                    EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT),
                    PinPullResistance.all());
    }

    private static Pin createDigitalAndPwmPin(int address, String name) {
        return new PinImpl(RaspiGpioProvider.NAME, address, name, 
                           EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, PinMode.PWM_OUTPUT),
                           PinPullResistance.all());
    }
}
