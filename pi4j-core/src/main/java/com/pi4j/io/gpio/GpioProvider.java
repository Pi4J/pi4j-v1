package com.pi4j.io.gpio;

import com.pi4j.io.gpio.event.PinListener;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

/**
 * Gpio provider interface.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public interface GpioProvider {

    String getName();

    boolean hasPin(Pin pin);

    void export(Pin pin, PinMode mode, PinState defaultState);
    void export(Pin pin, PinMode mode);
    boolean isExported(Pin pin);
    void unexport(Pin pin);

    void setMode(Pin pin, PinMode mode);
    PinMode getMode(Pin pin);

    void setPullResistance(Pin pin, PinPullResistance resistance);
    PinPullResistance getPullResistance(Pin pin);

    void setState(Pin pin, PinState state);
    PinState getState(Pin pin);

    void setValue(Pin pin, double value);
    double getValue(Pin pin);

    void setPwm(Pin pin, int value);
    void setPwmRange(Pin pin, int range);
    int getPwm(Pin pin);

    void addListener(Pin pin, PinListener listener);
    void removeListener(Pin pin, PinListener listener);
    void removeAllListeners();

    void shutdown();
    boolean isShutdown();
}
