package com.pi4j.component.switches.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioSwitchComponent.java
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

import com.pi4j.component.switches.Switch;
import com.pi4j.component.switches.SwitchBase;
import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GpioSwitchComponent extends SwitchBase {

    // internal class members
    private GpioPinDigitalInput pin = null;
    private PinState offState = PinState.LOW;
    private PinState onState = PinState.HIGH;
    private final Switch switchComponent = this;

    // create internal pin listener
    private GpioPinListenerDigital pinListener = new GpioPinListenerDigital() {

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

            // notify any switch state change listeners
            if(event.getState() == onState) {
                notifyListeners(new SwitchStateChangeEvent(switchComponent, SwitchState.OFF, SwitchState.ON));
            }
            else if(event.getState() == offState) {
                notifyListeners(new SwitchStateChangeEvent(switchComponent, SwitchState.ON, SwitchState.OFF));
            }
        }
    };

    /**
     * using this constructor requires that the consumer
     *  define the SWITCH OPEN/OFF and SWITCH CLOSED/ON pin states
     *
     * @param pin GPIO digital input pin
     * @param offState pin state to set when SWITCH is OPEN/OFF
     * @param onState pin state to set when SWITCH is CLOSED/ON
     */
    public GpioSwitchComponent(GpioPinDigitalInput pin, PinState offState, PinState onState) {
        this.pin = pin;
        this.onState = onState;
        this.offState = offState;

        // add pin listener
        this.pin.addListener(pinListener);
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is SWITCH CLOSED/ON
     *  (2) a pin state of LOW  is SWITCH OPEN/OFF
     *
     * @param pin GPIO digital input pin
     */
    public GpioSwitchComponent(GpioPinDigitalInput pin) {
        this.pin = pin;

        // add pin listener
        this.pin.addListener(pinListener);
    }

    /**
     * Return the current switch state based on the
     * GPIO digital output pin state.
     *
     * @return SwitchState
     */
    @Override
    public SwitchState getState() {
        if(pin.isState(onState))
            return SwitchState.ON;
        else
            return SwitchState.OFF;
    }
}
