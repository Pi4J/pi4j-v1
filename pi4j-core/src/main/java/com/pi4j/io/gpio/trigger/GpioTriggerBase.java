package com.pi4j.io.gpio.trigger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioTriggerBase.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
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
import java.util.Vector;

public abstract class GpioTriggerBase implements GpioTrigger {

    private List<PinState> states = new Vector<>();

    public GpioTriggerBase() {
        addPinState(PinState.allStates());
    }

    public GpioTriggerBase(PinState state) {
        addPinState(state);
    }

    public GpioTriggerBase(PinState[] states) {
        addPinState(states);
    }

    public GpioTriggerBase(List<PinState> states) {
        addPinState(states);
    }

    public void addPinState(PinState... state) {
        if (state == null || state.length == 0) {
            throw new IllegalArgumentException("Missing pin state argument.");
        }
        for (PinState s : state) {
            if (!states.contains(s)) {
                states.add(s);
            }
        }
    }

    public void addPinState(List<? extends PinState> states) {
        for (PinState state : states) {
            addPinState(state);
        }
    }

    public boolean hasPinState(PinState state) {
        return states.contains(state);
    }

    public abstract void invoke(GpioPin pin, PinState state);
}
