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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
 // TODO : redefine indexing logic on pins map collection; must now account for multiple providers
    private final Map<Pin, GpioPin> pins = new ConcurrentHashMap<Pin, GpioPin>();
    private final GpioProvider defaultProvider;
    //private Map<String, GpioProvider> providers = new ConcurrentHashMap<String, GpioProvider>();
    
// TODO : deal with GpioProviderWrapper
    
    /**
     * Default Constructor
     */
    public GpioControllerImpl()
    {
        // set the local default provider reference
        defaultProvider = GpioFactory.getDefaultProvider();                

        // register shutdown callback hook class
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());        
    }

//    public GpioProvider getPinProvider(Pin pin)
//    {
//        if(!providers.containsKey(pin.getProvider()))
//            throw new PinProviderException(pin);
//        return providers.get(pin.getProvider());
//    }

    @Override    
    public boolean hasPin(GpioProvider provider, Pin... pin)
    {
        for(Pin p : pin)
        {            
            if(provider.hasPin(p))
                return false;
        }
        return true; 
    }
    
    @Override
    public boolean hasPin(Pin... pin)
    {
        return hasPin(defaultProvider, pin);
    }

    @Override
    public boolean isProvisioned(GpioProvider provider, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
// TODO : need more work
        for(Pin p : pin)
        {
            if(!pins.containsKey(p))
                return false;
        }
        
        return true;
    }
    
    @Override
    public boolean isProvisioned(Pin... pin)
    {
        return isProvisioned(defaultProvider, pin);
    }
    
    @Override
    public GpioPin getProvisionedPin(GpioProvider provider, Pin pin)
    {
// TODO : use provider as part of index to obtain GpioPin instance        
        if (pins.containsKey(pin))
            return pins.get(pin);
        return null;
    }

    @Override
    public GpioPin getProvisionedPin(Pin pin)
    {
        return getProvisionedPin(defaultProvider, pin);
    }
    
    @Override
    public Collection<GpioPin> getProvisionedPins(GpioProvider provider, Pin... pin)
    {
        // if no argument is provided, return all pins
        if(pin == null || pin.length == 0)
            return pins.values();

// TODO : need more work        
        ArrayList<GpioPin> result = new ArrayList<GpioPin>();
        for(Pin p : pin)
        {
           if(pins.containsKey(p))
            result.add(pins.get(p)); 
        }

        return result;
    }
    
    @Override
    public Collection<GpioPin> getProvisionedPins(Pin... pin)
    {
        return getProvisionedPins(defaultProvider, pin);
    }

    @Override
    public void unexportAll()
    {
        // un-export all GPIO pins that are currently exported
        for (GpioPin pin : pins.values())
        {            
            if (pin.isExported())
                pin.unexport();
        }
    }

    @Override
    public void export(GpioProvider provider, PinMode mode, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // export the pin
        for (Pin p : pin)
            provider.export(p, mode);
    }

    @Override
    public void export(PinMode mode, Pin... pin)
    {
        export(defaultProvider, mode, pin);
    }
    
    @Override
    public void export(PinMode mode, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
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
    public boolean isExported(GpioProvider provider, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // return the pin exported state
        for(Pin p : pin)
        {            
            if(!defaultProvider.isExported(p))
                return false;                
        }
        return true;
    }

    /**
     * 
     * @param pin
     * @return <p>
     *         A value of 'true' is returned if the requested pin is exported.
     *         </p>
     */
    @Override
    public boolean isExported(Pin... pin)
    {
        return isExported(defaultProvider, pin);
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
            if (!pins.containsValue(pin))
                throw new GpioPinNotProvisionedException(p.getPin());
            
            if(!p.isExported())
                return false;
        }

        // return the pin exported state
        return true;
    }

    @Override
    public void unexport(GpioProvider provider, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // unexport the pins
        for (Pin p : pin)
            provider.unexport(p);
    }

    
    @Override
    public void unexport(Pin... pin)
    {
        unexport(defaultProvider, pin);
    }
    

    @Override
    public void unexport(GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
                throw new GpioPinNotProvisionedException(p.getPin());
            
            // unexport the pin
            p.unexport();            
        }
    }

    @Override
    public PinMode getMode(GpioProvider provider, Pin pin)
    {
        return provider.getMode(pin);
    }
    
    @Override
    public PinMode getMode(Pin pin)
    {
        return getMode(defaultProvider, pin);
    }

    @Override
    public PinMode getMode(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if (!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // return pin edge setting
        return pin.getMode();    
    }

    @Override
    public boolean isMode(GpioProvider provider, PinMode mode, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
        {
            if(getMode(provider, p) != mode)
                return false;
        }
        return true;  
    }
    
    @Override
    public boolean isMode(PinMode mode, Pin... pin)
    {
        return isMode(defaultProvider, mode, pin);  
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
    public void setMode(GpioProvider provider, PinMode mode, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin mode
        for (Pin p : pin)
            provider.setMode(p, mode);
    }

    @Override
    public void setMode(PinMode mode, Pin... pin)
    {
        setMode(defaultProvider, mode, pin);
    }    

    @Override
    public void setMode(PinMode mode, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(pin))
                throw new GpioPinNotProvisionedException(p.getPin());
            
            // set pin mode
            p.setMode(mode);        
        }
    }

    @Override
    public void setPullResistance(GpioProvider provider, PinPullResistance resistance, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin pull resistance
        for (Pin p : pin)
            provider.setPullResistance(p, resistance);
    }
    
    @Override
    public void setPullResistance(PinPullResistance resistance, Pin... pin)
    {
        setPullResistance(defaultProvider, resistance, pin);
    }

    @Override
    public void setPullResistance(PinPullResistance resistance, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin pull resistance
            p.setPullResistance(resistance);
        }
    }

    @Override
    public PinPullResistance getPullResistance(GpioProvider provider, Pin pin)
    {
        return provider.getPullResistance(pin);
    }

    @Override
    public PinPullResistance getPullResistance(Pin pin)
    {
        return getPullResistance(defaultProvider, pin);
    }

    @Override
    public PinPullResistance getPullResistance(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if (!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
    
        // get pin pull resistance
        return pin.getPullResistance();
    }

    @Override
    public boolean isPullResistance(GpioProvider provider, PinPullResistance resistance, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
        {
            if(getPullResistance(provider, p) != resistance)
                return false;
        }
        return true; 
    }
    
    @Override
    public boolean isPullResistance(PinPullResistance resistance, Pin... pin)
    {
        return isPullResistance(defaultProvider, resistance, pin); 
    }

    @Override
    public boolean isPullResistance(PinPullResistance resistance, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin pull resistance
            if(!p.isPullResistance(resistance))
                return false;
        }

        return true;
    }
    
    @Override
    public void high(GpioProvider provider, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin state high
        for (Pin p : pin)
            setState(provider, PinState.HIGH, p);
    }
    
    @Override
    public void high(Pin... pin)
    {
        high(defaultProvider, pin);
    }

    @Override
    public void high(GpioPinDigitalOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // ensure the requested pin has been provisioned
        for (GpioPinDigitalOutput p : pin)
        {
            if (!pins.containsValue(pin))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin state high
            p.high();        
        }
    }

    @Override
    public void low(GpioProvider provider, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin state low
        for (Pin p : pin)
            setState(provider, PinState.LOW, p);
    }

    @Override
    public void low(Pin... pin)
    {
        low(defaultProvider, pin);
    }

    @Override
    public void low(GpioPinDigitalOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // ensure the requested pin has been provisioned
        for (GpioPinDigitalOutput p : pin)
        {
            if (!pins.containsValue(p))
                throw new GpioPinNotProvisionedException(p.getPin());
            
            // set pin state low
            p.low();            
        }
    }

    @Override
    public boolean isHigh(GpioProvider provider, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
        {
            if(getState(provider, p).isLow())
                return false;
        }
        return true;        
    }
    
    @Override
    public boolean isHigh(Pin... pin)
    {
        return isHigh(defaultProvider, pin);        
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
    public boolean isLow(GpioProvider provider, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (Pin p : pin)
        {
            if(getState(provider, p).isHigh())
                return false;
        }
        return true;                
    }
    
    @Override
    public boolean isLow(Pin... pin)
    {
        return isLow(defaultProvider, pin);                
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
    public void toggle(GpioProvider provider, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // toggle pin state
        for (Pin p : pin)
        {
            if (getState(p) == PinState.HIGH)
                setState(provider, PinState.LOW, p);
            else
                setState(provider, PinState.HIGH, p);
        }
    }

    @Override
    public void toggle(Pin... pin)
    {
        toggle(defaultProvider, pin);
    }
    
    @Override
    public void toggle(GpioPinDigitalOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPinDigitalOutput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // toggle pin state
            p.toggle();
        }
    }

    @Override
    public void pulse(GpioProvider provider, long milliseconds, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // toggle pin state
        for (Pin p : pin)
            GpioPulseImpl.execute(this, provider, p, milliseconds);
    }
    
    @Override
    public void pulse(long milliseconds, Pin... pin)
    {
        pulse(defaultProvider, milliseconds, pin);
    }

    @Override
    public void pulse(long milliseconds, GpioPinDigitalOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinDigitalOutput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // toggle pin state
            p.pulse(milliseconds);
        }
    }

    @Override
    public void setState(GpioProvider provider, PinState state, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin state
        for (Pin p : pin)
            provider.setState(p, state);
    }

    @Override
    public void setState(PinState state, Pin... pin)
    {
        setState(defaultProvider, state, pin);
    }

    @Override
    public void setState(GpioProvider provider, boolean state, Pin... pin)
    {
        setState(provider, (state) ? PinState.HIGH : PinState.LOW, pin);
    }

    @Override
    public void setState(boolean state, Pin... pin)
    {
        setState(defaultProvider, state, pin);
    }
    
    @Override
    public void setState(PinState state, GpioPinDigitalOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinDigitalOutput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
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
    public PinState getState(GpioProvider provider, Pin pin)
    {
        // return pin state
        return provider.getState(pin);
    }

    @Override
    public PinState getState(Pin pin)
    {
        // return pin state
        return getState(defaultProvider, pin);
    }
    
    @Override
    public PinState getState(GpioPinDigital pin)
    {
        // ensure the requested pin has been provisioned
        if (!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // return pin state
        return pin.getState();
    }    

    @Override
    public boolean isState(GpioProvider provider, PinState state, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
        {
            if(getState(provider, p) != state)
                return false;
        }
        return true;                
    }
    
    @Override
    public boolean isState(PinState state, Pin... pin)
    {
        return isState(defaultProvider, state, pin);                
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
    public void setValue(GpioProvider provider, int value, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin PWM value
        for (Pin p : pin)
            provider.setValue(p, value);
    }
    
    @Override
    public void setValue(int value, Pin... pin)
    {
        setValue(defaultProvider, value, pin);
    }


    @Override
    public void setValue(int value, GpioPinAnalogOutput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinAnalogOutput p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin PWM value
            p.setValue(value);
        }        
    }

    @Override
    public int getValue(GpioProvider provider, Pin pin)
    {
        return provider.getValue(pin);
    }
    
    @Override
    public int getValue(Pin pin)
    {
        return getValue(defaultProvider, pin);
    }

    @Override
    public int getValue(GpioPinAnalog pin)
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
            if (!pins.containsValue(p))
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
            if (!pins.containsKey(p))
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
// TODO : redefine indexing logic on pins map collection; must now account for multiple providers        
        for (GpioPin pin : this.pins.values())
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
            if (!pins.containsValue(p))
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
            if (!pins.containsValue(p))
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
        for (GpioPin pin : this.pins.values())
        {
            if(pin instanceof GpioPinInput)
                ((GpioPinInput)pin).removeAllTriggers();            
        }
    }

    @Override
    public GpioPin provisionPin(GpioProvider provider, Pin pin, String name, PinMode mode)
    {
        // if an existing pin has been previously created, then throw an error
        if (pins.containsKey(pin))
            throw new GpioPinExistsException(pin);

        // create new GPIO pin instance
        GpioPin gpioPin = new GpioPinImpl(this, provider, pin);

        // set the gpio pin name
        if (name != null)
            gpioPin.setName(name);

        // export this pin as a DIGITAL_INPUT
        gpioPin.export(mode);

        // add this new pin instance to the managed collection
        pins.put(pin, gpioPin);

        // return new new pin instance
        return gpioPin;
    }

    @Override
    public GpioPin provisionPin(Pin pin, String name, PinMode mode)
    {
        return provisionPin(defaultProvider, pin, name, mode);
    }
    

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinDigitalInput)provisionPin(provider, pin, name, PinMode.DIGITAL_INPUT);
    }
    
    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name)
    {
        return provisionDigitalInputPin(defaultProvider, pin, name);
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
    public GpioPinDigitalOutput provisionDigitalOuputPin(GpioProvider provider, Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinDigitalOutput)provisionPin(provider, pin, name, PinMode.DIGITAL_OUTPUT);
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOuputPin(Pin pin, String name)
    {
        return provisionDigitalOuputPin(defaultProvider, pin, name);
    }
    
    @Override
    public GpioPinDigitalOutput provisionDigitalOuputPin(GpioProvider provider, Pin pin, String name, PinState defaultState)
    {
        // create new GPIO pin instance
        GpioPinDigitalOutput gpioPin = provisionDigitalOuputPin(provider, pin, name);

        // apply default state
        if (defaultState != null)
            gpioPin.setState(defaultState);

        // return new new pin instance
        return gpioPin;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOuputPin(Pin pin, String name, PinState defaultState)
    {
        return provisionDigitalOuputPin(defaultProvider, pin, name, defaultState);
    }
    
    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinAnalogInput)provisionPin(provider, pin, name, PinMode.ANALOG_INPUT);
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin, String name)
    {
        return provisionAnalogInputPin(defaultProvider, pin, name);
    }
    
    @Override
    public GpioPinAnalogOutput provisionAnalogOuputPin(GpioProvider provider, Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinAnalogOutput)provisionPin(provider, pin, name, PinMode.ANALOG_OUTPUT);
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOuputPin(Pin pin, String name)
    {
        return provisionAnalogOuputPin(defaultProvider, pin, name);
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOuputPin(GpioProvider provider, Pin pin, String name, int defaultValue)
    {
        // create new GPIO pin instance
        GpioPinAnalogOutput gpioPin = provisionAnalogOuputPin(provider, pin, name);

        // apply default value
        gpioPin.setValue(defaultValue);

        // return new new pin instance
        return gpioPin;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOuputPin(Pin pin, String name, int defaultValue)
    {
        return provisionAnalogOuputPin(defaultProvider, pin, name, defaultValue);
    }
    
    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinPwmOutput)provisionPin(provider, pin, name, PinMode.PWM_OUTPUT);
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name)
    {
        return provisionPwmOutputPin(defaultProvider, pin, name);
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
    public void unprovisionPin(GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
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
            for (GpioPin pin : pins.values())
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
