package com.pi4j.component.relay.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioRelayComponent.java
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import com.pi4j.component.relay.RelayBase;
import com.pi4j.component.relay.RelayState;
import com.pi4j.component.relay.RelayStateChangeEvent;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.util.Timer;

public class GpioRelayComponent extends RelayBase {
    
    // internal class members
    GpioPinDigitalOutput pin = null;
    PinState openState = PinState.LOW;
    PinState closedState = PinState.HIGH;
    Timer pulseTimer = new Timer();

    // create a GPIO PIN listener for change changes; use this to send RELAY state change events
    private GpioPinListenerDigital listener = new GpioPinListenerDigital() {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            // notify any state change listeners
            if(event.getState() == openState){
                notifyListeners(new RelayStateChangeEvent(GpioRelayComponent.this, RelayState.CLOSED, RelayState.OPEN));
            }
            if(event.getState() == closedState){
                notifyListeners(new RelayStateChangeEvent(GpioRelayComponent.this, RelayState.OPEN, RelayState.CLOSED));
            }
        }
    };

    /**
     * using this constructor requires that the consumer 
     *  define the RELAY OPEN and RELAY closed pin states 
     *  
     * @param pin GPIO digital output pin
     * @param openState pin state to set when relay is OPEN
     * @param closedState pin state to set when relay is CLOSED
     */
    public GpioRelayComponent(GpioPinDigitalOutput pin, PinState openState, PinState closedState) {
        this(pin);
        this.openState = openState;
        this.closedState = closedState;
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is RELAY CLOSED
     *  (2) a pin state of LOW  is RELAY OPEN
     *  
     * @param pin GPIO digital output pin
     */
    public GpioRelayComponent(GpioPinDigitalOutput pin) {
        this.pin = pin;
        this.pin.addListener(listener);
    }

    /**
     * Return the current relay state based on the  
     * GPIO digital output pin state.
     *  
     * @return PowerState 
     */
    @Override
    public RelayState getState() {
        if(pin.isState(openState))
            return RelayState.OPEN;
        else 
            return RelayState.CLOSED;
    }

    /**
     * Set the current GPIO digital output pin state
     * based on the supplied power state
     * 
     * @param state new power state to apply
     */
    @Override
    public void setState(RelayState state) {

        // apply the new pin state
        switch(state) {
            case OPEN: {
                if(!isOpen()) {
                    pin.setState(openState);
                }
                break;
            }
            case CLOSED: {
                if(!isClosed()) {
                    pin.setState(closedState);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Cannot set power state: " + state.toString());
            }
        }
    }

    @Override
    public void pulse(int milliseconds) {
        // initiate a pulse on the GPIO pin
        pin.pulse(milliseconds, closedState);
    }
}
