/*
 * **********************************************************************
 * This file is part of the pi4j project: http://www.pi4j.com/
 * 
 * pi4j is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * pi4j is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with pi4j. If not,
 * see <http://www.gnu.org/licenses/>.
 * **********************************************************************
 */
package com.pi4j.wiringpi;

import java.util.Vector;

/**
 * <h1>GpioInterrupt Monitoring Class</h1>
 * 
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
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class GpioInterrupt
{
    private static Vector<GpioInterruptListener> listeners = new Vector<GpioInterruptListener>();
    private Object lock;

    // Load the platform library
    static
    {
        System.loadLibrary("pi4j");
    }

    /**
     * <h1>Enable Monitoring on GPIO Pin</h1>
     * 
     * <p>
     * This method is used to instruct the native code to setup a monitoring thread to monitor
     * interrupts that represent changes to the selected GPIO pin.
     * </p>
     * 
     * <p>
     * <b>The GPIO pin must first be exported before it can be monitored.</b>
     * </p>
     * 
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     * @return <p>
     *         A return value of a negative number represents an error. A return value of '0'
     *         represents success and that the GPIO pin is already being monitored. A return value
     *         of '1' represents success and that a new monitoring thread was created to handle the
     *         requested GPIO pin number.
     *         </p>
     */
    public static native int enablePinStateChangeCallback(int pin);

    /**
     * <h1>Disable Monitoring on GPIO Pin</h1>
     * 
     * <p>
     * This method is used to instruct the native code to stop the monitoring thread monitoring
     * interrupts on the selected GPIO pin.
     * </p>
     * 
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     * @return <p>
     *         A return value of a negative number represents an error. A return value of '0'
     *         represents success and that no existing monitor was previously running. A return
     *         value of '1' represents success and that an existing monitoring thread was stopped
     *         for the requested GPIO pin number.
     *         </p>
     */
    public static native int disablePinStateChangeCallback(int pin);

    /**
     * <h1>Pin State Change Callback</h1>
     * 
     * <p>
     * This method is provided as the callback handler for the Pi4J native library to invoke when a
     * GPIO interrupt is detected. This method should not be called from any Java consumers. (Thus
     * is is marked as a private method.)
     * </p>
     * 
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     * @param state <p>
     *            New GPIO pin state.
     *            </p>
     */
    @SuppressWarnings("unchecked")
    private static void pinStateChangeCallback(int pin, boolean state)
    {
        Vector<GpioInterruptListener> dataCopy;
        dataCopy = (Vector<GpioInterruptListener>) listeners.clone();

        for (int i = 0; i < dataCopy.size(); i++)
        {
            GpioInterruptEvent event = new GpioInterruptEvent(listeners, pin, state);
            ((GpioInterruptListener) dataCopy.elementAt(i)).pinStateChange(event);
        }

        // System.out.println("GPIO PIN [" + pin + "] = " + state);
    }

    /**
     * <h1>Add GPIO Interrupt Listener</h1>
     * 
     * <p>
     * Java consumer code can all this method to register itself as a listener for pin state
     * changes.
     * </p>
     * 
     * @see #GpioInterruptListener
     * @see #GpioInterruptEvent
     * 
     * @param listener <p>
     *            A class instance that implements the GpioInterruptListener interface.
     *            </p>
     */
    public static synchronized void addListener(GpioInterruptListener listener)
    {
        listeners.addElement(listener);
    }

    /**
     * <h1>Remove GPIO Interrupt Listener</h1>
     * 
     * <p>
     * Java consumer code can all this method to unregister itself as a listener for pin state
     * changes.
     * </p>
     * 
     * @see #GpioInterruptListener
     * @see #GpioInterruptEvent
     * 
     * @param listener <p>
     *            A class instance that implements the GpioInterruptListener interface.
     *            </p>
     */
    public static synchronized void removeListener(GpioInterruptListener listener)
    {
        listeners.removeElement(listener);
    }
}
