package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioMotionSensorComponent.java  
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


import com.pi4j.component.sensor.MotionSensor;
import com.pi4j.component.sensor.MotionSensorBase;
import com.pi4j.component.sensor.MotionSensorChangeEvent;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GpioMotionSensorComponent extends MotionSensorBase {
    
    // internal class members
    private GpioPinDigitalInput pin = null;
    private PinState motionDetectedState = PinState.HIGH;
    private final MotionSensor sensor = this;
    
    // create internal pin listener
    private GpioPinListenerDigital pinListener = new GpioPinListenerDigital() {

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {            
            // notify any motion sensor change listeners
            notifyListeners(new MotionSensorChangeEvent(sensor, (event.getState() == motionDetectedState)));                
        }
    };
    
    /**
     * using this constructor requires that the consumer 
     *  define the MOTION DETECTED pin state 
     *  
     * @param pin GPIO digital input pin
     * @param motionDetectedState pin state to set when SENSOR DETECTS MOTION
     */
    public GpioMotionSensorComponent(GpioPinDigitalInput pin, PinState motionDetectedState) {
        this.pin = pin;
        this.motionDetectedState = motionDetectedState;
        
        // add pin listener
        this.pin.addListener(pinListener);
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is MOTION DETECTED
     *  
     * @param pin GPIO digital input pin
     */
    public GpioMotionSensorComponent(GpioPinDigitalInput pin) {
        this.pin = pin;
        
        // add pin listener
        this.pin.addListener(pinListener); 
    }

    /**
     * Return 'true' if motion is currently detected   
     * based on the GPIO digital input pin state.
     *  
     * @return motion detected status 
     */
    @Override
    public boolean isMotionDetected() {
        return pin.isState(motionDetectedState);
    }
}
