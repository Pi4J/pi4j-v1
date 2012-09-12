package com.pi4j.io.gpio.trigger;

import java.util.List;
import java.util.concurrent.Callable;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

public class GpioCallbackTrigger extends GpioTriggerBase
{
    private Callable<Void> callback;
    
    public GpioCallbackTrigger(Callable<Void> callback)
    {
        super();
        this.callback = callback;
    }

    public GpioCallbackTrigger(PinState state, Callable<Void> callback)
    {
        super(state);
        this.callback = callback;
    }

    public GpioCallbackTrigger(PinState[] states, Callable<Void> callback)
    {
        super(states);
        this.callback = callback;
    }

    public GpioCallbackTrigger(List<PinState> states, Callable<Void> callback)
    {
        super(states);
        this.callback = callback;
    }
    
    public void setCallback(Callable<Void> callback)
    {
        this.callback = callback;
    }

    @Override
    public void invoke(GpioPin pin, PinState state)
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
