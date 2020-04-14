package com.pi4j.io.serial.tasks;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialDataEventDispatchTaskImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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


import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;

import java.util.ArrayList;
import java.util.Collection;

public class SerialDataEventDispatchTaskImpl implements Runnable {

    private final SerialDataEvent event;
    private final Collection<SerialDataEventListener> listeners;

    public SerialDataEventDispatchTaskImpl(SerialDataEvent event, Collection<SerialDataEventListener> listeners) {
        this.event = event;
        this.listeners = listeners;
    }

    @Override
    public void run() {

        // create a copy of the listeners collection
        Collection<SerialDataEventListener> listenersCopy  = new ArrayList<>(listeners);

        // process event callbacks for serial data listeners
        for (SerialDataEventListener listener : listenersCopy) {
            if (listener != null) {
                listener.dataReceived(event);
            }
        }
    }
}
