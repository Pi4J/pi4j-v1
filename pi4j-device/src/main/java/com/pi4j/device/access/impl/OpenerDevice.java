package com.pi4j.device.access.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  OpenerDevice.java
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


import com.pi4j.component.relay.Relay;
import com.pi4j.component.sensor.Sensor;
import com.pi4j.component.sensor.SensorListener;
import com.pi4j.component.sensor.SensorState;
import com.pi4j.component.sensor.SensorStateChangeEvent;
import com.pi4j.component.switches.Switch;
import com.pi4j.component.switches.SwitchListener;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.device.access.Opener;
import com.pi4j.device.access.OpenerBase;
import com.pi4j.device.access.OpenerLockChangeEvent;
import com.pi4j.device.access.OpenerLockedException;
import com.pi4j.device.access.OpenerState;
import com.pi4j.device.access.OpenerStateChangeEvent;

public class OpenerDevice extends OpenerBase implements Opener
{
    private Relay relay;
    private Sensor sensor;
    private SensorState openSensorState;
    private Switch lock = null;
    private final OpenerDevice opener = this;

    // create sensor listener
    private SensorListener sensorListener = new SensorListener() {
        @Override
        public void onStateChange(SensorStateChangeEvent event) {
            OpenerState oldState = getState(event.getOldState());
            OpenerState newState = getState(event.getNewState());
            opener.notifyListeners(new OpenerStateChangeEvent(opener, oldState, newState));
        }
    };

    // create lock switch listener
    private SwitchListener lockSwitchListener = new SwitchListener() {
        @Override
        public void onStateChange(SwitchStateChangeEvent event) {
            opener.notifyListeners(new OpenerLockChangeEvent(opener, event.getSwitch().isOn()));
        }
    };

    public OpenerDevice(Relay relay, Sensor sensor, SensorState openSensorState) {
        this.relay = relay;
        this.sensor = sensor;
        this.openSensorState= openSensorState;
        this.sensor.addListener(sensorListener);
    }

    public OpenerDevice(Relay relay, Sensor sensor, SensorState openSensorState, Switch lock) {
        this(relay,sensor, openSensorState);
        this.lock = lock;
        this.lock.addListener(lockSwitchListener);
    }

    @Override
    public void open() throws OpenerLockedException {

        // abort if the opener is locked
        if(isLocked())
            throw new OpenerLockedException(this);

        // if the open sensor determines that the door/gate is
        // not in the open state, then pulse the relay to
        // perform the open operation
        if(!sensor.isState(openSensorState)) {
            // pulse the control relay to open the garage door/gate
            relay.pulse();
        }
    }

    @Override
    public void close() throws OpenerLockedException {

        // abort if the opener is locked
        if(isLocked())
            throw new OpenerLockedException(this);

        // if the open sensor determines that the door/gate is
        // in the open state, then pulse the relay to
        // perform the close operation
        if(sensor.isState(openSensorState)) {
            // pulse the control relay to close the garage door/gate
            relay.pulse();
        }
    }

    @Override
    public OpenerState getState() {
        if(sensor.getState().equals(openSensorState))
            return OpenerState.OPEN;
        else
            return OpenerState.CLOSED;
    }

    @Override
    public boolean isLocked() {
        if(lock == null)
            return false;
        return lock.isOn();
    }

    protected OpenerState getState(SensorState sensorState) {
        if(sensorState.equals(openSensorState))
            return OpenerState.OPEN;
        else
            return OpenerState.CLOSED;
    }
}
