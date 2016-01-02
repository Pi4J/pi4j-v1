package com.pi4j.gpio.extension.base;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  DacGpioProvider.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;

import java.io.IOException;

/**
 *
 * <p>
 * This interface defines the required interfaces for a DAC GPIO provider.
 * (digital to analog converter chip)
 * </p>
 *
 * @author Robert Savage
 */
public interface DacGpioProvider extends GpioProvider {


    // ------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    // ------------------------------------------------------------------------------------------

    /**
     * Set the current analog value as a percentage of the available range instead of a raw value.
     * Thr framework will automatically convert the percentage to a scaled number in the ADC's value range.
     *
     * @return percentage value between 0 and 100.
     */
    void setPercentValue(Pin pin, Number percent);

    /**
     * Set the current analog value as a percentage of the available range instead of a raw value.
     * Thr framework will automatically convert the percentage to a scaled number in the ADC's value range.
     *
     * @return percentage value between 0 and 100.
     */
    void setPercentValue(GpioPinAnalogOutput pin, Number percent);

    /**
     * Set the requested analog output pin's conversion value.
     *
     * @param pin to get conversion values for
     * @param value analog output pin conversion value
     */
    void setValue(Pin pin, Number value);

    /**
     * Get the minimum supported analog value for the ADC implementation.
     *
     * @return Returns the minimum supported analog value.
     */
    double getMinSupportedValue();

    /**
     * Get the maximum supported analog value for the ADC implementation.
     *
     * (For example, a 10 bit ADC's maximum value is 1023 and
     *  a 12-bit ADC's maximum value is 4095.
     *
     * @return Returns the maximum supported analog value.
     */
    double getMaxSupportedValue();

    /**
     * Set the shutdown/terminate value that the DAC should apply to the given GPIO pin
     * when the class is destroyed/terminated.
     *
     * @param value the shutdown value to apply to the given pin(s)
     * @param pin analog output pin (vararg: one or more pins)
     */
    void setShutdownValue(Number value, GpioPinAnalogOutput... pin);

    /**
     * Get the shutdown/terminate value that the DAC should apply to the given GPIO pin
     * when the class is destroyed/terminated.
     *
     * @param pin analog output pin
     * @return return the shutdown value if one has been defined, else NULL.
     */
    Number getShutdownValue(GpioPinAnalogOutput pin);

    /**
     * Set the shutdown/terminate value that the DAC should apply to the given GPIO pin
     * when the class is destroyed/terminated.
     *
     * @param value the shutdown value to apply to the given pin(s)
     * @param pin analog output pin (vararg: one or more pins)
     */
    void setShutdownValue(Number value, Pin... pin);

    /**
     * Get the shutdown/terminate value that the DAC should apply to the given GPIO pin
     * when the class is destroyed/terminated.
     *
     * @param pin analog output pin
     * @return return the shutdown value if one has been defined, else NULL.
     */
    Number getShutdownValue(Pin pin);
}
