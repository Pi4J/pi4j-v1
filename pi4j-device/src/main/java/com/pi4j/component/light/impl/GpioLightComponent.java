package com.pi4j.component.light.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioLightComponent.java
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


import com.pi4j.component.light.LightBase;
import com.pi4j.component.light.LightStateChangeEvent;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GpioLightComponent extends LightBase {

    // internal class members
    GpioPinDigitalOutput pin = null;
    PinState onState = PinState.HIGH;
    PinState offState = PinState.LOW;

    // create a GPIO PIN listener for change changes; use this to send light state change events
    private GpioPinListenerDigital listener = new GpioPinListenerDigital() {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            // notify any state change listeners
            if(event.getState() == onState){
                notifyListeners(new LightStateChangeEvent(GpioLightComponent.this, true));
            }
            if(event.getState() == offState){
                notifyListeners(new LightStateChangeEvent(GpioLightComponent.this, false));
            }
        }
    };

    /**
     * using this constructor requires that the consumer
     *  define the LIGHT ON and LIGHT OFF pin states
     *
     * @param pin GPIO digital output pin
     * @param onState pin state to set when power is ON
     * @param offState pin state to set when power is OFF
     */
    public GpioLightComponent(GpioPinDigitalOutput pin, PinState onState, PinState offState) {
        this(pin);
        this.onState = onState;
        this.offState = offState;
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is LIGHT ON
     *  (2) a pin state of LOW  is LIGHT OFF
     *
     * @param pin GPIO digital output pin
     */
    public GpioLightComponent(GpioPinDigitalOutput pin) {
        this.pin = pin;
        this.pin.addListener(listener);
    }

    /**
     * Set the current GPIO digital output pin state
     * based for LIGHT ON
     */
    @Override
    public void on()
    {
        // turn the light ON by settings the GPIO pin to the on state
        pin.setState(onState);
    }

    /**
     * Set the current GPIO digital output pin state
     * based for LIGHT OFF
     */
    @Override
    public void off()
    {
        // turn the light OFF by settings the GPIO pin to the off state
        pin.setState(offState);
    }


    /**
     * Return the current power state based on the
     * GPIO digital output pin state.
     *
     * @return boolean is light on
     */
    @Override
    public boolean isOn()
    {
        return pin.isState(onState);
    }
}
