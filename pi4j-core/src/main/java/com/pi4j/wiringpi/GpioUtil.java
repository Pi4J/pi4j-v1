package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioUtil.java
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


import com.pi4j.util.NativeLibraryLoader;

/**
 * <p>This utility class is provided to export, unexport, and manipulate pin direction.</p>
 *
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
 *      href="http://wiringpi.com/">http://wiringpi.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class GpioUtil {
    // private constructor
    private GpioUtil() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * <p>GPIO PIN DIRECTION</p>
     * <p>
     * GPIO pin constant for IN direction for reading pin states
     * </p>
     *
     * @see #export(int,int)
     * @see #setDirection(int,int)
     * @see #getDirection(int)
     */
    public static final int DIRECTION_IN = 0;

    /**
     * <p>GPIO PIN DIRECTION</p>
     * <p>
     * GPIO pin constant for OUT direction for writing digital pin states (0/1).
     * </p>
     *
     * @see #export(int,int)
     * @see #setDirection(int,int)
     * @see #getDirection(int)
     */
    public static final int DIRECTION_OUT = 1;

    /**
     * <p>GPIO PIN DIRECTION</p>
     * <p>
     * GPIO pin constant for OUT direction with an initial default HIGH value for writing digital pin states (0/1).
     * </p>
     *
     * @see #export(int,int)
     * @see #setDirection(int,int)
     * @see #getDirection(int)
     */
    public static final int DIRECTION_HIGH = 2;

    /**
     * <p>GPIO PIN DIRECTION</p>
     * <p>
     * GPIO pin constant for OUT direction with an initial default LOW value for writing digital pin states (0/1).
     * </p>
     *
     * @see #export(int,int)
     * @see #setDirection(int,int)
     * @see #getDirection(int)
     */
    public static final int DIRECTION_LOW= 3;

    /**
     * <p>GPIO PIN EDGE DETECTION</p>
     * <p>
     * This constant is provided as an edge detection mode for use with the 'edge' method. This
     * constants instructs the edge detection to be disabled.
     * </p>
     *
     * @see #setEdgeDetection(int,int)
     */
    public static final int EDGE_NONE = 0;

    /**
     * <p>GPIO PIN EDGE DETECTION</p>
     * <p>
     * This constant is provided as an edge detection mode for use with the 'edge' method. This
     * constants instructs the edge detection to only look for rising and falling pins states; pins
     * changing from LOW to HIGH or HIGH to LOW.
     * </p>
     *
     * @see #setEdgeDetection(int,int)
     */
    public static final int EDGE_BOTH = 1;

    /**
     * <p>GPIO PIN EDGE DETECTION</p>
     * <p>
     * This constant is provided as an edge detection mode for use with the 'edge' method. This
     * constants instructs the edge detection to only look for rising pins states; pins changing
     * from LOW to HIGH.
     * </p>
     *
     * @see #setEdgeDetection(int,int)
     */
    public static final int EDGE_RISING = 2;

    /**
     * <p>GPIO PIN EDGE DETECTION</p>
     * <p>
     * This constant is provided as an edge detection mode for use with the 'edge' method. This
     * constants instructs the edge detection to only look for falling pins states; pins changing
     * from HIGH to LOW.
     * </p>
     *
     * @see #setEdgeDetection(int,int)
     */
    public static final int EDGE_FALLING = 3;

    /**
     * <p>
     * This method will export the selected GPIO pin.
     * </p>
     * <p>
     * This method required root permissions access.
     * </p>
     *
     * @see #DIRECTION_IN
     * @see #DIRECTION_OUT
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param direction pin direction
     */
    public static native void export(int pin, int direction) throws RuntimeException;

    /**
     * <p>This method will unexport the selected GPIO pin.</p>
     * <p>This method required root permissions access.</p>
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     */
    public static native void unexport(int pin) throws RuntimeException;

    /**
     * <p>This method determines if the requested GPIO pin is already exported.</p>
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @return A return value of '0' represents that the pin is not exported. </br> A return value
     *         of '1' represents that the pin is exported.
     */
    public static native boolean isExported(int pin) throws RuntimeException;

    /**
     * <p>This method will set the selected GPIO pin's edge detection. Edge detection instructs when
     * the hardware GPIO changes raise interrupts on the system.</p>
     * <p>
     * NOTE: Calling this method will automatically export the pin and set the pin direction to
     * INPUT.</br> This method required root permissions access.
     * </p>
     *
     * @see #EDGE_NONE
     * @see #EDGE_BOTH
     * @see #EDGE_RISING
     * @see #EDGE_FALLING
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param edge The edge condition to detect: none, rising, falling, or both. </br>The following
     *            constants are provided for use with this parameter:
     *            <ul>
     *            <li>EDGE_NONE</li>
     *            <li>EDGE_BOTH</li>
     *            <li>EDGE_RISING</li>
     *            <li>EDGE_FALLING</li>
     *            </ul>
     * @return A return value of '0' represents success. Errors are returned as negative numbers.
     */
    public static native boolean setEdgeDetection(int pin, int edge) throws RuntimeException;

    /**
     * <p>This method will get the selected GPIO pin's edge detection setting. Edge detection instructs
     * when the hardware GPIO changes raise interrupts on the system.
     * </p>
     *
     * @see #EDGE_NONE
     * @see #EDGE_BOTH
     * @see #EDGE_RISING
     * @see #EDGE_FALLING
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @return The edge condition detected on the selected pin: none, rising, falling, or both.
     *         </br>The following constants are provided for use with this parameter:
     *         <ul>
     *         <li>EDGE_NONE</li>
     *         <li>EDGE_BOTH</li>
     *         <li>EDGE_RISING</li>
     *         <li>EDGE_FALLING</li>
     *         </ul>
     */
    public static native int getEdgeDetection(int pin) throws RuntimeException;

    /**
     * <p>This method will set the selected GPIO pin's export direction.</p>
     *
     * @see #DIRECTION_IN
     * @see #DIRECTION_OUT
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param direction
     *            The export direction to apply: IN, OUT. </br>The following constants are provided
     *            for use with this parameter:
     *            <ul>
     *            <li>DIRECTION_IN</li>
     *            <li>DIRECTION_OUT</li>
     *            </ul>
     * @return A return value of '0' represents success. Errors are returned as negative numbers.
     */
    public static native boolean setDirection(int pin, int direction) throws RuntimeException;

    /**
     * <p>
     * This method will get the selected GPIO pin's export direction.
     * </p>
     *
     * @see #DIRECTION_IN
     * @see #DIRECTION_OUT
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @return The GPIO pin's configured export direction is returned: IN (0), OUT (1). </br>The
     *         following constants are provided for use with this parameter:
     *         <ul>
     *         <li>DIRECTION_IN</li>
     *         <li>DIRECTION_OUT</li>
     *         </ul>
     */
    public static native int getDirection(int pin) throws RuntimeException;

    /**
     * <p>
     * This method will return a value of '1' if the pin is supported
     * </p>
     *
     * @param pin pin number
     * @return '1' is the pin is supported, else '0'
     */
    public static native int isPinSupported(int pin) throws RuntimeException;

    /**
     * <p>
     * This method will return a value of '1' if Privileged access is required.
     * This method will return a value of '0' if Privileged access is NOT required.
     * Privileged access is required if any of the the following conditions are not met:
     *    - You are running with Linux kernel version 4.1.7 or greater
     *    - The Device Tree is enabled
     *    - The 'bcm2835_gpiomem' kernel module loaded.
     *    - Udev rules are configured to permit write access to '/sys/class/gpio/**'
     *
     * </p>
     *
     * @return 'true' if privileged access is required; else returns 'false'.
     */
    public static native boolean isPrivilegedAccessRequired() throws RuntimeException;

    /**
     * <p>
     *  This method is used to enable non-privileged access to the GPIO pins on
     *  to system.  This method will throw a runtime exception if privileged access
     *  is required.  You can test for required access using the 'isPrivilegedAccessRequired()'
     *  method.
     *
     *  Please note when non-privileged access is enabled, you will not be able to
     *  use any hardware PWM or CLOCK functions.
     * </p>
     */
    public static native void enableNonPrivilegedAccess() throws RuntimeException;
}
