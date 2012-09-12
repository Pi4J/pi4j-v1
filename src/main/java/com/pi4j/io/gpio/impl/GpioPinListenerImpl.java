package com.pi4j.io.gpio.impl;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.event.GpioPinStateChangeEvent;
import com.pi4j.io.gpio.trigger.GpioTrigger;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioInterruptListener;

public class GpioPinListenerImpl implements GpioInterruptListener
{
    private GpioPin pin;
    
    public GpioPinListenerImpl(GpioPin pin)
    {
        this.pin = pin;
    }
    
    /**
     * 
     * @param event
     */
    public void pinStateChange(GpioInterruptEvent event)
    {
        PinState state = PinState.getState(event.getState());

        // process events
        for(GpioListener listener : pin.getListeners())
        {
            listener.pinStateChanged(new GpioPinStateChangeEvent(this, pin, state));
        }
        
        // process triggers
        for(GpioTrigger trigger : pin.getTriggers())
        {
            if(trigger.hasPinState(state))
                trigger.invoke(pin, state);
        }
    }
}
