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
import java.util.concurrent.Callable;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

public class GpioCallbackTrigger extends GpioTriggerBase
{
    private Callable<Void> callback;
    
    public GpioCallbackTrigger(Callable<Void> callback)
    {
        super();
        this.callback = callback;
    }

    public GpioCallbackTrigger(PinState state, Callable<Void> callback)
    {
        super(state);
        this.callback = callback;
    }

    public GpioCallbackTrigger(PinState[] states, Callable<Void> callback)
    {
        super(states);
        this.callback = callback;
    }

    public GpioCallbackTrigger(List<PinState> states, Callable<Void> callback)
    {
        super(states);
        this.callback = callback;
    }
    
    public void setCallback(Callable<Void> callback)
    {
        this.callback = callback;
    }

    @Override
    public void invoke(GpioPin pin, PinState state)
    {        
        if(callback != null)
        {
            try
            {
                callback.call();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
