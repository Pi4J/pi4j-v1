package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioProviderBase.java  
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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinListener;

public abstract class GpioProviderBase implements GpioProvider
{
    protected final Map<Pin, List<PinListener>> listeners = new ConcurrentHashMap<Pin, List<PinListener>>();

    public abstract void initialize();
    public abstract String getName();
    public abstract boolean hasPin(Pin pin);
    public abstract void export(Pin pin, PinMode mode);
    public abstract boolean isExported(Pin pin);
    public abstract void unexport(Pin pin);
    public abstract void setMode(Pin pin, PinMode mode);
    public abstract PinMode getMode(Pin pin);
    public abstract void setPullResistance(Pin pin, PinPullResistance resistance);
    public abstract PinPullResistance getPullResistance(Pin pin);
    public abstract void setState(Pin pin, PinState state);
    public abstract PinState getState(Pin pin);
    public abstract void setValue(Pin pin, int value);
    public abstract int getValue(Pin pin);        
 
    public void addListener(Pin pin, PinListener listener)
    {
        // create new pin listener entry if one does not already exist
        if(!listeners.containsKey(pin))
            listeners.put(pin, new ArrayList<PinListener>());
        
        // add the listener instance to the listeners map entry 
        List<PinListener> lsnrs = listeners.get(pin);
        if(!lsnrs.contains(listener))
            lsnrs.add(listener);
    }
    
    public void removeListener(Pin pin, PinListener listener)
    {
        // lookup to pin entry in the listeners map
        if(listeners.containsKey(pin))
        {
            // remote the listener instance from the listeners map entry if found 
            List<PinListener> lsnrs = listeners.get(pin);
            if(lsnrs.contains(listener))
                lsnrs.remove(listener);
            
            // if the listener list is empty, then remove the listener pin from the map
            if(lsnrs.isEmpty())
                listeners.remove(pin);
        }
    }    
    
    public void removeAllListeners()
    {
        // iterate over all listener pins in the map
        for(Pin pin : listeners.keySet())
        {
            // iterate over all listener handler in the map entry
            // and remove each listener handler instance
            for(PinListener listener : listeners.get(pin))
                removeListener(pin, listener);
        }
    }
}
