package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OrangePiZeroPin.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
 * OrangePiZero pin definitions for (default) WiringPi pin numbering scheme.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 * @author Balazs Matyas
 */
public class OrangePiZeroPin extends PinProvider {

    public static final Pin GPIO_00 = createDigitalPin(0, "GPIO 0");
    public static final Pin GPIO_01 = createDigitalPin(1, "GPIO 1");
    public static final Pin GPIO_02 = createDigitalPin(2, "GPIO 2");
    public static final Pin GPIO_03 = createDigitalPin(3, "GPIO 3");
    public static final Pin GPIO_04 = createDigitalOputputPin(4, "GPIO 4"); // this pin is permanently pulled up, i2c1
    public static final Pin GPIO_05 = createDigitalOputputPin(5, "GPIO 5"); // this pin is permanently pulled up, i2c1
    public static final Pin GPIO_06 = createDigitalPin(6, "GPIO 6");
    public static final Pin GPIO_07 = createDigitalPin(7, "GPIO 7");

    public static final Pin GPIO_08 = createDigitalOputputPin(8, "GPIO 8"); // this pin is permanently pulled up, i2c0
    public static final Pin GPIO_09 = createDigitalOputputPin(9, "GPIO 9"); // this pin is permanently pulled up, i2c0

    public static final Pin GPIO_10 = createDigitalPin(10, "GPIO 10");
    public static final Pin GPIO_11 = createDigitalPin(11, "GPIO 11");
    public static final Pin GPIO_12 = createDigitalPin(12, "GPIO 12");
    public static final Pin GPIO_13 = createDigitalPin(13, "GPIO 13");
    public static final Pin GPIO_14 = createDigitalPin(14, "GPIO 14");
    public static final Pin GPIO_15 = createDigitalPin(15, "GPIO 15");
    public static final Pin GPIO_16 = createDigitalPin(16, "GPIO 16");

    public static final Pin STAT_LED = createDigitalOputputPin(30, "STAT LED");

    protected static Pin createDigitalPin(int address, String name) {
        return createDigitalPin(OrangePiZeroGpioProvider.NAME, address, name);
    }

    protected static Pin createDigitalOputputPin(int address, String name) {
        return createPin(OrangePiZeroGpioProvider.NAME, address, name,
                EnumSet.of(PinMode.DIGITAL_OUTPUT, PinMode.SOFT_PWM_OUTPUT),
                EnumSet.noneOf(PinPullResistance.class),
                EnumSet.noneOf(PinEdge.class)
        );
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
