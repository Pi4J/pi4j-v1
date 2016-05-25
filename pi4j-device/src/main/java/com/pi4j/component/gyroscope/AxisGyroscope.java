package com.pi4j.component.gyroscope;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  AxisGyroscope.java
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


public class AxisGyroscope implements Gyroscope {

    private MultiAxisGyro multiAxisGyro;

    private int trigger;

    private int value;
    private int offset;
    private float angle;

    private float degPerSecondFactor;
    private boolean factorSet = false;

    public AxisGyroscope(MultiAxisGyro multiAxisGyro) {
        this.multiAxisGyro = multiAxisGyro;
    }

    public AxisGyroscope(MultiAxisGyro multiAxisGyro, float degPerSecondFactor) {
        this.multiAxisGyro = multiAxisGyro;
        this.degPerSecondFactor = degPerSecondFactor;
        factorSet = true;
    }

    public void setRawValue(int value) {
        this.value = value;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void setReadTrigger(int trigger) {
        this.trigger = trigger;
    }

    protected float readAndUpdateAngle() throws IOException {
        multiAxisGyro.readGyro();
        int adjusted = ((value - offset) / 40) * 40;
        //int adjusted = value - offset;
        float angularVelocity;
        if (factorSet) {
            angularVelocity = adjusted / degPerSecondFactor;
        } else {
            angularVelocity = adjusted;
        }
        angle = angle + angularVelocity * multiAxisGyro.getTimeDelta() / 1000f;
        return angularVelocity;
    }

    @Override
    public int getRawValue() throws IOException {
        if (trigger == GET_RAW_VALUE_TRIGGER_READ) {
            readAndUpdateAngle();
        }
        return value;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public float getAngle() throws IOException {
        if (trigger == GET_ANGLE_TRIGGER_READ) {
            readAndUpdateAngle();
        }
        return angle;
    }

    @Override
    public float getAngularVelocity() throws IOException {
        if (trigger == GET_ANGULAR_VELOCITY_TRIGGER_READ) {
            return (float)readAndUpdateAngle();
        } else {
            int adjusted = value - offset;
            if (factorSet) {
                return adjusted / degPerSecondFactor;
            } else {
                return adjusted;
            }
        }
    }

    @Override
    public void recalibrateOffset() throws IOException {
        multiAxisGyro.recalibrateOffset();
    }

}

