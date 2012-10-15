package com.pi4j.io.gpio.impl;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinPullResistanceException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioProviderWrapper.java  
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


public class GpioProviderWrapper implements GpioProvider
{
    private final GpioProvider provider;

    public GpioProviderWrapper(GpioProvider provider)
    {
        this.provider = provider;
    }
    
    public GpioProvider getUnderlyingProvider()
    {
        return provider;
    }
    
    public String getName()
    {
        return provider.getName();
    }
    
    public boolean hasPin(Pin pin)
    {
        return provider.hasPin(pin);
    }
    
    @Override
    public void export(Pin pin, PinMode mode)
    {
        if(hasPin(pin) == false)
            throw new InvalidPinException(pin);
        
        if(!pin.getSupportedPinModes().contains(mode))
            throw new UnsupportedPinModeException(pin, mode);
        
        provider.export(pin, mode);
    }
    
    @Override
    public boolean isExported(Pin pin)
    {
        if(hasPin(pin) == false)
            throw new InvalidPinException(pin);
        
        return provider.isExported(pin);
    }

    @Override
    public void unexport(Pin pin)
    {
        if(hasPin(pin) == false)
            throw new InvalidPinException(pin);
        
        provider.unexport(pin);        
    }

    @Override
    public void setMode(Pin pin, PinMode mode)
    {
        if(!pin.getSupportedPinModes().contains(mode))
            throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName() + "]; pin [" + pin.getName() + "] does not support this mode.");

        if(!pin.getSupportedPinModes().contains(mode))
            throw new UnsupportedPinModeException(pin, mode);
        
        provider.setMode(pin, mode);
    }

    @Override
    public PinMode getMode(Pin pin)
    {
        if(hasPin(pin) == false)
            throw new InvalidPinException(pin);
        
        return provider.getMode(pin);
    }
    
    
    @Override
    public void setPullResistance(Pin pin, PinPullResistance resistance)
    {
        if(hasPin(pin) == false)
            throw new InvalidPinException(pin);
        
        if(!pin.getSupportedPinPullResistance().contains(resistance))
            throw new UnsupportedPinPullResistanceException(pin, resistance);
        
        provider.setPullResistance(pin, resistance);
    }

    @Override
    public PinPullResistance getPullResistance(Pin pin)
    {
        if(hasPin(pin) == false)
            throw new InvalidPinException(pin);
        
        return provider.getPullResistance(pin);
    }
    
    @Override
    public void setState(Pin pin, PinState state)
    {
        if(hasPin(pin) == false)
            throw new InvalidPinException(pin);
        
        PinMode mode = getMode(pin);
        
        // only permit invocation on pins set to DIGITAL_OUTPUT modes 
        if(mode != PinMode.DIGITAL_OUTPUT)
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName() + "]; cannot setState() when pin mode is [" + mode.getName() + "]");
        
        provider.setState(pin, state);
    }

    @Override
    public PinState getState(Pin pin)
    {
        if(hasPin(pin) == false)
            throw new InvalidPinException(pin);
        
        PinMode mode = getMode(pin);

        // only permit invocation on pins set to DIGITAL modes 
        if(!PinMode.allDigital().contains(mode))
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName() + "]; cannot getState() when pin mode is [" + mode.getName() + "]");

        return provider.getState(pin);
    }

    @Override
    public void setValue(Pin pin, int value)
    {
        if(hasPin(pin) == false)
            throw new InvalidPinException(pin);
        
        PinMode mode = getMode(pin);

        // only permit invocation on pins set to OUTPUT modes 
        if(!PinMode.allOutput().contains(mode))
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName() + "]; cannot setValue(" + value + ") when pin mode is [" + mode.getName() + "]");
        
        // if this pin is set as a digital output mode, 
        // then use the set state method instead
        if(mode == PinMode.DIGITAL_OUTPUT)
        {
            setState(pin, (value > 0) ? PinState.HIGH : PinState.LOW);
        }
        else
        {
            provider.setValue(pin, value);
        }
    }

    @Override
    public int getValue(Pin pin)
    {
        if(hasPin(pin) == false)
            throw new InvalidPinException(pin);

        PinMode mode = getMode(pin);
        
        if(mode == PinMode.DIGITAL_OUTPUT)
        {
            return getState(pin).getValue(); 
        }
        else
        {
            return provider.getValue(pin);
        }
    }

    @Override
    public void setPwm(Pin pin, int value)
    {
        provider.setPwm(pin, value);
    }

    @Override
    public int getPwm(Pin pin)
    {
        return provider.getPwm(pin);
    }
    
    @Override
    public void addListener(Pin pin, PinListener listener)
    {
        provider.addListener(pin, listener);        
    }
    
    @Override
    public void removeListener(Pin pin, PinListener listener)
    {
        provider.removeListener(pin, listener);        
    }
    
    @Override
    public void removeAllListeners()
    {
        provider.removeAllListeners();        
    }
}
