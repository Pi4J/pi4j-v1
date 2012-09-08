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

/**
 * <h1>WiringPi GPIO Utility</h1>
 * 
 * <p>
 * This utility class is provided to export, unexport, and manipulate pin direction.
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
 * @see <a
 *      href="https://projects.drogon.net/raspberry-pi/wiringpi/">https://projects.drogon.net/raspberry-pi/wiringpi/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class GpioUtil
{
    // Load the platform library
    static
    {
        System.loadLibrary("pi4j");
    }

    /**
     * <h1>GPIO PIN DIRECTION</h1>
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
     * <h1>GPIO PIN DIRECTION</h1>
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
     * <h1>GPIO PIN EDGE DETECTION</h1>
     * <p>
     * This constant is provided as an edge detection mode for use with the 'edge' method. This
     * constants instructs the edge detection to be disabled.
     * </p>
     * 
     * @see #edge(int,int)
     */
    public static final int EDGE_NONE = 0;

    /**
     * <h1>GPIO PIN EDGE DETECTION</h1>
     * <p>
     * This constant is provided as an edge detection mode for use with the 'edge' method. This
     * constants instructs the edge detection to only look for rising and falling pins states; pins
     * changing from LOW to HIGH or HIGH to LOW.
     * </p>
     * 
     * @see #edge(int,int)
     */
    public static final int EDGE_BOTH = 1;

    /**
     * <h1>GPIO PIN EDGE DETECTION</h1>
     * <p>
     * This constant is provided as an edge detection mode for use with the 'edge' method. This
     * constants instructs the edge detection to only look for rising pins states; pins changing
     * from LOW to HIGH.
     * </p>
     * 
     * @see #edge(int,int)
     */
    public static final int EDGE_RISING = 2;

    /**
     * <h1>GPIO PIN EDGE DETECTION</h1>
     * <p>
     * This constant is provided as an edge detection mode for use with the 'edge' method. This
     * constants instructs the edge detection to only look for falling pins states; pins changing
     * from HIGH to LOW.
     * </p>
     * 
     * @see #edge(int,int)
     */
    public static final int EDGE_FALLING = 3;

    /**
     * <h1>Export GPIO Pin</h1>
     * 
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
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     * @param direction
     */
    public static native void export(int pin, int direction) throws RuntimeException;

    /**
     * <h1>Unexport GPIO Pin</h1>
     * 
     * <p>
     * This method will unexport the selected GPIO pin.
     * </p>
     * <p>
     * This method required root permissions access.
     * </p>
     * 
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     */
    public static native void unexport(int pin) throws RuntimeException;

    /**
     * <h1>Is GPIO Pin Exported</h1>
     * 
     * <p>
     * This method determines if the requested GPIO pin is already exported.
     * </p>
     * 
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     * @return <p>
     *         A return value of '0' represents that the pin is not exported. </br> A return value
     *         of '1' represents that the pin is exported.
     *         </p>
     */
    public static native boolean isExported(int pin) throws RuntimeException;

    /**
     * <h1>Set GPIO Pin Edge Detection</h1>
     * 
     * <p>
     * This method will set the selected GPIO pin's edge detection. Edge detection instructs when
     * the hardware GPIO changes raise interrupts on the system.
     * </p>
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
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     * @param edge <p>
     *            The edge condition to detect: none, rising, falling, or both. </br>The following
     *            constants are provided for use with this parameter:
     *            <ul>
     *            <li>EDGE_NONE</li>
     *            <li>EDGE_BOTH</li>
     *            <li>EDGE_RISING</li>
     *            <li>EDGE_FALLING</li>
     *            </ul>
     *            </p>
     * @return <p>
     *         A return value of '0' represents success. Errors are returned as negative numbers.
     *         </p>
     */
    public static native boolean setEdgeDetection(int pin, int edge) throws RuntimeException;

    /**
     * <h1>Get GPIO Pin Edge Detection</h1>
     * 
     * <p>
     * This method will get the selected GPIO pin's edge detection setting. Edge detection instructs
     * when the hardware GPIO changes raise interrupts on the system.
     * </p>
     * 
     * @see #EDGE_NONE
     * @see #EDGE_BOTH
     * @see #EDGE_RISING
     * @see #EDGE_FALLING
     * 
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     * @return <p>
     *         The edge condition detected on the selected pin: none, rising, falling, or both.
     *         </br>The following constants are provided for use with this parameter:
     *         <ul>
     *         <li>EDGE_NONE</li>
     *         <li>EDGE_BOTH</li>
     *         <li>EDGE_RISING</li>
     *         <li>EDGE_FALLING</li>
     *         </ul>
     *         </p>
     */
    public static native int getEdgeDetection(int pin) throws RuntimeException;

    /**
     * <h1>Set GPIO Pin Direction</h1>
     * 
     * <p>
     * This method will set the selected GPIO pin's export direction.
     * </p>
     * 
     * @see #DIRECTION_IN
     * @see #DIRECTION_OUT
     * 
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     * @param direction <p>
     *            The export direction to apply: IN, OUT. </br>The following constants are provided
     *            for use with this parameter:
     *            <ul>
     *            <li>DIRECTION_IN</li>
     *            <li>DIRECTION_OUT</li>
     *            </ul>
     *            </p>
     * @return <p>
     *         A return value of '0' represents success. Errors are returned as negative numbers.
     *         </p>
     */
    public static native boolean setDirection(int pin, int direction) throws RuntimeException;

    /**
     * <h1>Get GPIO Pin Direction</h1>
     * 
     * <p>
     * This method will get the selected GPIO pin's export direction.
     * </p>
     * 
     * @see #DIRECTION_IN
     * @see #DIRECTION_OUT
     * 
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     * @return <p>
     *         The GPIO pin's configured export direction is returned: IN (0), OUT (1). </br>The
     *         following constants are provided for use with this parameter:
     *         <ul>
     *         <li>DIRECTION_IN</li>
     *         <li>DIRECTION_OUT</li>
     *         </ul>
     *         </p>
     */
    public static native int getDirection(int pin) throws RuntimeException;
}
