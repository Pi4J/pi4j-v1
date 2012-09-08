package com.pi4j.io.gpio.event;

import java.util.EventObject;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinState;

public class GpioPinStateChangeEvent extends EventObject
{
    static final long serialVersionUID = 1L;
    private GpioPin pin;
    private GpioPinState state;

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
    public GpioPinStateChangeEvent(Object obj, GpioPin pin, GpioPinState state)
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
    public GpioPin getPin()
    {
        return pin;
    }

    /**
     * <p>
     * Get the new pin state raised in this event.
     * </p>
     * 
     * @return <p>
     *         GPIO pin state (HIGH, LOW)
     *         </p>
     */
    public GpioPinState getState()
    {
        return state;
    }
}
