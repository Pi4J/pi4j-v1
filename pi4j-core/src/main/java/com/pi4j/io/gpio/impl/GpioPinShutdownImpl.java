package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinShutdownImpl.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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

import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.PinState;

public class GpioPinShutdownImpl implements GpioPinShutdown {

    Boolean unexport = null;
    PinMode mode = null;
    PinPullResistance resistance = null;
    PinState state = null;

    @Override
    public void setUnexport(Boolean unexport) {
        this.unexport = unexport;
    }

    @Override
    public Boolean getUnexport() {
        return unexport;
    }

    @Override
    public void setMode(PinMode mode) {
        this.mode = mode;
    }

    @Override
    public PinMode getMode() {
        return mode;
    }

    @Override
    public void setPullResistor(PinPullResistance resistance) {
        this.resistance = resistance;
    }

    @Override
    public PinPullResistance getPullResistor() {
        return resistance;
    }

    @Override
    public void setState(PinState state) {
        this.state = state;
    }

    @Override
    public PinState getState() {
        return state;
    }
}
