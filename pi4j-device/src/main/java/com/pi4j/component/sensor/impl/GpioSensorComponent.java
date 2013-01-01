package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioSensorComponent.java  
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

import com.pi4j.component.sensor.Sensor;
import com.pi4j.component.sensor.SensorBase;
import com.pi4j.component.sensor.SensorState;
import com.pi4j.component.sensor.SensorStateChangeEvent;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GpioSensorComponent extends SensorBase {
    
    // internal class members
    private GpioPinDigitalInput pin = null;
    private PinState openState = PinState.LOW;
    private PinState closedState = PinState.HIGH;
    private final Sensor sensor = this;
    
    // create internal pin listener
    private GpioPinListenerDigital pinListener = new GpioPinListenerDigital() {

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            
            // notify any sensor state change listeners
            if(event.getState() == openState) {
                notifyListeners(new SensorStateChangeEvent(sensor, SensorState.CLOSED, SensorState.OPEN));                
            }
            else if(event.getState() == closedState) {
                notifyListeners(new SensorStateChangeEvent(sensor, SensorState.CLOSED, SensorState.OPEN));
            }
        }
    };
    
    /**
     * using this constructor requires that the consumer 
     *  define the SENSOR OPEN and SENSOR CLOSED pin states 
     *  
     * @param pin GPIO digital input pin
     * @param openState pin state to set when SENSOR is OPEN
     * @param closedState pin state to set when SENSOR is CLOSED
     */
    public GpioSensorComponent(GpioPinDigitalInput pin, PinState openState, PinState closedState) {
        this.pin = pin;
        this.openState = openState;
        this.closedState = closedState;
        
        // add pin listener
        this.pin.addListener(pinListener);
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is SENSOR CLOSED
     *  (2) a pin state of LOW  is SENSOR OPEN
     *  
     * @param pin GPIO digital input pin
     */
    public GpioSensorComponent(GpioPinDigitalInput pin) {
        this.pin = pin;
        
        // add pin listener
        this.pin.addListener(pinListener); 
    }

    /**
     * Return the current sensor state based on the  
     * GPIO digital output pin state.
     *  
     * @return PowerState 
     */
    @Override
    public SensorState getState() {
        if(pin.isState(openState))
            return SensorState.OPEN;
        else 
            return SensorState.CLOSED;
    }
}
