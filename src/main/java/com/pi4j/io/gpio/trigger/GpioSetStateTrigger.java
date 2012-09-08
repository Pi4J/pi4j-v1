package com.pi4j.io.gpio.trigger;

import java.util.List;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinState;

public class GpioSetStateTrigger extends GpioTriggerBase
{
    private GpioPin targetPin;
    private GpioPinState targetPinState;
    
    public GpioSetStateTrigger() 
    {
        super();
    }
    
    public GpioSetStateTrigger(GpioPin pin, GpioPinState state, GpioPin targetPin, GpioPinState targetPinState)
    {
        super(pin,state);
        this.targetPin = targetPin;
        this.targetPinState = targetPinState;
    }

    public GpioSetStateTrigger(GpioPin pin, GpioPinState[] states, GpioPin targetPin, GpioPinState targetPinState)
    {
        super(pin,states);
        this.targetPin = targetPin;
        this.targetPinState = targetPinState;
    }

    public GpioSetStateTrigger(GpioPin pin, List<GpioPinState> states, GpioPin targetPin, GpioPinState targetPinState)
    {
        super(pin,states);
        this.targetPin = targetPin;
        this.targetPinState = targetPinState;
    }
    
    public GpioSetStateTrigger(GpioPin[] pins, GpioPinState[] states, GpioPin targetPin, GpioPinState targetPinState)
    {
        super(pins,states);
        this.targetPin = targetPin;
        this.targetPinState = targetPinState;
    }

    public GpioSetStateTrigger(List<GpioPin> pins, List<GpioPinState> states, GpioPin targetPin, GpioPinState targetPinState)
    {
        super(pins,states);
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

    public void setTargetPinState(GpioPinState state)
    {
        targetPinState = state;
    }

    public GpioPinState getTargetPinState()
    {
        return targetPinState;
    }
    
    @Override
    public void invoke(Gpio gpio, GpioPin pin, GpioPinState state)
    {
        if(targetPin != null)
        {
            gpio.setState(targetPin, targetPinState);
        }
    }
}
