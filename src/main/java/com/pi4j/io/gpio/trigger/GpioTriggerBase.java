package com.pi4j.io.gpio.trigger;

import java.util.List;
import java.util.Vector;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinState;

public abstract class GpioTriggerBase implements GpioTrigger
{

    private Vector<GpioPin> pins = new Vector<GpioPin>();
    private Vector<GpioPinState> states = new Vector<GpioPinState>();

    public GpioTriggerBase()
    {
    }
    
    public GpioTriggerBase(GpioPin pin, GpioPinState state)
    {
        addPin(pin);
        addPinState(state);
    }

    public GpioTriggerBase(GpioPin pin, GpioPinState[] states)
    {
        addPin(pin);
        addPinState(states);
    }

    public GpioTriggerBase(GpioPin pin, List<GpioPinState> states)
    {
        addPin(pin);
        addPinState(states);
    }
    
    public GpioTriggerBase(GpioPin[] pins, GpioPinState[] states)
    {
        addPin(pins);
        addPinState(states);
    }

    public GpioTriggerBase(List<GpioPin> pins, List<GpioPinState> states)
    {
        addPin(pins);
        addPinState(states);
    }
    
    public void addPin(GpioPin pin)
    {
        if(!pins.contains(pin))
            pins.add(pin);
    }

    public void addPin(GpioPin[] pins)
    {
        for(GpioPin pin : pins)
            addPin(pin);
    }

    public void addPin(List<GpioPin> pins)
    {
        for(GpioPin pin : pins)
            addPin(pin);
    }
    
    public void addPinState(GpioPinState state)
    {
        if(!states.contains(state))
            states.add(state);
    }

    public void addPinState(GpioPinState[] states)
    {
        for(GpioPinState state : states)
            addPinState(state);
    }

    public void addPinState(List<GpioPinState> states)
    {
        for(GpioPinState state : states)
            addPinState(state);
    }
    
    public boolean hasPin(GpioPin pin)
    {
        return pins.contains(pin);
    }

    public boolean hasPinState(GpioPinState state)
    {
        return states.contains(state);
    }

    public abstract void invoke(Gpio gpio, GpioPin pin, GpioPinState state);
}
