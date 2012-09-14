package com.pi4j.wiringpi;

import java.util.EventObject;

/**
 * <h1>Gpio Interrupt Event</h1>
 * 
 * <p>
 * This class provides the event object for GPIO interrupt state changes.
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
public class GpioInterruptEvent extends EventObject
{
    private static final long serialVersionUID = 1L;
    private int pin;
    private boolean state;

    /**
     * <h1>Default event constructor</h1>
     * 
     * @param obj <p>
     *            Ignore this parameter
     *            </p>
     * @param pin <p>
     *            GPIO pin number (not header pin number; not wiringPi pin number)
     *            </p>
     * @param state <p>
     *            New GPIO pin state.
     *            </p>
     */
    public GpioInterruptEvent(Object obj, int pin, boolean state)
    {
        super(obj);
        this.pin = pin;
        this.state = state;
    }

    /**
     * <p>
     * Get the pin number that changed and raised this event.
     * </p>
     * 
     * @return <p>
     *         GPIO pin number (not header pin number; not wiringPi pin number)
     *         </p>
     */
    public int getPin()
    {
        return pin;
    }

    /**
     * <p>
     * Get the new pin state raised in this event.
     * </p>
     * 
     * @return <p>
     *         GPIO pin state (HIGH=true, LOW=false)
     *         </p>
     */
    public boolean getState()
    {
        return state;
    }

    /**
     * <p>
     * Get the new pin state value raised in this event.
     * </p>
     * 
     * @return <p>
     *         GPIO pin state (HIGH=1, LOW=0)
     *         </p>
     */
    public int getStateValue()
    {
        return (state == true) ? 1 : 0;
    }
}
