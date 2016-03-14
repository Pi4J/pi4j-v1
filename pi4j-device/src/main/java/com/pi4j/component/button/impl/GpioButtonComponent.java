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
