package com.pi4j.io.serial;

import java.util.EventObject;

/**
 * <h1>Serial Data Event</h1>
 * 
 * <p>
 * This class provides the serial data event object.
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
 * @see #SerialDataListener
 * @see #SerialFactory
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SerialDataEvent extends EventObject
{
    private static final long serialVersionUID = 1L;
    private String data;

    /**
     * <h1>Default event constructor</h1>
     * 
     * @param obj <p>
     *            The com.pi4j.io.serial.Serial class instance that initiates this event.
     *            </p>
     * @param data <p>
     *            The data received.
     *            </p>
     */
    public SerialDataEvent(Object obj, String data)
    {
        super(obj);
        this.data = data;
    }

    /**
     * <p>
     * Get the data string received.
     * </p>
     * 
     * @return <p>
     *         The data string received.
     *         </p>
     */
    public String getData()
    {
        return data;
    }
}
