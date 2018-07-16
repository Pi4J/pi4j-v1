package com.pi4j.io.serial.tasks;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialDataEventDispatchTaskImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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
