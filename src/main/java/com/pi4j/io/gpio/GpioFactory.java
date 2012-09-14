package com.pi4j.io.gpio;

import com.pi4j.io.gpio.impl.GpioController;

/**
 * <h1>GPIO Factory</h1>
 * 
 * <p>
 * This factory class provides a static method to create new 'Gpio' instances.
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
 * @see #com.pi4j.io.gpio.Gpio
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class GpioFactory
{
    /**
     * <h1>Create New Serial instance</h1>
     * 
     * @return <p>
     *         Return a new Serial impl instance.
     *         </p>
     */
    public static Gpio createInstance()
    {
        return new GpioController();
    }
}
