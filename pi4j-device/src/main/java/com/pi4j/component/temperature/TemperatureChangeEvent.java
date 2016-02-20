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
