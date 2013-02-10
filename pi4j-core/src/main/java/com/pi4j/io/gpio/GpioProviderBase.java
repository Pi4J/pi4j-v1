package com.pi4j.io.gpio;

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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinPullResistanceException;

/**
 * Abstract base implementation of {@link GpioProvider}.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public abstract class GpioProviderBase implements GpioProvider {

    public abstract String getName();

    protected final Map<Pin, List<PinListener>> listeners = new ConcurrentHashMap<Pin, List<PinListener>>();
    protected final Map<Pin, GpioProviderPinCache> cache = new ConcurrentHashMap<Pin, GpioProviderPinCache>();
    protected boolean isshutdown = false;
    
    @Override
    public boolean hasPin(Pin pin) {
        return (pin.getProvider() == getName());
    }
    
    protected GpioProviderPinCache getPinCache(Pin pin) {
        if (!cache.containsKey(pin)) {
            cache.put(pin, new GpioProviderPinCache(pin));
        }
        return cache.get(pin);
    }
    
    @Override
    public void export(Pin pin, PinMode mode) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        
        if (!pin.getSupportedPinModes().contains(mode)) {
            throw new UnsupportedPinModeException(pin, mode);
        }
        
        // cache exported state
        getPinCache(pin).setExported(true);
        
        // cache mode
        getPinCache(pin).setMode(mode);
    }
    
    @Override
    public boolean isExported(Pin pin) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        
        // return cached exported state
        return getPinCache(pin).isExported();
    }

    @Override
    public void unexport(Pin pin) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        
        // cache exported state
        getPinCache(pin).setExported(false);
    }

    @Override
    public void setMode(Pin pin, PinMode mode) {
        if (!pin.getSupportedPinModes().contains(mode)) {
            throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName() + "]; pin [" + pin.getName() + "] does not support this mode.");
        }

        if (!pin.getSupportedPinModes().contains(mode)) {
            throw new UnsupportedPinModeException(pin, mode);
        }
        
        // cache mode
        getPinCache(pin).setMode(mode);
    }

    @Override
    public PinMode getMode(Pin pin) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }

        // return cached mode value
        return getPinCache(pin).getMode();
    }
    
    
    @Override
    public void setPullResistance(Pin pin, PinPullResistance resistance) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        
        if (!pin.getSupportedPinPullResistance().contains(resistance)) {
            throw new UnsupportedPinPullResistanceException(pin, resistance);
        }
        
        // cache resistance
        getPinCache(pin).setResistance(resistance);
    }

    @Override
    public PinPullResistance getPullResistance(Pin pin) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        
        // return cached resistance
        return getPinCache(pin).getResistance();
    }
    
    @Override
    public void setState(Pin pin, PinState state) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        
        PinMode mode = getMode(pin);
        
        // only permit invocation on pins set to DIGITAL_OUTPUT modes 
        if (mode != PinMode.DIGITAL_OUTPUT) {
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName() + "]; cannot setState() when pin mode is [" + mode.getName() + "]");
        }
        
        // cache pin state
        getPinCache(pin).setState(state);
    }

    @Override
    public PinState getState(Pin pin) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        
        PinMode mode = getMode(pin);

        // only permit invocation on pins set to DIGITAL modes 
        if (!PinMode.allDigital().contains(mode)) {
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName() + "]; cannot getState() when pin mode is [" + mode.getName() + "]");
        }

        // return cached pin state
        return getPinCache(pin).getState();           
    }

    @Override
    public void setValue(Pin pin, double value) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        
        PinMode mode = getMode(pin);

        // only permit invocation on pins set to OUTPUT modes 
        if (!PinMode.allOutput().contains(mode)) {
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName() + "]; cannot setValue(" + value + ") when pin mode is [" + mode.getName() + "]");
        }
        
        // cache pin analog value
        getPinCache(pin).setAnalogValue(value);                        
    }

    @Override
    public double getValue(Pin pin) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }

        PinMode mode = getMode(pin);
        
        if (mode == PinMode.DIGITAL_OUTPUT) {
            return getState(pin).getValue(); 
        }
        
        // return cached pin analog value
        return getPinCache(pin).getAnalogValue();                             
    }
    
    @Override
    public void setPwm(Pin pin, int value) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);        
        }
        
        PinMode mode = getMode(pin);

        if (mode != PinMode.PWM_OUTPUT) {
            throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName() + "]; unable to setPwm(" + value + ")");
        }
        
        // cache pin PWM value
        getPinCache(pin).setPwmValue(value);                       
    }
    
    
    @Override
    public int getPwm(Pin pin) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }

        // return cached pin PWM value
        return getPinCache(pin).getPwmValue();                           
    }

    @Override
    public void addListener(Pin pin, PinListener listener) {
        // create new pin listener entry if one does not already exist
        if (!listeners.containsKey(pin)) {
            listeners.put(pin, new ArrayList<PinListener>());
        }
        
        // add the listener instance to the listeners map entry 
        List<PinListener> lsnrs = listeners.get(pin);
        if (!lsnrs.contains(listener)) {
            lsnrs.add(listener);
        }
    }
    
    @Override
    public void removeListener(Pin pin, PinListener listener) {
        // lookup to pin entry in the listeners map
        if (listeners.containsKey(pin)) {
            // remote the listener instance from the listeners map entry if found 
            List<PinListener> lsnrs = listeners.get(pin);
            if (lsnrs.contains(listener)) {
                lsnrs.remove(listener);
            }
            
            // if the listener list is empty, then remove the listener pin from the map
            if (lsnrs.isEmpty()) {
                listeners.remove(pin);
            }
        }
    }    
    
    @Override
    public void removeAllListeners() {
        // iterate over all listener pins in the map
        for (Pin pin : listeners.keySet()) {
            // iterate over all listener handler in the map entry
            // and remove each listener handler instance
            for (PinListener listener : listeners.get(pin)) {
                removeListener(pin, listener);
            }
        }
    }
    
    protected void dispatchPinDigitalStateChangeEvent(Pin pin, PinState state) {
        // if the pin listeners map contains this pin, then dispatch event
        if (listeners.containsKey(pin)) {
            // dispatch this event to all listener handlers
            for (PinListener listener : listeners.get(pin)) {
                listener.handlePinEvent(new PinDigitalStateChangeEvent(this, pin, state));
            }            
        }
    }
    
    protected void dispatchPinAnalogValueChangeEvent(Pin pin, double value) {
        // if the pin listeners map contains this pin, then dispatch event
        if (listeners.containsKey(pin)) {
            // dispatch this event to all listener handlers
            for (PinListener listener : listeners.get(pin)) {
                listener.handlePinEvent(new PinAnalogValueChangeEvent(this, pin, value));
            }            
        }
    }
    
    @Override
    public void shutdown() {
        
        // prevent reentrant invocation
        if(isShutdown())
            return;
        
        // set shutdown tracking state variable
        isshutdown = true;
    }     
        
    /**
     * This method returns TRUE if the GPIO provider has been shutdown.
     * 
     * @return shutdown state
     */
    @Override
    public boolean isShutdown(){
        return isshutdown;
    }    
}
