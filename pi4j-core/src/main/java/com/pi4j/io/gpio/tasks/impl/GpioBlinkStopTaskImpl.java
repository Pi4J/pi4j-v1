package com.pi4j.io.gpio.tasks.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioBlinkStopTaskImpl.java
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
