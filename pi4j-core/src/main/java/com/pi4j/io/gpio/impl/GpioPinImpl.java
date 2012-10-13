package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinImpl.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public class GpioPinImpl implements GpioPin
{
    private final GpioController gpio;
    private String name = null;
    private final GpioProvider provider;
    private final Pin pin;
    private GpioEventMonitorImpl monitor;
    private final GpioPinShutdownImpl shutdownOptions;
    private final Map<String, String> properties = new ConcurrentHashMap<String, String>();
    private final List<GpioListener> listeners = new ArrayList<GpioListener>();
    private final List<GpioTrigger> triggers = new ArrayList<GpioTrigger>();
    
    public GpioPinImpl(GpioController gpio, GpioProvider provider, Pin pin)
    {
        this.gpio = gpio;
        this.provider = provider;
        this.pin = pin;
        shutdownOptions = new GpioPinShutdownImpl();
    }

    @Override
    public Pin getPin()
    {
        return this.pin;
    }

    @Override
    public GpioProvider getProvider()
    {
        return this.provider;
    }
    
    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        if (name == null || name.length() == 0)
            return pin.toString();
        return name;
    }

    @Override
    public void setProperty(String key, String value)
    {
        properties.put(key, value);
    }

    @Override
    public boolean hasProperty(String key)
    {
        return properties.containsKey(key);
    }

    @Override
    public String getProperty(String key)
    {
        if (properties.containsKey(key))
            return properties.get(key);
        return null;
    }

    @Override
    public Map<String, String> getProperties()
    {
        return properties;
    }

    @Override
    public void removeProperty(String key)
    {
        if (properties.containsKey(key))
            properties.remove(key);
    }

    @Override
    public void clearProperties()
    {
        properties.clear();
    }

    @Override
    public void export(PinMode mode)
    {
        // export the pin
        provider.export(pin, mode);
    }

    @Override
    public void unexport()
    {
        // unexport the pin
        provider.unexport(pin);
    }

    @Override
    public boolean isExported()
    {
        return provider.isExported(pin);
    }

    @Override
    public void setMode(PinMode mode)
    {
        provider.setMode(pin, mode);
    }

    @Override
    public PinMode getMode()
    {
        return provider.getMode(pin);
    }

    @Override
    public boolean isMode(PinMode mode)
    {
        return (getMode() == mode);
    }

    @Override
    public void setPullResistance(PinPullResistance resistance)
    {
        provider.setPullResistance(pin, resistance);
    }

    @Override
    public PinPullResistance getPullResistance()
    {
        return provider.getPullResistance(pin);
    }

    @Override
    public boolean isPullResistance(PinPullResistance resistance)
    {
        return (getPullResistance() == resistance);
    }

    @Override
    public void high()
    {
        setState(PinState.HIGH);
    }

    @Override
    public void low()
    {
        setState(PinState.LOW);
    }

    @Override
    public void toggle()
    {
        if (getState() == PinState.HIGH)
            setState(PinState.LOW);
        else
            setState(PinState.HIGH);
    }

    @Override
    public void pulse(long milliseconds)
    {
        GpioPulseImpl.execute(gpio, pin, milliseconds);
    }

    @Override
    public void setState(PinState state)
    {
        provider.setState(pin, state);
    }

    @Override
    public void setState(boolean state)
    {
        provider.setState(pin, (state) ? PinState.HIGH : PinState.LOW);
    }

    @Override
    public boolean isHigh()
    {
        return (getState() == PinState.HIGH);
    }

    @Override
    public boolean isLow()
    {
        return (getState() == PinState.LOW);
    }

    @Override
    public PinState getState()
    {
        return gpio.getState(pin);
    }

    @Override
    public boolean isState(PinState state)
    {
        return (getState() == state);
    }

    @Override
    public void setValue(int value)
    {
        provider.setValue(pin, value);
    }
    
    @Override
    public int getValue()
    {
        return provider.getValue(pin);
    }    

    private synchronized void updateInterruptListener()
    {
        if (listeners.size() > 0 || triggers.size() > 0)
        {
            if (monitor == null)
            {            
                // create new monitor and register for event callbacks
                monitor = new GpioEventMonitorImpl(this);
                provider.addListener(pin, monitor);
            }
        }
        else
        {
            if (monitor != null)
            {
                // remove monitor and unregister for event callbacks
                provider.removeListener(pin, monitor);

                // destroy monitor instance
                monitor = null;
            }
        }
    }

    /**
     * 
     * @param listener
     */
    public synchronized void addListener(GpioListener... listener)
    {
        if(listener == null || listener.length == 0)
            throw new IllegalArgumentException("Missing listener argument.");
        
        for (GpioListener lsnr : listener)
            listeners.add(lsnr);
        
        updateInterruptListener();
    }

    public synchronized void addListener(List<? extends GpioListener> listeners)
    {
        for (GpioListener listener : listeners)
            addListener(listener);
    }

    /**
     * 
     * @param listener
     */
    public synchronized Collection<GpioListener> getListeners()
    {
        return listeners;
    }

    @Override
    public boolean hasListener(GpioListener... listener)
    {
        if(listener == null || listener.length == 0)
            throw new IllegalArgumentException("Missing listener argument.");
        
        for (GpioListener lsnr : listener)
        {
            if(!listeners.contains(lsnr))
                return false;
        }

        return true;
    }
    
    public synchronized void removeListener(GpioListener... listener)
    {
        if(listener == null || listener.length == 0)
            throw new IllegalArgumentException("Missing listener argument.");
        
        for (GpioListener lsnr : listener)
            listeners.remove(lsnr);
        
        updateInterruptListener();
    }

    public synchronized void removeListener(List<? extends GpioListener> listeners)
    {
        for (GpioListener listener : listeners)
            removeListener(listener);
    }
    
    public synchronized void removeAllListeners()
    {
        for (GpioListener listener : this.listeners)
            removeListener(listener);
    }

    /**
     * 
     * @param trigger
     */
    public synchronized Collection<GpioTrigger> getTriggers()
    {
        return triggers;
    }

    public synchronized void addTrigger(GpioTrigger... trigger)
    {
        if(trigger == null || trigger.length == 0)
            throw new IllegalArgumentException("Missing trigger argument.");
        
        for (GpioTrigger trgr : trigger)
            triggers.add(trgr);
        
        updateInterruptListener();
    }

    public synchronized void addTrigger(List<? extends GpioTrigger> triggers)
    {
        for (GpioTrigger trigger : triggers)
            addTrigger(trigger);
    }

    /**
     * 
     * @param trigger
     */
    public synchronized void removeTrigger(GpioTrigger... trigger)
    {
        if(trigger == null || trigger.length == 0)
            throw new IllegalArgumentException("Missing trigger argument.");

        for (GpioTrigger trgr : trigger)
            triggers.remove(trgr);
        
        updateInterruptListener();
    }

    public synchronized void removeTrigger(List<? extends GpioTrigger> triggers)
    {
        for (GpioTrigger trigger : triggers)
            removeTrigger(trigger);
    }

    public synchronized void removeAllTriggers()
    {
        for (GpioTrigger trigger : this.triggers)
            removeTrigger(trigger);
    }

    @Override
    public String toString()
    {
        if (name != null && !name.isEmpty())
            return String.format("\"%s\" <%s>", name, pin.toString());
        else
            return pin.toString();
    }

    @Override
    public GpioPinShutdown getShutdownOptions()
    {
        return shutdownOptions;
    }

    @Override
    public void setShutdownOptions(GpioPinShutdown options)
    {
        shutdownOptions.setUnexport(options.getUnexport());
        shutdownOptions.setState(options.getState());
        shutdownOptions.setMode(options.getMode());
        shutdownOptions.setPullResistor(options.getPullResistor());
    }

    @Override
    public void setShutdownOptions(Boolean unexport)
    {
        setShutdownOptions(unexport, null);
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state)
    {
        setShutdownOptions(unexport, state, null);
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance)
    {
        setShutdownOptions(unexport, state, resistance, null);
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode)
    {
        shutdownOptions.setUnexport(unexport);
        shutdownOptions.setState(state);
        shutdownOptions.setMode(mode);
        shutdownOptions.setPullResistor(resistance);
    }
}
