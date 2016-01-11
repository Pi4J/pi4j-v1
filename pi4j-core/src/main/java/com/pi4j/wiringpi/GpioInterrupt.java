package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioInterrupt.java  
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
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 * @author Heikki Vesalainen
 */
public class GpioInterrupt {

    private static Vector<GpioInterruptListener> listeners = new Vector<>();
    private static volatile Thread[] threads = new Thread[Gpio.NUM_PINS];

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
     * This method is used to to setup resources needed to enable
     * listening for changes to the selected GPIO pin.
     * </p>
     * 
     * <p>
     * <b>The GPIO pin must first be exported before it can be monitored.</b>
     * </p>
     * 
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @return A return value of a negative number represents an error. A return value of '0'
     *         represents success and that the GPIO pin was alreaedy set up. A return value
     *         of '1' represents success and that the setup was successful.
     */
    public static int enablePinStateChangeCallback(int pin) {
        if (threads[pin] != null) {
            return 0;
        } else {
            int ret = initPoll(pin);
            threads[pin] = new PollThread(pin);
            threads[pin].start();
            return ret;
        }
    }

    private static class PollThread extends Thread {
        public int index;
        public PollThread(int index) {
            super("Pi4J polling thread for pin #" + index);
            this.index = index;
        }
        public void run() {
            int previousValue = Gpio.digitalRead(index);

            while (GpioInterrupt.threads[index] != null) {
                int nextValue = GpioInterrupt.pollPinStateChange(index, previousValue, 10000);
                if (nextValue >= 0) {
                    if (nextValue != previousValue) {
                        GpioInterrupt.pinStateChangeCallback(index, nextValue == 1);
                    }
                    previousValue = nextValue;
                } else {
                    GpioInterrupt.closePoll(index);
                    throw new IllegalStateException("Pin #" + index + " could not be polled");
                }
            }
        }
    }

    /**
     * <p>
     * This method is used to instruct the native code to setup resources to enable
     * polling for changes to the selected GPIO pin.
     * </p>
     * 
     * <p>
     * <b>The GPIO pin must first be exported before it can be monitored.</b>
     * </p>
     * 
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @return A return value of a negative number represents an error. A return value of '0'
     *         represents success and that the GPIO pin was alreaedy set up. A return value
     *         of '1' represents success and that the setup was successful.
     */
    public static native int initPoll(int pin);
 
    /**
     * <p>
     * This method is used to release the resources related to listening to the 
     * selected GPIO pin.
     * </p>
     * 
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)

     * @return A return value of a negative number represents an error. A return value of '0'
     *         represents success and that the resources were free alread. A return value of '1' 
     *         represents success and that the resources were freed.
     */
    public static int disablePinStateChangeCallback(int pin) {
        if (threads[pin] != null) {
            try {
                Thread t = threads[pin];
                threads[pin] = null;
                t.join();
                return closePoll(pin);
            } catch (InterruptedException e) {
                return -1;
            }
        } else {
            return 0;
        }
    }

    /**
     * <p>
     * This method is used to instruct the native code to release
     * the resources related to polling the selected GPIO pin.
     * </p>
     * 
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)

     * @return A return value of a negative number represents an error. A return value of '0'
     *         represents success and that the resources were free alread. A return value of '1' 
     *         represents success and that the resources were freed.
     */
    public static native int closePoll(int pin);

    /**
     * <p>
     * Java consumer code can call this method to register itself as a listener for pin state
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
     * This method is provided as the callback handler for the Pi4J to invoke when a
     * GPIO interrupt is detected. This method should not be called from outside. (Thus
     * is is marked as a private method.)
     * </p>
     * 
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param state New GPIO pin state.
     */
    @SuppressWarnings("unchecked")
    private static void pinStateChangeCallback(int pin, boolean state) {

        Vector<GpioInterruptListener> dataCopy;
        dataCopy = (Vector<GpioInterruptListener>) listeners.clone();

        for (int i = 0; i < dataCopy.size(); i++) {
            GpioInterruptEvent event = new GpioInterruptEvent(listeners, pin, state);
            (dataCopy.elementAt(i)).pinStateChange(event);
        }
    }

    /**
     * Polls the given pin for a change.
     *
     * The pin has to be inited first with the 'initPin' method.
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param previousValue a previous value to which the new value is compared. The poll
     *        returns when the value changes unless a timeout occurs.
     * @param pollTimeout a timeout in millisecond.
     * @return A negative return value indicates error. If the poll
     *         timeouts, the 'previousValue' is returned.
     */
    public static native int pollPinStateChange(int pin, int previousValue, int pollTimeout);

    
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
