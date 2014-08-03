package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  AnalogSensorComponent.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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

import com.pi4j.component.sensor.AnalogSensor;
import com.pi4j.component.sensor.AnalogSensorBase;
import com.pi4j.component.sensor.AnalogSensorValueChangeEvent;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;

public class AnalogSensorComponent extends AnalogSensorBase {
    
    // internal class members
    private GpioPinAnalogInput pin = null;
    private final AnalogSensor sensor = this;
    
    // create internal pin listener
    private GpioPinListenerAnalog pinListener = new GpioPinListenerAnalog() {

        @Override
        public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
            // notify any sensor change listeners
            notifyListeners(new AnalogSensorValueChangeEvent(sensor, sensor.getValue(), event.getValue()));
        }
    };
    
    /**
     * default constructor 
     *  
     * @param pin analog input pin
     */
    public AnalogSensorComponent(GpioPinAnalogInput pin) {
        this.pin = pin;
        
        // add pin listener
        this.pin.addListener(pinListener);
    }

    @Override
    public double getValue()
    {
        return pin.getValue();
    }
}
