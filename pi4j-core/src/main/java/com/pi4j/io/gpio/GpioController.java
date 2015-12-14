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
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

import java.util.Collection;

/**
 * Gpio controller interface. This interface describes all operations over GPIO.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public interface GpioController {

    void export(PinMode mode, PinState defaultState, GpioPin... pin);
    void export(PinMode mode, GpioPin... pin);
    boolean isExported(GpioPin... pin);
    void unexport(GpioPin... pin);
    void unexportAll();

    void setMode(PinMode mode, GpioPin... pin);
    PinMode getMode(GpioPin pin);
    boolean isMode(PinMode mode, GpioPin... pin);

    void setPullResistance(PinPullResistance resistance, GpioPin... pin);
    PinPullResistance getPullResistance(GpioPin pin);
    boolean isPullResistance(PinPullResistance resistance, GpioPin... pin);

    void high(GpioPinDigitalOutput... pin);
    boolean isHigh(GpioPinDigital... pin);

    void low(GpioPinDigitalOutput... pin);
    boolean isLow(GpioPinDigital... pin);

    void setState(PinState state, GpioPinDigitalOutput... pin);
    void setState(boolean state, GpioPinDigitalOutput... pin);
    boolean isState(PinState state, GpioPinDigital... pin);
    PinState getState(GpioPinDigital pin);

    void toggle(GpioPinDigitalOutput... pin);
    void pulse(long milliseconds, GpioPinDigitalOutput... pin);

    void setValue(double value, GpioPinAnalogOutput... pin);
    double getValue(GpioPinAnalog pin);

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

    GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, String name, PinMode mode, PinPullResistance resistance);
    GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, PinMode mode, PinPullResistance resistance);
    GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, String name, PinMode mode);
    GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, PinMode mode);
    GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, String name, PinMode mode, PinPullResistance resistance);
    GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, PinMode mode, PinPullResistance resistance);
    GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, String name, PinMode mode);
    GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, PinMode mode);

    GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name, PinPullResistance resistance);
    GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, PinPullResistance resistance);
    GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name);
    GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin);
    GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name, PinPullResistance resistance);
    GpioPinDigitalInput provisionDigitalInputPin(Pin pin, PinPullResistance resistance);
    GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name);
    GpioPinDigitalInput provisionDigitalInputPin(Pin pin);

    GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, String name, PinState defaultState);
    GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, PinState defaultState);
    GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, String name);
    GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin);
    GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String name, PinState defaultState);
    GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, PinState defaultState);
    GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String name);
    GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin);

    GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin, String name);
    GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin);
    GpioPinAnalogInput provisionAnalogInputPin(Pin pin, String name);
    GpioPinAnalogInput provisionAnalogInputPin(Pin pin);

    GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, String name, double defaultValue);
    GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, double defaultValue);
    GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, String name);
    GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin);
    GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String name, double defaultValue);
    GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, double defaultValue);
    GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String name);
    GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin);

    GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name, int defaultValue);
    GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, int defaultValue);
    GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name);
    GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin);
    GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name, int defaultValue);
    GpioPinPwmOutput provisionPwmOutputPin(Pin pin, int defaultValue);
    GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name);
    GpioPinPwmOutput provisionPwmOutputPin(Pin pin);

    GpioPinPwmOutput provisionSoftPwmOutputPin(GpioProvider provider, Pin pin, String name, int defaultValue);
    GpioPinPwmOutput provisionSoftPwmOutputPin(GpioProvider provider, Pin pin, int defaultValue);
    GpioPinPwmOutput provisionSoftPwmOutputPin(GpioProvider provider, Pin pin, String name);
    GpioPinPwmOutput provisionSoftPwmOutputPin(GpioProvider provider, Pin pin);
    GpioPinPwmOutput provisionSoftPwmOutputPin(Pin pin, String name, int defaultValue);
    GpioPinPwmOutput provisionSoftPwmOutputPin(Pin pin, int defaultValue);
    GpioPinPwmOutput provisionSoftPwmOutputPin(Pin pin, String name);
    GpioPinPwmOutput provisionSoftPwmOutputPin(Pin pin);

    GpioPin provisionPin(GpioProvider provider, Pin pin, String name, PinMode mode, PinState defaultState);
    GpioPin provisionPin(GpioProvider provider, Pin pin, String name, PinMode mode);
    GpioPin provisionPin(GpioProvider provider, Pin pin, PinMode mode);
    GpioPin provisionPin(Pin pin, String name, PinMode mode);
    GpioPin provisionPin(Pin pin, PinMode mode);

    void setShutdownOptions(GpioPinShutdown options, GpioPin... pin);
    void setShutdownOptions(Boolean unexport, GpioPin... pin);
    void setShutdownOptions(Boolean unexport, PinState state, GpioPin... pin);
    void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, GpioPin... pin);
    void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode, GpioPin... pin);

    Collection<GpioPin> getProvisionedPins();

    void unprovisionPin(GpioPin... pin);
    boolean isShutdown();
    void shutdown();
}
