package com.pi4j.io.gpio.tasks.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioEventDispatchTaskImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.pi4j.io.gpio.GpioPinInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.*;
import com.pi4j.io.gpio.trigger.GpioTrigger;

import java.util.ArrayList;
import java.util.Collection;

public class GpioEventDispatchTaskImpl implements Runnable {

    private final GpioPinInput pin;
    private final PinEvent event;

    public GpioEventDispatchTaskImpl(GpioPinInput pin, PinEvent event) {
        this.event = event;
        this.pin = pin;
    }

    @Override
    public void run() {
        // only process listeners and triggers if the received interrupt event
        // matches the pin number being tracked my this class instance
        if (this.pin.getPin().equals(event.getPin())) {
            if (event.getEventType() == PinEventType.DIGITAL_STATE_CHANGE) {
                PinState state = ((PinDigitalStateChangeEvent) event).getState();

                // create a copy of the listeners collection
                Collection<GpioPinListener> listeners  = new ArrayList<>(pin.getListeners());

                // process event callbacks for digital listeners
                for (GpioPinListener listener : listeners) {
                    if(listener != null && listener instanceof GpioPinListenerDigital) {
                        ((GpioPinListenerDigital) listener)
                            .handleGpioPinDigitalStateChangeEvent(new GpioPinDigitalStateChangeEvent(
                                    event.getSource(), pin, state));
                    }
                }

                // create a copy of the triggers collection
                Collection<GpioTrigger> triggers  = new ArrayList<>(pin.getTriggers());

                // process triggers
                for (GpioTrigger trigger : triggers) {
                    if (trigger != null && trigger.hasPinState(state)) {
                        trigger.invoke(pin, state);
                    }
                }
            } else if (event.getEventType() == PinEventType.ANALOG_VALUE_CHANGE) {
                double value = ((PinAnalogValueChangeEvent) event).getValue();

                // create a copy of the listeners collection
                Collection<GpioPinListener> listeners  = new ArrayList<>(pin.getListeners());

                // process event callbacks for analog listeners
                for (GpioPinListener listener : listeners) {
                    if (listener != null && listener instanceof GpioPinListenerAnalog) {
                        ((GpioPinListenerAnalog) listener)
                            .handleGpioPinAnalogValueChangeEvent(new GpioPinAnalogValueChangeEvent(
                                    event.getSource(), pin, value));
                    }
                }
            }
        }
    }
}
