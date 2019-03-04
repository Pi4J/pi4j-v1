package com.pi4j.io.gpio;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinDigitalOutput.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

/**
 * Gpio digital output pin interface.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public interface GpioPinDigitalOutput extends GpioPinDigital, GpioPinOutput {

    void high();
    void low();
    void toggle();
    Future<?> blink(long delay);
    Future<?> blink(long delay, TimeUnit timeUnit);
    Future<?> blink(long delay, PinState blinkState);
    Future<?> blink(long delay, PinState blinkState, TimeUnit timeUnit);
    Future<?> blink(long delay, long duration);
    Future<?> blink(long delay, long duration, TimeUnit timeUnit);
    Future<?> blink(long delay, long duration, PinState blinkState);
    Future<?> blink(long delay, long duration, PinState blinkState, TimeUnit timeUnit);
    Future<?> pulse(long duration);
    Future<?> pulse(long duration, TimeUnit timeUnit);
    Future<?> pulse(long duration, Callable<Void> callback);
    Future<?> pulse(long duration, Callable<Void> callback, TimeUnit timeUnit);
    Future<?> pulse(long duration, boolean blocking);
    Future<?> pulse(long duration, boolean blocking, TimeUnit timeUnit);
    Future<?> pulse(long duration, boolean blocking, Callable<Void> callback);
    Future<?> pulse(long duration, boolean blocking, Callable<Void> callback, TimeUnit timeUnit);
    Future<?> pulse(long duration, PinState pulseState);
    Future<?> pulse(long duration, PinState pulseState, TimeUnit timeUnit);
    Future<?> pulse(long duration, PinState pulseState, Callable<Void> callback);
    Future<?> pulse(long duration, PinState pulseState, Callable<Void> callback, TimeUnit timeUnit);
    Future<?> pulse(long duration, PinState pulseState, boolean blocking);
    Future<?> pulse(long duration, PinState pulseState, boolean blocking, TimeUnit timeUnit);
    Future<?> pulse(long duration, PinState pulseState, boolean blocking, Callable<Void> callback);
    Future<?> pulse(long duration, PinState pulseState, boolean blocking, Callable<Void> callback, TimeUnit timeUnit);
    void setState(PinState state);
    void setState(boolean state);

}
