package com.pi4j.concurrent;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  DefaultExecutorServiceFactory.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultExecutorServiceFactory implements ExecutorServiceFactory {

    public static int MAX_THREADS_IN_POOL = 25;
    private static List<ExecutorService> singleThreadExecutorServices = new ArrayList<>();

    // this seemingly odd pattern is the recommended way to lazy-initialize static fields in effective java.
    // The static "holder" class doesn't have it's static initializer called until it is accessed - and it's not accessed until the
    // getInternalScheduledExecutor() method is called.
    //
    // (see effective java item 71:Use lazy initialization judiciously)
    private static class ScheduledExecutorServiceHolder {
        static final ScheduledExecutorService heldExecutor = Executors.newScheduledThreadPool(MAX_THREADS_IN_POOL, getThreadFactory("pi4j-scheduled-executor-%d"));
    }
    private static ScheduledExecutorService getInternalScheduledExecutor() {
        return ScheduledExecutorServiceHolder.heldExecutor;
    }
    private static class ScheduledExecutorServiceWrapperHolder {
        static final ScheduledExecutorServiceWrapper heldWrapper = new ScheduledExecutorServiceWrapper(getInternalScheduledExecutor());
    }

    // follow a similar lazy initialization pattern for the gpio events
    private static ScheduledExecutorServiceWrapper getServiceWrapper() {
        return ScheduledExecutorServiceWrapperHolder.heldWrapper;
    }
    private static class GpioEventServiceHolder {
        static final ExecutorService cachedExecutor = new ShutdownDisabledExecutorWrapper(Executors.newCachedThreadPool(getThreadFactory("pi4j-gpio-event-executor-%d")));
    }
    private static ExecutorService getInternalGpioExecutorService() {
        return GpioEventServiceHolder.cachedExecutor;
    }

    /**
     * return an instance to the thread factory used to create new executor services
     */
    private static ThreadFactory getThreadFactory(final String nameFormat) {
        final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
        return new ThreadFactory() {
            final AtomicLong count = (nameFormat != null) ? new AtomicLong(0) : null;

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = defaultThreadFactory.newThread(runnable);
                if (nameFormat != null) {
                    thread.setName(String.format(nameFormat, count.getAndIncrement()));
                }
                return thread;
            }
        };
    }




    /**
     * return an instance to the scheduled executor service (wrapper)
     */
    public ScheduledExecutorService getScheduledExecutorService() {
        // we return the protected wrapper to prevent any consumers from 
        // being able to shutdown the scheduled executor service
        return getServiceWrapper();
    }

    @Override
    public ExecutorService getGpioEventExecutorService() {
        return getInternalGpioExecutorService();
    }

    /**
     * return a new instance of a single thread executor service
     *
     * This method is deprecated in favor of the getGpioEventExecutorService - which provides better guarantees around resource
     * management
     */
    @Override
    public ExecutorService newSingleThreadExecutorService() {
       return Executors.newSingleThreadExecutor(getThreadFactory("pi4j-single-executor-%d"));
    }

    /**
     * shutdown executor threads
     */
    public void shutdown() {
        // shutdown each single thread executor in the managed collection
        for (ExecutorService singleThreadExecutorService : singleThreadExecutorServices) {
            shutdownExecutor(singleThreadExecutorService);
        }

        // shutdown scheduled executor instance
        shutdownExecutor(getInternalScheduledExecutor());
        shutdownExecutor(getInternalGpioExecutorService());

    }

    private void shutdownExecutor(ExecutorService executor) {
        if (executor != null) {
            if (!executor.isShutdown()) {
                // this is a forceful shutdown;
                // don't wait for the scheduled tasks to complete
                executor.shutdownNow();
            }
        }
    }
}
