package com.pi4j.component.power.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioPowerComponent.java
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


import com.pi4j.component.power.PowerBase;
import com.pi4j.component.power.PowerState;
import com.pi4j.component.power.PowerStateChangeEvent;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GpioPowerComponent extends PowerBase {

    // internal class members
    GpioPinDigitalOutput pin = null;
    PinState onState = PinState.HIGH;
    PinState offState = PinState.LOW;


    // create a GPIO PIN listener for change changes; use this to send POWER state change events
    private GpioPinListenerDigital listener = new GpioPinListenerDigital() {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            // notify any state change listeners
            if(event.getState() == onState){
                notifyListeners(new PowerStateChangeEvent(GpioPowerComponent.this, PowerState.OFF, PowerState.ON));
            }
            if(event.getState() == offState){
                notifyListeners(new PowerStateChangeEvent(GpioPowerComponent.this, PowerState.ON, PowerState.OFF));
            }
        }
    };

    /**
     * using this constructor requires that the consumer
     *  define the POWER ON and POWER OFF pin states
     *
     * @param pin GPIO digital output pin
     * @param onState pin state to set when power is ON
     * @param offState pin state to set when power is OFF
     */
    public GpioPowerComponent(GpioPinDigitalOutput pin, PinState onState, PinState offState) {
        this(pin);
        this.onState = onState;
        this.offState = offState;
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is POWER ON
     *  (2) a pin state of LOW  is POWER OFF
     *
     * @param pin GPIO digital output pin
     */
    public GpioPowerComponent(GpioPinDigitalOutput pin) {
        this.pin = pin;
        this.pin.addListener(listener);
    }

    /**
     * Return the current power state based on the
     * GPIO digital output pin state.
     *
     * @return PowerState
     */
    @Override
    public PowerState getState() {
        if(pin.isState(onState))
            return PowerState.ON;
        else if(pin.isState(offState))
            return PowerState.OFF;
        else
            return PowerState.UNKNOWN;
    }

    /**
     * Set the current GPIO digital output pin state
     * based on the supplied power state
     *
     * @param state new power state to apply
     */
    @Override
    public void setState(PowerState state) {
        switch(state) {
            case OFF: {
                if(!isOff()) {

                    // apply the new pin state
                    pin.setState(offState);
                }
                break;
            }
            case ON: {
                if(!isOn()) {

                    // apply the new pin state
                    pin.setState(onState);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Cannot set power state: " + state.toString());
            }
        }
    }



}
