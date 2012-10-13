package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioEventMonitorImpl.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * #L%
 */

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.PinEvent;
import com.pi4j.io.gpio.event.PinEventType;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public class GpioEventMonitorImpl implements PinListener
{
    private final GpioPin pin;

    public GpioEventMonitorImpl(GpioPin pin)
    {
        this.pin = pin;
    }
    
    @Override
    public void handlePinEvent(PinEvent event)
    {
        // only process listeners and triggers if the received interrupt event
        // matches the pin number being tracked my this class instance 
        if (this.pin.getPin().equals(event.getPin()))
        {
            if(event.getEventType() == PinEventType.DIGITAL_STATE_CHANGE)
            {
                PinState state = ((PinDigitalStateChangeEvent)event).getState();
                
                // process events
                for (GpioListener listener : pin.getListeners())
                {
                    listener.handleGpioPinEvent(new GpioPinDigitalStateChangeEvent(event.getSource(), pin, state));
                }
    
                // process triggers
                for (GpioTrigger trigger : pin.getTriggers())
                {
                    if (trigger.hasPinState(state))
                        trigger.invoke(pin, state);
                }
            }
            else if(event.getEventType() == PinEventType.ANALOG_VALUE_CHANGE)
            {
                int value = ((PinAnalogValueChangeEvent)event).getValue();

                // process events
                for (GpioListener listener : pin.getListeners())
                {
                    listener.handleGpioPinEvent(new GpioPinAnalogValueChangeEvent(event.getSource(), pin, value));
                }
            }
        }
    }
}
