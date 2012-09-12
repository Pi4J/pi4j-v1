package com.pi4j.io.gpio.trigger;

import java.util.List;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

public class GpioPulseStateTrigger extends GpioTriggerBase
{
    private GpioPin targetPin;
    private long milliseconds = 1000;

    public GpioPulseStateTrigger(GpioPin targetPin, long milliseconds)
    {
        super();
        this.targetPin = targetPin;
        this.milliseconds = milliseconds;
    }
    
    public GpioPulseStateTrigger(PinState state, GpioPin targetPin, long milliseconds)
    {
        super(state);
        this.targetPin = targetPin;
        this.milliseconds = milliseconds;
    }

    public GpioPulseStateTrigger(PinState[] states, GpioPin targetPin, long milliseconds)
    {
        super(states);
        this.targetPin = targetPin;
        this.milliseconds = milliseconds;
    }

    public GpioPulseStateTrigger(List<PinState> states, GpioPin targetPin, long milliseconds)
    {
        super(states);
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
    public void invoke(GpioPin pin, PinState state)
    {
        if(targetPin != null)
        {
            targetPin.pulse(milliseconds);
        }
    }
}
