package com.pi4j.component.sensor;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  DistanceSensorChangeEvent.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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



import java.util.Date;
import java.util.EventObject;

public class DistanceSensorChangeEvent extends EventObject {

    private static final long serialVersionUID = 3819057560198197449L;
    protected final Date timestamp;
    protected final double distance;
    protected final double value;

    public DistanceSensorChangeEvent(DistanceSensor sensor, double value, double distance, Date timestamp) {
        super(sensor);
        this.value = value;
        this.distance = distance;
        this.timestamp = timestamp;        
    }

    public DistanceSensorChangeEvent(DistanceSensor sensor, double value, double distance) {
        super(sensor);
        this.value = value;
        this.distance = distance;
        this.timestamp = new Date();        
    }
    
    public DistanceSensor getSensor() {
        return (DistanceSensor)getSource();
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public double getDistance() {
        return distance;
    }

    public double getRawValue() {
        return value;
    }
    
}
