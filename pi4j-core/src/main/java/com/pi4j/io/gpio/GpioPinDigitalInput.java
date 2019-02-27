package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinDigitalInput.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
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
