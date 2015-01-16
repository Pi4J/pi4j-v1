package com.pi4j.io.gpio.tasks.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioEventDebounceTaskImpl.java  
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


import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;

import java.util.concurrent.ExecutorService;

public class GpioEventDebounceTaskImpl implements Runnable {

    private final GpioPinDigitalInput pin;
    private final PinState originalPinState;
    private static ExecutorService executor;

    public GpioEventDebounceTaskImpl(GpioPinDigitalInput pin, PinState state) {
        executor = GpioFactory.getExecutorServiceFactory().newSingleThreadExecutorService();
        this.originalPinState = state;
        this.pin = pin;
    }

    @Override
    public void run() {
        // if the current pin state is not the same as the original pin state,
        // then we need to raise a new pin event to notify the user that the pin
        // state has changed during the debounce delay period
        if(!pin.isState(originalPinState)){
            // add a new pin event notification to the thread pool for *immediate* execution
            executor.execute(new GpioEventDispatchTaskImpl(pin,
                    new PinDigitalStateChangeEvent(this, pin.getPin(), pin.getState())));
        }
    }
}
