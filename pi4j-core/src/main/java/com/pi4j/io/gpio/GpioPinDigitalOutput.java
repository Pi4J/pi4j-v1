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
