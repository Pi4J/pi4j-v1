package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPin.java  
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


import java.util.List;
import java.util.Map;

import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public interface GpioPin
{
    Pin getPin();
    
    void setName(String name);
    String getName();

    void setProperty(String key, String value);
    boolean hasProperty(String key);
    String getProperty(String key);
    Map<String,String> getProperties();
    void removeProperty(String key);
    void clearProperties();
    
    void export(PinDirection direction);
    void unexport();
    boolean isExported();
    
    void setDirection(PinDirection direction);
    PinDirection getDirection();
    
    void setEdge(PinEdge edge);

    PinEdge getEdge();

    void setMode(PinMode mode);
    PinMode getMode();
    
    void setPullResistor(PinResistor resistance);
    PinResistor getPullResistor();

    void high();
    void low();    
    void toggle();
    void pulse(long milliseconds);
    void setState(PinState state);
    boolean isHigh();
    boolean isLow();
    PinState getState();
    
    void setPwmValue(int value);

    GpioListener[] getListeners();
    void addListener(GpioListener listener);
    void addListener(GpioListener[] listeners);
    void addListener(List<GpioListener> listeners);
    
    void removeListener(GpioListener listener);
    void removeListener(GpioListener[] listeners);
    void removeListener(List<GpioListener> listeners);
    void removeAllListeners();
    
    GpioTrigger[] getTriggers();
    void addTrigger(GpioTrigger trigger);
    void addTrigger(GpioTrigger[] triggers);
    void addTrigger(List<GpioTrigger> triggers);
    
    void removeTrigger(GpioTrigger trigger);    
    void removeTrigger(GpioTrigger[] triggers);
    void removeTrigger(List<GpioTrigger> triggers);
    void removeAllTriggers();
    
    GpioPinShutdown getShutdownOptions();
    void setShutdownOptions(GpioPinShutdown options);
    void setShutdownOptions(Boolean unexport);
    void setShutdownOptions(Boolean unexport, PinState state);
    void setShutdownOptions(Boolean unexport, PinState state, PinEdge edge);
    void setShutdownOptions(Boolean unexport, PinState state, PinEdge edge, PinResistor resistance);
    void setShutdownOptions(Boolean unexport, PinState state, PinEdge edge, PinResistor resistance, PinDirection direction);
}
