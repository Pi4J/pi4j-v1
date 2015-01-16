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
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
    private ScheduledFuture debounceFuture = null;
    
    public GpioEventMonitorExecutorImpl(GpioPinInput pin) {
        this.pin = pin;        
        executor = GpioFactory.getExecutorServiceFactory().newSingleThreadExecutorService();
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
