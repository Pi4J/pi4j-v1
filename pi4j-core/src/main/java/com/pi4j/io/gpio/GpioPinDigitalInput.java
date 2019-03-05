package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinDigitalInput.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
 * This is a decorator interface to describe digital input pin.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public interface GpioPinDigitalInput extends GpioPinDigital, GpioPinInput {

    /**
     * Determines if a debounce delay interval has been configured for the given pin state.
     *
     * @param state the pin state to test for debounce delay.
     * @return 'true' if the specified ping state has been configured with a debounce delay; else return 'false'.
     */
    boolean hasDebounce(PinState state);

    /**
     * Gets the configured debounce delay interval (in milliseconds) for the given pin state.
     *
     * @param state the pin state to get the configured debounce delay interval.
     * @return the debounce delay interval (in milliseconds) for the specified pin state.
     */
    int getDebounce(PinState state);

    /**
     * Sets the debounce delay interval (in milliseconds) for all pin states.
     *
     * @param debounce The debounce delay interval in milliseconds.
     */
    void setDebounce(int debounce);

    /**
     * Sets the debounce delay interval (in milliseconds) for the specified pin state.
     *
     * @param debounce The debounce delay interval in milliseconds.
     * @param state The pin states to apply the debounce delay interval to.
     */
    void setDebounce(int debounce, PinState ... state);
}
