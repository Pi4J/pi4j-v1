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
 * Copyright (C) 2012 - 2013 Pi4J
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
