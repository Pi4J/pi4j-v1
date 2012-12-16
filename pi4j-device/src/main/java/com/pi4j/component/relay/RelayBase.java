package com.pi4j.component.relay;

import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  RelayBase.java  
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


public abstract class RelayBase extends ObserveableComponentBase implements Relay {
    
    @Override
    public void open() {
        setState(RelayState.OPEN);
    }

    @Override
    public void close() {
        setState(RelayState.CLOSED);
    }

    @Override
    public boolean isOpen() {
        return (getState() == RelayState.OPEN);
    }

    @Override
    public boolean isClosed() {
        return (getState() == RelayState.CLOSED);
    }

    @Override
    public abstract RelayState getState();

    @Override
    public abstract void setState(RelayState state);
    
    @Override
    public void addListener(RelayListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(RelayListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(RelayStateChangeEvent event) {
        for(ComponentListener listener : super.listeners) {
            ((RelayListener)listener).onStateChange(event);
        }
    } 
}
