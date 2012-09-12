package com.pi4j.io.gpio.trigger;

import java.util.List;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

public class GpioToggleStateTrigger extends GpioTriggerBase
{
    private GpioPin targetPin;

    public GpioToggleStateTrigger(PinState state, GpioPin targetPin)
    {
        super();
        this.targetPin = targetPin;
    }
    
    public GpioToggleStateTrigger(GpioPin pin, PinState state, GpioPin targetPin)
    {
        super(state);
        this.targetPin = targetPin;
    }

    public GpioToggleStateTrigger(GpioPin pin, PinState[] states, GpioPin targetPin)
    {
        super(states);
        this.targetPin = targetPin;
    }

    public GpioToggleStateTrigger(GpioPin pin, List<PinState> states, GpioPin targetPin)
    {
        super(states);
        this.targetPin = targetPin;
    }
    
    public GpioToggleStateTrigger(GpioPin[] pins, PinState[] states, GpioPin targetPin)
    {
        super(states);
        this.targetPin = targetPin;
    }

    public GpioToggleStateTrigger(List<GpioPin> pins, List<PinState> states, GpioPin targetPin)
    {
        super(states);
        this.targetPin = targetPin;
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
            targetPin.toggle();
        }
    }
}
