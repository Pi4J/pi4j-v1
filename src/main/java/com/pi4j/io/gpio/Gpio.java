package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library
 * FILENAME      :  Gpio.java  
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


import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public interface Gpio
{
    void export(Pin pin, PinDirection direction);
    void export(Pin pins[], PinDirection direction);
    void export(GpioPin pin, PinDirection direction);
    void export(GpioPin pins[], PinDirection direction);
    
    boolean isExported(Pin pin);
    boolean isExported(GpioPin pin);
    
    void unexport(Pin pin);
    void unexport(Pin pins[]);
    void unexport(GpioPin pin);
    void unexport(GpioPin pins[]);
    void unexportAll();

    void setDirection(Pin pin, PinDirection direction);
    void setDirection(Pin pins[], PinDirection direction);
    void setDirection(GpioPin pin, PinDirection direction);
    void setDirection(GpioPin pins[], PinDirection direction);
    
    PinDirection getDirection(Pin pin);
    PinDirection getDirection(GpioPin pin);
    
    void setEdge(Pin pin, PinEdge edge);
    void setEdge(Pin pins[], PinEdge edge);
    void setEdge(GpioPin pin, PinEdge edge);
    void setEdge(GpioPin pins[], PinEdge edge);
    
    PinEdge getEdge(Pin pin);
    PinEdge getEdge(GpioPin pin);

    void setMode(Pin pin, PinMode mode);
    void setMode(Pin pins[], PinMode mode);
    void setMode(GpioPin pin, PinMode mode);
    void setMode(GpioPin pins[], PinMode mode);
    
    void setPullResistor(Pin pin, PinResistor resistance);
    void setPullResistor(Pin pins[], PinResistor resistance);
    void setPullResistor(GpioPin pin, PinResistor resistance);
    void setPullResistor(GpioPin pins[], PinResistor resistance);

    void high(Pin pin);
    void high(Pin pins[]);
    void high(GpioPin pin);
    void high(GpioPin pins[]);

    void low(Pin pin);
    void low(Pin pins[]);
    void low(GpioPin pin);
    void low(GpioPin pins[]);
    
    void setState(Pin pin, PinState state);
    void setState(Pin pins[], PinState state);
    void setState(GpioPin pin, PinState state);
    void setState(GpioPin pins[], PinState state);

    void toggle(Pin pin);
    void toggle(Pin pins[]);
    void toggle(GpioPin pin);
    void toggle(GpioPin pins[]);

    void pulse(Pin pin, long milliseconds);
    void pulse(Pin pins[], long milliseconds);
    void pulse(GpioPin pin, long milliseconds);
    void pulse(GpioPin pins[], long milliseconds);
    
    PinState getState(Pin pin);
    PinState getState(GpioPin pin);
    
    void setPwmValue(Pin pin, int value);
    void setPwmValue(Pin pins[], int value);
    void setPwmValue(GpioPin pin, int value);
    void setPwmValue(GpioPin pins[], int value);

    void addListener(Pin pin, GpioListener listener);
    void addListener(Pin pin, GpioListener listeners[]);
    void addListener(Pin pins[], GpioListener listener);
    void addListener(Pin pins[], GpioListener listeners[]);
    void addListener(GpioPin pin, GpioListener listener);
    void addListener(GpioPin pin, GpioListener listeners[]);
    void addListener(GpioPin pins[], GpioListener listener);
    void addListener(GpioPin pins[], GpioListener listeners[]);
    
    void removeListener(Pin pin, GpioListener listener);
    void removeListener(Pin pin, GpioListener listeners[]);
    void removeListener(Pin pins[], GpioListener listener);
    void removeListener(Pin pins[], GpioListener listeners[]);
    void removeListener(GpioPin pin, GpioListener listener);
    void removeListener(GpioPin pin, GpioListener listeners[]);
    void removeListener(GpioPin pins[], GpioListener listener);
    void removeListener(GpioPin pins[], GpioListener listeners[]);
    
    void removeAllListeners();
    
    void addTrigger(Pin pin, GpioTrigger trigger);
    void addTrigger(Pin pin, GpioTrigger[] triggers);
    void addTrigger(Pin pins[], GpioTrigger trigger);
    void addTrigger(Pin pins[], GpioTrigger[] triggers);
    void addTrigger(GpioPin pin, GpioTrigger trigger);
    void addTrigger(GpioPin pin, GpioTrigger[] triggers);
    void addTrigger(GpioPin pins[], GpioTrigger trigger);
    void addTrigger(GpioPin pins[], GpioTrigger[] triggers);
    
    void removeTrigger(Pin pin, GpioTrigger trigger);    
    void removeTrigger(Pin pin, GpioTrigger[] triggers);
    void removeTrigger(Pin pins[], GpioTrigger trigger);    
    void removeTrigger(Pin pins[], GpioTrigger[] triggers);
    void removeTrigger(GpioPin pin, GpioTrigger trigger);    
    void removeTrigger(GpioPin pin, GpioTrigger[] triggers);
    void removeTrigger(GpioPin pins[], GpioTrigger trigger);    
    void removeTrigger(GpioPin pins[], GpioTrigger[] triggers);
    
    void removeAllTriggers();
    
    GpioPin provisionInputPin(Pin pin, String name, PinEdge edge, PinResistor resistance);
    GpioPin provisionInputPin(Pin pin, String name, PinEdge edge);
    GpioPin provisionInputPin(Pin pin, String name);
    GpioPin provisionOuputPin(Pin pin, String name, PinState defaultState);
}
