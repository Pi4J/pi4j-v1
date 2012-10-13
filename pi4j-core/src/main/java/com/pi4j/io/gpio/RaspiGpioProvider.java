package com.pi4j.io.gpio;

import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.impl.GpioProviderBase;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioInterruptListener;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  RaspiGpioProvider.java  
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


public class RaspiGpioProvider extends GpioProviderBase implements GpioProvider, GpioInterruptListener
{
    public static final String NAME = "RaspberryPi GPIO Provider";
    
    private PinMode mode;
    private PinPullResistance resistance;

    @Override
    public void initialize()
    {
        // set wiringPi interface for internal use
        // we will use the WiringPi pin number scheme with the wiringPi library
        com.pi4j.wiringpi.Gpio.wiringPiSetup();
    }
    
    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public boolean hasPin(Pin pin)
    {
        return (com.pi4j.wiringpi.Gpio.wpiPinToGpio(pin.getAddress()) >= 0);
    }

    @Override
    public void export(Pin pin, PinMode mode)
    {
        // export the pin and set the mode
        com.pi4j.wiringpi.GpioUtil.export(pin.getAddress(), mode.getDirection().getValue());
        setMode(pin, mode);
    }

    @Override
    public boolean isExported(Pin pin)
    {
        // return the pin exported state
        return com.pi4j.wiringpi.GpioUtil.isExported(pin.getAddress());
    }

    @Override
    public void unexport(Pin pin)
    {
        // unexport the pins
        com.pi4j.wiringpi.GpioUtil.unexport(pin.getAddress());
    }

    @Override
    public void setMode(Pin pin, PinMode mode)
    {
        com.pi4j.wiringpi.Gpio.pinMode(pin.getAddress(), mode.getValue());
        
        // if this is an input pin, then configure edge detection
        if(PinMode.allInputs().contains(mode))
            com.pi4j.wiringpi.GpioUtil.setEdgeDetection(pin.getAddress(), PinEdge.BOTH.getValue());
        
        // cache mode
        this.mode = mode;
    }

    @Override
    public PinMode getMode(Pin pin)
    {
        // TODO get actual pin mode from native impl
        return mode;
    }
    
    @Override
    public void setPullResistance(Pin pin, PinPullResistance resistance)
    {
        com.pi4j.wiringpi.Gpio.pullUpDnControl(pin.getAddress(), resistance.getValue());
        
        // cache resistance
        this.resistance = resistance;
    }

    @Override
    public PinPullResistance getPullResistance(Pin pin)
    {
        // TODO get actual pin pull resistance from native impl
        return resistance;
    }
    
    @Override
    public void setState(Pin pin, PinState state)
    {
        com.pi4j.wiringpi.Gpio.digitalWrite(pin.getAddress(), state.getValue());        
    }

    @Override
    public PinState getState(Pin pin)
    {
        // return pin state
        PinState state = null;
        int ret = com.pi4j.wiringpi.Gpio.digitalRead(pin.getAddress());
        if (ret >= 0)
            state = PinState.getState(ret);
        return state;
    }

    @Override
    public void setValue(Pin pin, int value)
    {
        throw new RuntimeException("This GPIO provider does not support analog pins.");
    }

    @Override
    public int getValue(Pin pin)
    {
        throw new RuntimeException("This GPIO provider does not support analog pins.");
    }

    @Override
    public void setPwm(Pin pin, int value)
    {
        PinMode mode = getMode(pin);

        if(mode == PinMode.PWM_OUTPUT)
        {
            setPwmValue(pin, value);
        }
        else
        {
            throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName() + "]; unable to setPwm(" + value + ")");
        }
    }

    @Override
    public int getPwm(Pin pin)
    {
        // TODO implement actual pin value getter in native impl
        throw new RuntimeException("NOT IMPLEMENTED");
    }
    
    public void setPwmValue(Pin pin, int value)
    {
        // set pin PWM value
        com.pi4j.wiringpi.Gpio.pwmWrite(pin.getAddress(), value);
    }

    @Override
    public void pinStateChange(GpioInterruptEvent event)
    {
        // iterate over the pin listeners map
        for(Pin pin : listeners.keySet())
        {
            // dispatch this event to the listener 
            // if a matching pin address is found
            if(pin.getAddress() == event.getPin())
            {
                // dispatch this event to all listener handlers
                for(PinListener listener : listeners.get(pin))
                {
                    listener.handlePinEvent(new PinDigitalStateChangeEvent(this, pin, PinState.getState(event.getState())));
                }
            }            
        }
    }

    @Override
    public void addListener(Pin pin, PinListener listener)
    {
        super.addListener(pin, listener);

        // update the native interrupt listener thread for callbacks
        updateInterruptListener(pin);        
    }
    
    @Override
    public void removeListener(Pin pin, PinListener listener)
    {
        super.removeListener(pin, listener);
        
        // update the native interrupt listener thread for callbacks
        updateInterruptListener(pin);        
    }
    
    // internal 
    private void updateInterruptListener(Pin pin)
    {
        if (listeners.size() > 0)
        {
            // setup interrupt listener native thread and enable callbacks
            com.pi4j.wiringpi.GpioInterrupt.addListener(this);
            com.pi4j.wiringpi.GpioInterrupt.enablePinStateChangeCallback(pin.getAddress());
        }
        else
        {
            // remove interrupt listener, disable native thread and callbacks
            com.pi4j.wiringpi.GpioInterrupt.disablePinStateChangeCallback(pin.getAddress());
            com.pi4j.wiringpi.GpioInterrupt.removeListener(this);
        }
    }    
}
