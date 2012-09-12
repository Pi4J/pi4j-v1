package com.pi4j.io.gpio.impl;

import java.util.Map;
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
import com.pi4j.io.gpio.exception.GpioPinExistsException;
import com.pi4j.io.gpio.exception.GpioPinNotProvisionedException;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public class GpioController implements Gpio
{
    private Map<Pin,GpioPin> pins = new ConcurrentHashMap<Pin,GpioPin>();

    /**
     * Default Constructor
     */
    public GpioController()
    {
        // set wiringPi interface for internal use
        // we will use the GPIO pin number scheme with wiringPi
        com.pi4j.wiringpi.Gpio.wiringPiSetupGpio();
    }
    
    public Map<Pin,GpioPin> getPins()
    {
        return pins;
    }

    public boolean hasPin(Pin pin)
    {
        return pins.containsKey(pin);
    }

    public GpioPin getPin(Pin pin)
    {
        if(pins.containsKey(pin))
            return pins.get(pin);
        return null;
    }
    
    /**
     * 
     */
    public void unexportAll()
    {
        // un-export all GPIO pins that are currently exported
        for (Pin pin : Pin.allPins())
        {
            if (isExported(pin))
                unexport(pin);
        }
    }

    /**
     * 
     * @param pin
     * @param direction
     */
    public void export(Pin pin, PinDirection direction)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);
        
        // export the pin
        pins.get(pin).export(direction);
    }

    /**
     * 
     * @param pins
     * @param direction
     */
    public void export(Pin pins[], PinDirection direction)
    {
        for (Pin pin : pins)
            export(pin, direction);
    }


    /**
     * 
     * @param pin
     * @param direction
     */
    public void export(GpioPin pin, PinDirection direction)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
        
        // export the pin
        pin.export(direction);
    }

    /**
     * 
     * @param pins
     * @param direction
     */
    public void export(GpioPin pins[], PinDirection direction)
    {
        for (GpioPin pin : pins)
            export(pin, direction);
    }    
    
    
    /**
     * 
     * @param pin
     * @return <p>A value of 'true' is returned if the requested pin is exported.</p> 
     */
    public boolean isExported(Pin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);
        
        // return the pin exported state
        return pins.get(pin).isExported();
    }

    /**
     * 
     * @param pin
     * @return <p>A value of 'true' is returned if the requested pin is exported.</p> 
     */
    public boolean isExported(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
        
        // return the pin exported state
        return pin.isExported();
    }
    
    /**
     * 
     * @param pin
     */
    public void unexport(Pin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);
        
        // unexport the pin
        pins.get(pin).unexport();
    }

    /**
     * 
     * @param pins
     */
    public void unexport(Pin pins[])
    {
        for (Pin pin : pins)
            unexport(pin);
    }

    /**
     * 
     * @param pin
     */
    public void unexport(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
        
        // unexport the pin
        pins.get(pin).unexport();
    }

    /**
     * 
     * @param pins
     */
    public void unexport(GpioPin pins[])
    {
        for (GpioPin pin : pins)
            unexport(pin);
    }

    /**
     * 
     * @param pin
     * @param direction
     */
    public void setDirection(Pin pin, PinDirection direction)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);
        
        // set the pin direction
        pins.get(pin).setDirection(direction);
    }

    /**
     * 
     * @param pins
     * @param direction
     */
    public void setDirection(Pin pins[], PinDirection direction)
    {
        for (Pin pin : pins)
            setDirection(pin, direction);
    }

    /**
     * 
     * @param pin
     * @param direction
     */
    public void setDirection(GpioPin pin, PinDirection direction)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
        
        // set the pin direction
        pins.get(pin).setDirection(direction);
    }

    /**
     * 
     * @param pins
     * @param direction
     */
    public void setDirection(GpioPin pins[], PinDirection direction)
    {
        for (GpioPin pin : pins)
            setDirection(pin, direction);
    }

    /**
     * 
     * @param pin
     * @return
     */
    public PinDirection getDirection(Pin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);
        
        return pins.get(pin).getDirection();
    }

    
    /**
     * 
     * @param pin
     * @return
     */
    public PinDirection getDirection(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
        
        return pin.getDirection();
    }

    
    /**
     * 
     * @param pin
     * @param edge
     */
    public void setEdge(Pin pin, PinEdge edge)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        pins.get(pin).setEdge(edge);
    }

    /**
     * 
     * @param pins
     * @param edge
     */
    public void setEdge(Pin pins[], PinEdge edge)
    {
        for (Pin pin : pins)
            setEdge(pin, edge);
    }

    /**
     * 
     * @param pin
     * @param edge
     */
    public void setEdge(GpioPin pin, PinEdge edge)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        pin.setEdge(edge);
    }

    /**
     * 
     * @param pins
     * @param edge
     */
    public void setEdge(GpioPin pins[], PinEdge edge)
    {
        for (GpioPin pin : pins)
            setEdge(pin, edge);
    }

    /**
     * 
     * @param pin
     * @return
     */
    public PinEdge getEdge(Pin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        // return pin edge setting
        return pins.get(pin).getEdge();
    }

    /**
     * 
     * @param pin
     * @return
     */
    public PinEdge getEdge(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // return pin edge setting
        return pin.getEdge();
    }
    
    /**
     * 
     * @param pin
     * @param mode
     */
    public void setMode(Pin pin, PinMode mode)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        // set pin mode
        pins.get(pin).setMode(mode);
    }

    /**
     * 
     * @param pins
     * @param mode
     */
    public void setMode(Pin pins[], PinMode mode)
    {
        for (Pin pin : pins)
            setMode(pin, mode);
    }

    /**
     * 
     * @param pin
     * @param mode
     */
    public void setMode(GpioPin pin, PinMode mode)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // set pin mode
        pins.get(pin).setMode(mode);
    }

    /**
     * 
     * @param pins
     * @param mode
     */
    public void setMode(GpioPin pins[], PinMode mode)
    {
        for (GpioPin pin : pins)
            setMode(pin, mode);
    }

    /**
     * 
     * @param pin
     * @param resistance
     */
    public void setPullResistor(Pin pin, PinResistor resistance)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        // set pin pull resistance
        pins.get(pin).setPullResistor(resistance);
    }

    /**
     * 
     * @param pins
     * @param resistance
     */
    public void setPullResistor(Pin pins[], PinResistor resistance)
    {
        for (Pin pin : pins)
            setPullResistor(pin, resistance);
    }

    /**
     * 
     * @param pin
     * @param resistance
     */
    public void setPullResistor(GpioPin pin, PinResistor resistance)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // set pin pull resistance
        pins.get(pin).setPullResistor(resistance);
    }

    /**
     * 
     * @param pins
     * @param resistance
     */
    public void setPullResistor(GpioPin pins[], PinResistor resistance)
    {
        for (GpioPin pin : pins)
            setPullResistor(pin, resistance);
    }
    
    
    /**
     * 
     * @param pin
     * @param state
     */
    public void setState(Pin pin, PinState state)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        // set pin state
        pins.get(pin).setState(state);
    }

    /**
     * 
     * @param pins
     * @param state
     */
    public void setState(Pin pins[], PinState state)
    {
        for (Pin pin : pins)
            setState(pin, state);
    }
    
    /**
     * 
     * @param pin
     * @param state
     */
    public void setState(GpioPin pin, PinState state)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // set pin state
        pin.setState(state);
    }

    /**
     * 
     * @param pins
     * @param state
     */
    public void setState(GpioPin pins[], PinState state)
    {
        for (GpioPin pin : pins)
            setState(pin, state);
    }
    
    public void high(Pin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        // set pin state high
        pins.get(pin).high();
    }
    
    public void high(Pin pins[])
    {
        for (Pin pin : pins)
            high(pin);
    }

    public void high(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // set pin state high
        pin.high();
    }
    
    public void high(GpioPin pins[])
    {
        for (GpioPin pin : pins)
            high(pin);
    }
    
    public void low(Pin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        // set pin state low
        pins.get(pin).low();
    }
    public void low(Pin pins[])
    {
        for (Pin pin : pins)
            low(pin);
    }

    public void low(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // set pin state low
        pin.low();
    }

    public void low(GpioPin pins[])
    {
        for (GpioPin pin : pins)
            low(pin);
    }


    /**
     * 
     * @param pin
     * @param state
     */
    public void toggle(Pin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        // toggle pin state
        pins.get(pin).toggle();
    }

    /**
     * 
     * @param pins
     * @param state
     */
    public void toggle(Pin pins[])
    {
        for (Pin pin : pins)
            toggle(pin);
    }

    /**
     * 
     * @param pin
     * @param state
     */
    public void toggle(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // toggle pin state
        pin.toggle();
    }

    /**
     * 
     * @param pins
     * @param state
     */
    public void toggle(GpioPin pins[])
    {
        for (GpioPin pin : pins)
            toggle(pin);
    }

    public void pulse(Pin pin, long milliseconds)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        // toggle pin state
        pins.get(pin).pulse(milliseconds);
    }

    public void pulse(Pin pins[], long milliseconds)
    {
        for (Pin pin : pins)
            pulse(pin, milliseconds);
    }

    public void pulse(GpioPin pin, long milliseconds)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // toggle pin state
        pins.get(pin).pulse(milliseconds);
    }

    public void pulse(GpioPin pins[], long milliseconds)
    {
        for (GpioPin pin : pins)
            pulse(pin, milliseconds);
    }
    
    /**
     * 
     * @param pin
     * @return
     */
    public PinState getState(Pin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        // return pin state
        return pins.get(pin).getState();
    }

    /**
     * 
     * @param pin
     * @return
     */
    public PinState getState(GpioPin pin)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // return pin state
        return pin.getState();
    }
    
    /**
     * 
     * @param pin
     * @param value
     */
    public void setPwmValue(Pin pin, int value)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);

        // set pin PWM value
        pins.get(pin).setPwmValue(value);
    }

    /**
     * 
     * @param pins
     * @param value
     */
    public void setPwmValue(Pin pins[], int value)
    {
        for (Pin pin : pins)
            setPwmValue(pin, value);
    }

    /**
     * 
     * @param pin
     * @param value
     */
    public void setPwmValue(GpioPin pin, int value)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());

        // set pin PWM value
        pin.setPwmValue(value);
    }

    /**
     * 
     * @param pins
     * @param value
     */
    public void setPwmValue(GpioPin pins[], int value)
    {
        for (GpioPin pin : pins)
            setPwmValue(pin, value);
    }


    /**
     * 
     * @param listener
     */
    public synchronized void addListener(Pin pin, GpioListener listener)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);
        
        getPin(pin).addListener(listener);
    }

    public synchronized void addListener(Pin pin, GpioListener[] listeners)
    {
        for(GpioListener listener : listeners)
            addListener(pin, listener);
    }

    public synchronized void addListener(GpioPin pin, GpioListener listener)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
        
        pin.addListener(listener);
    }

    public synchronized void addListener(GpioPin pin, GpioListener[] listeners)
    {
        for(GpioListener listener : listeners)
            addListener(pin, listener);
    }
    

    public synchronized void addListener(Pin pins[], GpioListener listener)
    {
        for(Pin pin : pins)
            addListener(pin, listener);
    }

    public synchronized void addListener(Pin pins[], GpioListener[] listeners)
    {
        for(Pin pin : pins)
            addListener(pin, listeners);
    }

    public synchronized void addListener(GpioPin pins[], GpioListener listener)
    {
        for(GpioPin pin : pins)
            addListener(pin, listener);
    }

    public synchronized void addListener(GpioPin pins[], GpioListener[] listeners)
    {
        for(GpioPin pin : pins)
            addListener(pin, listeners);
    }    
    
    /**
     * 
     * @param listener
     */
    public synchronized void removeListener(Pin pin, GpioListener listener)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);
        
        getPin(pin).removeListener(listener);        
    }
    
    public synchronized void removeListener(Pin pin, GpioListener[] listeners)
    {
        for(GpioListener listener : listeners)
            removeListener(pin ,listener);
    }

    public synchronized void removeListener(GpioPin pin, GpioListener listener)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
        
        pin.removeListener(listener);        
    }
    
    public synchronized void removeListener(GpioPin pin, GpioListener[] listeners)
    {
        for(GpioListener listener : listeners)
            removeListener(pin ,listener);
    }
    
    public synchronized void removeListener(Pin pins[], GpioListener listener)
    {
        for(Pin pin : pins)
            removeListener(pin, listener);
    }
    
    public synchronized void removeListener(Pin pins[], GpioListener[] listeners)
    {
        for(Pin pin : pins)
            removeListener(pin, listeners);
    }

    public synchronized void removeListener(GpioPin pins[], GpioListener listener)
    {
        for(GpioPin pin : pins)
            removeListener(pin, listener);
    }
    
    public synchronized void removeListener(GpioPin pins[], GpioListener[] listeners)
    {
        for(GpioPin pin : pins)
            removeListener(pin, listeners);
    }
    
   
    public synchronized void removeAllListeners()
    {
        for(GpioPin pin : this.pins.values())
            pin.removeAllListeners();
    }

    
    /**
     * 
     * @param trigger
     */
    public synchronized void addTrigger(Pin pin, GpioTrigger trigger)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);
        
        getPin(pin).addTrigger(trigger);        
    }

    public synchronized void addTrigger(Pin pin, GpioTrigger[] triggers)
    {
        for(GpioTrigger trigger : triggers)
            addTrigger(pin, trigger);
    }

    public synchronized void addTrigger(GpioPin pin, GpioTrigger trigger)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
        
        pin.addTrigger(trigger);        
    }

    public synchronized void addTrigger(GpioPin pin, GpioTrigger[] triggers)
    {
        for(GpioTrigger trigger : triggers)
            addTrigger(pin, trigger);
    }


    public synchronized void addTrigger(Pin pins[], GpioTrigger trigger)
    {
        for(Pin pin : pins)
            addTrigger(pin, trigger);
    }

    public synchronized void addTrigger(Pin pins[], GpioTrigger[] triggers)
    {
        for(Pin pin : pins)
            addTrigger(pin, triggers);
    }

    public synchronized void addTrigger(GpioPin pins[], GpioTrigger trigger)
    {
        for(GpioPin pin : pins)
            addTrigger(pin, trigger);
    }

    public synchronized void addTrigger(GpioPin pins[], GpioTrigger[] triggers)
    {
        for(GpioPin pin : pins)
            addTrigger(pin, triggers);
    }
    

    /**
     * 
     * @param trigger
     */
    public synchronized void removeTrigger(Pin pin, GpioTrigger trigger)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsKey(pin))
            throw new GpioPinNotProvisionedException(pin);
        
        getPin(pin).removeTrigger(trigger);        
    }    

    public synchronized void removeTrigger(Pin pin, GpioTrigger[] triggers)
    {
        for(GpioTrigger trigger : triggers)
            removeTrigger(pin, trigger);
    }

    public synchronized void removeTrigger(GpioPin pin, GpioTrigger trigger)
    {
        // ensure the requested pin has been provisioned
        if(!pins.containsValue(pin))
            throw new GpioPinNotProvisionedException(pin.getPin());
        
        pin.removeTrigger(trigger);        
    }    

    public synchronized void removeTrigger(GpioPin pin, GpioTrigger[] triggers)
    {
        for(GpioTrigger trigger : triggers)
            removeTrigger(pin, trigger);
    }
    
    public synchronized void removeTrigger(Pin pins[], GpioTrigger trigger)
    {
        for(Pin pin : pins)
            removeTrigger(pin, trigger);
    }    

    public synchronized void removeTrigger(Pin pins[], GpioTrigger[] triggers)
    {
        for(Pin pin : pins)
            removeTrigger(pin, triggers);
    }

    public synchronized void removeTrigger(GpioPin pins[], GpioTrigger trigger)
    {
        for(GpioPin pin : pins)
            removeTrigger(pin, trigger);
    }    

    public synchronized void removeTrigger(GpioPin pins[], GpioTrigger[] triggers)
    {
        for(GpioPin pin : pins)
            removeTrigger(pin, triggers);
    }
    
    
    public synchronized void removeAllTriggers()
    {
        for(GpioPin pin : this.pins.values())
            pin.removeAllTriggers();
    }


    public GpioPin provisionInputPin(Pin pin, String name, PinEdge edge, PinResistor resistance)
    {
        // if an existing pin has been previously created, then throw an error
        if(pins.containsKey(pin))
            throw new GpioPinExistsException(pin);
        
        // create new GPIO pin instance
        GpioPin gpioPin = new GpioPinImpl(this, pin);
        
        // set the gpio pin name
        if(name != null)
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
        if(pins.containsKey(pin))
            throw new GpioPinExistsException(pin);
        
        // create new GPIO pin instance
        GpioPin gpioPin = new GpioPinImpl(this, pin); 

        // set the gpio pin name
        if(name != null)
            gpioPin.setName(name);
        
        // export this pin as IN
        gpioPin.export(PinDirection.OUT);
        
        // set the gpio mode
        gpioPin.setMode(PinMode.OUTPUT);
        
        // add this new pin instance to the managed collection
        pins.put(pin, gpioPin);
        
        if(defaultState != null)
            gpioPin.setState(defaultState);

        // return new new pin instance
        return gpioPin;        
    }
}
