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
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;

import java.util.concurrent.ExecutorService;

public class GpioEventDebounceTaskImpl implements Runnable {

    private final GpioPinDigitalInput pin;
    private final PinState originalPinState;
    private static ExecutorService executor;

    public GpioEventDebounceTaskImpl(GpioPinDigitalInput pin, PinState state) {
        executor = GpioFactory.getExecutorServiceFactory().getGpioEventExecutorService();
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
