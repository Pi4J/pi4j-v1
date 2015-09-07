package com.pi4j.gpio.extension.base;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  AdcGpioProvider.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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

import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.PinListener;

import java.io.IOException;

/**
 *
 * <p>
 * This interface defines the required interfaces for an ADC GPIO provider.
 * </p>
 *
 * @author Robert Savage
 */
public interface AdcGpioProvider extends GpioProvider {

    // minimum allowed background monitoring interval in milliseconds
    int MIN_MONITOR_INTERVAL = 50;

    // default background monitoring interval in milliseconds
    int DEFAULT_MONITOR_INTERVAL = 250;

    // default amount the input value has to change before publishing a value change event
    int DEFAULT_THRESHOLD = 5;

	// this value is returned for any invalid analog input conversion value
    int INVALID_VALUE = -1;

    // ------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    // ------------------------------------------------------------------------------------------

    /**
     * Get the event threshold value for a given analog input pin.
     *
     * The event threshold value determines how much change in the
     * analog input pin's conversion value must occur before the
     * framework issues an analog input pin change event.  A threshold
     * is necessary to prevent a significant number of analog input
     * change events from getting propagated and dispatched for input
     * values that may have an expected range of drift.
     *
     * see the DEFAULT_THRESHOLD constant for the default threshold value.
     *
     * @param pin analog input pin
     * @return event threshold value for requested analog input pin
     */
    double getEventThreshold(Pin pin);

    /**
     * Get the event threshold value for a given analog input pin.
     *
     * The event threshold value determines how much change in the
     * analog input pin's conversion value must occur before the
     * framework issues an analog input pin change event.  A threshold
     * is necessary to prevent a significant number of analog input
     * change events from getting propagated and dispatched for input
     * values that may have an expected range of drift.
     *
     * see the DEFAULT_THRESHOLD constant for the default threshold value.
     *
     * @param pin analog input pin
     * @return event threshold value for requested analog input pin
     */
    double getEventThreshold(GpioPin pin);

    /**
     * Set the event threshold value for a given analog input pin.
     *
     * The event threshold value determines how much change in the
     * analog input pin's conversion value must occur before the
     * framework issues an analog input pin change event.  A threshold
     * is necessary to prevent a significant number of analog input
     * change events from getting propagated and dispatched for input
     * values that may have an expected range of drift.
     *
     * see the DEFAULT_THRESHOLD constant for the default threshold value.
     *
     * @param threshold value between 0 and 1023.
     * @param pin analog input pin (vararg, one or more inputs can be defined.)
     */
    void setEventThreshold(double threshold, Pin...pin);

    /**
     * Set the event threshold value for a given analog input pin.
     *
     * The event threshold value determines how much change in the
     * analog input pin's conversion value must occur before the
     * framework issues an analog input pin change event.  A threshold
     * is necessary to prevent a significant number of analog input
     * change events from getting propagated and dispatched for input
     * values that may have an expected range of drift.
     *
     * see the DEFAULT_THRESHOLD constant for the default threshold value.
     *
     * @param threshold value between 0 and 1023.
     * @param pin analog input pin (vararg, one or more inputs can be defined.)
     */
    void setEventThreshold(double threshold, GpioPin...pin);

    /**
     * Get the background monitoring thread's rate of data acquisition. (in milliseconds)
     *
     * The default interval is 100 milliseconds.
     * The minimum supported interval is 50 milliseconds.
     *
     * @return monitoring interval in milliseconds
     */
    int getMonitorInterval();

    /**
     * Change the background monitoring thread's rate of data acquisition. (in milliseconds)
     *
     * The default interval is 100 milliseconds.
     * The minimum supported interval is 50 milliseconds.
     *
     * @param monitorInterval
     */
    void setMonitorInterval(int monitorInterval);


    /**
     * Get the background monitoring thread's enabled state.
     *
     * @return monitoring enabled or disabled state
     */
    boolean getMonitorEnabled();

    /**
     * Set the background monitoring thread's enabled state.
     *
     * @param enabled monitoring enabled or disabled state
     */
    void setMonitorEnabled(boolean enabled);

    /**
     * This method will perform an immediate data acquisition directly to the ADC chip to get the
     * requested pin's input conversion value.
     *
     * @param pin requested input pin to acquire conversion value
     * @return conversion value for requested analog input pin
     * @throws IOException
     */
    double getImmediateValue(Pin pin) throws IOException;

}
