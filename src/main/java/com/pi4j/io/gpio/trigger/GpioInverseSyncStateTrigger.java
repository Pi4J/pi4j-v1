package com.pi4j.io.gpio.trigger;

import java.util.List;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

public class GpioInverseSyncStateTrigger extends GpioTriggerBase
{
    private GpioPin targetPin;

    public GpioInverseSyncStateTrigger(GpioPin targetPin)
    {
        super();
        this.targetPin = targetPin;
    }
    
    public GpioInverseSyncStateTrigger(PinState state, GpioPin targetPin)
    {
        super(state);
        this.targetPin = targetPin;
    }

    public GpioInverseSyncStateTrigger(PinState[] states, GpioPin targetPin)
    {
        super(states);
        this.targetPin = targetPin;
    }

    public GpioInverseSyncStateTrigger(List<PinState> states, GpioPin targetPin)
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
            if(state == PinState.HIGH)
                targetPin.setState(PinState.LOW);
            else
                targetPin.setState(PinState.HIGH);
        }
    }
}
