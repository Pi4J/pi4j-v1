package com.pi4j.io.gpio.trigger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioCallbackTrigger.java
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


import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unused")
public class GpioCallbackTrigger extends GpioTriggerBase {

    private final Callable<Void> callback;

    public GpioCallbackTrigger(Callable<Void> callback) {
        super();
        this.callback = callback;
    }

    public GpioCallbackTrigger(PinState state, Callable<Void> callback) {
        super(state);
        this.callback = callback;
    }

    public GpioCallbackTrigger(PinState[] states, Callable<Void> callback) {
        super(states);
        this.callback = callback;
    }

    public GpioCallbackTrigger(List<PinState> states, Callable<Void> callback) {
        super(states);
        this.callback = callback;
    }

    @Override
    public void invoke(GpioPin pin, PinState state) {
        if (callback != null) {
            try {
                callback.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
