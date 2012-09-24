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

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinResistor;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public class GpioPinImpl implements GpioPin
{
    private GpioController gpio;
    private String name = null;
    private Pin pin;
    private GpioPinListenerImpl monitor;
    private GpioPinShutdownImpl shutdownOptions;
    private Map<String, String> properties = new ConcurrentHashMap<String, String>();
    private Vector<GpioListener> listeners = new Vector<GpioListener>();
    private Vector<GpioTrigger> triggers = new Vector<GpioTrigger>();

    public GpioPinImpl(GpioController gpio, Pin pin)
    {
        this.gpio = gpio;
        this.pin = pin;
        shutdownOptions = new GpioPinShutdownImpl();
    }

    @Override
    public Pin getPin()
    {
        return this.pin;
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
    public void export(PinDirection direction)
    {
        // export the pin
        gpio.export(direction, pin);
    }

    @Override
    public void unexport()
    {
        // unexport the pin
        gpio.unexport(pin);
    }

    @Override
    public boolean isExported()
    {
        return gpio.isExported(pin);
    }

    @Override
    public void setDirection(PinDirection direction)
    {
        gpio.setDirection(direction, pin);
    }

    @Override
    public PinDirection getDirection()
    {
        return gpio.getDirection(pin);
    }

    @Override
    public boolean isDirection(PinDirection direction)
    {
        return (getDirection() == direction);
    }

    @Override
    public void setEdge(PinEdge edge)
    {
        gpio.setEdge(edge, pin);
    }

    @Override
    public PinEdge getEdge()
    {
        return gpio.getEdge(pin);
    }

    @Override
    public boolean isEdge(PinEdge edge)
    {
        return (getEdge() == edge);
    }

    @Override
    public void setMode(PinMode mode)
    {
        gpio.setMode(mode, pin);
    }

    @Override
    public PinMode getMode()
    {
        // TODO Implement pin mode getter method
        return null;
    }

    @Override
    public boolean isMode(PinMode mode)
    {
        return (getMode() == mode);
    }

    @Override
    public void setPullResistor(PinResistor resistance)
    {
        gpio.setPullResistor(resistance, pin);
    }

    @Override
    public PinResistor getPullResistor()
    {
        // TODO Implement pull resistor getter method
        return null;
    }

    @Override
    public boolean isPullResistor(PinResistor resistance)
    {
        return (getPullResistor() == resistance);
    }

    @Override
    public void high()
    {
        gpio.high(pin);
    }

    @Override
    public void low()
    {
        gpio.low(pin);
    }

    @Override
    public void toggle()
    {
        gpio.toggle(pin);
    }

    @Override
    public void pulse(long milliseconds)
    {
        gpio.pulse(milliseconds, pin);
    }

    @Override
    public void setState(PinState state)
    {
        gpio.setState(state, pin);
    }

    @Override
    public void setState(boolean state)
    {
        gpio.setState(state, pin);
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
    public void setPwmValue(int value)
    {
        gpio.setPwmValue(value, pin);
    }

    private synchronized void updateInterruptListener()
    {
        if (listeners.size() > 0 || triggers.size() > 0)
        {
            if (monitor == null)
            {            
                // create new monitor
                monitor = new GpioPinListenerImpl(this);
    
                // setup interrupt listener native thread and enable callbacks
                com.pi4j.wiringpi.GpioInterrupt.addListener(monitor);
                com.pi4j.wiringpi.GpioInterrupt.enablePinStateChangeCallback(pin.getValue());
            }
        }
        else
        {
            if (monitor != null)
            {
                // remove interrupt listener, disable native thread and callbacks
                com.pi4j.wiringpi.GpioInterrupt.removeListener(monitor);
                com.pi4j.wiringpi.GpioInterrupt.disablePinStateChangeCallback(pin.getValue());

                // destroy monitor instance
                monitor = null;
                ;
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
            listeners.addElement(lsnr);
        
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
    public synchronized GpioListener[] getListeners()
    {
        return listeners.toArray(new GpioListener[0]);
    }

    public synchronized void removeListener(GpioListener... listener)
    {
        if(listener == null || listener.length == 0)
            throw new IllegalArgumentException("Missing listener argument.");
        
        for (GpioListener lsnr : listener)
            listeners.removeElement(lsnr);
        
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
    public synchronized GpioTrigger[] getTriggers()
    {
        return triggers.toArray(new GpioTrigger[0]);
    }

    public synchronized void addTrigger(GpioTrigger... trigger)
    {
        if(trigger == null || trigger.length == 0)
            throw new IllegalArgumentException("Missing trigger argument.");
        
        for (GpioTrigger trgr : trigger)
            triggers.addElement(trgr);
        
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
            triggers.removeElement(trgr);
        
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
        shutdownOptions.setEdge(options.getEdge());
        shutdownOptions.setDirection(options.getDirection());
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
    public void setShutdownOptions(Boolean unexport, PinState state, PinEdge edge)
    {
        setShutdownOptions(unexport, state, edge, null);
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinEdge edge,
            PinResistor resistance)
    {
        setShutdownOptions(unexport, state, edge, resistance, null);
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinEdge edge,
            PinResistor resistance, PinDirection direction)
    {
        shutdownOptions.setUnexport(unexport);
        shutdownOptions.setState(state);
        shutdownOptions.setEdge(edge);
        shutdownOptions.setDirection(direction);
        shutdownOptions.setPullResistor(resistance);
    }
}
