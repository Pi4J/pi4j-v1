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
 * <h1>WiringPi Software PWM Library</h1>
 * 
 * <p>
 * WiringPi includes a software-driven PWM handler capable of outputting a PWM signal on any of the
 * Raspberry Pi’s GPIO pins.
 * </p>
 * 
 * <p>
 * There are some limitations… To maintain a low CPU usage, the minimum pulse width is 100uS. That
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
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @see <a
 *      href="https://projects.drogon.net/raspberry-pi/wiringpi/software-pwm-library/">https://projects.drogon.net/raspberry-pi/wiringpi/software-pwm-library/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SoftPwm
{
    // Load the platform library
    static
    {
        System.loadLibrary("pi4j");
    }

    /**
     * <h1>int softPwmCreate (int pin, int initialValue, int pwmRange);</h1>
     * 
     * <p>
     * This creates a software controlled PWM pin. You can use any GPIO pin and the pin numbering
     * will be that of the wiringPiSetup function you used. Use 100 for the pwmRange, then the value
     * can be anything from 0 (off) to 100 (fully on) for the given pin.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/software-pwm-library/">https://projects.drogon.net/raspberry-pi/wiringpi/software-pwm-library/</a>
     * 
     * @param pin <p>
     *            The GPIO pin to use as a PWM pin.
     *            </p>
     * @param value <p>
     *            The value to initialize the PWM pin (between 0 <i>(off)</i> to 100 <i>(fully
     *            on)</i>)
     *            </p>
     * @param range <p>
     *            The maximum range. Use 100 for the pwmRange.
     *            </p>
     * @return <p>
     *         The return value is 0 for success. Anything else and you should check the global
     *         errno variable to see what went wrong.
     *         </p>
     */
    public static native int softPwmCreate(int pin, int value, int range);

    /**
     * <h1>void softPwmWrite (int pin, int value);</h1>
     * 
     * <p>
     * This updates the PWM value on the given pin. The value is checked to be in-range and pins
     * that haven't previously been initialized via softPwmCreate will be silently ignored.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/software-pwm-library/">https://projects.drogon.net/raspberry-pi/wiringpi/software-pwm-library/</a>
     * @param pin <p>
     *            The GPIO pin to use as a PWM pin.
     *            </p>
     * @param value <p>
     *            The value to initialize the PWM pin (between 0 <i>(off)</i> to 100 <i>(fully
     *            on)</i>)
     *            </p>
     */
    public static native void softPwmWrite(int pin, int value);
}
