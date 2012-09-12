package com.pi4j.io.gpio.exception;

import com.pi4j.io.gpio.Pin;

public class GpioPinNotProvisionedException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 1171484082578232353L;

    private Pin pin;

    public GpioPinNotProvisionedException(Pin pin)
    {
        super("This GPIO pin has not been provisioned: " + pin.toString());
        this.pin = pin;
    }

    public Pin getPin()
    {
        return pin;
    }
}
