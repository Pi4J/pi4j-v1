package com.pi4j.gpio.extension.pca;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  PCA9685GpioProviderPinCache.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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
