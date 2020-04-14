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
