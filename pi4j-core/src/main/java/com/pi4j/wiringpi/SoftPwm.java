package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SoftPwm.java
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


import com.pi4j.util.NativeLibraryLoader;

/**
 * <p>
 * WiringPi includes a software-driven PWM handler capable of outputting a PWM signal on any of the
 * Raspberry Pi's GPIO pins.
 * </p>
 *
 * <p>
 * There are some limitations. To maintain a low CPU usage, the minimum pulse width is 100uS. That
 * combined with the default suggested range of 100 gives a PWM frequency of 100Hz. You can lower
 * the range to get a higher frequency, at the expense of resolution, or increase to get more
 * resolution, but that will lower the frequency. If you change the pulse-width in the drive code,
 * then be aware that at delays of less than 100uS wiringPi does it in a software loop, which means
 * that CPU usage will rise dramatically, and controlling more than one pin will be almost
 * impossible.
 * </p>
 *
 * <p>
 * Also note that while the routines run themselves at a higher and real-time priority, Linux can
 * still affect the accuracy of the generated signal.
 * </p>
 *
 * <p>
 * However, within these limitations, control of a light/LED or a motor is very achievable.
 * </p>
 *
 * <p>
 * <b> You must initialize wiringPi with one of wiringPiSetup() or wiringPiSetupGpio() functions
 * beforehand. wiringPiSetupSys() is not fast enough, so you must run your programs with sudo. </b>
 * </p>
 *
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * <li>pthread</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library. (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see <a href="https://www.pi4j.com/">https://www.pi4j.com/</a>
 * @see <a
 *      href="http://wiringpi.com/reference/software-pwm-library/">http://wiringpi.com/reference/software-pwm-library/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SoftPwm {

    // private constructor
    private SoftPwm() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * <p>int softPwmCreate (int pin, int initialValue, int pwmRange);</p>
     *
     * <p>
     * This creates a software controlled PWM pin. You can use any GPIO pin and the pin numbering
     * will be that of the wiringPiSetup function you used. Use 100 for the pwmRange, then the value
     * can be anything from 0 (off) to 100 (fully on) for the given pin.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/software-pwm-library/">http://wiringpi.com/reference/software-pwm-library/</a>
     *
     * @param pin The GPIO pin to use as a PWM pin.
     * @param value The value to initialize the PWM pin (between 0 <i>(off)</i> to 100 <i>(fully
     *            on)</i>)
     * @param range The maximum range. Use 100 for the pwmRange.
     * @return The return value is 0 for success. Anything else and you should check the global
     *         errno variable to see what went wrong.
     */
    public static native int softPwmCreate(int pin, int value, int range);

    /**
     * <p>void softPwmWrite (int pin, int value);</p>
     *
     * <p>
     * This updates the PWM value on the given pin. The value is checked to be in-range and pins
     * that haven't previously been initialized via softPwmCreate will be silently ignored.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/software-pwm-library/">http://wiringpi.com/reference/software-pwm-library/</a>
     *
     * @param pin The GPIO pin to use as a PWM pin.
     * @param value The value to initialize the PWM pin (between 0 <i>(off)</i> to 100 <i>(fully
     *            on)</i>)
     */
    public static native void softPwmWrite(int pin, int value);

    /**
     /**
     * <p>void softPwmStop (int pin);</p>
     *
     * <p>
     * This stops the software emulated PWM driver/thread for the given pin.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/software-pwm-library/">http://wiringpi.com/reference/software-pwm-library/>     *
     * @param pin The GPIO pin to use as a PWM pin.
     */
    public static native void softPwmStop(int pin);
}
