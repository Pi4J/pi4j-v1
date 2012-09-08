package com.pi4j.io.gpio.trigger;

import java.util.List;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinState;

public class GpioPulseStateTrigger extends GpioTriggerBase
{
    private GpioPin targetPin;
    private long milliseconds = 1000;
    
    public GpioPulseStateTrigger() 
    {
        super();
    }
    
    public GpioPulseStateTrigger(GpioPin pin, GpioPinState state, GpioPin targetPin, long milliseconds)
    {
        super(pin,state);
        this.targetPin = targetPin;
        this.milliseconds = milliseconds;
    }

    public GpioPulseStateTrigger(GpioPin pin, GpioPinState[] states, GpioPin targetPin, long milliseconds)
    {
        super(pin,states);
        this.targetPin = targetPin;
        this.milliseconds = milliseconds;
    }

    public GpioPulseStateTrigger(GpioPin pin, List<GpioPinState> states, GpioPin targetPin, long milliseconds)
    {
        super(pin,states);
        this.targetPin = targetPin;
        this.milliseconds = milliseconds;
    }
    
    public GpioPulseStateTrigger(GpioPin[] pins, GpioPinState[] states, GpioPin targetPin, long milliseconds)
    {
        super(pins,states);
        this.targetPin = targetPin;
        this.milliseconds = milliseconds;
    }

    public GpioPulseStateTrigger(List<GpioPin> pins, List<GpioPinState> states, GpioPin targetPin, long milliseconds)
    {
        super(pins,states);
        this.targetPin = targetPin;
        this.milliseconds = milliseconds;
    }
    
    public void setTargetPin(GpioPin pin)
    {
        targetPin = pin;
    }

    public GpioPin getTargetPin()
    {
        return targetPin;
    }

    @Override
    public void invoke(Gpio gpio, GpioPin pin, GpioPinState state)
    {
        if(targetPin != null)
        {
            gpio.pulse(targetPin, milliseconds);
        }
    }
}
