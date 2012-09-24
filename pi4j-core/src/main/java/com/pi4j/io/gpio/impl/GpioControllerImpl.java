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
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinResistor;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.exception.GpioPinExistsException;
import com.pi4j.io.gpio.exception.GpioPinNotProvisionedException;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public class GpioControllerImpl implements GpioController
{
    private final Map<Pin, GpioPin> pins = new ConcurrentHashMap<Pin, GpioPin>();

    /**
     * Default Constructor
     */
    public GpioControllerImpl()
    {
        // set wiringPi interface for internal use
        // we will use the WiringPi pin number scheme with the wiringPi library
        com.pi4j.wiringpi.Gpio.wiringPiSetup();
        
        // register shutdown callback hook class
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());        
    }

    public boolean hasPin(Pin... pin)
    {
        for(Pin p : pin)
        {
            if(com.pi4j.wiringpi.Gpio.wpiPinToGpio(p.getValue()) < 0)
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
        for (Pin pin : Pin.allPins())
        {
            if(hasPin(pin))
            {
                if (isExported(pin))
                    unexport(pin);
            }
        }
    }

    /**
     * 
     * @param pin
     * @param direction
     */
    public void export(PinDirection direction, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // export the pin
        for (Pin p : pin)
            com.pi4j.wiringpi.GpioUtil.export(p.getValue(), direction.getValue());
    }

    /**
     * 
     * @param pin
     * @param direction
     */
    public void export(PinDirection direction, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // export the pin
            p.export(direction);
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
            if(!com.pi4j.wiringpi.GpioUtil.isExported(p.getValue()))
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
            com.pi4j.wiringpi.GpioUtil.unexport(p.getValue());
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
            pins.get(p).unexport();            
        }
    }

    /**
     * 
     * @param pin
     * @param direction
     */
    public void setDirection(PinDirection direction, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set the pin direction
        for (Pin p : pin)
            com.pi4j.wiringpi.GpioUtil.setDirection(p.getValue(), direction.getValue());
    }


    /**
     * 
     * @param pin
     * @param direction
     */
    public void setDirection(PinDirection direction, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
        {        
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set the pin direction
            pins.get(p).setDirection(direction);
        }
    }

    /**
     * 
     * @param pin
     * @return
     */
    public PinDirection getDirection(Pin pin)
    {
        PinDirection direction = null;
        int ret = com.pi4j.wiringpi.GpioUtil.getDirection(pin.getValue());
        if (ret >= 0)
            direction = PinDirection.getDirection(ret);
        return direction;
    }

    /**
     * 
     * @param pin
     * @return
     */
    public PinDirection getDirection(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if (!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        return pin.getDirection();
    }
    
    public boolean isDirection(PinDirection direction, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
        {
            if(getDirection(p) != direction)
                return false;
        }
        return true;        
    }

    public boolean isDirection(PinDirection direction, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
        {
            if(!p.isDirection(direction))
                return false;
        }
        return true;        
    }    
    
    /**
     * 
     * @param pin
     * @param edge
     */
    public void setEdge(PinEdge edge, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
            com.pi4j.wiringpi.GpioUtil.setEdgeDetection(p.getValue(), edge.getValue());
    }

    /**
     * 
     * @param pin
     * @param edge
     */
    public void setEdge(PinEdge edge, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
        {        
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.setEdge(edge);
        }
    }
    
    /**
     * 
     * @param pin
     * @return
     */
    public PinEdge getEdge(Pin pin)
    {
     // return pin edge setting
        PinEdge edge = null;
        int ret = com.pi4j.wiringpi.GpioUtil.getEdgeDetection(pin.getValue());
        if (ret >= 0)
            edge = PinEdge.getEdge(ret);
        return edge;
    }

    /**
     * 
     * @param pin
     * @return
     */
    public PinEdge getEdge(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if (!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // return pin edge setting
        return pin.getEdge();
    }

    public boolean isEdge(PinEdge edge, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (Pin p : pin)
        {
            if(getEdge(p) != edge)
                return false;
        }
        return true;                
    }

    public boolean isEdge(PinEdge edge, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
        {
            if(!p.isEdge(edge))
                return false;
        }
        return true;     
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
            com.pi4j.wiringpi.Gpio.pinMode(p.getValue(), mode.getValue());
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
            pins.get(p).setMode(mode);        
        }
    }

    /**
     * 
     * @param pin
     * @param resistance
     */
    public void setPullResistor(PinResistor resistance, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin pull resistance
        for (Pin p : pin)
            com.pi4j.wiringpi.Gpio.pullUpDnControl(p.getValue(), resistance.getValue());
    }

    /**
     * 
     * @param pin
     * @param resistance
     */
    public void setPullResistor(PinResistor resistance, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin pull resistance
            pins.get(p).setPullResistor(resistance);
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
            com.pi4j.wiringpi.Gpio.digitalWrite(p.getValue(), state.getValue());
    }

    /**
     * 
     * @param pin
     * @param state
     */
    public void setState(boolean state, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin state
        for (Pin p : pin)
            com.pi4j.wiringpi.Gpio.digitalWrite(p.getValue(), state);
    }
    
    /**
     * 
     * @param pin
     * @param state
     */
    public void setState(PinState state, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
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
    public void setState(boolean state, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin state
            p.setState(state);
        }
    }
    
    public void high(Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin state high
        for (Pin p : pin)
            setState(PinState.HIGH, p);
    }


    public void high(GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // ensure the requested pin has been provisioned
        for (GpioPin p : pin)
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


    public void low(GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // ensure the requested pin has been provisioned
        for (GpioPin p : pin)
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
    public boolean isHigh(GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
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
    public boolean isLow(GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
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
    public void toggle(GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
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

    public void pulse(long milliseconds, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // toggle pin state
            pins.get(p).pulse(milliseconds);
        }
    }


    /**
     * 
     * @param pin
     * @return
     */
    public PinState getState(Pin pin)
    {
        // return pin state
        PinState state = null;
        int ret = com.pi4j.wiringpi.Gpio.digitalRead(pin.getValue());
        if (ret >= 0)
            state = PinState.getState(ret);
        return state;
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

    public boolean isState(PinState state, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (GpioPin p : pin)
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
    public PinState getState(GpioPin pin)
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
    public void setPwmValue(int value, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        // set pin PWM value
        for (Pin p : pin)
            com.pi4j.wiringpi.Gpio.pwmWrite(p.getValue(), value);
    }


    /**
     * 
     * @param pin
     * @param value
     */
    public void setPwmValue(int value, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            // set pin PWM value
            p.setPwmValue(value);
        }        
    }


    /**
     * 
     * @param listener
     */
    public synchronized void addListener(GpioListener listener, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (Pin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p);
    
            getProvisionedPin(p).addListener(listener);
        }
    }


    public synchronized void addListener(GpioListener listener, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
                throw new GpioPinNotProvisionedException(p.getPin());        
    
            p.addListener(listener);
        }
    }

    public synchronized void addListener(GpioListener[] listeners, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioListener listener: listeners)
            addListener(listener, pin);
    }

    public synchronized void addListener(GpioListener[] listeners, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioListener listener: listeners)
            addListener(listener, pin);
    }


    /**
     * 
     * @param listener
     */
    public synchronized void removeListener(GpioListener listener, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");

        for (Pin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p);
    
            getProvisionedPin(p).removeListener(listener);
        }
    }

    public synchronized void removeListener(GpioListener listener, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsKey(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.removeListener(listener);            
        }
    }

    public synchronized void removeListener(GpioListener[] listeners, Pin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioListener listener : listeners)
        {
            removeListener(listener, pin);
        }
    }

    public synchronized void removeListener(GpioListener[] listeners, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioListener listener : listeners)
        {
            removeListener(listener, pin);
        }
    }

    public synchronized void removeAllListeners()
    {
        for (GpioPin pin : this.pins.values())
            pin.removeAllListeners();
    }

    /**
     * 
     * @param trigger
     */
    public synchronized void addTrigger(GpioTrigger trigger, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.addTrigger(trigger);
        }        
    }

    public synchronized void addTrigger(GpioTrigger[] triggers, GpioPin... pin)
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
    public synchronized void removeTrigger(GpioTrigger trigger, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioPin p : pin)
        {
            // ensure the requested pin has been provisioned
            if (!pins.containsValue(p))
                throw new GpioPinNotProvisionedException(p.getPin());
    
            p.removeTrigger(trigger);
        }
    }

    public synchronized void removeTrigger(GpioTrigger[] triggers, GpioPin... pin)
    {
        if(pin == null || pin.length == 0)
            throw new IllegalArgumentException("Missing pin argument.");
        
        for (GpioTrigger trigger : triggers)
            removeTrigger(trigger, pin);
    }

    public synchronized void removeAllTriggers()
    {
        for (GpioPin pin : this.pins.values())
            pin.removeAllTriggers();
    }

    public GpioPin provisionInputPin(Pin pin, String name, PinEdge edge, PinResistor resistance)
    {
        // if an existing pin has been previously created, then throw an error
        if (pins.containsKey(pin))
            throw new GpioPinExistsException(pin);

        // create new GPIO pin instance
        GpioPin gpioPin = new GpioPinImpl(this, pin);

        // set the gpio pin name
        if (name != null)
            gpioPin.setName(name);

        // export this pin as IN
        gpioPin.export(PinDirection.IN);

        // set the gpio mode
        gpioPin.setMode(PinMode.INPUT);

        // set the gpio edge detection
        if (edge != null)
            gpioPin.setEdge(edge);

        // set the gpio pull resistor
        if (resistance != null)
            gpioPin.setPullResistor(resistance);

        // add this new pin instance to the managed collection
        pins.put(pin, gpioPin);

        // return new new pin instance
        return gpioPin;
    }

    public GpioPin provisionInputPin(Pin pin, String name, PinEdge edge)
    {
        return provisionInputPin(pin, name, edge, null);
    }

    public GpioPin provisionInputPin(Pin pin, String name)
    {
        return provisionInputPin(pin, name, null);
    }

    public GpioPin provisionOuputPin(Pin pin, String name, PinState defaultState)
    {
        // if an existing pin has been previously created, then throw an error
        if (pins.containsKey(pin))
            throw new GpioPinExistsException(pin);

        // create new GPIO pin instance
        GpioPin gpioPin = new GpioPinImpl(this, pin);

        // set the gpio pin name
        if (name != null)
            gpioPin.setName(name);

        // export this pin as IN
        gpioPin.export(PinDirection.OUT);

        // set the gpio mode
        gpioPin.setMode(PinMode.OUTPUT);

        // add this new pin instance to the managed collection
        pins.put(pin, gpioPin);

        if (defaultState != null)
            gpioPin.setState(defaultState);

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
                    PinDirection direction = shutdownOptions.getDirection();
                    PinEdge edge = shutdownOptions.getEdge();
                    PinResistor resistance = shutdownOptions.getPullResistor();
                    Boolean unexport = shutdownOptions.getUnexport();
                    
                    // perform shutdown actions
                    if(state != null)
                        pin.setState(state);
                    if(resistance != null)
                        pin.setPullResistor(resistance);
                    if(edge != null)
                        pin.setEdge(edge);
                    if(direction != null)
                        pin.setDirection(direction);
                    if(unexport != null && unexport == Boolean.TRUE)
                        pin.unexport();
                }
            }
        }
    }
}
