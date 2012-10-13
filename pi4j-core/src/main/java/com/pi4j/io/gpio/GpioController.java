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
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

public interface GpioController
{
    boolean hasPin(Pin... pin);
    
    void export(PinMode mode, Pin... pin);
    void export(PinMode mode, GpioPin... pin);
    boolean isExported(Pin... pin);
    boolean isExported(GpioPin... pin);
    void unexport(Pin... pin);
    void unexport(GpioPin... pin);
    void unexportAll();

    void setMode(PinMode mode, Pin... pin);
    void setMode(PinMode mode, GpioPin... pin);
    PinMode getMode(Pin pin);
    PinMode getMode(GpioPin pin);    
    boolean isMode(PinMode mode, Pin... pin);
    boolean isMode(PinMode mode, GpioPin... pin);

    void setPullResistance(PinPullResistance resistance, Pin... pin);
    void setPullResistance(PinPullResistance resistance, GpioPin... pin);    
    PinPullResistance getPullResistance(Pin pin);
    PinPullResistance getPullResistance(GpioPin pin);
    boolean isPullResistance(PinPullResistance resistance, Pin... pin);
    boolean isPullResistance(PinPullResistance resistance, GpioPin... pin);

    void high(Pin... pin);
    void high(GpioPinDigitalOutput... pin);
    boolean isHigh(Pin... pin);
    boolean isHigh(GpioPinDigital... pin);
    void low(Pin... pin);
    void low(GpioPinDigitalOutput... pin);
    boolean isLow(Pin... pin);
    boolean isLow(GpioPinDigital... pin);
    
    void setState(PinState state, Pin... pin);
    void setState(PinState state, GpioPinDigitalOutput... pin);
    void setState(boolean state, Pin... pin);
    void setState(boolean state, GpioPinDigitalOutput... pin);
    boolean isState(PinState state, Pin... pin);
    boolean isState(PinState state, GpioPinDigital... pin);
    PinState getState(Pin pin);
    PinState getState(GpioPinDigital pin);
    
    void toggle(Pin... pin);
    void toggle(GpioPinDigitalOutput... pin);
    void pulse(long milliseconds, Pin... pin);
    void pulse(long milliseconds, GpioPinDigitalOutput... pin);
    
    void setValue(int value, Pin... pin);
    void setValue(int value, GpioPinAnalogOutput... pin);
    int getValue(Pin pin);
    int getValue(GpioPinAnalog pin);

    void addListener(GpioPinListener listener, Pin... pin);
    void addListener(GpioPinListener[] listeners, Pin... pin);
    void addListener(GpioPinListener listener, GpioPinInput... pin);
    void addListener(GpioPinListener[] listeners, GpioPinInput... pin);
    void removeListener(GpioPinListener listener, Pin... pin);
    void removeListener(GpioPinListener[] listeners, Pin... pin);
    void removeListener(GpioPinListener listener, GpioPinInput... pin);
    void removeListener(GpioPinListener[] listeners, GpioPinInput... pin);
    void removeAllListeners();
    
    void addTrigger(GpioTrigger trigger, GpioPinInput... pin);
    void addTrigger(GpioTrigger[] triggers, GpioPinInput... pin);
    void removeTrigger(GpioTrigger trigger, GpioPinInput... pin);    
    void removeTrigger(GpioTrigger[] triggers, GpioPinInput... pin);
    void removeAllTriggers();
    
    GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name, PinPullResistance resistance);
    GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name);
    GpioPinDigitalOutput provisionDigitalOuputPin(Pin pin, String name, PinState defaultState);
    GpioPinDigitalOutput provisionDigitalOuputPin(Pin pin, String name);

    GpioPinAnalogInput provisionAnalogInputPin(Pin pin, String name);    
    GpioPinAnalogOutput provisionAnalogOuputPin(Pin pin, String name, int defaultValue);
    GpioPinAnalogOutput provisionAnalogOuputPin(Pin pin, String name);

    GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name, int defaultValue);
    GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name);
    
    GpioPin provisionPin(Pin pin, String name, PinMode mode);
    
    boolean isProvisioned(Pin... pin);
    GpioPin getProvisionedPin(Pin pin);
    Collection<GpioPin> getProvisionedPins(Pin... pin);
}
