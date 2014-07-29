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

    protected Map<Pin, PCA9685GpioServoDriver> allocatedDrivers = new HashMap<Pin, PCA9685GpioServoDriver>();
    
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
