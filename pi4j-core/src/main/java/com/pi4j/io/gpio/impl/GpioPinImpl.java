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
 * Copyright (C) 2012 - 2015 Pi4J
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

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class GpioPinImpl implements GpioPin, 
                                    GpioPinDigitalInput, 
                                    GpioPinDigitalOutput, 
                                    GpioPinDigitalMultipurpose,
                                    GpioPinAnalogInput, 
                                    GpioPinAnalogOutput,
                                    GpioPinPwmOutput,
                                    GpioPinInput,
                                    GpioPinOutput
{
 
    @SuppressWarnings("unused")
    private String name = null;
    private Object tag = null;
    private final GpioProvider provider;
    private final Pin pin;
    private PinListener monitor;
    private final GpioPinShutdownImpl shutdownOptions;
    private final Map<String, String> properties = new ConcurrentHashMap<>();
    private final List<GpioPinListener> listeners = new ArrayList<>();
    private final List<GpioTrigger> triggers = new ArrayList<>();
    private final Map<PinState, Integer> debounce = new HashMap<>();
    protected final int NO_DEBOUCE = 0;

    @SuppressWarnings("unused")
    public GpioPinImpl(GpioController gpio, GpioProvider provider, Pin pin) {
        this.provider = provider;
        this.pin = pin;
        shutdownOptions = new GpioPinShutdownImpl();
    }

    @Override
    public Pin getPin() {
        return this.pin;
    }

    @Override
    public GpioProvider getProvider() {
        return this.provider;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        if (name == null || name.length() == 0) {
            return pin.toString();
        }
        return name;
    }
    
    @Override
    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        if (properties.containsKey(key)) {
            if(properties.get(key) == null || properties.get(key).isEmpty())
                return defaultValue;
            else
                return properties.get(key);
        }
        return defaultValue;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, null);
    }
    
    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void removeProperty(String key) {
        if (properties.containsKey(key)) {
            properties.remove(key);
        }
    }

    @Override
    public void clearProperties() {
        properties.clear();
    }

    @Override
    public void export(PinMode mode) {
        // export the pin
        provider.export(pin, mode);
    }

    @Override
    public void export(PinMode mode, PinState defaultState) {
        // export the pin
        provider.export(pin, mode, defaultState);
    }

    @Override
    public void unexport() {
        // unexport the pin
        provider.unexport(pin);
    }

    @Override
    public boolean isExported() {
        return provider.isExported(pin);
    }

    @Override
    public void setMode(PinMode mode) {
        provider.setMode(pin, mode);
    }

    @Override
    public PinMode getMode() {
        return provider.getMode(pin);
    }

    @Override
    public boolean isMode(PinMode mode) {
        return (getMode() == mode);
    }

    @Override
    public void setPullResistance(PinPullResistance resistance) {
        provider.setPullResistance(pin, resistance);
    }

    @Override
    public PinPullResistance getPullResistance() {
        return provider.getPullResistance(pin);
    }

    @Override
    public boolean isPullResistance(PinPullResistance resistance) {
        return (getPullResistance() == resistance);
    }

    @Override
    public void high() {
        setState(PinState.HIGH);
    }

    @Override
    public void low() {
        setState(PinState.LOW);
    }

    @Override
    public void toggle() {
        if (getState() == PinState.HIGH) {
            setState(PinState.LOW);
        } else {
            setState(PinState.HIGH);
        }
    }

    @Override
    public Future<?> blink(long delay) {
        return blink(delay, PinState.HIGH);
    }

    @Override
    public Future<?> blink(long delay, PinState blinkState) {
        // NOTE: a value of 0 milliseconds will stop the blinking
        return blink(delay, 0, blinkState);
    }

    @Override
    public Future<?> blink(long delay, long duration) {
        return blink(delay, duration, PinState.HIGH);
    }

    @Override
    public Future<?> blink(long delay, long duration, PinState blinkState) {
        // NOTE: a value of 0 milliseconds will stop the blinking
        return GpioScheduledExecutorImpl.blink(this, delay, duration, blinkState);
    }
    
    @Override
    public Future<?> pulse(long duration) {
        return pulse(duration, false);
    }

    @Override
    public Future<?> pulse(long duration, Callable<Void> callback) {
        return pulse(duration, false, callback);
    }

    @Override
    public Future<?> pulse(long duration, PinState pulseState) {
        return pulse(duration, pulseState, false);
    }

    @Override
    public Future<?> pulse(long duration, PinState pulseState, Callable<Void> callback) {
        return pulse(duration, pulseState, false, callback);
    }

    @Override
    public Future<?> pulse(long duration, boolean blocking) {
        return pulse(duration, PinState.HIGH, blocking);
    }

    @Override
    public Future<?> pulse(long duration, boolean blocking, Callable<Void> callback) {
        return pulse(duration, PinState.HIGH, blocking, callback);
    }

    @Override
    public Future<?> pulse(long duration, PinState pulseState, boolean blocking) {
        return pulse(duration, pulseState, blocking, null);
    }

    @Override
    public Future<?> pulse(long duration, PinState pulseState, boolean blocking, Callable<Void> callback) {
        
        // validate duration argument
        if(duration <= 0)
            throw new IllegalArgumentException("Pulse duration must be greater than 0 milliseconds.");
        
        // if this is a blocking pulse, then execute the pulse 
        // and sleep the caller's thread to block the operation 
        // until the pulse is complete
        if(blocking) {
            // start the pulse state
            setState(pulseState);
            
            // block the current thread for the pulse duration 
            try {
                Thread.sleep(duration);
            }
            catch (InterruptedException e) {
                throw new RuntimeException("Pulse blocking thread interrupted.", e);
            }

            // end the pulse state
            setState(PinState.getInverseState(pulseState));

            // invoke callback if one was defined
            if(callback != null){
                try {
                    callback.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            // we are done; no future is returned for blocking pulses
            return null;
        }
        else {            
            // if this is not a blocking call, then setup the pulse 
            // instruction to be completed in a background worker
            // thread pool using a scheduled executor 
            return GpioScheduledExecutorImpl.pulse(this, duration, pulseState, callback);
        }
    }
    
    @Override
    public void setState(PinState state) {
        provider.setState(pin, state);
    }

    @Override
    public void setState(boolean state) {
        provider.setState(pin, (state) ? PinState.HIGH : PinState.LOW);
    }

    @Override
    public boolean isHigh() {
        return (getState() == PinState.HIGH);
    }

    @Override
    public boolean isLow() {
        return (getState() == PinState.LOW);
    }

    @Override
    public PinState getState() {
        return provider.getState(pin);
    }

    @Override
    public boolean isState(PinState state) {
        return (getState() == state);
    }

    @Override
    public boolean hasDebounce(PinState state) {
        return (getDebounce(state) > NO_DEBOUCE);
    }

    @Override
    public int getDebounce(PinState state) {
        if(debounce.containsKey(state)){
            return debounce.get(state).intValue();
        }
        return NO_DEBOUCE;
    }

    @Override
    public void setDebounce(int debounce, PinState ... state) {
        for(PinState ps : state) {
            this.debounce.put(ps, new Integer(debounce));
        }
    }

    @Override
    public void setDebounce(int debounce) {
        setDebounce(debounce, PinState.HIGH, PinState.LOW);
    }

    @Override
    public void setValue(double value) {
        provider.setValue(pin, value);
    }
    
    @Override
    public double getValue() {
        return provider.getValue(pin);
    }    

    @Override
    public void setPwm(int value) {
        provider.setPwm(pin, value);
    }
    
    @Override
    public int getPwm() {
        return provider.getPwm(pin);
    }  
    
    private synchronized void updateInterruptListener() {
        if (listeners.size() > 0 || triggers.size() > 0) {
            if (monitor == null) { 
                // create new monitor and register for event callbacks
                monitor = new GpioEventMonitorExecutorImpl(this);
                provider.addListener(pin, monitor);
            }
        } else {
            if (monitor != null) {
                // remove monitor and unregister for event callbacks
                provider.removeListener(pin, monitor);

                // destroy monitor instance
                monitor = null;
            }
        }
    }

    /**
     * 
     * @param listener gpio pin listener interface
     */
    public synchronized void addListener(GpioPinListener... listener) {
        if (listener == null || listener.length == 0) {
            throw new IllegalArgumentException("Missing listener argument.");
        }
        Collections.addAll(listeners, listener);
        updateInterruptListener();
    }

    public synchronized void addListener(List<? extends GpioPinListener> listeners) {
        for (GpioPinListener listener : listeners) {
            addListener(listener);
        }
    }

    /**
     * 
     */
    public synchronized Collection<GpioPinListener> getListeners() {
        return listeners;
    }

    @Override
    public boolean hasListener(GpioPinListener... listener) {
        if (listener == null || listener.length == 0) {
            throw new IllegalArgumentException("Missing listener argument.");
        }
        for (GpioPinListener lsnr : listener) {
            if (!listeners.contains(lsnr)) {
                return false;
            }
        }

        return true;
    }
    
    public synchronized void removeListener(GpioPinListener... listener) {
        if (listener == null || listener.length == 0) {
            throw new IllegalArgumentException("Missing listener argument.");
        }
        for (GpioPinListener lsnr : listener) {
            listeners.remove(lsnr);
        }
        
        updateInterruptListener();
    }

    public synchronized void removeListener(List<? extends GpioPinListener> listeners) {
        for (GpioPinListener listener : listeners) {
            removeListener(listener);
        }
    }
    
    public synchronized void removeAllListeners() {
        List<GpioPinListener> listeners_copy = new ArrayList<>(listeners);
        for (int index = (listeners_copy.size()-1); index >= 0; index --) {
            GpioPinListener listener = listeners_copy.get(index);
            removeListener(listener);
        }
    }

    /**
     * 
     */
    public synchronized Collection<GpioTrigger> getTriggers() {
        return triggers;
    }

    public synchronized void addTrigger(GpioTrigger... trigger) {
        if (trigger == null || trigger.length == 0) {
            throw new IllegalArgumentException("Missing trigger argument.");
        }
        Collections.addAll(triggers, trigger);
        updateInterruptListener();
    }

    public synchronized void addTrigger(List<? extends GpioTrigger> triggers) {
        for (GpioTrigger trigger : triggers) {
            addTrigger(trigger);
        }
    }

    /**
     * 
     * @param trigger GPIO trigger interface
     */
    public synchronized void removeTrigger(GpioTrigger... trigger) {
        if (trigger == null || trigger.length == 0) {
            throw new IllegalArgumentException("Missing trigger argument.");
        }
        for (GpioTrigger trgr : trigger) {
            triggers.remove(trgr);
        }
        
        updateInterruptListener();
    }

    public synchronized void removeTrigger(List<? extends GpioTrigger> triggers) {
        for (GpioTrigger trigger : triggers) {
            removeTrigger(trigger);
        }
    }

    public synchronized void removeAllTriggers() {
        List<GpioTrigger> triggers_copy = new ArrayList<>(triggers);
        for (int index = triggers_copy.size() - 1; index >= 0; index--) {
            GpioTrigger trigger = triggers_copy.get(index);
            removeTrigger(trigger);
        }
    }

    @Override
    public String toString() {
        if (name != null && !name.isEmpty()) {
            return String.format("\"%s\" <%s>", name, pin.toString());
        } else {
            return pin.toString();
        }
    }

    @Override
    public GpioPinShutdown getShutdownOptions() {
        return shutdownOptions;
    }

    @Override
    public void setShutdownOptions(GpioPinShutdown options) {
        shutdownOptions.setUnexport(options.getUnexport());
        shutdownOptions.setState(options.getState());
        shutdownOptions.setMode(options.getMode());
        shutdownOptions.setPullResistor(options.getPullResistor());
    }

    @Override
    public void setShutdownOptions(Boolean unexport) {
        setShutdownOptions(unexport, null);
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state) {
        setShutdownOptions(unexport, state, null);
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance)
    {
        setShutdownOptions(unexport, state, resistance, null);
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode) {
        shutdownOptions.setUnexport(unexport);
        shutdownOptions.setState(state);
        shutdownOptions.setMode(mode);
        shutdownOptions.setPullResistor(resistance);
    }

}
