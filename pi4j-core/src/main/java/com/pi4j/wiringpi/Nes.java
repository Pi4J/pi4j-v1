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
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
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
