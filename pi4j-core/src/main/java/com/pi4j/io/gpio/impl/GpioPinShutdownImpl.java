package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinShutdownImpl.java
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

import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.PinState;

public class GpioPinShutdownImpl implements GpioPinShutdown {

    Boolean unexport = null;
    PinMode mode = null;
    PinPullResistance resistance = null;
    PinState state = null;

    @Override
    public void setUnexport(Boolean unexport) {
        this.unexport = unexport;
    }

    @Override
    public Boolean getUnexport() {
        return unexport;
    }

    @Override
    public void setMode(PinMode mode) {
        this.mode = mode;
    }

    @Override
    public PinMode getMode() {
        return mode;
    }

    @Override
    public void setPullResistor(PinPullResistance resistance) {
        this.resistance = resistance;
    }

    @Override
    public PinPullResistance getPullResistor() {
        return resistance;
    }

    @Override
    public void setState(PinState state) {
        this.state = state;
    }

    @Override
    public PinState getState() {
        return state;
    }
}
