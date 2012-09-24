package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioController.java  
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


import java.util.Collection;

import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public interface GpioController
{
    boolean hasPin(Pin... pin);
    
    void export(PinDirection direction, Pin... pin);
    void export(PinDirection direction, GpioPin... pin);
    
    boolean isExported(Pin... pin);
    boolean isExported(GpioPin... pin);
    
    void unexport(Pin... pin);
    void unexport(GpioPin... pin);
    void unexportAll();

    void setDirection(PinDirection direction, Pin... pin);
    void setDirection(PinDirection direction, GpioPin... pin);
    
    PinDirection getDirection(Pin pin);
    PinDirection getDirection(GpioPin pin);
    
    boolean isDirection(PinDirection direction, Pin... pin);
    boolean isDirection(PinDirection direction, GpioPin... pin);
    
    void setEdge(PinEdge edge, Pin... pin);
    void setEdge(PinEdge edge, GpioPin... pin);
    
    PinEdge getEdge(Pin pin);
    PinEdge getEdge(GpioPin pin);
    
    boolean isEdge(PinEdge edge, Pin... pin);
    boolean isEdge(PinEdge edge, GpioPin... pin);

    void setMode(PinMode mode, Pin... pin);
    void setMode(PinMode mode, GpioPin... pin);

    // TODO: implement get and is mode methods
    //PinMode getMode(Pin... pin);    
    //PinMode getMode(GpioPin... pin);    
    //boolean isMode(PinMode mode, Pin... pin);
    //boolean isMode(PinMode mode, GpioPin... pin);
        
    void setPullResistor(PinResistor resistance, Pin... pin);
    void setPullResistor(PinResistor resistance, GpioPin... pin);
    
    // TODO: implement get and is pull resistor methods
    //PinResistor getPullResistor(Pin pin);
    //PinResistor getPullResistor(GpioPin pin);
    //boolean isPullResistor(PinResistor resistance, Pin... pin);
    //boolean isPullResistor(PinResistor resistance, GpioPin... pin);

    void high(Pin... pin);
    void high(GpioPin... pin);

    void low(Pin... pin);
    void low(GpioPin... pin);

    boolean isHigh(Pin... pin);
    boolean isHigh(GpioPin... pin);

    boolean isLow(Pin... pin);
    boolean isLow(GpioPin... pin);
    
    void setState(PinState state, Pin... pin);
    void setState(PinState state, GpioPin... pin);
    void setState(boolean state, Pin... pin);
    void setState(boolean state, GpioPin... pin);
    
    boolean isState(PinState state, Pin... pin);
    boolean isState(PinState state, GpioPin... pin);
    
    void toggle(Pin... pin);
    void toggle(GpioPin... pin);

    void pulse(long milliseconds, Pin... pin);
    void pulse(long milliseconds, GpioPin... pin);
    
    PinState getState(Pin pin);
    PinState getState(GpioPin pin);
    
    void setPwmValue(int value, Pin... pin);
    void setPwmValue(int value, GpioPin... pin);

    void addListener(GpioListener listener, Pin... pin);
    void addListener(GpioListener[] listeners, Pin... pin);
    void addListener(GpioListener listener, GpioPin... pin);
    void addListener(GpioListener[] listeners, GpioPin... pin);
    void removeListener(GpioListener listener, Pin... pin);
    void removeListener(GpioListener[] listeners, Pin... pin);
    void removeListener(GpioListener listener, GpioPin... pin);
    void removeListener(GpioListener[] listeners, GpioPin... pin);
    void removeAllListeners();
    
    void addTrigger(GpioTrigger trigger, GpioPin... pin);
    void addTrigger(GpioTrigger[] triggers, GpioPin... pin);
    void removeTrigger(GpioTrigger trigger, GpioPin... pin);    
    void removeTrigger(GpioTrigger[] triggers, GpioPin... pin);
    void removeAllTriggers();
    
    GpioPin provisionInputPin(Pin pin, String name, PinEdge edge, PinResistor resistance);
    GpioPin provisionInputPin(Pin pin, String name, PinEdge edge);
    GpioPin provisionInputPin(Pin pin, String name);
    GpioPin provisionOuputPin(Pin pin, String name, PinState defaultState);
    boolean isProvisioned(Pin... pin);
    GpioPin getProvisionedPin(Pin pin);
    Collection<GpioPin> getProvisionedPins(Pin... pin);
}
