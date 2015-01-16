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


import com.pi4j.io.gpio.impl.PinImpl;

import java.util.EnumSet;

/**
 * Raspberry Pi pin definitions.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class RaspiPin  {
    
    public static final Pin GPIO_00 = createDigitalPin(0, "GPIO 0"); 
    public static final Pin GPIO_01 = createDigitalAndPwmPin(1, "GPIO 1"); // supports PWM0 [ALT5]
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

    // the following GPIO pins are only available on the Raspbery Pi Model A, B (revision 2.0), B+
    public static final Pin GPIO_17 = createDigitalPin(17, "GPIO 17"); // requires B rev2 or newer model (P5 header)
    public static final Pin GPIO_18 = createDigitalPin(18, "GPIO 18"); // requires B rev2 or newer model (P5 header)
    public static final Pin GPIO_19 = createDigitalPin(19, "GPIO 19"); // requires B rev2 or newer model (P5 header)
    public static final Pin GPIO_20 = createDigitalPin(20, "GPIO 20"); // requires B rev2 or newer model (P5 header)

    // the following GPIO pins are only available on the Raspbery Pi Model B+
    public static final Pin GPIO_21 = createDigitalPin(21, "GPIO 21"); // requires A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_22 = createDigitalPin(22, "GPIO 22"); // requires A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_23 = createDigitalAndPwmPin(23, "GPIO 23"); // requires A+, B+ or newer model (40 pin header) : supports PWM1 [ALT0]
    public static final Pin GPIO_24 = createDigitalAndPwmPin(24, "GPIO 24"); // requires A+, B+ or newer model (40 pin header) : supports PWM1 [ALT5]
    public static final Pin GPIO_25 = createDigitalPin(25, "GPIO 25"); // requires A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_26 = createDigitalAndPwmPin(26, "GPIO 26"); // requires A+, B+ or newer model (40 pin header) : supports PWM0 [ALT0]
    public static final Pin GPIO_27 = createDigitalPin(27, "GPIO 27"); // requires A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_28 = createDigitalPin(28, "GPIO 28"); // requires A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_29 = createDigitalPin(29, "GPIO 29"); // requires A+, B+ or newer model (40 pin header)

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
