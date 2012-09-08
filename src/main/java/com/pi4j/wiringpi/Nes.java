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
 * <h1>WiringPi NES Controller Library</h1>
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
 *      href="https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/">https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Nes
{
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

    // Load the platform library
    static
    {
        System.loadLibrary("pi4j");
    }

    /**
     * <h1>setupNesJoystick:</h1>
     * 
     * <p>
     * Create a new NES joystick interface, program the pins, etc.
     * </p>
     * 
     * @param dPin
     * @param cPin
     * @param lPin
     * @return
     */
    public static native int setupNesJoystick(int dPin, int cPin, int lPin);

    /**
     * <h1>readNesJoystick:</h1>
     * 
     * <p>
     * Do a single scan of the NES Joystick.
     * </p>
     * 
     * @param joystick
     * @return
     */
    public static native int readNesJoystick(int joystick);
}
