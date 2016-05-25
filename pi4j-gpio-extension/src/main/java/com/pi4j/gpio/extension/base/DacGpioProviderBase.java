package com.pi4j.gpio.extension.base;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  DacGpioProviderBase.java
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

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.PinListener;

import java.io.IOException;

/**
 *
 * <p>
 * This base GPIO provider defined the required interfaces and implements the base functionality for DAC
 * (digital to analog) expansion chips as native Pi4J GPIO pins.
 * </p>
 *
 * @author Robert Savage
 */
public abstract class DacGpioProviderBase extends GpioProviderBase implements DacGpioProvider {

    // used to store the pins used in this implementation
    protected Pin[] allPins = null;

    // use to store the configured shutdown value for the
    protected Double[] shutdownValues = null;

    // ------------------------------------------------------------------------------------------
    // DEFAULT CONSTRUCTOR
    // ------------------------------------------------------------------------------------------

    /**
     * Default Constructor
     *
     * @param pins the collection of all GPIO pins used with this ADC provider implementation
     */
    public DacGpioProviderBase(Pin[] pins){
        this.allPins = pins;                       // initialize pins collection

        // create collection for shutdown values for each analog output pin
        shutdownValues = new Double[pins.length];
        for(Double sv : shutdownValues){
            sv = null;
        }
    }

    // ------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    // ------------------------------------------------------------------------------------------

    /**
     * Set the current value in a percentage of the available range instead of a raw value.
     *
     * @param pin
     * @param percent percentage value between 0 and 100.
     */
    public void setPercentValue(Pin pin, Number percent){
        // validate range
        if(percent.doubleValue() <= 0){
            setValue(pin, getMinSupportedValue());
        }
        else if(percent.doubleValue() >= 100){
            setValue(pin, getMaxSupportedValue());
        }
        else{
            double value = (getMaxSupportedValue() - getMinSupportedValue()) * (percent.doubleValue()/100f);
            setValue(pin, value);
        }
    }

    /**
     * Set the current analog value as a percentage of the available range instead of a raw value.
     * Thr framework will automatically convert the percentage to a scaled number in the ADC's value range.
     *
     * @return percentage value between 0 and 100.
     */
    @Override
    public void setPercentValue(GpioPinAnalogOutput pin, Number percent){
        setPercentValue(pin.getPin(), percent);
    }

    /**
     * Set the requested analog output pin's conversion value.
     *
     * @param pin to get conversion values for
     * @param value analog output pin conversion value
     */
    @Override
    public void setValue(Pin pin, Number value) {
        super.setValue(pin, value.doubleValue());
    }

    /**
     * This method is used by the framework to shutdown the
     * DAC instance and apply any configured shutdown values to the DAC pins.
     */
    @Override
    public void shutdown() {

        // prevent reentrant invocation
        if(isShutdown())
            return;

        // perform shutdown login in base
        super.shutdown();

        try {
            // iterate over all pins and apply shutdown values if configured for the pin instance
            for(Pin pin : allPins){
                Double value = getShutdownValue(pin).doubleValue();
                if(value != null){
                    setValue(pin, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set the shutdown/terminate value that the DAC should apply to the given GPIO pin
     * when the class is destroyed/terminated.
     *
     * @param value the shutdown value to apply to the given pin(s)
     * @param pin analog output pin (vararg: one or more pins)
     */
    public void setShutdownValue(Number value, Pin ... pin){
        for(Pin p : pin){
            shutdownValues[p.getAddress()] = value.doubleValue();
        }
    }

    /**
     * Get the shutdown/terminate value that the DAC should apply to the given GPIO pin
     * when the class is destroyed/terminated.
     *
     * @param pin analog output pin
     * @return return the shutdown value if one has been defined, else NULL.
     */
    public Number getShutdownValue(Pin pin){
        return shutdownValues[pin.getAddress()];
    }


    /**
     * Set the shutdown/terminate value that the DAC should apply to the given GPIO pin
     * when the class is destroyed/terminated.
     *
     * @param value the shutdown value to apply to the given pin(s)
     * @param pin analog output pin (vararg: one or more pins)
     */
    public void setShutdownValue(Number value, GpioPinAnalogOutput ... pin){
        for(GpioPinAnalogOutput p : pin) {
            setShutdownValue(value, p.getPin());
        }
    }

    /**
     * Get the shutdown/terminate value that the DAC should apply to the given GPIO pin
     * when the class is destroyed/terminated.
     *
     * @param pin analog output pin
     * @return return the shutdown value if one has been defined, else NULL.
     */
    public Number getShutdownValue(GpioPinAnalogOutput pin){
        return getShutdownValue(pin.getPin());
    }
}
