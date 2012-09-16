package com.pi4j.io.gpio.trigger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library
 * FILENAME      :  GpioToggleStateTrigger.java  
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


import java.util.List;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

public class GpioToggleStateTrigger extends GpioTriggerBase
{
    private GpioPin targetPin;

    public GpioToggleStateTrigger(PinState state, GpioPin targetPin)
    {
        super();
        this.targetPin = targetPin;
    }
    
    public GpioToggleStateTrigger(GpioPin pin, PinState state, GpioPin targetPin)
    {
        super(state);
        this.targetPin = targetPin;
    }

    public GpioToggleStateTrigger(GpioPin pin, PinState[] states, GpioPin targetPin)
    {
        super(states);
        this.targetPin = targetPin;
    }

    public GpioToggleStateTrigger(GpioPin pin, List<PinState> states, GpioPin targetPin)
    {
        super(states);
        this.targetPin = targetPin;
    }
    
    public GpioToggleStateTrigger(GpioPin[] pins, PinState[] states, GpioPin targetPin)
    {
        super(states);
        this.targetPin = targetPin;
    }

    public GpioToggleStateTrigger(List<GpioPin> pins, List<PinState> states, GpioPin targetPin)
    {
        super(states);
        this.targetPin = targetPin;
    }
    
    public void setTargetPin(GpioPin pin)
    {
        targetPin = pin;
    }

    public GpioPin getTargetPin()
    {
        return targetPin;
    }
    
    @Override
    public void invoke(GpioPin pin, PinState state)
    {
        if(targetPin != null)
        {
            targetPin.toggle();
        }
    }
}
