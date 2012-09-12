package com.pi4j.io.gpio.impl;

import com.pi4j.io.gpio.GpioPin;

public class GpioPulseOffImpl implements Runnable
{
    private GpioPin pin;
    
    public GpioPulseOffImpl(GpioPin pin)
    {
        this.pin = pin;        
    }

    public void run()
    {
        pin.low();
    }
}
