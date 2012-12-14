package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioControllerImpl.java  
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
import java.util.Collections;
import java.util.List;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinAnalog;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinInput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.exception.GpioPinExistsException;
import com.pi4j.io.gpio.exception.GpioPinNotProvisionedException;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public class GpioControllerImpl implements GpioController
{
    private final List<GpioPin> pins = Collections.synchronizedList(new ArrayList<GpioPin>());
    private final GpioProvider defaultProvider;
    
    /**
     * Default Constructor
     */
    public GpioControllerImpl()
    {
        // set the local default provider reference
        this(GpioFactory.getDefaultProvider());                
    }

    /**
     * Default Constructor
     */
    public GpioControllerImpl(GpioProvider provider)
    {
        // set the local default provider reference
        defaultProvider = provider;                

        // register shutdown callback hook class
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());        
    }
    
    @Override
    public Collection<GpioPin> getProvisionedPins()
    {
        return pins;
    }
    
    @Override
    public void unexportAll()
    {
        // un-export all GPIO pins that are currently exported
        for (GpioPin pin : pins)
        {            
            if (pin.isExported())
                pin.unexport();
        }
    }

    @Override
    public void export(PinMode mode, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // export the pin
            p.export(mode);
        }
    }

    /**
     * 
     * @param pin
     * @return <p>
     *         A value of 'true' is returned if the requested pin is exported.
     *         </p>
     */
    @Override
    public boolean isExported(GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for(GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(pin))
                throw new GpioPinNotProvisionedException(p.getPin());
            
            if(!p.isExported())
                return false;
        }

        // return the pin exported state
        return true;
    }

    @Override
    public void unexport(GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
            
            // unexport the pin
            p.unexport();            
        }
    }

    @Override
    public PinMode getMode(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if (!pins.contains(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // return pin edge setting
        return pin.getMode();    
    }

    @Override
    public boolean isMode(PinMode mode, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            if(!p.isMode(mode))
                return false;
        }
        return true;  
    }

    @Override
    public void setMode(PinMode mode, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(pin))
                throw new GpioPinNotProvisionedException(p.getPin());
            
            // set pin mode
            p.setMode(mode);        
        }
    }

    @Override
    public void setPullResistance(PinPullResistance resistance, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin pull resistance
            p.setPullResistance(resistance);
        }
    }

    @Override
    public PinPullResistance getPullResistance(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if (!pins.contains(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
    
        // get pin pull resistance
        return pin.getPullResistance();
    }

    @Override
    public boolean isPullResistance(PinPullResistance resistance, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin pull resistance
            if(!p.isPullResistance(resistance))
                return false;
        }

        return true;
    }
    
    @Override
    public void high(GpioPinDigitalOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // ensure the requested pin has been provisioned
        for (GpioPinDigitalOutput p : pin)
        {
            if (!pins.contains(pin))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin state high
            p.high();        
        }
    }

    @Override
    public void low(GpioPinDigitalOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // ensure the requested pin has been provisioned
        for (GpioPinDigitalOutput p : pin)
        {
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
            
            // set pin state low
            p.low();            
        }
    }

    @Override
    public boolean isHigh(GpioPinDigital... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPinDigital p : pin)
        {
            if(p.isLow())
                return false;
        }
        return true;        
    }

    @Override
    public boolean isLow(GpioPinDigital... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPinDigital p : pin)
        {
            if(p.isHigh())
                return false;
        }
        return true;                
    }

    @Override
    public void toggle(GpioPinDigitalOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPinDigitalOutput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // toggle pin state
            p.toggle();
        }
    }

    @Override
    public void pulse(long milliseconds, GpioPinDigitalOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinDigitalOutput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // toggle pin state
            p.pulse(milliseconds);
        }
    }

    @Override
    public void setState(PinState state, GpioPinDigitalOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinDigitalOutput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin state
            p.setState(state);
        }
    }

    @Override
    public void setState(boolean state, GpioPinDigitalOutput... pin)
    {
        setState((state) ? PinState.HIGH : PinState.LOW, pin);
    }

    @Override
    public PinState getState(GpioPinDigital pin)
    {
        // ensure the requested pin has been provisioned
        if (!pins.contains(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // return pin state
        return pin.getState();
    }    

    @Override
    public boolean isState(PinState state, GpioPinDigital... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPinDigital p : pin)
        {
            if(!p.isState(state))
                return false;
        }
        return true;     
    }
    
    @Override
    public void setValue(double value, GpioPinAnalogOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinAnalogOutput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin PWM value
            p.setValue(value);
        }        
    }

    @Override
    public double getValue(GpioPinAnalog pin)
    {
        return pin.getValue();
    }    

     @Override
    public synchronized void addListener(GpioPinListener listener, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinInput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());        
    
            p.addListener(listener);
        }
    }

    @Override
    public synchronized void addListener(GpioPinListener[] listeners, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinListener listener: listeners)
            addListener(listener, pin);
    }


    @Override
    public synchronized void removeListener(GpioPinListener listener, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinInput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.removeListener(listener);
        }
    }

    @Override
    public synchronized void removeListener(GpioPinListener[] listeners, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinListener listener : listeners)
        {
            removeListener(listener, pin);
        }
    }

    @Override
    public synchronized void removeAllListeners()
    {
        for (GpioPin pin : this.pins)
        {
            if(pin instanceof GpioPinInput)
                ((GpioPinInput)pin).removeAllListeners();
        }
    }

    @Override
    public synchronized void addTrigger(GpioTrigger trigger, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinInput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.addTrigger(trigger);
        }        
    }

    @Override
    public synchronized void addTrigger(GpioTrigger[] triggers, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioTrigger trigger : triggers)
            addTrigger(trigger, pin);
    }
    
    @Override
    public synchronized void removeTrigger(GpioTrigger trigger, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinInput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.removeTrigger(trigger);
        }
    }

    @Override
    public synchronized void removeTrigger(GpioTrigger[] triggers, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioTrigger trigger : triggers)
            removeTrigger(trigger, pin);
    }

    @Override
    public synchronized void removeAllTriggers()
    {
        for (GpioPin pin : this.pins)
        {
            if(pin instanceof GpioPinInput)
                ((GpioPinInput)pin).removeAllTriggers();            
        }
    }

    @Override
    public GpioPin provisionPin(GpioProvider provider, Pin pin, PinMode mode)
    {
        return provisionPin(provider, pin, pin.getName(), mode);
    }
    
    @Override
    public GpioPin provisionPin(GpioProvider provider, Pin pin, String name, PinMode mode)
    {
        // if an existing pin has been previously created, then throw an error
        for(GpioPin p : pins)
        {
            if (p.getProvider().equals(provider) && p.getPin().equals(pin))
                throw new GpioPinExistsException(pin);
        }

        // create new GPIO pin instance
        GpioPin gpioPin = new GpioPinImpl(this, provider, pin);

        // set the gpio pin name
        if (name != null)
            gpioPin.setName(name);

        // export this pin 
        gpioPin.export(mode);

        // add this new pin instance to the managed collection
        pins.add(gpioPin);

        // return new new pin instance
        return gpioPin;
    }

    @Override
    public GpioPin provisionPin(Pin pin, String name, PinMode mode)
    {
        return provisionPin(defaultProvider, pin, name, mode);
    }

    @Override
    public GpioPin provisionPin(Pin pin, PinMode mode)
    {
        return provisionPin(defaultProvider, pin, mode);
    }
    
    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinDigitalInput)provisionPin(provider, pin, name, PinMode.DIGITAL_INPUT);
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin)
    {
        // return new new pin instance
        return (GpioPinDigitalInput)provisionPin(provider, pin, PinMode.DIGITAL_INPUT);
    }
    
    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name)
    {
        return provisionDigitalInputPin(defaultProvider, pin, name);
    }
    
    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin)
    {
        return provisionDigitalInputPin(defaultProvider, pin);
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, PinPullResistance resistance)
    {
        // create new GPIO pin instance
        return provisionDigitalInputPin(provider, pin, pin.getName(), resistance);
    }
    
    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name, PinPullResistance resistance)
    {
        // create new GPIO pin instance
        GpioPinDigitalInput gpioPin = provisionDigitalInputPin(provider, pin, name);

        // set the gpio pull resistor
        if (resistance != null)
            gpioPin.setPullResistance(resistance);

        // return new new pin instance
        return gpioPin;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name, PinPullResistance resistance)
    {
        return provisionDigitalInputPin(defaultProvider, pin, name, resistance);
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, PinPullResistance resistance)
    {
        return provisionDigitalInputPin(defaultProvider, pin, resistance);
    }
    
    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinDigitalOutput)provisionPin(provider, pin, name, PinMode.DIGITAL_OUTPUT);
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin)
    {
        // return new new pin instance
        return (GpioPinDigitalOutput)provisionPin(provider, pin, PinMode.DIGITAL_OUTPUT);
    }
    
    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String name)
    {
        return provisionDigitalOutputPin(defaultProvider, pin, name);
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin)
    {
        return provisionDigitalOutputPin(defaultProvider, pin);
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, PinState defaultState)
    {
        return provisionDigitalOutputPin(provider, pin, pin.getName(), defaultState);
    }
    
    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, String name, PinState defaultState)
    {
        // create new GPIO pin instance
        GpioPinDigitalOutput gpioPin = provisionDigitalOutputPin(provider, pin, name);

        // apply default state
        if (defaultState != null)
            gpioPin.setState(defaultState);

        // return new new pin instance
        return gpioPin;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String name, PinState defaultState)
    {
        return provisionDigitalOutputPin(defaultProvider, pin, name, defaultState);
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, PinState defaultState)
    {
        return provisionDigitalOutputPin(defaultProvider, pin, defaultState);
    }
    
    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinAnalogInput)provisionPin(provider, pin, name, PinMode.ANALOG_INPUT);
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin)
    {
        // return new new pin instance
        return (GpioPinAnalogInput)provisionPin(provider, pin, PinMode.ANALOG_INPUT);
    }
    
    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin, String name)
    {
        return provisionAnalogInputPin(defaultProvider, pin, name);
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin)
    {
        return provisionAnalogInputPin(defaultProvider, pin);
    }
    
    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinAnalogOutput)provisionPin(provider, pin, name, PinMode.ANALOG_OUTPUT);
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin)
    {
        // return new new pin instance
        return (GpioPinAnalogOutput)provisionPin(provider, pin, PinMode.ANALOG_OUTPUT);
    }
    
    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String name)
    {
        return provisionAnalogOutputPin(defaultProvider, pin, name);
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin)
    {
        return provisionAnalogOutputPin(defaultProvider, pin);
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, double defaultValue)
    {
        return provisionAnalogOutputPin(provider, pin, pin.getName(), defaultValue);
    }
    
    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, String name, double defaultValue)
    {
        // create new GPIO pin instance
        GpioPinAnalogOutput gpioPin = provisionAnalogOutputPin(provider, pin, name);

        // apply default value
        gpioPin.setValue(defaultValue);

        // return new new pin instance
        return gpioPin;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String name, double defaultValue)
    {
        return provisionAnalogOutputPin(defaultProvider, pin, name, defaultValue);
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, double defaultValue)
    {
        return provisionAnalogOutputPin(defaultProvider, pin, defaultValue);
    }
    
    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinPwmOutput)provisionPin(provider, pin, name, PinMode.PWM_OUTPUT);
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin)
    {
        // return new new pin instance
        return (GpioPinPwmOutput)provisionPin(provider, pin, PinMode.PWM_OUTPUT);
    }
    
    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name)
    {
        return provisionPwmOutputPin(defaultProvider, pin, name);
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin)
    {
        return provisionPwmOutputPin(defaultProvider, pin);
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, int defaultValue)
    {
        return provisionPwmOutputPin(provider, pin, pin.getName(), defaultValue);
    }
    
    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name, int defaultValue)
    {
        // create new GPIO pin instance
        GpioPinPwmOutput gpioPin = provisionPwmOutputPin(provider, pin, name);

        // apply default value
        gpioPin.setPwm(defaultValue);

        // return new new pin instance
        return gpioPin;
    }
    
    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name, int defaultValue)
    {
        return provisionPwmOutputPin(defaultProvider, pin, name, defaultValue);
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, int defaultValue)
    {
        return provisionPwmOutputPin(defaultProvider, pin, defaultValue);
    }
    
    @Override
    public void unprovisionPin(GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (int index = (pin.length-1); index >= 0; index--)
        {
            GpioPin p  = pin[index];
            
            // ensure the requested pin has been provisioned
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
            
            // remove all listeners and triggers
            if(p instanceof GpioPinInput)
            {
                ((GpioPinInput)p).removeAllListeners();
                ((GpioPinInput)p).removeAllTriggers();
            }
            
            // remove this pin instance from the managed collection
            pins.remove(p);
        }        
    }
    
    public void setShutdownOptions(GpioPinShutdown options, GpioPin... pin)
    {
        for (GpioPin p : pin)
        {
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.setShutdownOptions(options);
        }  
    }
    
    public void setShutdownOptions(Boolean unexport, GpioPin... pin)
    {
        for (GpioPin p : pin)
        {
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.setShutdownOptions(unexport);
        }          
    }
    
    public void setShutdownOptions(Boolean unexport, PinState state, GpioPin... pin)
    {
        for (GpioPin p : pin)
        {
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.setShutdownOptions(unexport, state);
        }          
    }
    
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, GpioPin... pin)
    {
        for (GpioPin p : pin)
        {
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.setShutdownOptions(unexport, state, resistance);
        }          
    }
    
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode, GpioPin... pin)
    {
        for (GpioPin p : pin)
        {
            if (!pins.contains(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.setShutdownOptions(unexport, state, resistance, mode);
        }                  
    }

    
    /**
     * This class is used to perform any configured shutdown actions
     * on the provisioned GPIO pins
     * 
     * @author Robert Savage
     *
     */
    private class ShutdownHook extends Thread
    {        
        public void run()
        {
            // shutdown executor services
            GpioScheduledExecutorImpl.shutdown();
            GpioEventMonitorExecutorImpl.shutdown();
            
            // shutdown explicit configured GPIO pins
            for (GpioPin pin : pins)
            {
                GpioPinShutdown shutdownOptions = pin.getShutdownOptions(); 
                if(shutdownOptions != null)
                {
                    // get shutdown option configuration 
                    PinState state = shutdownOptions.getState();
                    PinMode mode = shutdownOptions.getMode();
                    PinPullResistance resistance = shutdownOptions.getPullResistor();
                    Boolean unexport = shutdownOptions.getUnexport();
                    
                    // perform shutdown actions
                    if((state != null) && (pin instanceof GpioPinDigitalOutput))
                    {                        
                        ((GpioPinDigitalOutput)pin).setState(state);
                    }
                    if(resistance != null)
                    {
                        pin.setPullResistance(resistance);
                    }
                    if(mode != null)
                    {
                        pin.setMode(mode);
                    }
                    if(unexport != null && unexport == Boolean.TRUE)
                    {
                        pin.unexport();
                    }
                }
            }
        }
    }
}
