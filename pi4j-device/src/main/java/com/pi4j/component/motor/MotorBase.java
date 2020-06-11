package com.pi4j.component.motor;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  MotorBase.java
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


import com.pi4j.component.ComponentBase;

public abstract class MotorBase extends ComponentBase implements Motor {

    @Override
    public void forward()    {
        setState(MotorState.FORWARD);
    }

    @Override
    public void forward(long milleseconds) {
        try {
            forward();
            Thread.sleep(milleseconds);
            stop();
        }
        catch (InterruptedException e) { }
    }

    @Override
    public void reverse() {
        setState(MotorState.REVERSE);
    }

    @Override
    public void reverse(long milleseconds) {
        try {
            reverse();
            Thread.sleep(milleseconds);
            stop();
        }
        catch (InterruptedException e) { }
    }

    @Override
    public void stop() {
        setState(MotorState.STOP);
    }

    @Override
    public abstract MotorState getState();

    @Override
    public abstract void setState(MotorState state);

    @Override
    public boolean isState(MotorState state){
        return getState().equals(state);
    }

    @Override
    public boolean isStopped() {
        return getState().equals(MotorState.STOP);
    }
}
