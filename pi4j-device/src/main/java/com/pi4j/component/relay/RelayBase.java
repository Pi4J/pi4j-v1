package com.pi4j.component.relay;

import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  RelayBase.java
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


public abstract class RelayBase extends ObserveableComponentBase implements Relay {

    public static final int DEFAULT_PULSE_MILLISECONDS = 500;

    @Override
    public void open() {
        setState(RelayState.OPEN);
    }

    @Override
    public void close() {
        setState(RelayState.CLOSED);
    }

    @Override
    public boolean isOpen() {
        return (getState() == RelayState.OPEN);
    }

    @Override
    public boolean isClosed() {
        return (getState() == RelayState.CLOSED);
    }

    @Override
    public abstract RelayState getState();

    @Override
    public abstract void setState(RelayState state);

    @Override
    public void pulse() {
        pulse(DEFAULT_PULSE_MILLISECONDS);
    }

    @Override
    public void pulse(int milliseconds) {
        close();
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        open();
    }

    @Override
    public void toggle() {
        if(isOpen())
            close();
        else
            open();
    }

    @Override
    public boolean isState(RelayState state) {
        return getState().equals(state);
    }

    @Override
    public void addListener(RelayListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(RelayListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(RelayStateChangeEvent event) {
        for(ComponentListener listener : super.listeners) {
            ((RelayListener)listener).onStateChange(event);
        }
    }
}
