package com.pi4j.jni;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  AnalogInputMonitor.java
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


import java.util.Vector;

import com.pi4j.util.NativeLibraryLoader;

/**
 * <p>
 * This class provides static methods to configure the native Pi4J library to listen to GPIO
 * analog input value changes and invoke callbacks into this class. Additionally, this class
 * provides a listener registration allowing Java consumers to subscribe to GPIO analog input
 * value changes.
 * </p>
 *
 * @see <a href="https://pi4j.com/">https://pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class AnalogInputMonitor {

    private static Vector<AnalogInputListener> listeners = new Vector<>();
    private Object lock;

    // private constructor
    private AnalogInputMonitor()  {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j");
    }

    /**
     * <p>
     * This method is used to instruct the native code to setup a monitoring thread to monitor
     * analog input values for changes on the selected GPIO pin.
     * </p>
     *
     * @param pin GPIO pin number
     * @param pollingRate the polling rate in milliseconds for the input monitoring thread to
     *                    read analog input values from the hardware
     * @param changeThreshold the amount of change (delta) in the analog input value required before a new
     *                        analog input change event is dispatched.
     * @return A return value of a negative number represents an error. A return value of '0'
     *         represents success and that the GPIO pin is already being monitored. A return value
     *         of '1' represents success and that a new monitoring thread was created to handle the
     *         requested GPIO pin number.
     */
    public static native int enablePinValueChangeCallback(int pin, int pollingRate, double changeThreshold);

    /**
     * <p>
     * This method is used to instruct the native code to stop the monitoring thread monitoring
     * analog input values on the selected GPIO pin.
     * </p>
     *
     * @param pin GPIO pin number

     * @return A return value of a negative number represents an error. A return value of '0'
     *         represents success and that no existing monitor was previously running. A return
     *         value of '1' represents success and that an existing monitoring thread was stopped
     *         for the requested GPIO pin number.
     */
    public static native int disablePinValueChangeCallback(int pin);

    /**
     * <p>
     * This method is provided as the callback handler for the Pi4J native library to invoke when a
     * GPIO analog input calue change is detected. This method should not be called from any Java
     * consumers. (Thus it is marked as a private method.)
     * </p>
     *
     * @param pin GPIO pin number
     * @param value New GPIO analog input value
     */
    @SuppressWarnings("unchecked")
    private static void pinValueChangeCallback(int pin, double value) {

        Vector<AnalogInputListener> listenersClone;
        listenersClone = (Vector<AnalogInputListener>) listeners.clone();

        for (int i = 0; i < listenersClone.size(); i++) {
            AnalogInputListener listener = listenersClone.elementAt(i);
            if(listener != null) {
                AnalogInputEvent event = new AnalogInputEvent(listener, pin, value);
                listener.pinValueChange(event);
            }
        }
    }

    /**
     * <p>
     * Java consumer code can all this method to register itself as a listener for pin analog
     * input value changes.
     * </p>
     *
     * @see AnalogInputListener
     * @see AnalogInputEvent
     *
     * @param listener A class instance that implements the AnalogInputListener interface.
     */
    public static synchronized void addListener(AnalogInputListener listener) {
        if (!listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }

    /**
     * <p>
     * Java consumer code can all this method to unregister itself as a listener for pin analog
     * input value changes.
     * </p>
     *
     * @see AnalogInputListener
     * @see AnalogInputEvent
     *
     * @param listener A class instance that implements the AnalogInputListener interface.
     */
    public static synchronized void removeListener(AnalogInputListener listener) {
        if (listeners.contains(listener)) {
            listeners.removeElement(listener);
        }
    }


    /**
     * <p>
     * Returns true if the listener is already registered for event callbacks.
     * </p>
     *
     * @see AnalogInputListener
     * @see AnalogInputEvent
     *
     * @param listener A class instance that implements the AnalogInputListener interface.
     */
    public static synchronized boolean hasListener(AnalogInputListener listener) {
        return listeners.contains(listener);
    }
}
