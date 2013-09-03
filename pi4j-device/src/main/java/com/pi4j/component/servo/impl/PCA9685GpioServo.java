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


import java.util.Map;

import com.pi4j.component.servo.ServoBase;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.io.gpio.Pin;

/**
 * @author Christian Wehrli
 */
public class PCA9685GpioServo extends ServoBase {

    public PCA9685GpioServo(PCA9685GpioProvider provider, Pin pin, String name) {
        this(provider, pin, name, null);
    }

    public PCA9685GpioServo(PCA9685GpioProvider provider, Pin pin, String name, Map<String, String> properties) {
        setProvider(provider);
        setPin(pin);
        super.setName(name);
        if (properties != null && properties.isEmpty() == false) {
            for (String key : properties.keySet()) {
                setProperty(key, properties.get(key));
            }
        } else {
            init();
        }
    }

    @Override
    public void setPosition(int position) {
        super.setPosition(position);
        getProvider().setPwm(getPin(), getPwmDuration());
    }

    @Override
    public void setProperty(String key, String value) {
        super.setProperty(key, value);
        setPosition(getPosition());
    }

    @Override
    public PCA9685GpioProvider getProvider() {
        return (PCA9685GpioProvider) super.getProvider();
    }
}
