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
 * Copyright (C) 2012 - 2017 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.pi4j.io.gpio.GpioPinInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.PinEvent;
import com.pi4j.io.gpio.event.PinEventType;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unused")
public class GpioEventMonitorImpl implements PinListener {
    private final GpioPinInput pin;

    public GpioEventMonitorImpl(GpioPinInput pin) {
        this.pin = pin;
    }

    @Override
    public void handlePinEvent(PinEvent event) {
        // only process listeners and triggers if the received interrupt event
        // matches the pin number being tracked my this class instance
        if (this.pin.getPin().equals(event.getPin())) {
            if (event.getEventType() == PinEventType.DIGITAL_STATE_CHANGE) {
                PinState state = ((PinDigitalStateChangeEvent)event).getState();

                // create a copy of the listeners collection
                Collection<GpioPinListener> listeners  = new ArrayList<GpioPinListener>(pin.getListeners());

                // process event callbacks for digital listeners
                for (GpioPinListener listener : listeners) {
                    if (listener != null && listener instanceof GpioPinListenerDigital) {
                        ((GpioPinListenerDigital)listener).handleGpioPinDigitalStateChangeEvent(new GpioPinDigitalStateChangeEvent(event.getSource(), pin, state));
                    }
                }

                // create a copy of the triggers collection
                Collection<GpioTrigger> triggers  = new ArrayList<GpioTrigger>(pin.getTriggers());

                // process triggers
                for (GpioTrigger trigger : triggers) {
                    if (trigger != null && trigger.hasPinState(state)) {
                        trigger.invoke(pin, state);
                    }
                }
            } else if(event.getEventType() == PinEventType.ANALOG_VALUE_CHANGE) {
                double value = ((PinAnalogValueChangeEvent)event).getValue();

                // create a copy of the listeners collection
                Collection<GpioPinListener> listeners  = new ArrayList<GpioPinListener>(pin.getListeners());

                // process event callbacks for analog listeners
                for (GpioPinListener listener : listeners) {
                    if (listener != null && listener instanceof GpioPinListenerAnalog) {
                        ((GpioPinListenerAnalog)listener).handleGpioPinAnalogValueChangeEvent(new GpioPinAnalogValueChangeEvent(event.getSource(), pin, value));
                    }
                }
            }
        }
    }
}
