package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPin.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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
