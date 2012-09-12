/*
 * **********************************************************************
 * This file is part of the pi4j project: http://www.pi4j.com/
 * 
 * pi4j is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * pi4j is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with pi4j. If not,
 * see <http://www.gnu.org/licenses/>.
 * **********************************************************************
 */
package com.pi4j.io.gpio;

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
}
