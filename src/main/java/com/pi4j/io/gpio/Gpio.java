package com.pi4j.io.gpio;

import java.util.List;

import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public interface Gpio
{
    void exportAll(GpioPinDirection direction);
    void unexportAll();
    
    void export(GpioPin pin, GpioPinDirection direction);
    void export(GpioPin pins[], GpioPinDirection direction);
    void export(List<GpioPin> pins, GpioPinDirection direction);
    
    boolean isExported(GpioPin pin);
    
    void unexport(GpioPin pin);
    void unexport(GpioPin pins[]);
    void unexport(List<GpioPin> pins);

    void setDirection(GpioPin pin, GpioPinDirection direction);
    void setDirection(GpioPin pins[], GpioPinDirection direction);
    void setDirection(List<GpioPin> pins, GpioPinDirection direction);
    
    GpioPinDirection getDirection(GpioPin pin);
    
    void setEdge(GpioPin pin, GpioPinEdge edge);
    void setEdge(GpioPin pins[], GpioPinEdge edge);
    void setEdge(List<GpioPin> pins, GpioPinEdge edge);
    
    GpioPinEdge getEdge(GpioPin pin);

    void setMode(GpioPin pin, GpioPinMode mode);
    void setMode(GpioPin pins[], GpioPinMode mode);
    void setMode(List<GpioPin> pins, GpioPinMode mode);
    
    void setPullResistor(GpioPin pin, GpioPinResistor resistance);
    void setPullResistor(GpioPin pins[], GpioPinResistor resistance);
    void setPullResistor(List<GpioPin> pins, GpioPinResistor resistance);

    void setHigh(GpioPin pin);
    void setHigh(GpioPin pins[]);
    void setHigh(List<GpioPin> pins);

    void setLow(GpioPin pin);
    void setLow(GpioPin pins[]);
    void setLow(List<GpioPin> pins);
    
    void setState(GpioPin pin, GpioPinState state);
    void setState(GpioPin pins[], GpioPinState state);
    void setState(List<GpioPin> pins, GpioPinState state);

    void toggleState(GpioPin pin);
    void toggleState(GpioPin pins[]);
    void toggleState(List<GpioPin> pins);

    void pulse(GpioPin pin, long milliseconds);
    void pulse(GpioPin pins[], long milliseconds);
    void pulse(List<GpioPin> pins, long milliseconds);
    
    GpioPinState getState(GpioPin pin);
    
    void setPwmValue(GpioPin pin, int value);
    void setPwmValue(GpioPin pins[], int value);
    void setPwmValue(List<GpioPin> pins, int value);

    void addListener(GpioListener listener);
    void addListener(GpioListener[] listeners);
    void addListener(List<GpioListener> listeners);
    
    //void addListener(GpioListener listener, GpioPin pin);
    //void addListener(GpioListener listener, GpioPin pins[]);
    //void addListener(GpioListener listener, List<GpioPin> pins);
    
    void removeListener(GpioListener listener);
    void removeListener(GpioListener[] listeners);
    void removeListener(List<GpioListener> listeners);
    void removeAllListeners();
    
    void addTrigger(GpioTrigger trigger);
    void addTrigger(GpioTrigger[] triggers);
    void addTrigger(List<GpioTrigger> triggers);
    
    void removeTrigger(GpioTrigger trigger);    
    void removeTrigger(GpioTrigger[] triggers);
    void removeTrigger(List<GpioTrigger> triggers);
    void removeAllTriggers();
    
    void setup(GpioPin pin, GpioPinDirection direction, GpioPinEdge edge, GpioPinResistor resistance);    
    void setup(GpioPin pins[], GpioPinDirection direction, GpioPinEdge edge, GpioPinResistor resistance);
    void setup(List<GpioPin> pins, GpioPinDirection direction, GpioPinEdge edge, GpioPinResistor resistance);
    
    void setup(GpioPin pin, GpioPinDirection direction, GpioPinEdge edge);
    void setup(GpioPin pins[], GpioPinDirection direction, GpioPinEdge edge);
    void setup(List<GpioPin> pins, GpioPinDirection direction, GpioPinEdge edge);
    
    void setup(GpioPin pin, GpioPinDirection direction);
    void setup(GpioPin pins[], GpioPinDirection direction);
    void setup(List<GpioPin> pins, GpioPinDirection direction);    
}
