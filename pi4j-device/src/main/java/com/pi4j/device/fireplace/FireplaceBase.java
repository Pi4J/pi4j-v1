package com.pi4j.device.fireplace;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  FireplaceBase.java  
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


import com.pi4j.device.DeviceListener;
import com.pi4j.device.ObserveableDeviceBase;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class FireplaceBase extends ObserveableDeviceBase implements Fireplace {

    protected long timeoutDelay = 0;
    protected Future timeoutTask = null;
    protected TimeUnit timeoutUnit = TimeUnit.MINUTES;
    protected ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public FireplaceBase(){
        // add a state change listener so that the timeout can be canceled if any state is changed
        addListener(new FireplaceStateChangeListener() {
            @Override
            public void onStateChange(FireplaceStateChangeEvent event) {
                cancelTimeoutTask();
            }
        });
    }

    @Override
    public boolean isOn() {
        return (getState() == FireplaceState.ON);
    }

    @Override
    public boolean isOff() {
        return (getState() == FireplaceState.OFF);
    }

    @Override
    public abstract FireplaceState getState();

    @Override
    public boolean isState(FireplaceState state) {
        return getState().equals(state);
    }

    @Override
    public void addListener(FireplaceStateChangeListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(FireplaceStateChangeListener... listener) {
        super.removeListener(listener);
    }

    @Override
    public void addListener(FireplacePilotLightListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(FireplacePilotLightListener... listener) {
        super.removeListener(listener);
    }

    @Override
    public void addListener(FireplaceTimeoutListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(FireplaceTimeoutListener... listener) {
        super.removeListener(listener);
    }

    @Override
    public void on() throws FireplacePilotLightException {
        // turn on the fireplace
        setState(FireplaceState.ON);
    }

    @Override
    public void off() {
        try {
            setState(FireplaceState.OFF);
        } catch (FireplacePilotLightException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void on(long timeoutDelay, TimeUnit timeoutUnit) throws FireplacePilotLightException{
        on();
        setTimeout(timeoutDelay, timeoutUnit);
    }

    @Override
    public void cancelTimeout(){
        // cancel any pending task
        cancelTimeoutTask();
    }

    @Override
    public void setTimeout(long delay, TimeUnit unit){

        // validate that the fireplace is ON
        if(isOff()){
            throw new RuntimeException("Unable to set timeout when fireplace is off.");
        }

        // set timeout properties
        this.timeoutDelay = delay;
        this.timeoutUnit = unit;

        // cancel any pending task
        cancelTimeoutTask();

        // only create timeout task if the delay is greater than zero
        if(this.timeoutDelay > 0) {
            // create a scheduled executor future task to turn off the fireplace
            timeoutTask = executor.schedule(new Runnable() {
                @Override
                public void run() {

                    // notify timeout listeners
                    FireplaceTimeoutEvent event = new FireplaceTimeoutEvent(FireplaceBase.this);
                    notifyListeners(event);

                    // an event listener could override the impl and handle the timeout behavior
                    if(!event.isHandled()) {
                        off(); // turn off fireplace
                    }
                }
            }, this.timeoutDelay, this.timeoutUnit);
        }
    }

    @Override
    public long getTimeoutDelay() { return timeoutDelay; }

    @Override
    public TimeUnit getTimeoutUnit() { return timeoutUnit; }


    protected synchronized void notifyListeners(FireplaceStateChangeEvent event) {
        for(DeviceListener listener : super.listeners) {
            if(listener instanceof FireplaceStateChangeListener) {
                ((FireplaceStateChangeListener) listener).onStateChange(event);
            }
        }
    }

    protected synchronized void notifyListeners(FireplacePilotLightEvent event) {
        for(DeviceListener listener : super.listeners) {
            if(listener instanceof FireplacePilotLightListener) {
                ((FireplacePilotLightListener) listener).onChange(event);
            }
        }
    }

    protected synchronized void notifyListeners(FireplaceTimeoutEvent event) {
        for(DeviceListener listener : super.listeners) {
            if(listener instanceof FireplaceTimeoutListener) {
                ((FireplaceTimeoutListener) listener).onTimeout(event);
            }
        }
    }

    protected void cancelTimeoutTask() {
        if (timeoutTask != null) {
            if (!timeoutTask.isDone() && !timeoutTask.isCancelled()) {
                timeoutTask.cancel(true); // cancel timer task
            }
            timeoutTask = null; // dispose object
        }
    }

    @Override
    public void shutdown(){
        cancelTimeoutTask();  // cancel any pending task
        off();                // turn off the fireplace
        executor.shutdown();  // shutdown the executor thread
    }

}
