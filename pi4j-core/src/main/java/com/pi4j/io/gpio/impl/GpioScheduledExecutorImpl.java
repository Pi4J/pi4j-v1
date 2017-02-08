package com.pi4j.io.gpio.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioScheduledExecutorImpl.java
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

    private static final ConcurrentHashMap<GpioPinDigitalOutput, ArrayList<ScheduledFuture<?>>> pinTaskQueue = new ConcurrentHashMap<>();
    private static ScheduledExecutorService scheduledExecutorService;

    private synchronized static void init(GpioPinDigitalOutput pin) {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = GpioFactory.getExecutorServiceFactory().getScheduledExecutorService();
        }

        // determine if any existing future tasks are already scheduled for this pin
        if (pinTaskQueue.containsKey(pin)) {
            // if a task is found, then cancel all pending tasks immediately and remove them
            ArrayList<ScheduledFuture<?>> tasks = pinTaskQueue.get(pin);
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

    private synchronized static ScheduledFuture<?> createCleanupTask(long delay) {
        // create future task to clean up completed tasks

        @SuppressWarnings({"rawtypes", "unchecked"})
        ScheduledFuture<?> cleanupFutureTask = scheduledExecutorService.schedule(new Callable() {
            public Object call() throws Exception {
                for (Entry<GpioPinDigitalOutput, ArrayList<ScheduledFuture<?>>> item : pinTaskQueue.entrySet()) {
                    ArrayList<ScheduledFuture<?>> remainingTasks = item.getValue();

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
        }, delay, TimeUnit.MILLISECONDS);

        return cleanupFutureTask;
    }

    public synchronized static Future<?> pulse(GpioPinDigitalOutput pin, long duration, PinState pulseState) {
        return pulse(pin, duration, pulseState, null);
    }

    public synchronized static Future<?> pulse(GpioPinDigitalOutput pin, long duration, PinState pulseState, Callable<?> callback) {

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
                .schedule(new GpioPulseTaskImpl(pin, PinState.getInverseState(pulseState), callback), duration, TimeUnit.MILLISECONDS);

            // get pending tasks for the current pin
            ArrayList<ScheduledFuture<?>> tasks;
            if (!pinTaskQueue.containsKey(pin)) {
                pinTaskQueue.put(pin, new ArrayList<ScheduledFuture<?>>());
            }
            tasks = pinTaskQueue.get(pin);

            // add the new scheduled task to the tasks collection
            tasks.add(scheduledFuture);

            // create future task to clean up completed tasks
            createCleanupTask(duration + 500);
        }

        // return future task
        return scheduledFuture;
    }

    public synchronized static Future<?> blink(GpioPinDigitalOutput pin, long delay, long duration, PinState blinkState) {

        // perform the initial startup and cleanup for this pin
        init(pin);

        // we only blink for requests with a valid delay in milliseconds
        if (delay > 0) {
            // make sure pin starts in active state
            pin.setState(blinkState);

            // create future job to toggle the pin state
            ScheduledFuture<?> scheduledFutureBlinkTask = scheduledExecutorService
                .scheduleAtFixedRate(new GpioBlinkTaskImpl(pin), delay, delay, TimeUnit.MILLISECONDS);

            // get pending tasks for the current pin
            ArrayList<ScheduledFuture<?>> tasks;
            if (!pinTaskQueue.containsKey(pin)) {
                pinTaskQueue.put(pin, new ArrayList<ScheduledFuture<?>>());
            }
            tasks = pinTaskQueue.get(pin);

            // add the new scheduled task to the tasks collection
            tasks.add(scheduledFutureBlinkTask);

            // if a duration was defined, then schedule a future task to kill the blinker task
            if (duration > 0) {
                // create future job to stop blinking
                ScheduledFuture<?> scheduledFutureBlinkStopTask = scheduledExecutorService
                    .schedule(new GpioBlinkStopTaskImpl(pin,PinState.getInverseState(blinkState), scheduledFutureBlinkTask), duration, TimeUnit.MILLISECONDS);

                // add the new scheduled stop task to the tasks collection
                tasks.add(scheduledFutureBlinkStopTask);

                // create future task to clean up completed tasks
                createCleanupTask(duration + 500);
            }

            // return future task
            return scheduledFutureBlinkTask;
        }

        // no future task when a delay time has not been specified
        return null;
    }
}
