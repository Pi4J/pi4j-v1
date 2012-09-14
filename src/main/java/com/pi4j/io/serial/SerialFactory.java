package com.pi4j.io.serial;

import com.pi4j.io.serial.impl.SerialImpl;

/**
 * <h1>Serial Factory</h1>
 * 
 * <p>
 * This factory class provide a static method to create new 'Serial' instances.
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
 * @see #Serial
 * @see #SerialDataEvent
 * @see #SerialDataListener
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SerialFactory
{
    /**
     * <h1>Create New Serial instance</h1>
     * 
     * @return <p>
     *         Return a new Serial implementation instance.
     *         </p>
     */
    public static Serial createInstance()
    {
        return new SerialImpl();
    }
}
