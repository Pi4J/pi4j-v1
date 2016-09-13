package com.pi4j.component.servo.impl;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MaestroServoDriver.java
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

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.io.gpio.Pin;

/**
 * Servo Driver implementation for the Pololu Maestro series of Servo Controllers.
 * @see MaestroServoProvider for details.
 * 
 * @author Bob Brodt (rbrodt@gmail.com)
 *
 */
public class MaestroServoDriver implements ServoDriver {

	public final static int MIN_PULSE_WIDTH = 800; // in usec
	public final static int MAX_PULSE_WIDTH = 2200; // in usec

    protected Pin servoPin;
    protected MaestroServoProvider provider;

	public MaestroServoDriver(MaestroServoProvider provider, Pin servoPin) {
        this.provider = provider;
        this.servoPin = servoPin;
	}

    public int getServoPulseWidth() {
        return provider.getServoPosition(servoPin.getAddress());
    }

    public void setServoPulseWidth(int width) {
        provider.setServoPosition(servoPin.getAddress(), width);
    }

    public int getServoPulseResolution() {
        return 100;
    }

    public void setSpeed(int value) {
    	provider.setSpeed(servoPin.getAddress(), value);
    }

    public void setAcceleration(int value) {
    	provider.setAcceleration(servoPin.getAddress(), value);
    }

    public boolean isMoving() {
    	return provider.isMoving();
    }

    public int getMinValue() {
    	// values for setValue() are in 1/4 usec intervals
    	return MIN_PULSE_WIDTH * 4;
    }

    public int getMaxValue() {
    	// values for setValue() are in 1/4 usec intervals
    	return MAX_PULSE_WIDTH * 4;
    }

    public Pin getPin() {
        return servoPin;
    }
}
