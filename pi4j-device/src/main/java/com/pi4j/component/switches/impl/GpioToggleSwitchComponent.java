package com.pi4j.component.switches.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioToggleSwitchComponent.java  
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

import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.component.switches.ToggleSwitch;
import com.pi4j.component.switches.ToggleSwitchBase;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GpioToggleSwitchComponent extends ToggleSwitchBase {
    
    // internal class members
    private GpioPinDigitalInput pin = null;
    private PinState offState = PinState.LOW;
    private PinState onState = PinState.HIGH;
    private final ToggleSwitch switchComponent = this;
    
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
    public GpioToggleSwitchComponent(GpioPinDigitalInput pin, PinState offState, PinState onState) {
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
    public GpioToggleSwitchComponent(GpioPinDigitalInput pin) {
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
