package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioScheduledExecutorImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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


import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.tasks.impl.GpioBlinkStopTaskImpl;
import com.pi4j.io.gpio.tasks.impl.GpioBlinkTaskImpl;
import com.pi4j.io.gpio.tasks.impl.GpioPulseTaskImpl;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.*;

public class GpioScheduledExecutorImpl {

    private static final ConcurrentHashMap<GpioPinDigitalOutput, CopyOnWriteArrayList<ScheduledFuture<?>>> pinTaskQueue = new ConcurrentHashMap<>();
    private static ScheduledExecutorService scheduledExecutorService;

    private synchronized static void init(GpioPinDigitalOutput pin) {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = GpioFactory.getExecutorServiceFactory().getScheduledExecutorService();
        }

        // determine if any existing future tasks are already scheduled for this pin
        if (pinTaskQueue.containsKey(pin)) {
            // if a task is found, then cancel all pending tasks immediately and remove them
            CopyOnWriteArrayList<ScheduledFuture<?>> tasks = pinTaskQueue.get(pin);
            if (tasks != null && !tasks.isEmpty()) {
                for (int index =  (tasks.size() - 1); index >= 0; index--) {
                    ScheduledFuture<?> task = tasks.get(index);
                    task.cancel(true);
                    tasks.remove(index);
                }
            }

            // if no remaining future tasks are remaining, then remove this pin from the tasks queue
            if (tasks != null && tasks.isEmpty()) {
                pinTaskQueue.remove(pin);
            }
        }
    }

    private synchronized static ScheduledFuture<?> createCleanupTask(long delay, TimeUnit timeUnit) {
        // create future task to clean up completed tasks

        @SuppressWarnings({"rawtypes", "unchecked"})
        ScheduledFuture<?> cleanupFutureTask = scheduledExecutorService.schedule(new Callable() {
            public Object call() throws Exception {
                for (Entry<GpioPinDigitalOutput, CopyOnWriteArrayList<ScheduledFuture<?>>> item : pinTaskQueue.entrySet()) {
                    CopyOnWriteArrayList<ScheduledFuture<?>> remainingTasks = item.getValue();

                    // if a task is found, then cancel all pending tasks immediately and remove them
                    if (remainingTasks != null && !remainingTasks.isEmpty()) {
                        for (int index = (remainingTasks.size() - 1); index >= 0; index--) {
                            ScheduledFuture<?> remainingTask = remainingTasks.get(index);
                            if (remainingTask.isCancelled() || remainingTask.isDone()) {
                                remainingTasks.remove(index);
                            }
                        }

                        // if no remaining future tasks are remaining, then remove this pin from the tasks queue
                        if (remainingTasks.isEmpty()) {
                            pinTaskQueue.remove(item.getKey());
                        }
                    }
                }
                return null;
            }
        }, delay, timeUnit);

        return cleanupFutureTask;
    }


    public synchronized static Future<?> pulse(GpioPinDigitalOutput pin, long duration, PinState pulseState, TimeUnit unit) {
        return pulse(pin, duration, pulseState, null, unit);
    }

    public synchronized static Future<?> pulse(GpioPinDigitalOutput pin, long duration, PinState pulseState, Callable<?> callback, TimeUnit timeUnit) {

        // create future return object
        ScheduledFuture<?> scheduledFuture = null;

        // perform the initial startup and cleanup for this pin
        init(pin);

        // we only pulse for requests with a valid duration in milliseconds
        if (duration > 0) {
            // set the active state
            pin.setState(pulseState);

            // create future job to return the pin to the low state
            scheduledFuture = scheduledExecutorService
                .schedule(new GpioPulseTaskImpl(pin, PinState.getInverseState(pulseState), callback), duration, timeUnit);

            // get pending tasks for the current pin
            CopyOnWriteArrayList<ScheduledFuture<?>> tasks;
            if (!pinTaskQueue.containsKey(pin)) {
                pinTaskQueue.put(pin, new CopyOnWriteArrayList<>());
            }
            tasks = pinTaskQueue.get(pin);

            // add the new scheduled task to the tasks collection
            tasks.add(scheduledFuture);

            // create future task to clean up completed tasks
            createCleanupTask(duration + 500, timeUnit);
        }

        // return future task
        return scheduledFuture;
    }

    public synchronized static Future<?> blink(GpioPinDigitalOutput pin, long delay, long duration, PinState blinkState, TimeUnit timeUnit) {

        // perform the initial startup and cleanup for this pin
        init(pin);

        // we only blink for requests with a valid delay in milliseconds
        if (delay > 0) {
            // make sure pin starts in active state
            pin.setState(blinkState);

            // create future job to toggle the pin state
            ScheduledFuture<?> scheduledFutureBlinkTask = scheduledExecutorService
                .scheduleAtFixedRate(new GpioBlinkTaskImpl(pin), delay, delay, timeUnit);

            // get pending tasks for the current pin
            CopyOnWriteArrayList<ScheduledFuture<?>> tasks;
            if (!pinTaskQueue.containsKey(pin)) {
                pinTaskQueue.put(pin, new CopyOnWriteArrayList<>());
            }
            tasks = pinTaskQueue.get(pin);

            // add the new scheduled task to the tasks collection
            tasks.add(scheduledFutureBlinkTask);

            // if a duration was defined, then schedule a future task to kill the blinker task
            if (duration > 0) {
                // create future job to stop blinking
                ScheduledFuture<?> scheduledFutureBlinkStopTask = scheduledExecutorService
                    .schedule(new GpioBlinkStopTaskImpl(pin,PinState.getInverseState(blinkState), scheduledFutureBlinkTask), duration, timeUnit);

                // add the new scheduled stop task to the tasks collection
                tasks.add(scheduledFutureBlinkStopTask);

                // create future task to clean up completed tasks
                createCleanupTask(duration + 500, timeUnit);
            }

            // return future task
            return scheduledFutureBlinkTask;
        }

        // no future task when a delay time has not been specified
        return null;
    }
}
