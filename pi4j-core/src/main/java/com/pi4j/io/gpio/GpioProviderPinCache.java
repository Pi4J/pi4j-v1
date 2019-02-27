package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioProviderPinCache.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
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
 * This class provides cache for gpio pin instances.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class GpioProviderPinCache {

    private final Pin pin;
    private PinMode mode;
    private PinState state;
    private PinPullResistance resistance;
    private double analogValue = -1;
    private int pwmValue = -1;
    private boolean exported = false;

    public GpioProviderPinCache(Pin pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "PIN [" + pin.getName() + "] CACHE :: mode=" + mode.getName() + "; state=" + state.getName();
    }

    public PinMode getMode() {
        return mode;
    }

    public void setMode(PinMode mode) {
        this.mode = mode;
    }

    public PinState getState() {
        return state;
    }

    public void setState(PinState state) {
        this.state = state;
    }

    public PinPullResistance getResistance() {
        return resistance;
    }

    public void setResistance(PinPullResistance resistance) {
        this.resistance = resistance;
    }

    public double getAnalogValue() {
        return analogValue;
    }

    public void setAnalogValue(double value) {
        this.analogValue = value;
    }

    public int getPwmValue() {
        return pwmValue;
    }

    public void setPwmValue(int value) {
        this.pwmValue = value;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }
}
