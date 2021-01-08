package com.pi4j.component.power;

import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PowerBase.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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


public abstract class PowerBase extends ObserveableComponentBase implements Power {

    @Override
    public void on() {
        setState(PowerState.ON);
    }

    @Override
    public void off() {
        setState(PowerState.OFF);
    }

    @Override
    public boolean isOn() {
        return (getState() == PowerState.ON);
    }

    @Override
    public boolean isOff() {
        return (getState() == PowerState.OFF);
    }

    @Override
    public abstract PowerState getState();

    @Override
    public abstract void setState(PowerState state);

    @Override
    public void addListener(PowerListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(PowerListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(PowerStateChangeEvent event) {
        for(ComponentListener listener : super.listeners) {
            ((PowerListener)listener).onStateChange(event);
        }
    }
}
