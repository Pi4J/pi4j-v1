package com.pi4j.device.access;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  OpenerBase.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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


import com.pi4j.device.DeviceListener;
import com.pi4j.device.ObserveableDeviceBase;
import com.pi4j.device.access.OpenerState;

public abstract class OpenerBase extends ObserveableDeviceBase implements Opener
{
    @Override
    public abstract void open() throws OpenerLockedException;

    @Override
    public abstract void close() throws OpenerLockedException;

    @Override
    public abstract OpenerState getState();

    @Override
    public abstract boolean isLocked();

    @Override
    public boolean isOpen()
    {
        return (getState() == OpenerState.OPEN);
    }

    @Override
    public boolean isOpening()
    {
        return (getState() == OpenerState.OPENING);
    }

    @Override
    public boolean isClosed()
    {
        return (getState() == OpenerState.CLOSED);
    }

    @Override
    public boolean isClosing()
    {
        return (getState() == OpenerState.CLOSING);
    }

    @Override
    public void addListener(OpenerListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(OpenerListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(OpenerStateChangeEvent event) {
        for(DeviceListener listener : super.listeners) {
            ((OpenerListener)listener).onStateChange(event);
        }
    }
    
    protected synchronized void notifyListeners(OpenerLockChangeEvent event) {
        for(DeviceListener listener : super.listeners) {
            ((OpenerListener)listener).onLockChange(event);
        }
    }         
}
