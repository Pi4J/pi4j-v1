package com.pi4j.io.gpio.tasks.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioBlinkStopTaskImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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


import java.util.concurrent.ScheduledFuture;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

public class GpioBlinkStopTaskImpl implements Runnable {

    private final GpioPinDigitalOutput pin;
    private final PinState stopState;
    private final ScheduledFuture<?> blinkTask;

    public GpioBlinkStopTaskImpl(GpioPinDigitalOutput pin, PinState stopState, ScheduledFuture<?> blinkTask) {
        this.pin = pin;
        this.stopState = stopState;
        this.blinkTask = blinkTask;
    }

    public void run() {
        // cancel the blinking task
        blinkTask.cancel(true);

        // set the pin to the stop blinking state
        pin.setState(stopState);
    }
}
