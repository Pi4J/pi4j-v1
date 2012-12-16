package com.pi4j.component.temperature;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  TemperatureChangeEvent.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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


import java.util.EventObject;

public class TemperatureChangeEvent extends EventObject {

    private static final long serialVersionUID = 7247813112292612228L;
    protected final double oldTemperature;
    protected final double newTemperature;

    public TemperatureChangeEvent(TemperatureSensor sensor, double oldTemperature, double newTemperature) {
        super(sensor);
        this.oldTemperature = oldTemperature;        
        this.newTemperature = newTemperature;
    }

    public TemperatureSensor getTemperatureSensor() {
        return (TemperatureSensor)getSource();
    }
    
    public double getOldTemperature() {
        return oldTemperature;
    }

    public double getNewTemperature() {
        return newTemperature;
    }

    public double getTemperatureChange() {
        return (newTemperature - oldTemperature);
    }
    
}
