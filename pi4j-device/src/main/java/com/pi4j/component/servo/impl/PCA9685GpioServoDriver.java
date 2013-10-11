package com.pi4j.component.servo.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PCA9685GpioServo.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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


import java.math.BigDecimal;

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.io.gpio.Pin;

/**
 * @author Christian Wehrli
 */
public class PCA9685GpioServoDriver implements ServoDriver {

    private PCA9685GpioProvider provider;
    private Pin pin;
    private int position;
    private int resolution;
    
    public PCA9685GpioServoDriver(PCA9685GpioProvider provider, Pin pin) {
        setProvider(provider);
        setPin(pin);
        updateResolution();
    }

    protected void setProvider(PCA9685GpioProvider provider) {
        this.provider = provider;
    }

    public PCA9685GpioProvider getProvider() {
        return provider;
    }

    protected void setPin(Pin pin) {
        this.pin = pin;
    }

    public Pin getPin() {
        return pin;
    }

    public int getServoPulseWidth() {
        return position;
    }

    @Override
    public void setServoPulseWidth(int position) {
        this.position = position;
        getProvider().setPwm(getPin(), position);
    }

    @Override
    public int getServoPulseResolution() {
        return resolution;
    }
    
    protected void updateResolution() {
        resolution = new BigDecimal(4096).divide(getProvider().getFrequency()).intValue();
    }
}
