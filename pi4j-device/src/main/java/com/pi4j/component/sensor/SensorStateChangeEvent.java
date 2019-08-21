package com.pi4j.component.sensor;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  SensorStateChangeEvent.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
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


import java.util.EventObject;

public class SensorStateChangeEvent extends EventObject {

    private static final long serialVersionUID = 482071067043836024L;
    protected final SensorState oldState;
    protected final SensorState newState;

    public SensorStateChangeEvent(Sensor sensor, SensorState oldState, SensorState newState) {
        super(sensor);
        this.oldState = oldState;
        this.newState = newState;
    }

    public Sensor getSensor() {
        return (Sensor)getSource();
    }

    public SensorState getOldState() {
        return oldState;
    }

    public SensorState getNewState() {
        return newState;
    }
}
