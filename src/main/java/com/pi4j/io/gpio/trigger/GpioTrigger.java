package com.pi4j.io.gpio.trigger;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinState;


public interface GpioTrigger
{
    boolean hasPin(GpioPin pin);
    boolean hasPinState(GpioPinState state);
    void invoke(Gpio gpio, GpioPin pin, GpioPinState state);
}
