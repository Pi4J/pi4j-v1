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
 * Copyright (C) 2012 - 2013 Pi4J
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class DefaultExecutorServiceFactory implements ExecutorServiceFactory {
    
    public static int MAX_THREADS_IN_POOL = 25;
    private static List<ExecutorService> singleThreadExecutorServices  = new ArrayList<ExecutorService>();
    private static ScheduledExecutorService scheduledExecutorService = null;
    private static ScheduledExecutorServiceWrapper executorServiceWrapper = null;
    
    /**
     * return an instance to the thread factory used to create new executor services
     */
    private ThreadFactory getThreadFactory(){
        return Executors.defaultThreadFactory();
    }
    
    /**
     * return an instance to the scheduled executor service (wrapper)
     */
    public ScheduledExecutorService getScheduledExecutorService(){
        if(scheduledExecutorService == null){
            scheduledExecutorService = Executors.newScheduledThreadPool(MAX_THREADS_IN_POOL, getThreadFactory());
            executorServiceWrapper = new ScheduledExecutorServiceWrapper(scheduledExecutorService);
        }
        
        // we return the protected wrapper to prevent any consumers from 
        // being able to shutdown the scheduled executor service
        return executorServiceWrapper;
    }
    
    /**
     * return a new instance of a single thread executor service
     */
    public ExecutorService newSingleThreadExecutorService(){
        
        // create new single thread executor
        ExecutorService singleThreadExecutorService = Executors.newSingleThreadExecutor(getThreadFactory());

        // add new instance to managed collection
        singleThreadExecutorServices.add(singleThreadExecutorService);
        
        // return the new instance
        return singleThreadExecutorService;
    }
    
    /**
     * shutdown executor threads
     */
    public void shutdown(){
        
        // shutdown each single thread executor in the managed collection
        for(ExecutorService singleThreadExecutorService : singleThreadExecutorServices){
            if(singleThreadExecutorService != null){
                if(!singleThreadExecutorService.isShutdown()){
                    // this is a forceful shutdown; 
                    // don't wait for the active tasks to complete
                    singleThreadExecutorService.shutdownNow();
                }
            }
        }
        
        // shutdown scheduled executor instance 
        if(scheduledExecutorService != null){
            if(!scheduledExecutorService.isShutdown()){
                // this is a forceful shutdown; 
                // don't wait for the scheduled tasks to complete
                scheduledExecutorService.shutdownNow();
            }
        }
    }
}
