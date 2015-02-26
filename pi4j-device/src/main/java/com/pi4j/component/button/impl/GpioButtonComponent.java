package com.pi4j.component.button.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioButtonComponent.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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

import com.pi4j.component.button.Button;
import com.pi4j.component.button.ButtonBase;
import com.pi4j.component.button.ButtonState;
import com.pi4j.component.button.ButtonStateChangeEvent;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GpioButtonComponent extends ButtonBase {
    
    // internal class members
    private GpioPinDigitalInput pin = null;
    private PinState releasedState = PinState.LOW;
    private PinState pressedState = PinState.HIGH;
    private final Button buttonComponent = this;
    
    // create internal pin listener
    private GpioPinListenerDigital pinListener = new GpioPinListenerDigital() {

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            
            // notify any switch state change listeners
            if(event.getState() == pressedState) {
                notifyListeners(new ButtonStateChangeEvent(buttonComponent, ButtonState.RELEASED, ButtonState.PRESSED));
            }
            else if(event.getState() == releasedState) {
                notifyListeners(new ButtonStateChangeEvent(buttonComponent, ButtonState.PRESSED, ButtonState.RELEASED));
            }
        }
    };
    
    /**
     * using this constructor requires that the consumer 
     *  define the BUTTON PRESSED/RELEASED pin states
     *  
     * @param pin GPIO digital input pin
     * @param releasedState pin state to set when BUTTON is RELEASED
     * @param pressedState pin state to set when BUTTON is PRESSED
     */
    public GpioButtonComponent(GpioPinDigitalInput pin, PinState releasedState, PinState pressedState) {
        this.pin = pin;
        this.pressedState = pressedState;
        this.releasedState = releasedState;
        
        // add pin listener
        this.pin.addListener(pinListener);
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is BUTTON PRESSED
     *  (2) a pin state of LOW  is BUTTON RELEASED
     *  
     * @param pin GPIO digital input pin
     */
    public GpioButtonComponent(GpioPinDigitalInput pin) {
        this.pin = pin;
        
        // add pin listener
        this.pin.addListener(pinListener); 
    }

    /**
     * Return the current switch state based on the  
     * GPIO digital output pin state.
     *  
     * @return ButtonState
     */
    @Override
    public ButtonState getState() {
        if(pin.isState(pressedState))
            return ButtonState.PRESSED;
        else 
            return ButtonState.RELEASED;
    }
}
