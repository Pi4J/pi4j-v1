package com.pi4j.component.servo.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PCA9685GpioServoProvider.java
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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.component.servo.ServoProvider;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.Pin;

public class PCA9685GpioServoProvider implements ServoProvider {

    private PCA9685GpioProvider provider;

    protected Map<Pin, PCA9685GpioServoDriver> allocatedDrivers = new HashMap<>();

    public PCA9685GpioServoProvider(PCA9685GpioProvider provider) {
        this.provider = provider;
    }

    @Override
    public List<Pin> getDefinedServoPins() throws IOException {
        return Arrays.asList(PCA9685Pin.ALL);
    }

    @Override
    public synchronized ServoDriver getServoDriver(Pin servoPin) throws IOException {
        List<Pin> servoPins = getDefinedServoPins();
        int index = servoPins.indexOf(servoPin);
        if (index < 0) {
            throw new IllegalArgumentException("Servo driver cannot drive pin " + servoPin);
        }

        PCA9685GpioServoDriver driver = allocatedDrivers.get(servoPin);
        if (driver == null) {
            driver = new PCA9685GpioServoDriver(provider, servoPin);
        }

        return driver;
    }

}
