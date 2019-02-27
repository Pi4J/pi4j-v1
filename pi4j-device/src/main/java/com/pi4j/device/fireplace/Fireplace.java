package com.pi4j.device.fireplace;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Fireplace.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

import com.pi4j.device.ObserveableDevice;

import java.util.concurrent.TimeUnit;

public interface Fireplace extends ObserveableDevice {

    public FireplaceState getState();
    public void setState(FireplaceState state) throws FireplacePilotLightException;
    public boolean isState(FireplaceState state);

    public boolean isOn();
    public boolean isOff();
    public boolean isPilotLightOn();
    public boolean isPilotLightOff();

    public void on() throws FireplacePilotLightException;
    public void on(long timeoutDelay, TimeUnit timeoutUnit) throws FireplacePilotLightException;
    public void off();
    public void setTimeout(long delay, TimeUnit unit);
    public void cancelTimeout();
    public long getTimeoutDelay();
    public TimeUnit getTimeoutUnit();

    void addListener(FireplaceStateChangeListener... listener);
    void removeListener(FireplaceStateChangeListener... listener);
    void addListener(FireplacePilotLightListener... listener);
    void removeListener(FireplacePilotLightListener... listener);
    void addListener(FireplaceTimeoutListener... listener);
    void removeListener(FireplaceTimeoutListener... listener);

    public void shutdown();
}
