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
 * Copyright (C) 2012 - 2015 Pi4J
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
        for (int index = (listeners.size()-1); index >= 0; index --) {
            DeviceListener listener = listeners.get(index);
            removeListener(listener);
        }
    }
}
