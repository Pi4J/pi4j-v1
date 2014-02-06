package com.pi4j.component.temperature;

import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;
import com.pi4j.temperature.TemperatureConversion;
import com.pi4j.temperature.TemperatureScale;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  TemperatureSensorBase.java  
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

public abstract class TemperatureSensorBase extends ObserveableComponentBase implements TemperatureSensor {

    @Override
    public abstract double getTemperature();

    @Override
    public abstract TemperatureScale getScale();
    
    @Override
    public double getTemperature(TemperatureScale scale) {
        return TemperatureConversion.convert(getScale(), scale, getTemperature());
    }    
    
    @Override
    public void addListener(TemperatureListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(TemperatureListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(TemperatureChangeEvent event) {
        for(ComponentListener listener : super.listeners) {
            ((TemperatureListener)listener).onTemperatureChange(event);
        }
    } 
}
