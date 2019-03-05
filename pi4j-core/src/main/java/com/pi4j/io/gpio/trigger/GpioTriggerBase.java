package com.pi4j.io.gpio.trigger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioTriggerBase.java
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
