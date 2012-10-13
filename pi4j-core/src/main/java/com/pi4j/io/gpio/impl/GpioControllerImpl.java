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
import com.pi4j.io.gpio.exception.PinProviderException;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public class GpioControllerImpl implements GpioController
{
    private final Map<Pin, GpioPin> pins = new ConcurrentHashMap<Pin, GpioPin>();
    private Map<String, GpioProvider> providers = new ConcurrentHashMap<String, GpioProvider>();

    /**
     * Default Constructor
     */
    public GpioControllerImpl()
    {
        // set the local providers reference
        this.providers = GpioFactory.getProviders();
        
        // initialize providers
        for(GpioProvider provider : providers.values())
            provider.initialize();
        
        // register shutdown callback hook class
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());        
    }

    public GpioProvider getPinProvider(Pin pin)
    {
        if(!providers.containsKey(pin.getProvider()))
            throw new PinProviderException(pin);
        return providers.get(pin.getProvider());
    }
    
    public boolean hasPin(Pin... pin)
    {
        for(Pin p : pin)
        {            
            if(getPinProvider(p).hasPin(p))
                return false;
        }
        return true; 
    }

    public boolean isProvisioned(Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for(Pin p : pin)
        {
            if(!pins.containsKey(p))
                return false;
        }
        
        return true;
    }
    
    public GpioPin getProvisionedPin(Pin pin)
    {
        if (pins.containsKey(pin))
            return pins.get(pin);
        return null;
    }

    public Collection<GpioPin> getProvisionedPins(Pin... pin)
    {
        // if no argument is provided, return all pins
        if(pin == null || pin.length == 0)
            return pins.values();
        
        ArrayList<GpioPin> result = new ArrayList<GpioPin>();
        for(Pin p : pin)
        {
           if(pins.containsKey(p))
            result.add(pins.get(p)); 
        }

        return result;
    }
    
    /**
     * 
     */
    public void unexportAll()
    {
        // un-export all GPIO pins that are currently exported
        for (GpioPin pin : pins.values())
        {            
            if (pin.isExported())
                pin.unexport();
        }
    }

    /**
     * 
     * @param pin
     * @param direction
     */
    public void export(PinMode mode, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // export the pin
        for (Pin p : pin)
            getPinProvider(p).export(p, mode);
    }

    /**
     * 
     * @param pin
     * @param direction
     */
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
    public boolean isExported(Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // return the pin exported state
        for(Pin p : pin)
        {            
            if(!getPinProvider(p).isExported(p))
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

    /**
     * 
     * @param pin
     */
    public void unexport(Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // unexport the pins
        for (Pin p : pin)
            getPinProvider(p).unexport(p);
    }


    /**
     * 
     * @param pin
     */
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

    /**
     * 
     * @param pin
     * @param mode
     */
    public void setMode(PinMode mode, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin mode
        for (Pin p : pin)
            getPinProvider(p).setMode(p, mode);
    }

    @Override
    public PinMode getMode(Pin pin)
    {
        return getPinProvider(pin).getMode(pin);
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
    public boolean isMode(PinMode mode, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
        {
            if(getMode(p) != mode)
                return false;
        }
        return true;  
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
    
    /**
     * 
     * @param pin
     * @param mode
     */
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

    /**
     * 
     * @param pin
     * @param resistance
     */
    public void setPullResistance(PinPullResistance resistance, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin pull resistance
        for (Pin p : pin)
            getPinProvider(p).setPullResistance(p, resistance);
    }

    /**
     * 
     * @param pin
     * @param resistance
     */
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
    public PinPullResistance getPullResistance(Pin pin)
    {
        // TODO Auto-generated method stub
        return null;
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
    public boolean isPullResistance(PinPullResistance resistance, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
        {
            if(getPullResistance(p) != resistance)
                return false;
        }
        return true; 
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
    

    public void high(Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin state high
        for (Pin p : pin)
            setState(PinState.HIGH, p);
    }


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

    public void low(Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin state low
        for (Pin p : pin)
            setState(PinState.LOW, p);
    }


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
        
    public boolean isHigh(Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
        {
            if(getState(p).isLow())
                return false;
        }
        return true;        
    }
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

    public boolean isLow(Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (Pin p : pin)
        {
            if(getState(p).isHigh())
                return false;
        }
        return true;                
    }
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

    /**
     * 
     * @param pin
     * @param state
     */
    public void toggle(Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // toggle pin state
        for (Pin p : pin)
        {
            if (getState(p) == PinState.HIGH)
                setState(PinState.LOW, p);
            else
                setState(PinState.HIGH, p);
        }
    }

    /**
     * 
     * @param pin
     * @param state
     */
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


    public void pulse(long milliseconds, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // toggle pin state
        for (Pin p : pin)
            GpioPulseImpl.execute(this, p, milliseconds);
    }

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


    /**
     * 
     * @param pin
     * @param state
     */
    public void setState(PinState state, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin state
        for (Pin p : pin)
            getPinProvider(p).setState(p, state);
    }

    /**
     * 
     * @param pin
     * @param state
     */
    public void setState(boolean state, Pin... pin)
    {
        setState((state) ? PinState.HIGH : PinState.LOW, pin);
    }
    
    /**
     * 
     * @param pin
     * @param state
     */
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

    /**
     * 
     * @param pin
     * @param state
     */
    public void setState(boolean state, GpioPinDigitalOutput... pin)
    {
        setState((state) ? PinState.HIGH : PinState.LOW, pin);
    }

    /**
     * 
     * @param pin
     * @return
     */
    public PinState getState(Pin pin)
    {
        // return pin state
        return getPinProvider(pin).getState(pin);
    }
    
    
    public boolean isState(PinState state, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
        {
            if(getState(p) != state)
                return false;
        }
        return true;                
    }

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
    

    /**
     * 
     * @param pin
     * @return
     */
    public PinState getState(GpioPinDigital pin)
    {
        // ensure the requested pin has been provisioned
        if (!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // return pin state
        return pin.getState();
    }

    /**
     * 
     * @param pin
     * @param value
     */
    public void setValue(int value, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin PWM value
        for (Pin p : pin)
            getPinProvider(p).setValue(p, value);
    }


    /**
     * 
     * @param pin
     * @param value
     */
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
    public int getValue(Pin pin)
    {
        return getPinProvider(pin).getValue(pin);
    }

    @Override
    public int getValue(GpioPinAnalog pin)
    {
        return pin.getValue();
    }    

    /**
     * 
     * @param listener
     */
    public synchronized void addListener(GpioPinListener listener, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (Pin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p);
    
            GpioPin gpioPin = getProvisionedPin(p);
            if(gpioPin instanceof GpioPinInput)
                ((GpioPinInput)gpioPin).addListener(listener);
        }
    }


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

    public synchronized void addListener(GpioPinListener[] listeners, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinListener listener: listeners)
            addListener(listener, pin);
    }

    public synchronized void addListener(GpioPinListener[] listeners, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinListener listener: listeners)
            addListener(listener, pin);
    }


    /**
     * 
     * @param listener
     */
    public synchronized void removeListener(GpioPinListener listener, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (Pin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p);
    
            GpioPin gpioPin = getProvisionedPin(p);
            if(gpioPin instanceof GpioPinInput)
                ((GpioPinInput)gpioPin).removeListener(listener);
        }
    }

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

    public synchronized void removeListener(GpioPinListener[] listeners, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinListener listener : listeners)
        {
            removeListener(listener, pin);
        }
    }

    public synchronized void removeListener(GpioPinListener[] listeners, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPinListener listener : listeners)
        {
            removeListener(listener, pin);
        }
    }

    public synchronized void removeAllListeners()
    {
        for (GpioPin pin : this.pins.values())
        {
            if(pin instanceof GpioPinInput)
                ((GpioPinInput)pin).removeAllListeners();
        }
    }

    /**
     * 
     * @param trigger
     */
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

    public synchronized void addTrigger(GpioTrigger[] triggers, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioTrigger trigger : triggers)
            addTrigger(trigger, pin);
    }
    
    /**
     * 
     * @param trigger
     */
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

    public synchronized void removeTrigger(GpioTrigger[] triggers, GpioPinInput... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioTrigger trigger : triggers)
            removeTrigger(trigger, pin);
    }

    public synchronized void removeAllTriggers()
    {
        for (GpioPin pin : this.pins.values())
        {
            if(pin instanceof GpioPinInput)
                ((GpioPinInput)pin).removeAllTriggers();            
        }
    }

    public GpioPin provisionPin(Pin pin, String name, PinMode mode)
    {
        // if an existing pin has been previously created, then throw an error
        if (pins.containsKey(pin))
            throw new GpioPinExistsException(pin);

        // create new GPIO pin instance
        GpioPin gpioPin = new GpioPinImpl(this, getPinProvider(pin), pin);

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

    
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinDigitalInput)provisionPin(pin, name, PinMode.DIGITAL_INPUT);
    }
    
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name, PinPullResistance resistance)
    {
        // create new GPIO pin instance
        GpioPinDigitalInput gpioPin = provisionDigitalInputPin(pin, name);

        // set the gpio pull resistor
        if (resistance != null)
            gpioPin.setPullResistance(resistance);

        // return new new pin instance
        return gpioPin;
    }
    
    public GpioPinDigitalOutput provisionDigitalOuputPin(Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinDigitalOutput)provisionPin(pin, name, PinMode.DIGITAL_OUTPUT);
    }
    
    public GpioPinDigitalOutput provisionDigitalOuputPin(Pin pin, String name, PinState defaultState)
    {
        // create new GPIO pin instance
        GpioPinDigitalOutput gpioPin = provisionDigitalOuputPin(pin, name);

        // apply default state
        if (defaultState != null)
            gpioPin.setState(defaultState);

        // return new new pin instance
        return gpioPin;
    }
    
    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinAnalogInput)provisionPin(pin, name, PinMode.ANALOG_INPUT);
    }

    public GpioPinAnalogOutput provisionAnalogOuputPin(Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinAnalogOutput)provisionPin(pin, name, PinMode.ANALOG_OUTPUT);
    }
    
    public GpioPinAnalogOutput provisionAnalogOuputPin(Pin pin, String name, int defaultValue)
    {
        // create new GPIO pin instance
        GpioPinAnalogOutput gpioPin = provisionAnalogOuputPin(pin, name);

        // apply default value
        gpioPin.setValue(defaultValue);

        // return new new pin instance
        return gpioPin;
    }

    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name)
    {
        // return new new pin instance
        return (GpioPinPwmOutput)provisionPin(pin, name, PinMode.PWM_OUTPUT);
    }
    
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name, int defaultValue)
    {
        // create new GPIO pin instance
        GpioPinPwmOutput gpioPin = provisionPwmOutputPin(pin, name);

        // apply default value
        gpioPin.setPwm(defaultValue);

        // return new new pin instance
        return gpioPin;
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
