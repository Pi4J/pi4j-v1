package com.pi4j.io.gpio.trigger;

import java.util.List;
import java.util.Vector;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

public abstract class GpioTriggerBase implements GpioTrigger
{
    private Vector<PinState> states = new Vector<PinState>();

    public GpioTriggerBase()
    {
        addPinState(PinState.allStates());
    }
    
    public GpioTriggerBase(PinState state)
    {
        addPinState(state);
    }

    public GpioTriggerBase(PinState[] states)
    {
        addPinState(states);
    }

    public GpioTriggerBase(List<PinState> states)
    {
        addPinState(states);
    }
    
    public void addPinState(PinState state)
    {
        if(!states.contains(state))
            states.add(state);
    }

    public void addPinState(PinState[] states)
    {
        for(PinState state : states)
            addPinState(state);
    }

    public void addPinState(List<PinState> states)
    {
        for(PinState state : states)
            addPinState(state);
    }
    
    public boolean hasPinState(PinState state)
    {
        return states.contains(state);
    }

    public abstract void invoke(GpioPin pin, PinState state);
}
