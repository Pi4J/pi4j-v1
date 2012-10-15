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
    boolean hasPin(GpioProvider provider, Pin... pin);
    boolean hasPin(Pin... pin);
    
    void export(GpioProvider provider, PinMode mode, Pin... pin);
    void export(PinMode mode, Pin... pin);
    void export(PinMode mode, GpioPin... pin);
    
    boolean isExported(GpioProvider provider, Pin... pin);
    boolean isExported(Pin... pin);
    boolean isExported(GpioPin... pin);
    
    void unexport(GpioProvider provider, Pin... pin);
    void unexport(Pin... pin);
    void unexport(GpioPin... pin);
    
    void unexportAll();

    void setMode(GpioProvider provider, PinMode mode, Pin... pin);
    void setMode(PinMode mode, Pin... pin);
    void setMode(PinMode mode, GpioPin... pin);
    
    PinMode getMode(GpioProvider provider, Pin pin);
    PinMode getMode(Pin pin);
    PinMode getMode(GpioPin pin);    
    
    boolean isMode(GpioProvider provider, PinMode mode, Pin... pin);
    boolean isMode(PinMode mode, Pin... pin);
    boolean isMode(PinMode mode, GpioPin... pin);

    void setPullResistance(GpioProvider provider, PinPullResistance resistance, Pin... pin);
    void setPullResistance(PinPullResistance resistance, Pin... pin);
    void setPullResistance(PinPullResistance resistance, GpioPin... pin);
    
    PinPullResistance getPullResistance(GpioProvider provider, Pin pin);
    PinPullResistance getPullResistance(Pin pin);
    PinPullResistance getPullResistance(GpioPin pin);
    
    boolean isPullResistance(GpioProvider provider, PinPullResistance resistance, Pin... pin);
    boolean isPullResistance(PinPullResistance resistance, Pin... pin);
    boolean isPullResistance(PinPullResistance resistance, GpioPin... pin);

    void high(GpioProvider provider, Pin... pin);
    void high(Pin... pin);
    void high(GpioPinDigitalOutput... pin);
    
    boolean isHigh(GpioProvider provider, Pin... pin);
    boolean isHigh(Pin... pin);
    boolean isHigh(GpioPinDigital... pin);
    
    void low(GpioProvider provider, Pin... pin);
    void low(Pin... pin);
    void low(GpioPinDigitalOutput... pin);
    
    boolean isLow(GpioProvider provider, Pin... pin);
    boolean isLow(Pin... pin);
    boolean isLow(GpioPinDigital... pin);
    
    void setState(GpioProvider provider, PinState state, Pin... pin);
    void setState(PinState state, Pin... pin);
    void setState(PinState state, GpioPinDigitalOutput... pin);
    
    void setState(GpioProvider provider, boolean state, Pin... pin);
    void setState(boolean state, Pin... pin);
    void setState(boolean state, GpioPinDigitalOutput... pin);
    
    boolean isState(GpioProvider provider, PinState state, Pin... pin);
    boolean isState(PinState state, Pin... pin);
    boolean isState(PinState state, GpioPinDigital... pin);
    
    PinState getState(GpioProvider provider, Pin pin);
    PinState getState(Pin pin);
    PinState getState(GpioPinDigital pin);
    
    void toggle(GpioProvider provider, Pin... pin);
    void toggle(Pin... pin);
    void toggle(GpioPinDigitalOutput... pin);
    
    void pulse(GpioProvider provider, long milliseconds, Pin... pin);
    void pulse(long milliseconds, Pin... pin);
    void pulse(long milliseconds, GpioPinDigitalOutput... pin);
    
    void setValue(GpioProvider provider, int value, Pin... pin);
    void setValue(int value, Pin... pin);
    void setValue(int value, GpioPinAnalogOutput... pin);

    int getValue(GpioProvider provider, Pin pin);
    int getValue(Pin pin);
    int getValue(GpioPinAnalog pin);

    void addListener(GpioPinListener listener, GpioPinInput... pin);
    void addListener(GpioPinListener[] listeners, GpioPinInput... pin);
    void removeListener(GpioPinListener listener, GpioPinInput... pin);
    void removeListener(GpioPinListener[] listeners, GpioPinInput... pin);
    void removeAllListeners();
    
    void addTrigger(GpioTrigger trigger, GpioPinInput... pin);
    void addTrigger(GpioTrigger[] triggers, GpioPinInput... pin);
    void removeTrigger(GpioTrigger trigger, GpioPinInput... pin);    
    void removeTrigger(GpioTrigger[] triggers, GpioPinInput... pin);
    void removeAllTriggers();

    GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name, PinPullResistance resistance);
    GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name);
    GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name, PinPullResistance resistance);
    GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name);

    GpioPinDigitalOutput provisionDigitalOuputPin(GpioProvider provider, Pin pin, String name, PinState defaultState);
    GpioPinDigitalOutput provisionDigitalOuputPin(GpioProvider provider, Pin pin, String name);
    GpioPinDigitalOutput provisionDigitalOuputPin(Pin pin, String name, PinState defaultState);
    GpioPinDigitalOutput provisionDigitalOuputPin(Pin pin, String name);

    GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin, String name);    
    GpioPinAnalogInput provisionAnalogInputPin(Pin pin, String name);    
    
    GpioPinAnalogOutput provisionAnalogOuputPin(GpioProvider provider, Pin pin, String name, int defaultValue);
    GpioPinAnalogOutput provisionAnalogOuputPin(GpioProvider provider, Pin pin, String name);
    GpioPinAnalogOutput provisionAnalogOuputPin(Pin pin, String name, int defaultValue);
    GpioPinAnalogOutput provisionAnalogOuputPin(Pin pin, String name);

    GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name, int defaultValue);
    GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name);
    GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name, int defaultValue);
    GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name);
    
    GpioPin provisionPin(GpioProvider provider, Pin pin, String name, PinMode mode);
    GpioPin provisionPin(Pin pin, String name, PinMode mode);
    
    boolean isProvisioned(GpioProvider provider, Pin... pin);
    boolean isProvisioned(Pin... pin);
    
    GpioPin getProvisionedPin(GpioProvider provider, Pin pin);
    GpioPin getProvisionedPin(Pin pin);
    
    Collection<GpioPin> getProvisionedPins(GpioProvider provider, Pin... pin);
    Collection<GpioPin> getProvisionedPins(Pin... pin);
    
    void unprovisionPin(GpioPin... pin);
}
