package com.pi4j.component.sensor;

import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  SensorBase.java  
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


public abstract class SensorBase extends ObserveableComponentBase implements Sensor {
    
    @Override
    public boolean isOpen() {
        return (getState() == SensorState.OPEN);
    }

    @Override
    public boolean isClosed() {
        return (getState() == SensorState.CLOSED);
    }

    @Override
    public abstract SensorState getState();
    
    @Override
    public boolean isState(SensorState state) {
        return getState().equals(state);
    }
    
    @Override
    public void addListener(SensorListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(SensorListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(SensorStateChangeEvent event) {
        for(ComponentListener listener : super.listeners) {
            ((SensorListener)listener).onStateChange(event);
        }
    }    
}
