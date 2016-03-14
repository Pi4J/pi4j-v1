package com.pi4j.component.servo.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PCA9685GpioServoDriver.java
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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


import java.math.BigDecimal;
import java.math.MathContext;

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
        resolution = new BigDecimal(4096).divide(getProvider().getFrequency(), MathContext.DECIMAL32).intValue();
    }
}
