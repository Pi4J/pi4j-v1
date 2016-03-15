package com.pi4j.component.motor;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  StepperMotorBase.java
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

public abstract class StepperMotorBase extends MotorBase implements StepperMotor {

    protected long stepIntervalMilliseconds = 100;
    protected int stepIntervalNanoseconds = 0;
    protected byte[] stepSequence;
    protected int stepsPerRevolution = 0;

    @Override
    public float getStepsPerRevolution() {
        return stepsPerRevolution;
    }

    @Override
    public void setStepsPerRevolution(int steps) {
        stepsPerRevolution = steps;
    }

    @Override
    public void setStepInterval(long milliseconds){
        stepIntervalMilliseconds = milliseconds;
        stepIntervalNanoseconds = 0;
    }

    @Override
    public void setStepInterval(long milliseconds, int nanoseconds){
        stepIntervalMilliseconds = milliseconds;
        stepIntervalNanoseconds = nanoseconds;
    }

    @Override
    public void setStepSequence(byte[] sequence) {
        stepSequence = sequence;
    }

    @Override
    public byte[] getStepSequence() {
        return stepSequence;
    }

    @Override
    public void rotate(double revolutions) {
        long steps = Math.round(stepsPerRevolution * revolutions);
        step(steps);
    }

    @Override
    public abstract void step(long steps);
}
