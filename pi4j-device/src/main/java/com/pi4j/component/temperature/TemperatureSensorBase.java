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
