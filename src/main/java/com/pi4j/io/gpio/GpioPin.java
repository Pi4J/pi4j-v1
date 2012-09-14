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
