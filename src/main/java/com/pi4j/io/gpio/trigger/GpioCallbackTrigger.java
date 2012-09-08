package com.pi4j.io.gpio.trigger;

import java.util.List;
import java.util.concurrent.Callable;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinState;

public class GpioCallbackTrigger extends GpioTriggerBase
{
    private Callable<Void> callback;
    
    public GpioCallbackTrigger() 
    {
        super();
    }
    
    public GpioCallbackTrigger(GpioPin pin, GpioPinState state, Callable<Void> callback)
    {
        super(pin,state);
        this.callback = callback;
    }

    public GpioCallbackTrigger(GpioPin pin, GpioPinState[] states, Callable<Void> callback)
    {
        super(pin,states);
        this.callback = callback;
    }

    public GpioCallbackTrigger(GpioPin pin, List<GpioPinState> states, Callable<Void> callback)
    {
        super(pin,states);
        this.callback = callback;
    }
    
    public GpioCallbackTrigger(GpioPin[] pins, GpioPinState[] states, Callable<Void> callback)
    {
        super(pins,states);
        this.callback = callback;
    }

    public GpioCallbackTrigger(List<GpioPin> pins, List<GpioPinState> states, Callable<Void> callback)
    {
        super(pins,states);
        this.callback = callback;
    }
    
    public void setCallback(Callable<Void> callback)
    {
        this.callback = callback;
    }

    @Override
    public void invoke(Gpio gpio, GpioPin pin, GpioPinState state)
    {        
        if(callback != null)
        {
            try
            {
                callback.call();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
