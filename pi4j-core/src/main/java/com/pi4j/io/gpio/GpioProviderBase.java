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
 * Copyright (C) 2012 - 2019 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */


import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinPullResistanceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base implementation of {@link com.pi4j.io.gpio.GpioProvider}.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public abstract class GpioProviderBase implements GpioProvider {

    public static final int DEFAULT_CACHE_SIZE = 100;

    public abstract String getName();

    protected final Map<Pin, List<PinListener>> listeners = new ConcurrentHashMap<>();

    // support up to pin address 100 by default.
    // (dynamically expand array to accommodate cases where the pin addresses
    //  may be higher than the default allocation capacity of 100.)
    protected GpioProviderPinCache[] cache = new GpioProviderPinCache[DEFAULT_CACHE_SIZE];

    protected boolean isshutdown = false;

    @Override
    public boolean hasPin(Pin pin) {
        return (pin.getProvider().equals(getName()));
    }

    protected GpioProviderPinCache getPinCache(Pin pin) {

        int address = pin.getAddress();

        // dynamically resize pin cache storage if needed based on pin address
        if(address >= cache.length){
            // create a new array with existing contents
            // that is 100 elements larger than the requested address
            // (we add the extra 100 elements to provide additional overhead capacity in
            //  an attempt to minimize further array expansion)
            cache = Arrays.copyOf(cache, address + 100);
        }

        // get the cached pin object from the cache
        GpioProviderPinCache pc = cache[address];

        // if no pin object is found in the cache, then we need to create one at this address index in the cache array
        if(pc == null){
            pc = cache[pin.getAddress()] = new GpioProviderPinCache(pin);
        }
        return pc;
    }

    @Override
    public void export(Pin pin, PinMode mode, PinState defaultState) {
        // export the pin and set it's mode
        export(pin, mode);

        // apply default state if one was provided and only if this pin is a digital output
        if(defaultState != null && mode == PinMode.DIGITAL_OUTPUT) {
            setState(pin, defaultState);
        }
    }

    @Override
    public void export(Pin pin, PinMode mode) {
        if (!hasPin(pin)) {
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
        if (!hasPin(pin)) {
            throw new InvalidPinException(pin);
        }

        // return cached exported state
        return getPinCache(pin).isExported();
    }

    @Override
    public void unexport(Pin pin) {
        if (!hasPin(pin)) {
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
        if (!hasPin(pin)) {
            throw new InvalidPinException(pin);
        }

        // return cached mode value
        return getPinCache(pin).getMode();
    }


    @Override
    public void setPullResistance(Pin pin, PinPullResistance resistance) {
        if (!hasPin(pin)) {
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
        if (!hasPin(pin)) {
            throw new InvalidPinException(pin);
        }

        // return cached resistance
        return getPinCache(pin).getResistance();
    }

    @Override
    public void setState(Pin pin, PinState state) {
        if (!hasPin(pin)) {
            throw new InvalidPinException(pin);
        }

        GpioProviderPinCache pinCache = getPinCache(pin);

        // only permit invocation on pins set to DIGITAL_OUTPUT modes
        if (pinCache.getMode() != PinMode.DIGITAL_OUTPUT) {
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName() + "]; cannot setState() when pin mode is [" + pinCache.getMode().getName() + "]");
        }

        // for digital output pins, we will echo the event feedback
        dispatchPinDigitalStateChangeEvent(pin, state);

        // cache pin state
        pinCache.setState(state);
    }

    @Override
    public PinState getState(Pin pin) {
        // the getMode() will validate the pin exists with the hasPin() function
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

        // the getMode() will validate the pin exists with the hasPin() function
        PinMode mode = getMode(pin);

        // only permit invocation on pins set to OUTPUT modes
        if (!PinMode.allOutput().contains(mode)) {
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName() + "]; cannot setValue(" + value + ") when pin mode is [" + mode.getName() + "]");
        }

        // for digital analog pins, we will echo the event feedback
        dispatchPinAnalogValueChangeEvent(pin, value);

        // cache pin analog value
        getPinCache(pin).setAnalogValue(value);
    }

    @Override
    public double getValue(Pin pin) {
        // the getMode() will validate the pin exists with the hasPin() function
        PinMode mode = getMode(pin);

        if (mode == PinMode.DIGITAL_OUTPUT) {
            return getState(pin).getValue();
        }

        // return cached pin analog value
        return getPinCache(pin).getAnalogValue();
    }

    @Override
    public void setPwm(Pin pin, int value) {
        if (!hasPin(pin)) {
            throw new InvalidPinException(pin);
        }

        PinMode mode = getMode(pin);

        if (mode != PinMode.PWM_OUTPUT || mode != PinMode.SOFT_PWM_OUTPUT) {
            throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName() + "]; unable to setPwm(" + value + ")");
        }

        // cache pin PWM value
        getPinCache(pin).setPwmValue(value);
    }

    @Override
    public void setPwmRange(Pin pin, int range){
        if (!hasPin(pin)) {
            throw new InvalidPinException(pin);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPwm(Pin pin) {
        if (!hasPin(pin)) {
            throw new InvalidPinException(pin);
        }

        // return cached pin PWM value
        return getPinCache(pin).getPwmValue();
    }

    @Override
    public void addListener(Pin pin, PinListener listener) {
        synchronized (listeners) {
            // create new pin listener entry if one does not already exist
            if (!listeners.containsKey(pin)) {
                listeners.put(pin, new ArrayList<>());
            }

            // add the listener instance to the listeners map entry
            List<PinListener> lsnrs = listeners.get(pin);
            if (!lsnrs.contains(listener)) {
                lsnrs.add(listener);
            }
        }
    }

    @Override
    public void removeListener(Pin pin, PinListener listener) {
        synchronized (listeners) {
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
    }

    @Override
    public void removeAllListeners() {
        synchronized (listeners) {
            // iterate over all listener pins in the map
            List<Pin> pins_copy = new ArrayList<>(listeners.keySet());
            for (Pin pin : pins_copy) {
                if(listeners.containsKey(pin)) {
                    // iterate over all listener handler in the map entry
                    // and remove each listener handler instance
                    List<PinListener> lsnrs = listeners.get(pin);
                    if (!lsnrs.isEmpty()) {
                        List<PinListener> lsnrs_copy = new ArrayList<>(lsnrs);
                        for (int index = lsnrs_copy.size() - 1; index >= 0; index--) {
                            PinListener listener = lsnrs_copy.get(index);
                            removeListener(pin, listener);
                        }
                    }
                }
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

        // remove all listeners
        removeAllListeners();

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
