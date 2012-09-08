package com.pi4j.io.gpio.impl;

import java.util.List;
import java.util.Vector;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDirection;
import com.pi4j.io.gpio.GpioPinEdge;
import com.pi4j.io.gpio.GpioPinMode;
import com.pi4j.io.gpio.GpioPinResistor;
import com.pi4j.io.gpio.GpioPinState;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.event.GpioPinStateChangeEvent;
import com.pi4j.io.gpio.trigger.GpioTrigger;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioInterruptListener;

public class GpioImpl implements Gpio, GpioInterruptListener
{
    private Vector<GpioListener> listeners = new Vector<GpioListener>();
    private Vector<GpioTrigger> triggers= new Vector<GpioTrigger>();

    /**
     * Default Constructor
     */
    public GpioImpl()
    {
        // set wiringPi interface for internal use
        // we will use the GPIO pin number scheme with wiringPi
        com.pi4j.wiringpi.Gpio.wiringPiSetupGpio();
    }

    /**
     * 
     * @param direction
     */
    public void exportAll(GpioPinDirection direction)
    {
        // export all GPIO pins
        for (GpioPin pin : GpioPin.allPins())
            export(pin, direction);
    }

    /**
     * 
     */
    public void unexportAll()
    {
        // unexport all GPIO pins that are currently exported
        for (GpioPin pin : GpioPin.allPins())
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
    public void export(GpioPin pin, GpioPinDirection direction)
    {
        com.pi4j.wiringpi.GpioUtil.export(pin.getValue(), direction.getValue());
        
        // setup listener thread
        if(direction == GpioPinDirection.IN)
        {
            com.pi4j.wiringpi.GpioInterrupt.addListener(this);
            com.pi4j.wiringpi.GpioInterrupt.enablePinStateChangeCallback(pin.getValue());
        }
    }

    /**
     * 
     * @param pins
     * @param direction
     */
    public void export(GpioPin pins[], GpioPinDirection direction)
    {
        for (GpioPin pin : pins)
            export(pin, direction);
    }

    /**
     * 
     * @param pins
     * @param direction
     */
    public void export(List<GpioPin> pins, GpioPinDirection direction)
    {
        for (GpioPin pin : pins)
            export(pin, direction);
    }

    /**
     * 
     * @param pin
     * @return
     */
    public boolean isExported(GpioPin pin)
    {
        return com.pi4j.wiringpi.GpioUtil.isExported(pin.getValue());
    }

    /**
     * 
     * @param pin
     */
    public void unexport(GpioPin pin)
    {
        com.pi4j.wiringpi.GpioUtil.unexport(pin.getValue());
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
     * @param pins
     */
    public void unexport(List<GpioPin> pins)
    {
        for (GpioPin pin : pins)
            unexport(pin);
    }

    /**
     * 
     * @param pin
     * @param direction
     */
    public void setDirection(GpioPin pin, GpioPinDirection direction)
    {
        com.pi4j.wiringpi.GpioUtil.setDirection(pin.getValue(), direction.getValue());
    }

    /**
     * 
     * @param pins
     * @param direction
     */
    public void setDirection(GpioPin pins[], GpioPinDirection direction)
    {
        for (GpioPin pin : pins)
            setDirection(pin, direction);
    }

    /**
     * 
     * @param pins
     * @param direction
     */
    public void setDirection(List<GpioPin> pins, GpioPinDirection direction)
    {
        for (GpioPin pin : pins)
            setDirection(pin, direction);
    }

    /**
     * 
     * @param pin
     * @return
     */
    public GpioPinDirection getDirection(GpioPin pin)
    {
        GpioPinDirection direction = null;
        int ret = com.pi4j.wiringpi.GpioUtil.getDirection(pin.getValue());
        if (ret >= 0)
            direction = GpioPinDirection.getDirection(ret);
        return direction;
    }

    /**
     * 
     * @param pin
     * @param edge
     */
    public void setEdge(GpioPin pin, GpioPinEdge edge)
    {
        com.pi4j.wiringpi.GpioUtil.setEdgeDetection(pin.getValue(), edge.getValue());
    }

    /**
     * 
     * @param pins
     * @param edge
     */
    public void setEdge(GpioPin pins[], GpioPinEdge edge)
    {
        for (GpioPin pin : pins)
            setEdge(pin, edge);
    }

    /**
     * 
     * @param pins
     * @param edge
     */
    public void setEdge(List<GpioPin> pins, GpioPinEdge edge)
    {
        for (GpioPin pin : pins)
            setEdge(pin, edge);
    }

    /**
     * 
     * @param pin
     * @return
     */
    public GpioPinEdge getEdge(GpioPin pin)
    {
        GpioPinEdge edge = null;
        int ret = com.pi4j.wiringpi.GpioUtil.getEdgeDetection(pin.getValue());
        if (ret >= 0)
            edge = GpioPinEdge.getEdge(ret);
        return edge;
    }

    /**
     * 
     * @param pin
     * @param mode
     */
    public void setMode(GpioPin pin, GpioPinMode mode)
    {
        com.pi4j.wiringpi.Gpio.pinMode(pin.getValue(), mode.getValue());
    }

    /**
     * 
     * @param pins
     * @param mode
     */
    public void setMode(GpioPin pins[], GpioPinMode mode)
    {
        for (GpioPin pin : pins)
            setMode(pin, mode);
    }

    /**
     * 
     * @param pins
     * @param mode
     */
    public void setMode(List<GpioPin> pins, GpioPinMode mode)
    {
        for (GpioPin pin : pins)
            setMode(pin, mode);
    }

    /**
     * 
     * @param pin
     * @param resistance
     */
    public void setPullResistor(GpioPin pin, GpioPinResistor resistance)
    {
        com.pi4j.wiringpi.Gpio.pullUpDnControl(pin.getValue(), resistance.getValue());
    }

    /**
     * 
     * @param pins
     * @param resistance
     */
    public void setPullResistor(GpioPin pins[], GpioPinResistor resistance)
    {
        for (GpioPin pin : pins)
            setPullResistor(pin, resistance);
    }

    /**
     * 
     * @param pins
     * @param resistance
     */
    public void setPullResistor(List<GpioPin> pins, GpioPinResistor resistance)
    {
        for (GpioPin pin : pins)
            setPullResistor(pin, resistance);
    }

    /**
     * 
     * @param pin
     * @param state
     */
    public void setState(GpioPin pin, GpioPinState state)
    {
        com.pi4j.wiringpi.Gpio.digitalWrite(pin.getValue(), state.getValue());
    }

    /**
     * 
     * @param pins
     * @param state
     */
    public void setState(GpioPin pins[], GpioPinState state)
    {
        for (GpioPin pin : pins)
            setState(pin, state);
    }
    
    /**
     * 
     * @param pins
     * @param state
     */
    public void setState(List<GpioPin> pins, GpioPinState state)
    {
        for (GpioPin pin : pins)
            setState(pin, state);
    }
    
    public void setHigh(GpioPin pin)
    {
        setState(pin, GpioPinState.HIGH);
    }
    public void setHigh(GpioPin pins[])
    {
        for (GpioPin pin : pins)
            setHigh(pin);
    }
    public void setHigh(List<GpioPin> pins)
    {
        for (GpioPin pin : pins)
            setHigh(pin);
    }

    public void setLow(GpioPin pin)
    {
        setState(pin, GpioPinState.LOW);
    }
    public void setLow(GpioPin pins[])
    {
        for (GpioPin pin : pins)
            setLow(pin);
    }
    public void setLow(List<GpioPin> pins)
    {
        for (GpioPin pin : pins)
            setLow(pin);
    }


    /**
     * 
     * @param pin
     * @param state
     */
    public void toggleState(GpioPin pin)
    {
        if(getState(pin) == GpioPinState.HIGH)            
            setState(pin, GpioPinState.LOW);
        else
            setState(pin, GpioPinState.HIGH);
    }

    /**
     * 
     * @param pins
     * @param state
     */
    public void toggleState(GpioPin pins[])
    {
        for (GpioPin pin : pins)
            toggleState(pin);
    }

    /**
     * 
     * @param pins
     * @param state
     */
    public void toggleState(List<GpioPin> pins)
    {
        for (GpioPin pin : pins)
            toggleState(pin);
    }    

    public void pulse(GpioPin pin, long milliseconds)
    {
        //Thread pulse = new Thread();
        //GpioPulse pulse = new GpioPulse(this, pin, milliseconds);
        //pulse.start();
        GpioPulse.execute(this, pin, milliseconds);
    }

    public void pulse(GpioPin pins[], long milliseconds)
    {
        for (GpioPin pin : pins)
            pulse(pin, milliseconds);
    }

    public void pulse(List<GpioPin> pins, long milliseconds)
    {
        for (GpioPin pin : pins)
            pulse(pin, milliseconds);
    }
    
    /**
     * 
     * @param pin
     * @return
     */
    public GpioPinState getState(GpioPin pin)
    {
        GpioPinState state = null;
        int ret = com.pi4j.wiringpi.Gpio.digitalRead(pin.getValue());
        if (ret >= 0)
            state = GpioPinState.getState(ret);
        return state;
    }

    /**
     * 
     * @param pin
     * @param value
     */
    public void setPwmValue(GpioPin pin, int value)
    {
        com.pi4j.wiringpi.Gpio.pwmWrite(pin.getValue(), value);
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
     * @param pins
     * @param value
     */
    public void setPwmValue(List<GpioPin> pins, int value)
    {
        for (GpioPin pin : pins)
            setPwmValue(pin, value);
    }

    /**
     * 
     * @param listener
     */
    public synchronized void addListener(GpioListener listener)
    {
        listeners.addElement(listener);
    }

    /**
     * 
     * @param listener
     */
    public synchronized void removeListener(GpioListener listener)
    {
        listeners.removeElement(listener);
    }

    /**
     * 
     * @param trigger
     */
    public synchronized void addTrigger(GpioTrigger trigger)
    {
        triggers.addElement(trigger);
    }

    /**
     * 
     * @param trigger
     */
    public synchronized void removeTrigger(GpioTrigger trigger)
    {
        triggers.removeElement(trigger);
    }    

    
    public void setup(GpioPin pin, GpioPinDirection direction, GpioPinEdge edge, GpioPinResistor resistance)
    {
        // export the gpio pin
        export(pin, direction);

        // set the gpio mode based on the pin direction
        if (direction == GpioPinDirection.IN)
        {
            // set the gpio mode
            setMode(pin, GpioPinMode.INPUT);

            // set the gpio edge detection
            if (edge != null)
                setEdge(pin, edge);

            // set the gpio pull resistor
            if (resistance != null)
                setPullResistor(pin, resistance);
        }
        if (direction == GpioPinDirection.OUT)
        {
            // set the gpio mode
            setMode(pin, GpioPinMode.OUTPUT);
        }
    }

    public void setup(GpioPin pins[], GpioPinDirection direction, GpioPinEdge edge, GpioPinResistor resistance)
    {
        for (GpioPin pin : pins)
            setup(pin, direction, edge, resistance);
    }

    public void setup(List<GpioPin> pins, GpioPinDirection direction, GpioPinEdge edge, GpioPinResistor resistance)
    {
        for (GpioPin pin : pins)
            setup(pin, direction, edge, resistance);
    }

    public void setup(GpioPin pin, GpioPinDirection direction, GpioPinEdge edge)
    {
        setup(pin, direction, edge, null);
    }

    public void setup(GpioPin pins[], GpioPinDirection direction, GpioPinEdge edge)
    {
        for (GpioPin pin : pins)
            setup(pin, direction, edge);
    }

    public void setup(List<GpioPin> pins, GpioPinDirection direction, GpioPinEdge edge)
    {
        for (GpioPin pin : pins)
            setup(pin, direction, edge);
    }

    public void setup(GpioPin pin, GpioPinDirection direction)
    {
        setup(pin, direction, null);
    }

    public void setup(GpioPin pins[], GpioPinDirection direction)
    {
        for (GpioPin pin : pins)
            setup(pin, direction);
    }

    public void setup(List<GpioPin> pins, GpioPinDirection direction)
    {
        for (GpioPin pin : pins)
            setup(pin, direction);
    }

    
    /**
     * 
     * @param event
     */
    public void pinStateChange(GpioInterruptEvent event)
    {
        GpioPin pin  = GpioPin.getPin(event.getPin());
        GpioPinState state = GpioPinState.getState(event.getState());

        // process events
        for(GpioListener listener : listeners)
        {
            listener.pinStateChanged(new GpioPinStateChangeEvent(this, pin, state));
        }
        
        // process triggers
        for(GpioTrigger trigger : triggers)
        {
            if(trigger.hasPin(pin) && trigger.hasPinState(state))
                trigger.invoke(this, pin, state);
        }
    }  
}
