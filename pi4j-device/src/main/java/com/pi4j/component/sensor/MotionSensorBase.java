package com.pi4j.component.sensor;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MotionSensorBase.java  
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


import java.util.Date;

import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;


public abstract class MotionSensorBase extends ObserveableComponentBase implements MotionSensor {
    
    protected Date lastMotionTimestamp = null;
    protected Date lastInactivityTimestamp = null;
    
    @Override
    public Date getLastMotionTimestamp() {
        return lastMotionTimestamp;
    }

    @Override
    public Date getLastInactivityTimestamp() {
        return lastInactivityTimestamp;
    }
    
    @Override
    public abstract boolean isMotionDetected();

    @Override
    public void addListener(MotionSensorListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(MotionSensorListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(MotionSensorChangeEvent event) {
        // cache last detected timestamp
        if(event.isMotionDetected())
            lastMotionTimestamp = event.timestamp;
        else
            lastInactivityTimestamp = event.timestamp;
        
        // raise events to listeners
        for(ComponentListener listener : super.listeners) {
            ((MotionSensorListener)listener).onMotionStateChange(event);
        }
    }  
}
