package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  RaspiBcmPin.java
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


/**
 * Raspberry Pi pin definitions for the Broadcom pin numbering scheme.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class RaspiBcmPin extends PinProvider {

    public static final Pin GPIO_02 = createDigitalPin(2, "GPIO 2");         // <P1:03>
    public static final Pin GPIO_03 = createDigitalPin(3, "GPIO 3");         // <P1:05>
    public static final Pin GPIO_04 = createDigitalPin(4, "GPIO 4");         // <P1:07>
    public static final Pin GPIO_05 = createDigitalPin(5, "GPIO 5");         // <P1:29> requires 2B, A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_06 = createDigitalPin(6, "GPIO 6");         // <P1:31> requires 2B, A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_07 = createDigitalPin(7, "GPIO 7");         // <P1:26>
    public static final Pin GPIO_08 = createDigitalPin(8, "GPIO 8");         // <P1:24>
    public static final Pin GPIO_09 = createDigitalPin(9, "GPIO 9");         // <P1:21>
    public static final Pin GPIO_10 = createDigitalPin(10, "GPIO 10");       // <P1:09>
    public static final Pin GPIO_11 = createDigitalPin(11, "GPIO 11");       // <P1:23>
    public static final Pin GPIO_12 = createDigitalAndPwmPin(12, "GPIO 12"); // <P1:32> requires 2B, A+, B+ or newer model (40 pin header) : supports PWM0 [ALT0]
    public static final Pin GPIO_13 = createDigitalAndPwmPin(13, "GPIO 13"); // <P1:33> requires 2B, A+, B+ or newer model (40 pin header) : supports PWM1 [ALT0]
    public static final Pin GPIO_14 = createDigitalPin(14, "GPIO 14");       // <P1:08>
    public static final Pin GPIO_15 = createDigitalPin(15, "GPIO 15");       // <P1:10>
    public static final Pin GPIO_16 = createDigitalPin(16, "GPIO 16");       // <P1:36> requires 2B, A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_17 = createDigitalPin(17, "GPIO 17");       // <P1:11>
    public static final Pin GPIO_18 = createDigitalAndPwmPin(18, "GPIO 18"); // <P1:12> supports PWM0 [ALT5]
    public static final Pin GPIO_19 = createDigitalAndPwmPin(19, "GPIO 19"); // <P1:35> requires 2B, A+, B+ or newer model (40 pin header) : supports PWM1 [ALT5]
    public static final Pin GPIO_20 = createDigitalPin(20, "GPIO 20");       // <P1:38> requires 2B, A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_21 = createDigitalPin(21, "GPIO 21");       // <P1:40> requires 2B, A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_22 = createDigitalPin(22, "GPIO 22");       // <P1:05>
    public static final Pin GPIO_23 = createDigitalPin(23, "GPIO 23");       // <P1:16>
    public static final Pin GPIO_24 = createDigitalPin(24, "GPIO 24");       // <P1:18>
    public static final Pin GPIO_25 = createDigitalPin(25, "GPIO 25");       // <P1:22>
    public static final Pin GPIO_26 = createDigitalPin(26, "GPIO 26");       // <P1:37> requires 2B, A+, B+ or newer model (40 pin header)
    public static final Pin GPIO_27 = createDigitalPin(27, "GPIO 27");       // <P1:13>

    // the following GPIO pins are only available on the Raspbery Pi Model A, B (revision 2.0), B+
    public static final Pin GPIO_28 = createDigitalPin(28, "GPIO 28");       // <P5:03> requires B rev2 or newer model (P5 header)
    public static final Pin GPIO_29 = createDigitalPin(29, "GPIO 29");       // <P1:04> requires B rev2 or newer model (P5 header)
    public static final Pin GPIO_30 = createDigitalPin(30, "GPIO 30");       // <P1:05> requires B rev2 or newer model (P5 header)
    public static final Pin GPIO_31 = createDigitalPin(31, "GPIO 31");       // <P1:06> requires B rev2 or newer model (P5 header)

    protected static Pin createDigitalPin(int address, String name) {
        return createDigitalPin(RaspiGpioProvider.NAME, address, name);
    }

    protected static Pin createDigitalAndPwmPin(int address, String name) {
        return createDigitalAndPwmPin(RaspiGpioProvider.NAME, address, name);
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
