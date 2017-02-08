package com.pi4j.io.gpio.event;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinDigitalStateChangeEvent.java
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


import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinState;

/**
 * GPIO digital state pin value change event.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class GpioPinDigitalStateChangeEvent extends GpioPinEvent {

    static final long serialVersionUID = 1L;
    private final PinState state;
    private final PinEdge edge;

    /**
     * Default event constructor
     *
     * @param obj    Ignore this parameter
     * @param pin    GPIO pin number (not header pin number; not wiringPi pin number)
     * @param state  New GPIO pin state.
     */
    public GpioPinDigitalStateChangeEvent(Object obj, GpioPin pin, PinState state) {
        super(obj, pin, PinEventType.DIGITAL_STATE_CHANGE);
        this.state = state;

        // set pin edge caused by the state change
        this.edge = (state == PinState.HIGH) ? PinEdge.RISING : PinEdge.FALLING;
    }

    /**
     * Get the new pin state raised in this event.
     *
     * @return GPIO pin state (HIGH, LOW)
     */
    public PinState getState() {
        return state;
    }

    /**
     * Get the pin edge for the state change caused by this event.
     *
     * @return
     */
    public PinEdge getEdge() {
        return this.edge;
    }
}
