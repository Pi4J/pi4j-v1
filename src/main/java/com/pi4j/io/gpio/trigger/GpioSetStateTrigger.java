package com.pi4j.io.gpio.trigger;

import java.util.List;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

public class GpioSetStateTrigger extends GpioTriggerBase
{
    private GpioPin targetPin;
    private PinState targetPinState;

    public GpioSetStateTrigger(GpioPin targetPin, PinState targetPinState)
    {
        super();
        this.targetPin = targetPin;
        this.targetPinState = targetPinState;
    }
    
    public GpioSetStateTrigger(PinState state, GpioPin targetPin, PinState targetPinState)
    {
        super(state);
        this.targetPin = targetPin;
        this.targetPinState = targetPinState;
    }

    public GpioSetStateTrigger(PinState[] states, GpioPin targetPin, PinState targetPinState)
    {
        super(states);
        this.targetPin = targetPin;
        this.targetPinState = targetPinState;
    }

    public GpioSetStateTrigger(List<PinState> states, GpioPin targetPin, PinState targetPinState)
    {
        super(states);
        this.targetPin = targetPin;
        this.targetPinState = targetPinState;
    }
    
    public void setTargetPin(GpioPin pin)
    {
        targetPin = pin;
    }

    public GpioPin getTargetPin()
    {
        return targetPin;
    }

    public void setTargetPinState(PinState state)
    {
        targetPinState = state;
    }

    public PinState getTargetPinState()
    {
        return targetPinState;
    }
    
    @Override
    public void invoke(GpioPin pin, PinState state)
    {
        if(targetPin != null)
        {
            targetPin.setState(targetPinState);
        }
    }
}
