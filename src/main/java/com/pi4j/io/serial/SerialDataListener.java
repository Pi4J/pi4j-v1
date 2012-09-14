package com.pi4j.io.serial;

/**
 * <h1>Serial Data Listener Interface</h1>
 * 
 * <p>
 * This interface implements the callback event handler for Serial Data events.
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
 * @see #SerialFactory
 * @see #Serial
 * @see #SerialDataEvent
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public interface SerialDataListener extends java.util.EventListener
{
    /**
     * <p>
     * This is the event callback method that will be invoked when new serial data is received.
     * </p>
     * 
     * @see #SerialDataEvent
     * @param event
     */
    void dataReceived(SerialDataEvent event);
}
