package com.pi4j.component.sensor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioSensorComponent.java
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

import com.pi4j.component.sensor.Sensor;
import com.pi4j.component.sensor.SensorBase;
import com.pi4j.component.sensor.SensorState;
import com.pi4j.component.sensor.SensorStateChangeEvent;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GpioSensorComponent extends SensorBase {

    // internal class members
    private GpioPinDigitalInput pin = null;
    private PinState openState = PinState.LOW;
    private PinState closedState = PinState.HIGH;
    private final Sensor sensor = this;

    // create internal pin listener
    private GpioPinListenerDigital pinListener = new GpioPinListenerDigital() {

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

            // notify any sensor state change listeners
            if(event.getState() == openState) {
                notifyListeners(new SensorStateChangeEvent(sensor, SensorState.CLOSED, SensorState.OPEN));
            }
            else if(event.getState() == closedState) {
                notifyListeners(new SensorStateChangeEvent(sensor, SensorState.OPEN, SensorState.CLOSED));
            }
        }
    };

    /**
     * using this constructor requires that the consumer
     *  define the SENSOR OPEN and SENSOR CLOSED pin states
     *
     * @param pin GPIO digital input pin
     * @param openState pin state to set when SENSOR is OPEN
     * @param closedState pin state to set when SENSOR is CLOSED
     */
    public GpioSensorComponent(GpioPinDigitalInput pin, PinState openState, PinState closedState) {
        this(pin);
        this.openState = openState;
        this.closedState = closedState;
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is SENSOR CLOSED
     *  (2) a pin state of LOW  is SENSOR OPEN
     *
     * @param pin GPIO digital input pin
     */
    public GpioSensorComponent(GpioPinDigitalInput pin) {
        this.pin = pin;
        this.pin.addListener(pinListener);
    }

    /**
     * Return the current sensor state based on the
     * GPIO digital output pin state.
     *
     * @return PowerState
     */
    @Override
    public SensorState getState() {
        if(pin.isState(openState))
            return SensorState.OPEN;
        else
            return SensorState.CLOSED;
    }
}
