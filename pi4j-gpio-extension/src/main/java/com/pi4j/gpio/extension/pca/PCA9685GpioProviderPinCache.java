package com.pi4j.gpio.extension.pca;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  PCA9685GpioProviderPinCache.java
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
import com.pi4j.io.gpio.GpioProviderPinCache;
import com.pi4j.io.gpio.Pin;

/**
 * @author Christian Wehrli
 * @see PCA9685GpioProvider
 */
public class PCA9685GpioProviderPinCache extends GpioProviderPinCache {

    private int pwmOnValue = -1;
    private int pwmOffValue = -1;

    public PCA9685GpioProviderPinCache(Pin pin) {
        super(pin);
    }

    public int getPwmOnValue() {
        return pwmOnValue;
    }

    public void setPwmOnValue(int pwmOnValue) {
        this.pwmOnValue = pwmOnValue;
    }

    public int getPwmOffValue() {
        return pwmOffValue;
    }

    public void setPwmOffValue(int pwmOffValue) {
        this.pwmOffValue = pwmOffValue;
    }

    @Override
    public int getPwmValue() {
        throw new UnsupportedOperationException("Use getPwmOnValue() and getPwmOffValue() instead.");
    }

    @Override
    public void setPwmValue(int value) {
        throw new UnsupportedOperationException("Use setPwmOnValue() and setPwmOffValue() instead.");
    }
}
