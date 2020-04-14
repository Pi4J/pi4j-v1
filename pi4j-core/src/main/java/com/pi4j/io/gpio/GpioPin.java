package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPin.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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


import com.pi4j.io.gpio.event.GpioPinListener;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Gpio pin interface. This interface describes all operations over single GPIO pin.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public interface GpioPin {

    GpioProvider getProvider();
    Pin getPin();

    void setName(String name);
    String getName();

    void setTag(Object tag);
    Object getTag();

    void setProperty(String key, String value);
    boolean hasProperty(String key);
    String getProperty(String key);
    String getProperty(String key, String defaultValue);
    Map<String,String> getProperties();
    void removeProperty(String key);
    void clearProperties();

    void export(PinMode mode);
    void export(PinMode mode, PinState defaultState);
    void unexport();
    boolean isExported();

    void setMode(PinMode mode);
    PinMode getMode();
    boolean isMode(PinMode mode);

    void setPullResistance(PinPullResistance resistance);
    PinPullResistance getPullResistance();
    boolean isPullResistance(PinPullResistance resistance);

    Collection<GpioPinListener> getListeners();
    void addListener(GpioPinListener... listener);
    void addListener(List<? extends GpioPinListener> listeners);
    boolean hasListener(GpioPinListener... listener);
    void removeListener(GpioPinListener... listener);
    void removeListener(List<? extends GpioPinListener> listeners);
    void removeAllListeners();

    GpioPinShutdown getShutdownOptions();
    void setShutdownOptions(GpioPinShutdown options);
    void setShutdownOptions(Boolean unexport);
    void setShutdownOptions(Boolean unexport, PinState state);
    void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance);
    void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode);
}
