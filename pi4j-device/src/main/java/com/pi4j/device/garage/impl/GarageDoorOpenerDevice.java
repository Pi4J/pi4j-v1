package com.pi4j.device.garage.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GarageDoorOpenerDevice.java
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


import com.pi4j.component.relay.Relay;
import com.pi4j.component.sensor.Sensor;
import com.pi4j.component.sensor.SensorState;
import com.pi4j.component.switches.Switch;
import com.pi4j.device.access.impl.OpenerDevice;

public class GarageDoorOpenerDevice extends OpenerDevice
{
    public GarageDoorOpenerDevice(Relay relay, Sensor doorSensor, SensorState doorOpenSensorState) {
        super(relay, doorSensor, doorOpenSensorState);
    }

    public GarageDoorOpenerDevice(Relay relay, Sensor doorSensor, SensorState doorOpenSensorState, Switch lock) {
        super(relay, doorSensor, doorOpenSensorState, lock);
    }
}
