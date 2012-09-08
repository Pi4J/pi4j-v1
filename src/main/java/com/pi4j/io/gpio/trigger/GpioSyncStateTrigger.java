package com.pi4j.io.gpio.trigger;

import java.util.List;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinState;

public class GpioSyncStateTrigger extends GpioTriggerBase
{
    private GpioPin targetPin;
    
    public GpioSyncStateTrigger() 
    {
        super();
    }
    
    public GpioSyncStateTrigger(GpioPin pin, GpioPinState state, GpioPin targetPin)
    {
        super(pin,state);
        this.targetPin = targetPin;
    }

    public GpioSyncStateTrigger(GpioPin pin, GpioPinState[] states, GpioPin targetPin)
    {
        super(pin,states);
        this.targetPin = targetPin;
    }

    public GpioSyncStateTrigger(GpioPin pin, List<GpioPinState> states, GpioPin targetPin)
    {
        super(pin,states);
        this.targetPin = targetPin;
    }
    
    public GpioSyncStateTrigger(GpioPin[] pins, GpioPinState[] states, GpioPin targetPin)
    {
        super(pins,states);
        this.targetPin = targetPin;
    }

    public GpioSyncStateTrigger(List<GpioPin> pins, List<GpioPinState> states, GpioPin targetPin)
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
            gpio.setState(targetPin, state);
        }
    }
}
