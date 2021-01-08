package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioInterrupt.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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

import java.util.Vector;

/**
 * <p>
 * This class provides static methods to configure the native Pi4J library to listen to GPIO
 * interrupts and invoke callbacks into this class. Additionally, this class provides a listener
 * registration allowing Java consumers to subscribe to GPIO pin state changes.
 * </p>
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
 * @see <a href="https://www.pi4j.com/">https://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class GpioInterrupt {

    private static Vector<GpioInterruptListener> listeners = new Vector<>();
    private Object lock;

    // private constructor
    private GpioInterrupt()  {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * <p>
     * This method is used to instruct the native code to setup a monitoring thread to monitor
     * interrupts that represent changes to the selected GPIO pin.
     * </p>
     *
     * <p>
     * <b>The GPIO pin must first be exported before it can be monitored.</b>
     * </p>
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @return A return value of a negative number represents an error. A return value of '0'
     *         represents success and that the GPIO pin is already being monitored. A return value
     *         of '1' represents success and that a new monitoring thread was created to handle the
     *         requested GPIO pin number.
     */
    public static native int enablePinStateChangeCallback(int pin);

    /**
     * <p>
     * This method is used to instruct the native code to stop the monitoring thread monitoring
     * interrupts on the selected GPIO pin.
     * </p>
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)

     * @return A return value of a negative number represents an error. A return value of '0'
     *         represents success and that no existing monitor was previously running. A return
     *         value of '1' represents success and that an existing monitoring thread was stopped
     *         for the requested GPIO pin number.
     */
    public static native int disablePinStateChangeCallback(int pin);

    /**
     * <p>
     * This method is provided as the callback handler for the Pi4J native library to invoke when a
     * GPIO interrupt is detected. This method should not be called from any Java consumers. (Thus
     * is is marked as a private method.)
     * </p>
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param state New GPIO pin state.
     */
    @SuppressWarnings("unchecked")
    private static void pinStateChangeCallback(int pin, boolean state) {

        Vector<GpioInterruptListener> listenersClone;
        listenersClone = (Vector<GpioInterruptListener>) listeners.clone();

        for (int i = 0; i < listenersClone.size(); i++) {
            GpioInterruptListener listener = listenersClone.elementAt(i);
            if(listener != null) {
                GpioInterruptEvent event = new GpioInterruptEvent(listener, pin, state);
                listener.pinStateChange(event);
            }
        }

        //System.out.println("GPIO PIN [" + pin + "] = " + state);
    }

    /**
     * <p>
     * Java consumer code can all this method to register itself as a listener for pin state
     * changes.
     * </p>
     *
     * @see com.pi4j.wiringpi.GpioInterruptListener
     * @see com.pi4j.wiringpi.GpioInterruptEvent
     *
     * @param listener A class instance that implements the GpioInterruptListener interface.
     */
    public static synchronized void addListener(GpioInterruptListener listener) {
        if (!listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }

    /**
     * <p>
     * Java consumer code can all this method to unregister itself as a listener for pin state
     * changes.
     * </p>
     *
     * @see com.pi4j.wiringpi.GpioInterruptListener
     * @see com.pi4j.wiringpi.GpioInterruptEvent
     *
     * @param listener A class instance that implements the GpioInterruptListener interface.
     */
    public static synchronized void removeListener(GpioInterruptListener listener) {
        if (listeners.contains(listener)) {
            listeners.removeElement(listener);
        }
    }


    /**
     * <p>
     * Returns true if the listener is already registered for event callbacks.
     * </p>
     *
     * @see com.pi4j.wiringpi.GpioInterruptListener
     * @see com.pi4j.wiringpi.GpioInterruptEvent
     *
     * @param listener A class instance that implements the GpioInterruptListener interface.
     */
    public static synchronized boolean hasListener(GpioInterruptListener listener) {
        return listeners.contains(listener);
    }
}
