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
