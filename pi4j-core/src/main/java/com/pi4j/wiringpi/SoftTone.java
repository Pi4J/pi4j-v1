package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SoftTone.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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
 * <blockquote> This library depends on the wiringPi native system library. (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see <a href="https://pi4j.com/">https://pi4j.com/</a>
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
        NativeLibraryLoader.load("libpi4j");
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
