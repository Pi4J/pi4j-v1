package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  NanoPiPin.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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

/**
 * NanoPi Pin definitions for (default) WiringPi pin numbering scheme.
 *
 */
public class NanoPiPin extends PinProvider {

    public static final Pin GPIO_00 = createDigitalPin(0, "GPIO 0");
    public static final Pin GPIO_01 = createDigitalAndPwmPinNoEdge(1, "GPIO 1"); // supports PWM
    public static final Pin GPIO_02 = createDigitalPin(2, "GPIO 2");
    public static final Pin GPIO_03 = createDigitalPin(3, "GPIO 3");
    public static final Pin GPIO_04 = createDigitalPin(4, "GPIO 4");
    public static final Pin GPIO_05 = createDigitalPin(5, "GPIO 5");
    public static final Pin GPIO_06 = createDigitalPin(6, "GPIO 6");
    public static final Pin GPIO_07 = createDigitalPin(7, "GPIO 7");
    public static final Pin GPIO_08 = createDigitalPinNoEdge(8, "GPIO 8",
            EnumSet.of(PinPullResistance.OFF, PinPullResistance.PULL_UP)); // this pin is permanently pulled up
    public static final Pin GPIO_09 = createDigitalPinNoEdge(9, "GPIO 9",
            EnumSet.of(PinPullResistance.OFF, PinPullResistance.PULL_UP)); // this pin is permanently pulled up
    public static final Pin GPIO_10 = createDigitalPin(10, "GPIO 10");
    public static final Pin GPIO_11 = createDigitalPin(11, "GPIO 11");
    public static final Pin GPIO_12 = createDigitalPin(12, "GPIO 12");
    public static final Pin GPIO_13 = createDigitalPin(13, "GPIO 13");
    public static final Pin GPIO_14 = createDigitalPin(14, "GPIO 14");
    public static final Pin GPIO_15 = createDigitalPin(15, "GPIO 15");
    public static final Pin GPIO_16 = createDigitalPin(16, "GPIO 16");

    public static final Pin GPIO_21 = createDigitalPinNoEdge(21, "GPIO 21");
    public static final Pin GPIO_22 = createDigitalPinNoEdge(22, "GPIO 22");
    public static final Pin GPIO_23 = createDigitalPinNoEdge(23, "GPIO 23");
    public static final Pin GPIO_24 = createDigitalPinNoEdge(24, "GPIO 24");
    public static final Pin GPIO_25 = createDigitalPinNoEdge(25, "GPIO 25");
    public static final Pin GPIO_26 = createDigitalPinNoEdge(26, "GPIO 26");
    public static final Pin GPIO_27 = createDigitalPinNoEdge(27, "GPIO 27");
    public static final Pin GPIO_28 = createDigitalPinNoEdge(28, "GPIO 28");
    public static final Pin GPIO_29 = createDigitalPinNoEdge(29, "GPIO 29");
    //public static final Pin GPIO_30 = createDigitalPinNoEdge(30, "GPIO 30"); << This is SoC GPIO-00 which cannot be exported.
    public static final Pin GPIO_31 = createDigitalPinNoEdge(31, "GPIO 31",
            EnumSet.noneOf(PinPullResistance.class));  // this pin is permanently pulled up; pin pull settings do not work

    protected static Pin createDigitalPin(int address, String name) {
        return createDigitalPin(NanoPiGpioProvider.NAME, address, name);
    }

    protected static Pin createDigitalPinNoEdge(int address, String name, EnumSet<PinPullResistance> resistance) {
        return createDigitalPin(NanoPiGpioProvider.NAME, address, name, resistance, EnumSet.noneOf(PinEdge.class));
    }

    protected static Pin createDigitalPinNoEdge(int address, String name) {
        return createDigitalPin(NanoPiGpioProvider.NAME, address, name, EnumSet.noneOf(PinEdge.class));
    }

    protected static Pin createDigitalAndPwmPin(int address, String name) {
        return createDigitalAndPwmPin(NanoPiGpioProvider.NAME, address, name);
    }

    protected static Pin createDigitalAndPwmPinNoEdge(int address, String name) {
        return createDigitalAndPwmPin(NanoPiGpioProvider.NAME, address, name, EnumSet.noneOf(PinEdge.class));
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
