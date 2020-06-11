package com.pi4j.component.servo.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  RPIServoBlasterServoDriver.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
