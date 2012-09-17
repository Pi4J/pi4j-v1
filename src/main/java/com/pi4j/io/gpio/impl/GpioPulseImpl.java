package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library
 * FILENAME      :  GpioPulseImpl.java  
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


import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

import com.pi4j.io.gpio.GpioPin;

public class GpioPulseImpl
{
    @SuppressWarnings("rawtypes")
    private static ConcurrentHashMap<GpioPin, ScheduledFuture> tasks = new ConcurrentHashMap<GpioPin, ScheduledFuture>();
    private static ScheduledExecutorService scheduledExecutorService;

    public synchronized static void execute(GpioPin pin, long milliseconds)
    {
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown())
            scheduledExecutorService = Executors.newScheduledThreadPool(5);

        // first determine if an existing pulse job is already scheduled for this pin
        if (tasks.containsKey(pin))
        {
            // if a job is found, then cancel it immediately and remove the job
            ScheduledFuture<?> previouslyScheduled = tasks.get(pin);
            previouslyScheduled.cancel(true);
            tasks.remove(pin);
        }

        // set the high state
        pin.high();

        // create future job to return the pin to the low state
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService
            .schedule(new GpioPulseOffImpl(pin), milliseconds, TimeUnit.MILLISECONDS);

        // add the new scheduled job to the tasks map
        tasks.put(pin, scheduledFuture);

        // create future job to return the pin to the low state
        @SuppressWarnings(
        { "rawtypes", "unchecked", "unused" })
        ScheduledFuture<?> cleanupFuture = scheduledExecutorService.schedule(new Callable()
        {
            public Object call() throws Exception
            {
                for (Entry<GpioPin, ScheduledFuture> item : tasks.entrySet())
                {
                    ScheduledFuture task = item.getValue();
                    if (task.isCancelled() || task.isDone())
                    {
                        tasks.remove(item.getKey());
                    }
                }
                return null;
            }
        }, (milliseconds + 500), TimeUnit.MILLISECONDS);

        // shutdown service when tasks are complete
        scheduledExecutorService.shutdown();
    }
}
