package com.pi4j.device.gate.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GateOpenerDevice.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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

public class GateOpenerDevice extends OpenerDevice
{
    public GateOpenerDevice(Relay relay, Sensor gateSensor, SensorState gateOpenSensorState) {
        super(relay, gateSensor, gateOpenSensorState);
    }

    public GateOpenerDevice(Relay relay, Sensor gateSensor, SensorState gateOpenSensorState, Switch lock) {
        super(relay, gateSensor, gateOpenSensorState, lock);
    }
}
