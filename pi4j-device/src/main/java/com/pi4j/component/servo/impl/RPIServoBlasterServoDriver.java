package com.pi4j.component.servo.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  RPIServoBlasterServoDriver.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.io.gpio.Pin;

public class RPIServoBlasterServoDriver implements ServoDriver {

    protected Pin servoPin;
    protected int index;
    protected String pinString;
    protected int servoPosition;
    protected RPIServoBlasterProvider provider;
    
    protected RPIServoBlasterServoDriver(Pin servoPin, int index, String pinString, RPIServoBlasterProvider provider) throws IOException {
        this.index = index;
        this.servoPin = servoPin;
        this.pinString = pinString;
        this.provider = provider;
    }
    
    
    public int getServoPulseWidth() {
        return servoPosition;
    }
    
    public void setServoPulseWidth(int width) {
        this.servoPosition = width;
        provider.updateServo(pinString, width);
    }
    
    public int getServoPulseResolution() {
        return 100;
    }

    @Override
    public Pin getPin() {
        return servoPin;
    }
}
