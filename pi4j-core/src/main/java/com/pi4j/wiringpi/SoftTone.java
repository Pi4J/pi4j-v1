package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SoftTone.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
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


import com.pi4j.util.NativeLibraryLoader;

/**
 * <p>
 * WiringPi includes a software-driven sound handler capable of outputting a simple tone/square wave signal on any of
 * the Raspberry Pi’s GPIO pins.
 * </p>
 *
 * <p>
 * There are some limitations… To maintain a low CPU usage, the minimum pulse width is 100μS. That gives a maximum
 * frequency of  1/0.0002 = 5000Hz.
 * </p>
 *
 * <p>
 * Also note that while the routines run themselves at a higher and real-time priority, Linux can still affect the
 * accuracy of the generated tone.
 * </p>
 *
 * <p>
 * However, within these limitations, simple tones on a high impedance speaker or piezo sounder is possible.
 * </p>
 *
 * <p>
 * NOTES:  - Each pin activated in softTone mode uses approximately 0.5% of the CPU.
           - You need to keep your program running to maintain the sound output!
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @see <a
 *      href="http://wiringpi.com/reference/software-tone-library/">http://wiringpi.com/reference/software-tone-library/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SoftTone {

    // private constructor
    private SoftTone() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * <p>int softToneCreate (int pin)</p>
     *
     * <p>
     * This creates a software controlled tone pin. You can use any GPIO pin and the pin numbering will be that of
     * the wiringPiSetup() function you used.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/software-tone-library/">http://wiringpi.com/reference/software-tone-library/</a>
     *
     * @param pin The GPIO pin to use as a PWM pin.
     *            </p>
     * @return The return value is 0 for success. Anything else and you should check the global
     *         errno variable to see what went wrong.
     */
    public static native int softToneCreate(int pin);

    /**
     * <p>void softToneWrite (int pin, int frequency);</p>
     *
     * <p>
     * This updates the tone frequency value on the given pin. The tone will be played until you set the frequency to 0.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/software-tone-library/">http://wiringpi.com/reference/software-tone-library/</a>
     *
     * @param pin The GPIO pin to use.
     * @param frequency The frequency value set on the GPIO pin.  Set of value of '0' to stop the tone.
     */
    public static native void softToneWrite(int pin, int frequency);

    /**
     * <p>void softToneStop (int pin);</p>
     *
     * <p>
     * This stops any tone frequency value on the given pin.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/software-tone-library/">http://wiringpi.com/reference/software-tone-library/</a>
     *
     * @param pin The GPIO pin to use.
     */
    public static native void softToneStop(int pin);
}
