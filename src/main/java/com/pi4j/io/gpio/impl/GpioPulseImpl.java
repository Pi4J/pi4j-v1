package com.pi4j.io.gpio.impl;

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
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    public synchronized static void execute(GpioPin pin, long milliseconds)
    {        
        // first determine if an existing pulse job is already scheduled for this pin
        if(tasks.containsKey(pin))
        {
            // if a job is found, then cancel it immediately and remove the job
            ScheduledFuture<?> previouslyScheduled= tasks.get(pin);
            previouslyScheduled.cancel(true);            
            tasks.remove(pin);
        }

        // set the high state
        pin.high();

        // create future job to return the pin to the low state
        ScheduledFuture<?> scheduledFuture =        
            scheduledExecutorService.schedule(new GpioPulseOffImpl(pin), milliseconds, TimeUnit.MILLISECONDS);
        
        // add the new scheduled job to the tasks map
        tasks.put(pin, scheduledFuture);
        
        // create future job to return the pin to the low state
        @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
        ScheduledFuture<?> cleanupFuture =        
                scheduledExecutorService.schedule(new Callable() 
                {
                    public Object call() throws Exception 
                    {
                        for(Entry<GpioPin, ScheduledFuture> item : tasks.entrySet())
                        {   
                            ScheduledFuture task = item.getValue(); 
                            if(task.isCancelled() || task.isDone())
                                tasks.remove(item.getKey());
                        }
                        return null;
                    }
                },
                (milliseconds  + 500),
                TimeUnit.MILLISECONDS);   
    }     
}
