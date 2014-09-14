package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  RCMPin.java  
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
 * Raspberry Pi Compute Module pin definitions.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 *
 *  For PWM pin definitions see: http://elinux.org/RPi_BCM2835_GPIOs
 */
@SuppressWarnings("unused")
public class RCMPin {
    
    public static final Pin GPIO_00 = createDigitalPin(0, "GPIO 0"); 
    public static final Pin GPIO_01 = createDigitalPin(1, "GPIO 1");
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
    public static final Pin GPIO_12 = createDigitalAndPwmPin(12, "GPIO 12"); // supports PWM0 [ALT0]
    public static final Pin GPIO_13 = createDigitalAndPwmPin(13, "GPIO 13"); // supports PWM1 [ALT0]
    public static final Pin GPIO_14 = createDigitalPin(14, "GPIO 14"); 
    public static final Pin GPIO_15 = createDigitalPin(15, "GPIO 15"); 
    public static final Pin GPIO_16 = createDigitalPin(16, "GPIO 16");
    public static final Pin GPIO_17 = createDigitalPin(17, "GPIO 17");
    public static final Pin GPIO_18 = createDigitalAndPwmPin(18, "GPIO 18"); // supports PWM0 [ALT5]
    public static final Pin GPIO_19 = createDigitalAndPwmPin(19, "GPIO 19"); // supports PWM1 [ALT5]
    public static final Pin GPIO_20 = createDigitalPin(20, "GPIO 20");
    public static final Pin GPIO_21 = createDigitalPin(21, "GPIO 21");
    public static final Pin GPIO_22 = createDigitalPin(22, "GPIO 22");
    public static final Pin GPIO_23 = createDigitalPin(23, "GPIO 23");
    public static final Pin GPIO_24 = createDigitalPin(24, "GPIO 24");
    public static final Pin GPIO_25 = createDigitalPin(25, "GPIO 25");
    public static final Pin GPIO_26 = createDigitalPin(26, "GPIO 26");
    public static final Pin GPIO_27 = createDigitalPin(27, "GPIO 27");
    public static final Pin GPIO_28 = createDigitalPin(28, "GPIO 28");
    public static final Pin GPIO_29 = createDigitalPin(29, "GPIO 29");
    public static final Pin GPIO_30 = createDigitalPin(30, "GPIO 30");
    public static final Pin GPIO_31 = createDigitalPin(31, "GPIO 31");
    public static final Pin GPIO_32 = createDigitalPin(32, "GPIO 32");
    public static final Pin GPIO_33 = createDigitalPin(33, "GPIO 33");
    public static final Pin GPIO_34 = createDigitalPin(34, "GPIO 34");
    public static final Pin GPIO_35 = createDigitalPin(35, "GPIO 35");
    public static final Pin GPIO_36 = createDigitalPin(36, "GPIO 36");
    public static final Pin GPIO_37 = createDigitalPin(37, "GPIO 37");
    public static final Pin GPIO_38 = createDigitalPin(38, "GPIO 38");
    public static final Pin GPIO_39 = createDigitalPin(39, "GPIO 39");
    public static final Pin GPIO_40 = createDigitalAndPwmPin(40, "GPIO 40"); // supports PWM0 [ALT0]
    public static final Pin GPIO_41 = createDigitalAndPwmPin(41, "GPIO 41"); // supports PWM1 [ALT0]
    public static final Pin GPIO_42 = createDigitalPin(42, "GPIO 42");
    public static final Pin GPIO_43 = createDigitalPin(43, "GPIO 43");
    public static final Pin GPIO_44 = createDigitalPin(44, "GPIO 44");
    public static final Pin GPIO_45 = createDigitalAndPwmPin(45, "GPIO 45"); // supports PWM1 [ALT0]

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
