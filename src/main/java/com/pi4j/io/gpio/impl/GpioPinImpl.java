package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library
 * FILENAME      :  GpioPinImpl.java  
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


import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinResistor;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public class GpioPinImpl implements GpioPin
{
    //private Gpio gpio;
    private String name = null;
    private Pin pin;
    private GpioPinListenerImpl monitor;
    private Map<String, String> properties = new ConcurrentHashMap<>();
    private Vector<GpioListener> listeners = new Vector<GpioListener>();
    private Vector<GpioTrigger> triggers = new Vector<GpioTrigger>();

    public GpioPinImpl(Gpio gpio, Pin pin)
    {
        //this.gpio = gpio;
        this.pin = pin;
        monitor = new GpioPinListenerImpl(this);
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
        com.pi4j.wiringpi.GpioUtil.export(pin.getValue(), direction.getValue());
        
        // setup listener thread
        if(direction == PinDirection.IN)
        {
            com.pi4j.wiringpi.GpioInterrupt.addListener(monitor);
            com.pi4j.wiringpi.GpioInterrupt.enablePinStateChangeCallback(pin.getValue());
        }        
    }

    @Override
    public void unexport()
    {
        com.pi4j.wiringpi.GpioUtil.unexport(pin.getValue());
    }

    @Override
    public boolean isExported()
    {
        return com.pi4j.wiringpi.GpioUtil.isExported(pin.getValue());
    }

    @Override
    public void setDirection(PinDirection direction)
    {
        com.pi4j.wiringpi.GpioUtil.setDirection(pin.getValue(), direction.getValue());
    }

    @Override
    public PinDirection getDirection()
    {
        PinDirection direction = null;
        int ret = com.pi4j.wiringpi.GpioUtil.getDirection(pin.getValue());
        if (ret >= 0)
            direction = PinDirection.getDirection(ret);
        return direction;
    }

    @Override
    public void setEdge(PinEdge edge)
    {
        com.pi4j.wiringpi.GpioUtil.setEdgeDetection(pin.getValue(), edge.getValue());
    }

    @Override
    public PinEdge getEdge()
    {
        PinEdge edge = null;
        int ret = com.pi4j.wiringpi.GpioUtil.getEdgeDetection(pin.getValue());
        if (ret >= 0)
            edge = PinEdge.getEdge(ret);
        return edge;
    }

    @Override
    public void setMode(PinMode mode)
    {
        com.pi4j.wiringpi.Gpio.pinMode(pin.getValue(), mode.getValue());
    }

    @Override
    public PinMode getMode()
    {
        // TODO Implement pin mode getter method
        return null;
    }

    @Override
    public void setPullResistor(PinResistor resistance)
    {
        com.pi4j.wiringpi.Gpio.pullUpDnControl(pin.getValue(), resistance.getValue());
    }

    @Override
    public PinResistor getPullResistor()
    {
        // TODO Implement pull resistor getter method
        return null;
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
        GpioPulseImpl.execute(this, milliseconds);
    }

    @Override
    public void setState(PinState state)
    {
        com.pi4j.wiringpi.Gpio.digitalWrite(pin.getValue(), state.getValue());
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
        PinState state = null;
        int ret = com.pi4j.wiringpi.Gpio.digitalRead(pin.getValue());
        if (ret >= 0)
            state = PinState.getState(ret);
        return state;
    }

    @Override
    public void setPwmValue(int value)
    {
        com.pi4j.wiringpi.Gpio.pwmWrite(pin.getValue(), value);
    }

    /**
     * 
     * @param listener
     */
    public synchronized void addListener(GpioListener listener)
    {
        listeners.addElement(listener);
    }

    public synchronized void addListener(GpioListener[] listeners)
    {
        for (GpioListener listener : listeners)
            addListener(listener);
    }

    public synchronized void addListener(List<GpioListener> listeners)
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
    
    public synchronized void removeListener(GpioListener listener)
    {
        listeners.removeElement(listener);
    }

    public synchronized void removeListener(GpioListener[] listeners)
    {
        for (GpioListener listener : listeners)
            removeListener(listener);
    }

    public synchronized void removeListener(List<GpioListener> listeners)
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
    
    public synchronized void addTrigger(GpioTrigger trigger)
    {
        triggers.addElement(trigger);
    }

    public synchronized void addTrigger(List<GpioTrigger> triggers)
    {
        for (GpioTrigger trigger : triggers)
            addTrigger(trigger);
    }

    public synchronized void addTrigger(GpioTrigger[] triggers)
    {
        for (GpioTrigger trigger : triggers)
            addTrigger(trigger);
    }

    /**
     * 
     * @param trigger
     */
    public synchronized void removeTrigger(GpioTrigger trigger)
    {
        triggers.removeElement(trigger);
    }

    public synchronized void removeTrigger(List<GpioTrigger> triggers)
    {
        for (GpioTrigger trigger : triggers)
            removeTrigger(trigger);
    }

    public synchronized void removeTrigger(GpioTrigger[] triggers)
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
        if(name != null && !name.isEmpty())
            return String.format("\"%s\" <%s>", name, pin.toString());
        else
            return pin.toString();
    }
}
