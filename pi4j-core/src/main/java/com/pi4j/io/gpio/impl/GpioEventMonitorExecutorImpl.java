package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioEventMonitorExecutorImpl.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * #L%
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.pi4j.io.gpio.GpioPinInput;
import com.pi4j.io.gpio.event.PinEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.tasks.impl.GpioEventDispatchTaskImpl;

public class GpioEventMonitorExecutorImpl implements PinListener
{
    private final GpioPinInput pin;
    private static ExecutorService executor;
    
    public GpioEventMonitorExecutorImpl(GpioPinInput pin)
    {
        this.pin = pin;        
        executor = Executors.newSingleThreadExecutor();
    }
    
    @Override
    public void handlePinEvent(PinEvent event)
    {
        executor.execute(new GpioEventDispatchTaskImpl(pin, event));
    }
}
