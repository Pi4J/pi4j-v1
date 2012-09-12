package com.pi4j.io.gpio.exception;

import com.pi4j.io.gpio.Pin;

public class GpioPinExistsException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 1171484082578232353L;

    private Pin pin;

    public GpioPinExistsException(Pin pin)
    {
        super("This GPIO pin already exists: " + pin.toString());
        this.pin = pin;
    }

    public Pin getPin()
    {
        return pin;
    }
}
