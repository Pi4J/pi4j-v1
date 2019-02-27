package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioEventMonitorExecutorImpl.java
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

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinEvent;
import com.pi4j.io.gpio.event.PinEventType;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.tasks.impl.GpioEventDebounceTaskImpl;
import com.pi4j.io.gpio.tasks.impl.GpioEventDispatchTaskImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GpioEventMonitorExecutorImpl implements PinListener {

    private final GpioPinInput pin;
    private static ExecutorService executor;
    private static ScheduledExecutorService scheduledExecutor;
    private ScheduledFuture<?> debounceFuture = null;

    public GpioEventMonitorExecutorImpl(GpioPinInput pin) {
        this.pin = pin;
        executor = GpioFactory.getExecutorServiceFactory().getGpioEventExecutorService();
        scheduledExecutor = GpioFactory.getExecutorServiceFactory().getScheduledExecutorService();
    }

    @Override
    public void handlePinEvent(PinEvent event) {

        // for digital input pins, we need to enforce pin debounce event suppression
        if(pin instanceof GpioPinDigitalInput && event.getEventType() == PinEventType.DIGITAL_STATE_CHANGE){

            // cast to the digital input pin interface, get the current state from
            // the event, and determine the debounce interval for this pin state
            GpioPinDigitalInput dip = (GpioPinDigitalInput)pin;
            PinState state = ((PinDigitalStateChangeEvent) event).getState();
            int pinDebounceForState = dip.getDebounce(state);

            // if the pin has a debounce delay configured for this pin state,
            // then we will need to use the pin debounce logic to defer pin events
            if(pinDebounceForState > 0) {
                // if no existing debounce future task exists or no future task is still busy, then
                // create a new debounce future task and schedule it based on the debounce
                // interval defined for the current pin state
                if (debounceFuture == null || debounceFuture.isDone()) {
                    debounceFuture = scheduledExecutor.schedule(
                            new GpioEventDebounceTaskImpl(dip, state), pinDebounceForState, TimeUnit.MILLISECONDS);
                } else {
                    // if an existing debounce future task exists and is still busy,
                    // then exit this method effectively suppressing the current pin event
                    return;
                }
            }
        }

        // add a new pin event notification to the thread pool for *immediate* execution
        executor.execute(new GpioEventDispatchTaskImpl(pin, event));
    }
}
