package com.pi4j.device.fireplace.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  FireplaceDevice.java  
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

import com.pi4j.component.relay.Relay;
import com.pi4j.component.relay.RelayListener;
import com.pi4j.component.relay.RelayState;
import com.pi4j.component.relay.RelayStateChangeEvent;
import com.pi4j.component.sensor.Sensor;
import com.pi4j.component.sensor.SensorListener;
import com.pi4j.component.sensor.SensorState;
import com.pi4j.component.sensor.SensorStateChangeEvent;
import com.pi4j.device.fireplace.*;

public class FireplaceDevice extends FireplaceBase {

    protected final Relay fireplaceControlRelay;
    protected final RelayState fireplaceOnRelayState;
    protected final Sensor pilotLightSensor;
    protected final SensorState pilotLightOnState;

    public FireplaceDevice(final Relay fireplaceControlRelay,
                           final Sensor pilotLightSensor){
        this(fireplaceControlRelay, RelayState.CLOSED, pilotLightSensor, SensorState.CLOSED);
    }

    public FireplaceDevice(final Relay fireplaceControlRelay, final RelayState fireplaceOnRelayState) {
        this(fireplaceControlRelay, fireplaceOnRelayState, null, SensorState.CLOSED);
    }

    public FireplaceDevice(final Relay fireplaceControlRelay){
        this(fireplaceControlRelay, RelayState.CLOSED, null, SensorState.CLOSED);
    }

    public FireplaceDevice(final Relay fireplaceControlRelay, final RelayState fireplaceOnRelayState,
                           final Sensor pilotLightSensor, final SensorState pilotLightOnState){
        this.fireplaceControlRelay = fireplaceControlRelay;
        this.fireplaceOnRelayState = fireplaceOnRelayState;
        this.pilotLightSensor = pilotLightSensor;
        this.pilotLightOnState = pilotLightOnState;

        // listen to relay changes to notify fireplace state change events
        fireplaceControlRelay.addListener(new RelayListener() {
            @Override
            public void onStateChange(RelayStateChangeEvent event) {
                if(event.getNewState() == fireplaceOnRelayState){
                    notifyListeners(new FireplaceStateChangeEvent(FireplaceDevice.this,
                            FireplaceState.OFF, FireplaceState.ON));
                }
                else{
                    notifyListeners(new FireplaceStateChangeEvent(FireplaceDevice.this,
                            FireplaceState.ON, FireplaceState.OFF));
                }
            }
        });

        // listen to fireplace pilot light sensor (if provided)
        if(pilotLightSensor != null){
            pilotLightSensor.addListener(new SensorListener() {
                @Override
                public void onStateChange(SensorStateChangeEvent event) {
                    // if the pilot light sensor no longer detects a flame,
                    // then turn off the fireplace!
                    if(!pilotLightSensor.isState(pilotLightOnState)){
                        off();
                    }

                    // notify pilot light listeners
                    notifyListeners(new FireplacePilotLightEvent(FireplaceDevice.this, isPilotLightOn()));
                }
            });
        }
    }

    @Override
    public FireplaceState getState() {
        if(fireplaceControlRelay.isState(fireplaceOnRelayState))
            return FireplaceState.ON;
        else
            return FireplaceState.OFF;
    }

    @Override
    public void setState(FireplaceState state) throws FireplacePilotLightException {

        // turn fireplace OFF
        if(state == FireplaceState.OFF){
            // toggle the state of the relay if it's current in the ON state
            if(fireplaceControlRelay.isState(fireplaceOnRelayState))
                fireplaceControlRelay.toggle();
        }

        // turn fireplace ON
        else {
            // first, if a pilot light sensor was provided, then
            // we must determine if the pilot light is lit
            if (pilotLightSensor != null && !isPilotLightOn()) {
                throw new FireplacePilotLightException();
            }

            // set the state of the relay to the ON state
            if(!fireplaceControlRelay.isState(fireplaceOnRelayState))
                fireplaceControlRelay.setState(fireplaceOnRelayState);
        }
    }

    @Override
    public boolean isPilotLightOn() {
        if(pilotLightSensor == null) return false;
        return pilotLightSensor.isState(pilotLightOnState);
    }

    @Override
    public boolean isPilotLightOff() {
        return !(isPilotLightOn());
    }

}
