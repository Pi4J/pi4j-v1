package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  PiFacePin.java  
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


import java.util.EnumSet;
import com.pi4j.io.gpio.impl.PinImpl;

/**
 * Raspberry Pi pin definitions.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class PiFacePin  {
    
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
    public static final Pin GPIO_17 = createDigitalPin(17, "GPIO 17"); 
    public static final Pin GPIO_18 = createDigitalPin(18, "GPIO 18"); 
    public static final Pin GPIO_19 = createDigitalPin(19, "GPIO 19"); 
    public static final Pin GPIO_20 = createDigitalPin(20, "GPIO 20"); 
    
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
