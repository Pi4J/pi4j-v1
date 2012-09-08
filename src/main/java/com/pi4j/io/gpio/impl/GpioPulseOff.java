package com.pi4j.io.gpio.impl;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinState;

public class GpioPulseOff implements Runnable
{
    private Gpio gpio;
    private GpioPin pin;
    
    public GpioPulseOff(Gpio gpio, GpioPin pin)
    {
        this.gpio = gpio;
        this.pin = pin;        
    }

    public void run()
    {
        gpio.setState(pin, GpioPinState.LOW);
    }
}
