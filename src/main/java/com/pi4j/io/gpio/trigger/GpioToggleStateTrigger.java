package com.pi4j.io.gpio.trigger;

import java.util.List;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinState;

public class GpioToggleStateTrigger extends GpioTriggerBase
{
    private GpioPin targetPin;
    
    public GpioToggleStateTrigger() 
    {
        super();
    }
    
    public GpioToggleStateTrigger(GpioPin pin, GpioPinState state, GpioPin targetPin)
    {
        super(pin,state);
        this.targetPin = targetPin;
    }

    public GpioToggleStateTrigger(GpioPin pin, GpioPinState[] states, GpioPin targetPin)
    {
        super(pin,states);
        this.targetPin = targetPin;
    }

    public GpioToggleStateTrigger(GpioPin pin, List<GpioPinState> states, GpioPin targetPin)
    {
        super(pin,states);
        this.targetPin = targetPin;
    }
    
    public GpioToggleStateTrigger(GpioPin[] pins, GpioPinState[] states, GpioPin targetPin)
    {
        super(pins,states);
        this.targetPin = targetPin;
    }

    public GpioToggleStateTrigger(List<GpioPin> pins, List<GpioPinState> states, GpioPin targetPin)
    {
        super(pins,states);
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
    public void invoke(Gpio gpio, GpioPin pin, GpioPinState state)
    {
        if(targetPin != null)
        {
            gpio.toggleState(targetPin);
        }
    }
}
