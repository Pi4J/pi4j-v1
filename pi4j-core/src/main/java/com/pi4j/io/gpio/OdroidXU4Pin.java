package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OdroidXU4Pin.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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




/**
 * Odroid-XU4 pin definitions for (default) WiringPi pin numbering scheme.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class OdroidXU4Pin extends PinProvider {

    // (!! 1.8 VDC MAX ON ALL GPIO PINS !!)
    public static final Pin GPIO_00 = createDigitalPin(0,  "GPIO 0");
    public static final Pin GPIO_01 = createDigitalPin(1,  "GPIO 1");
    public static final Pin GPIO_02 = createDigitalPin(2,  "GPIO 2");
    public static final Pin GPIO_03 = createDigitalPin(3,  "GPIO 3");
    public static final Pin GPIO_04 = createDigitalPin(4,  "GPIO 4");
    public static final Pin GPIO_05 = createDigitalPin(5,  "GPIO 5");
    public static final Pin GPIO_06 = createDigitalPin(6,  "GPIO 6");
    public static final Pin GPIO_07 = createDigitalPin(7,  "GPIO 7");
    public static final Pin GPIO_08 = createDigitalPin(8,  "GPIO 8");
    public static final Pin GPIO_09 = createDigitalPin(9,  "GPIO 9");
    public static final Pin GPIO_10 = createDigitalPin(10, "GPIO 10");
    public static final Pin GPIO_11 = createDigitalPin(11, "GPIO 11");
    public static final Pin GPIO_12 = createDigitalPin(12, "GPIO 12");
    public static final Pin GPIO_13 = createDigitalPin(13, "GPIO 13");
    public static final Pin GPIO_14 = createDigitalPin(14, "GPIO 14");
    public static final Pin GPIO_15 = createDigitalPin(15, "GPIO 15");
    public static final Pin GPIO_16 = createDigitalPin(16, "GPIO 16");
    public static final Pin GPIO_21 = createDigitalPin(21, "GPIO 21");
    public static final Pin GPIO_22 = createDigitalPin(22, "GPIO 22");
    public static final Pin GPIO_23 = createDigitalPin(23, "GPIO 23");
    public static final Pin GPIO_26 = createDigitalPin(26, "GPIO 26");
    public static final Pin GPIO_27 = createDigitalPin(27, "GPIO 27");
    public static final Pin GPIO_30 = createDigitalPin(30, "GPIO 30");
    public static final Pin GPIO_31 = createDigitalPin(31, "GPIO 31");

    // (!! 1.8 VDC MAX ON ALL AIN PINS !!)
    // 03	ADC_0.AIN0  (!! 1.8 VDC MAX !!)
    public static final Pin AIN0 = createAnalogInputPin(0 + OdroidGpioProvider.AIN_ADDRESS_OFFSET, "AIN0");

    // 23	ADC_0.AIN3  (!! 1.8 VDC MAX !!)
    public static final Pin AIN3 = createAnalogInputPin(1 + OdroidGpioProvider.AIN_ADDRESS_OFFSET, "AIN3");

    private static Pin createAnalogInputPin(int address, String name) {
        return createAnalogInputPin(OdroidGpioProvider.NAME, address, name);
    }

    protected static Pin createDigitalPin(int address, String name) {
        return createDigitalPin(OdroidGpioProvider.NAME, address, name);
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
}
