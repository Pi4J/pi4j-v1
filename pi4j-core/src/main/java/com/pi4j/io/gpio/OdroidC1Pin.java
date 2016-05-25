package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OdroidC1Pin.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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


import com.pi4j.io.gpio.impl.PinImpl;

import java.util.EnumSet;

/**
 * Odroid-C1/C1+ pin definitions for (default) WiringPi pin numbering scheme.
 *
 * @link http://odroid.com/dokuwiki/doku.php?id=en:c1_hardware#expansion_connectors
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 *
 */
public class OdroidC1Pin extends PinProvider {

    // 11	GPIOY.BIT8	Export GPIO#88, Wiring Pi GPIO#0
    public static final Pin GPIO_00 = createDigitalPin(0, "GPIO 0");

    // 12	GPIOY.BIT7	Export GPIO#87, Wiring Pi GPIO#1
    public static final Pin GPIO_01 = createDigitalPin(1, "GPIO 1");

    // 13	GPIOX.BIT19	Export GPIO#116, Wiring Pi GPIO#2
    public static final Pin GPIO_02 = createDigitalPin(2, "GPIO 2");

    // 15	GPIOX.BIT18	Export GPIO#115, Wiring PI GPIO#3
    public static final Pin GPIO_03 = createDigitalPin(3, "GPIO 3");

    // 16	GPIOX.BIT7	Export GPIO#104, Wiring Pi GPIO#4
    public static final Pin GPIO_04 = createDigitalPin(4, "GPIO 4");

    // 18	GPIOX.BIT5	Export GPIO#102, Wiring Pi GPIO#5
    public static final Pin GPIO_05 = createDigitalPin(5, "GPIO 5");

    // 22	GPIOX.BIT6	Export GPIO#103, Wiring Pi GPIO#6
    public static final Pin GPIO_06 = createDigitalPin(6, "GPIO 6");

    // 7	GPIOY.BIT3	Export GPIO#83, Wiring Pi GPIO#7
    public static final Pin GPIO_07 = createDigitalPin(7, "GPIO 7");

    // 24	GPIOX.BIT20	Export GPIO#117, Wiring Pi GPIO#10
    public static final Pin GPIO_10 = createDigitalPin(10, "GPIO 10");

    // 26	GPIOX.BIT21	Export GPIO#118, Wiring Pi GPIO#11
    public static final Pin GPIO_11 = createDigitalPin(11, "GPIO 11");

    // 19   GPIOX.BIT10(MOSI)	Export GPIO#107, Wiring Pi GPIO#12, PWM1
    // PWM not currently supported, see known issues: https://github.com/Pi4J/pi4j/issues/229
    public static final Pin GPIO_12 = createDigitalPin(12, "GPIO 12");

    // 21	GPIOX.BIT9(MISO)	Export GPIO#106, Wiring Pi GPIO#13
    public static final Pin GPIO_13 = createDigitalPin(13, "GPIO 13");

    // 23	GPIOX.BIT8(SPI_SCLK)	Export GPIO#105, Wiring Pi GPIO#14
    public static final Pin GPIO_14 = createDigitalPin(14, "GPIO 14");

    // 29	GPIOX.BIT4	Export GPIO#101, Wiring Pi GPIO#21
    public static final Pin GPIO_21 = createDigitalPin(21, "GPIO 21");

    // 31	GPIOX.BIT3	Export GPIO#100, Wiring Pi GPIO#22
    public static final Pin GPIO_22 = createDigitalPin(22, "GPIO 22");

    // 33	GPIOX.BIT11	Export GPIO#108, Wiring Pi GPIO#23, PWM0
    // PWM not currently supported, see known issues: https://github.com/Pi4J/pi4j/issues/229
    public static final Pin GPIO_23 = createDigitalPin(23, "GPIO 23");

    // 35	GPIOX.BIT0	Export GPIO#97, Wiring Pi GPIO#24
    public static final Pin GPIO_24 = createDigitalPin(24, "GPIO 24");

    // 32	GPIOX.BIT2	Export GPIO#99, Wiring Pi#26
    public static final Pin GPIO_26 = createDigitalPin(26, "GPIO 26");

    // 36	GPIOX.BIT1	Wiring Pi GPIO#27
    public static final Pin GPIO_27 = createDigitalPin(27, "GPIO 27");

    // 40	ADC.AIN0  (!! 1.8 VDC MAX !!)
    public static final Pin AIN0 = createAnalogInputPin(0 + OdroidGpioProvider.AIN_ADDRESS_OFFSET, "AIN0");

    // 37	ADC.AIN1  (!! 1.8 VDC MAX !!)
    public static final Pin AIN1 = createAnalogInputPin(1 + OdroidGpioProvider.AIN_ADDRESS_OFFSET, "AIN1");

    private static Pin createAnalogInputPin(int address, String name) {
        return createAnalogInputPin(OdroidGpioProvider.NAME, address, name);
    }

    protected static Pin createDigitalPin(int address, String name) {
        return createDigitalPin(OdroidGpioProvider.NAME, address, name);
    }

    protected static Pin createDigitalAndPwmPin(int address, String name) {
        return createDigitalAndPwmPin(OdroidGpioProvider.NAME, address, name);
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
