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
