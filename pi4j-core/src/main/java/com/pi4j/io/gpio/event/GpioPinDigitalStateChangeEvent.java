package com.pi4j.io.gpio.event;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinDigitalStateChangeEvent.java
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
