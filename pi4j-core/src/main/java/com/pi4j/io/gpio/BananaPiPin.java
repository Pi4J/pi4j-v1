package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  BananaPiPin.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

/**
 * LeMaker BananaPi pin definitions for (default) WiringPi pin numbering scheme.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class BananaPiPin extends PinProvider {

    public static final Pin GPIO_00 = createDigitalPin(0, "GPIO 0");
    public static final Pin GPIO_01 = createDigitalPin(1, "GPIO 1");
    public static final Pin GPIO_02 = createDigitalPin(2, "GPIO 2");
    public static final Pin GPIO_03 = createDigitalPin(3, "GPIO 3");
    public static final Pin GPIO_04 = createDigitalPin(4, "GPIO 4");
    public static final Pin GPIO_05 = createDigitalPin(5, "GPIO 5");
    public static final Pin GPIO_06 = createDigitalPin(6, "GPIO 6");
    public static final Pin GPIO_07 = createDigitalAndPwmPinNoEdge(7, "GPIO 7");
    public static final Pin GPIO_08 = createDigitalPinNoEdge(8, "GPIO 8", EnumSet.of(PinPullResistance.OFF, PinPullResistance.PULL_UP));
    public static final Pin GPIO_09 = createDigitalPinNoEdge(9, "GPIO 9", EnumSet.of(PinPullResistance.OFF, PinPullResistance.PULL_UP));
    public static final Pin GPIO_10 = createDigitalPin(10, "GPIO 10");
    public static final Pin GPIO_11 = createDigitalPin(11, "GPIO 11");
    public static final Pin GPIO_12 = createDigitalPin(12, "GPIO 12");
    public static final Pin GPIO_13 = createDigitalPin(13, "GPIO 13");
    public static final Pin GPIO_14 = createDigitalPin(14, "GPIO 14");
    public static final Pin GPIO_15 = createDigitalPin(15, "GPIO 15");
    public static final Pin GPIO_16 = createDigitalPin(16, "GPIO 16");
    public static final Pin GPIO_17 = createDigitalPin(17, "GPIO 17");
    public static final Pin GPIO_18 = createDigitalPinNoEdge(18, "GPIO 18");
    public static final Pin GPIO_19 = createDigitalPin(19, "GPIO 19");
    public static final Pin GPIO_20 = createDigitalPinNoEdge(20, "GPIO 20");

    protected static Pin createDigitalPin(int address, String name) {
        return createDigitalPin(BananaPiGpioProvider.NAME, address, name);
    }

    protected static Pin createDigitalPinNoEdge(int address, String name, EnumSet<PinPullResistance> resistance) {
        return createDigitalPin(BananaPiGpioProvider.NAME, address, name, resistance, EnumSet.noneOf(PinEdge.class));
    }

    protected static Pin createDigitalPinNoEdge(int address, String name) {
        return createDigitalPin(BananaPiGpioProvider.NAME, address, name, EnumSet.noneOf(PinEdge.class));
    }

    protected static Pin createDigitalAndPwmPin(int address, String name) {
        return createDigitalAndPwmPin(BananaPiGpioProvider.NAME, address, name);
    }

    protected static Pin createDigitalAndPwmPinNoEdge(int address, String name) {
        return createDigitalAndPwmPin(BananaPiGpioProvider.NAME, address, name, EnumSet.noneOf(PinEdge.class));
    }

    // *override* static method from subclass
    // (overriding a static method is not supported in Java
    //  so this method definition will hide the subclass static method)
    public static Pin getPinByName(String name) {
        return PinProvider.getPinByName(name);
    }

    // *override* static method from subclass
    // (overriding a static method is not supported in Java
    //  so this method definition will hide the subclass static method)
    public static Pin getPinByAddress(int address) {
        return PinProvider.getPinByAddress(address);
    }

    // *override* static method from subclass
    // (overriding a static method is not supported in Java
    //  so this method definition will hide the subclass static method)
    public static Pin[] allPins() { return PinProvider.allPins(); }

    // *override* static method from subclass
    // (overriding a static method is not supported in Java
    //  so this method definition will hide the subclass static method)
    public static Pin[] allPins(PinMode ... mode) { return PinProvider.allPins(mode); }
}
