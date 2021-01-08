package com.pi4j.component.button;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  ButtonBase.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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


import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;
import com.pi4j.io.gpio.GpioFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class ButtonBase extends ObserveableComponentBase implements Button {

    @Override
    public boolean isPressed() {
        return (getState() == ButtonState.PRESSED);
    }

    @Override
    public boolean isReleased() {
        return (getState() == ButtonState.RELEASED);
    }

    @Override
    public abstract ButtonState getState();

    @Override
    public boolean isState(ButtonState state) {
        return getState().equals(state);
    }

    @Override
    public void addListener(ButtonStateChangeListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(ButtonStateChangeListener... listener) {
        super.removeListener(listener);
    }

    @Override
    public void addListener(ButtonPressedListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(ButtonPressedListener... listener) {
        super.removeListener(listener);
    }

    @Override
    public void addListener(ButtonReleasedListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(ButtonReleasedListener... listener) {
        super.removeListener(listener);
    }

    @Override
    public void addListener(long delay, ButtonHoldListener... listener) {
        for(ButtonHoldListener l : listener) {
            super.addListener(new ButtonHoldListenerWrapper(delay, l));
        }
    }

    @Override
    public synchronized void removeListener(ButtonHoldListener ... listener) {
        List<ComponentListener> listeners_copy = new ArrayList<>(super.listeners);
        for(ButtonHoldListener bhl : listener) {
            for (ComponentListener cl : listeners_copy){
                if(cl instanceof ButtonHoldListenerWrapper){
                    if(((ButtonHoldListenerWrapper)cl).listener == bhl){
                        super.removeListener(cl);
                    }
                }
            }
        }
    }

    final ScheduledExecutorService executor = GpioFactory.getExecutorServiceFactory().getScheduledExecutorService();
    final List<ScheduledFuture<?>> holdEventFutures = new ArrayList<>();

    protected synchronized void notifyListeners(final ButtonStateChangeEvent event) {

        // cancel any pending hold event futures
        if(!holdEventFutures.isEmpty()) {
            for (ScheduledFuture<?> future : holdEventFutures) {
                future.cancel(false);
            }
            holdEventFutures.clear();
        }

        // iterate over all the subscribed listeners
        for(ComponentListener listener : super.listeners) {

            if(listener instanceof ButtonStateChangeListener) {
                ((ButtonStateChangeListener) listener).onStateChange((ButtonStateChangeEvent) event);
            }
            else if(event.isPressed() && listener instanceof ButtonPressedListener) {
                ((ButtonPressedListener) listener).onButtonPressed(event);
            }
            else if(event.isReleased() && listener instanceof ButtonReleasedListener) {
                ((ButtonReleasedListener) listener).onButtonReleased(event);
            }

            if(event.isPressed() && listener instanceof ButtonHoldListenerWrapper) {
                final ButtonHoldListenerWrapper wrapper = (ButtonHoldListenerWrapper)listener;

                // register a new hold event future
                ScheduledFuture<?> scheduledFuture = executor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        wrapper.listener.onButtonHold(event);
                    }
                }, wrapper.delay, TimeUnit.MILLISECONDS);

                holdEventFutures.add(scheduledFuture);
            }
        }
    }

    private class ButtonHoldListenerWrapper implements ButtonListener{
        public final ButtonHoldListener listener;
        public final long delay;

        public ButtonHoldListenerWrapper(long delay, ButtonHoldListener listener){
            this.listener = listener;
            this.delay = delay;
        }
    }
}
