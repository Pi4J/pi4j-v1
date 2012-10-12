package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinListenerImpl.java  
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
import com.pi4j.io.gpio.event.GpioPinStateChangeEvent;
import com.pi4j.io.gpio.trigger.GpioTrigger;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioInterruptListener;

public class GpioPinListenerImpl implements GpioInterruptListener
{
    private final GpioPin pin;

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
        // only process listeners and triggers if the received interrupt event
        // matches the pin number being tracked my this class instance 
        if (this.pin.getPin().getAddress() == event.getPin())
        {
            PinState state = PinState.getState(event.getState());

            // process events
            for (GpioListener listener : pin.getListeners())
            {
                listener.pinStateChanged(new GpioPinStateChangeEvent(this, pin, state));
            }

            // process triggers
            for (GpioTrigger trigger : pin.getTriggers())
            {
                if (trigger.hasPinState(state))
                    trigger.invoke(pin, state);
            }
        }
    }
}
