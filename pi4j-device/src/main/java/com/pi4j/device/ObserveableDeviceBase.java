package com.pi4j.device;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  ObserveableDeviceBase.java
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


import java.util.ArrayList;
import java.util.List;

public class ObserveableDeviceBase extends DeviceBase implements ObserveableDevice {
    
    protected final List<DeviceListener> listeners = new ArrayList<DeviceListener>();;
 
    protected synchronized void addListener(DeviceListener... listener){
        if (listener == null || listener.length == 0) {
            throw new IllegalArgumentException("Missing listener argument.");
        }

        // add new listeners
        for (DeviceListener lsnr : listener) {
            listeners.add(lsnr);
        }
    }

    protected synchronized void removeListener(DeviceListener... listener) {
        if (listener == null || listener.length == 0) {
            throw new IllegalArgumentException("Missing listener argument.");
        }
        for (DeviceListener lsnr : listener) {
            listeners.remove(lsnr);
        }
    }

    @Override
    public synchronized void removeAllListeners() {
        List<DeviceListener> listeners_copy = new ArrayList<>(listeners);
        for (int index = (listeners_copy.size()-1); index >= 0; index --) {
            DeviceListener listener = listeners_copy.get(index);
            removeListener(listener);
        }
    }
}
