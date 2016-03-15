package com.pi4j.device.access;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  OpenerBase.java
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


import com.pi4j.device.DeviceListener;
import com.pi4j.device.ObserveableDeviceBase;
import com.pi4j.device.access.OpenerState;

public abstract class OpenerBase extends ObserveableDeviceBase implements Opener
{
    @Override
    public abstract void open() throws OpenerLockedException;

    @Override
    public abstract void close() throws OpenerLockedException;

    @Override
    public abstract OpenerState getState();

    @Override
    public abstract boolean isLocked();

    @Override
    public boolean isOpen()
    {
        return (getState() == OpenerState.OPEN);
    }

    @Override
    public boolean isOpening()
    {
        return (getState() == OpenerState.OPENING);
    }

    @Override
    public boolean isClosed()
    {
        return (getState() == OpenerState.CLOSED);
    }

    @Override
    public boolean isClosing()
    {
        return (getState() == OpenerState.CLOSING);
    }

    @Override
    public void addListener(OpenerListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(OpenerListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(OpenerStateChangeEvent event) {
        for(DeviceListener listener : super.listeners) {
            ((OpenerListener)listener).onStateChange(event);
        }
    }

    protected synchronized void notifyListeners(OpenerLockChangeEvent event) {
        for(DeviceListener listener : super.listeners) {
            ((OpenerListener)listener).onLockChange(event);
        }
    }
}
