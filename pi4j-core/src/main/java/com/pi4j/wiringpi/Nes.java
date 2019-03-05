package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Nes.java
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
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see <a href="https://pi4j.com/">https://pi4j.com/</a>
 * @see <a
 *      href="http://wiringpi.com/dev-lib/">http://wiringpi.com/dev-lib/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Nes {

    public static final int NES_RIGHT = 0x01;
    public static final int NES_LEFT = 0x02;
    public static final int NES_DOWN = 0x04;
    public static final int NES_UP = 0x08;
    public static final int NES_START = 0x10;
    public static final int NES_SELECT = 0x20;
    public static final int NES_B = 0x40;
    public static final int NES_A = 0x80;
    public static final int PULSE_TIME = 25;
    public static final int MAX_NES_JOYSTICKS = 8;

    // private constructor
    private Nes() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * <p>setupNesJoystick:</p>
     *
     * <p>Create a new NES joystick interface, program the pins, etc.</p>
     *
     * @param dPin data pin
     * @param cPin clock pin
     * @param lPin pin number
     * @return return joystick handle
     */
    public static native int setupNesJoystick(int dPin, int cPin, int lPin);

    /**
     * <p>readNesJoystick:</p>
     *
     * <p>Do a single scan of the NES Joystick.</p>
     *
     * @param joystick joystick handle
     * @return return value
     */
    public static native int readNesJoystick(int joystick);
}
