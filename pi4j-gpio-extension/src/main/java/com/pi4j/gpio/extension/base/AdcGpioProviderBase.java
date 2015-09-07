package com.pi4j.gpio.extension.base;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  AdcGpioProviderBase.java
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
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;

import java.io.IOException;

/**
 *
 * <p>
 * This base GPIO provider defined the required interfaces and implements the base functionality for ADC
 * (analog to digital) expansion chips as native Pi4J GPIO pins.
 * </p>
 *
 * @author Robert Savage
 */
public abstract class AdcGpioProviderBase extends GpioProviderBase implements AdcGpioProvider {

    // background ADC analog input value monitor
    protected ADCMonitor monitor = null;

    // the delay time required between analog input conversions for each input
    protected int conversionDelay = 0;

    // this value defines the sleep time between value reads by the event monitoring thread (in milliseconds)
    protected int monitorInterval = 100;

    // used to store the pins used in this implementation
    protected Pin[] allPins = null;

    // the threshold used to determine if a significant value warrants an event to be raised
    protected double[] threshold = null;


    // ------------------------------------------------------------------------------------------
    // DEFAULT CONSTRUCTOR
    // ------------------------------------------------------------------------------------------

    /**
     * Default Constructor
     *
     * @param pins the collection of all GPIO pins used with this ADC provider implementation
     */
    public AdcGpioProviderBase(Pin[] pins){
        this.allPins = pins;                       // initialize pins collection
        this.threshold = new double[pins.length];  // initialize pin thresholds collection

        // set default thresholds
        for(double item : threshold) { item = DEFAULT_THRESHOLD; }
    }

    // ------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    // ------------------------------------------------------------------------------------------

    /**
     * Get the requested analog input pin's conversion value.
     *
     * If you have the background monitoring thread enabled, then
     * this function will return the last cached value.  If you have the
     * background monitoring thread disabled, then this function will
     * will perform an immediate data acquisition directly to the ADC chip
     * to get the requested pin's input conversion value. (via getImmediateValue())
     *
     * @param pin to get conversion values for
     * @return analog input pin conversion value (10-bit: 0 to 1023)
     */
	@Override
	public double getValue(Pin pin) {
        // if we are not actively monitoring the ADC input values,
        // then interrogate the ADC chip and return the acquired input conversion value
        if(monitor == null) {
            // do not return, only let parent handle whether this pin is OK
            super.getValue(pin);
            try {
                return getImmediateValue(pin);
            } catch (IOException e) {
                return INVALID_VALUE;
            }
        }
        else{
            // if we are actively monitoring the ADC input values,
            // the simply return the last cached input value
            return super.getValue(pin);
        }
    }

    /**
     * Get the current value in a percentage of the available range instead of a raw value.
     *
     * @return percentage value between 0 and 100.
     */
    public float getPercentValue(Pin pin){
        double value = getValue(pin);
        if(value > INVALID_VALUE) {
            return (float) (value / (getMaxSupportedValue() - getMinSupportedValue())) * 100f;
        }
        return INVALID_VALUE;
    }

    /**
     * Get the current value in a percentage of the available range instead of a raw value.
     *
     * @return percentage value between 0 and 100.
     */
    public float getPercentValue(GpioPinAnalogInput pin){
        return getPercentValue(pin.getPin());
    }

    /**
     * This method will perform an immediate data acquisition directly to the ADC chip to get the
     * requested pin's input conversion value.
     *
     * @param pin requested input pin to acquire conversion value
     * @return conversion value for requested analog input pin
     * @throws IOException
     */
    @Override
    public double getImmediateValue(GpioPinAnalogInput pin) throws IOException{
        return getImmediateValue(pin.getPin());
    }

    /**
     * This method is used by the framework to shutdown the
     * background monitoring thread if needed when the program exits.
     */
    @Override
    public void shutdown() {

        // prevent reentrant invocation
        if(isShutdown())
            return;

        // perform shutdown login in base
        super.shutdown();

        try {
            // if a monitor is running, then shut it down now
            if (monitor != null) {
                // shutdown monitoring thread
                monitor.shutdown();
                monitor = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
    @Override
    public double getEventThreshold(Pin pin){
        return threshold[pin.getAddress()];
    }

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
    @Override
    public double getEventThreshold(GpioPinAnalogInput pin){
        return getEventThreshold(pin.getPin());
    }

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
     * @param threshold threshold value for requested analog input pin
     * @param pin analog input pin (vararg, one or more inputs can be defined.)
     */
    @Override
    public void setEventThreshold(double threshold, Pin...pin){
        for(Pin p : pin){
            this.threshold[p.getAddress()] = threshold;
        }
    }

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
     * @param threshold threshold value for requested analog input pin.
     * @param pin analog input pin (vararg, one or more inputs can be defined.)
     */
    @Override
    public void setEventThreshold(double threshold, GpioPinAnalogInput...pin){
        for(GpioPin p : pin){
            setEventThreshold(threshold, p.getPin());
        }
    }

    /**
     * Get the background monitoring thread's rate of data acquisition. (in milliseconds)
     *
     * The default interval is 100 milliseconds.
     * The minimum supported interval is 50 milliseconds.
     *
     * @return monitoring interval in milliseconds
     */
    @Override
    public int getMonitorInterval(){
        return monitorInterval;
    }

    /**
     * Change the background monitoring thread's rate of data acquisition. (in milliseconds)
     *
     * The default interval is 100 milliseconds.
     * The minimum supported interval is 50 milliseconds.
     *
     * @param monitorInterval
     */
    @Override
    public void setMonitorInterval(int monitorInterval){
        this.monitorInterval = monitorInterval;

        // enforce a minimum interval threshold.
        if(monitorInterval < MIN_MONITOR_INTERVAL)
            monitorInterval = MIN_MONITOR_INTERVAL;
    }

    /**
     * Get the background monitoring thread's enabled state.
     *
     * @return monitoring enabled or disabled state
     */
    @Override
    public boolean getMonitorEnabled() {
        return (monitor != null);
    }

    /**
     * Set the background monitoring thread's enabled state.
     *
     * @param enabled monitoring enabled or disabled state
     */
    @Override
    public void setMonitorEnabled(boolean enabled) {
        if(enabled) {
            // create and start background monitor
            if (monitor == null) {
                monitor = new AdcGpioProviderBase.ADCMonitor();
                monitor.start();
            }
        }
        else{
            try {
                // if a monitor is running, then shut it down now
                if (monitor != null) {
                    // shutdown monitoring thread
                    monitor.shutdown();
                    monitor = null;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method will perform an immediate data acquisition directly to the ADC chip to get the
     * requested pin's input conversion value.
     *
     * @param pin requested input pin to acquire conversion value
     * @return conversion value for requested analog input pin
     * @throws IOException
     */
    @Override
    public abstract double getImmediateValue(Pin pin) throws IOException;


    /**
     * This class/thread is used to to actively monitor ADC input changes
     *
     * @author Robert Savage
     *
     */
    private class ADCMonitor extends Thread {

        private boolean shuttingDown = false;

        public void shutdown() {
            shuttingDown = true;
        }

        public void run() {
            while (!shuttingDown) {
                try {
                    // determine if there is a pin state difference
                    if(allPins != null && allPins.length > 0){
                        for (Pin pin : allPins) {

                            try{
                                // get current cached value
                                double oldValue = getPinCache(pin).getAnalogValue();

                                // get actual value from ADC chip
                                double newValue = getImmediateValue(pin);

                                // no need to continue if we received an invalid value from the ADC chip.
                                if(newValue <= INVALID_VALUE){ break; }

                                // check to see if the pin value exceeds the event threshold
                                if(threshold == null || Math.abs(oldValue - newValue) > threshold[pin.getAddress()]){

                                    // cache new analog input conversion value
                                    getPinCache(pin).setAnalogValue(newValue);

                                    // only dispatch events for analog input pins
                                    if (getMode(pin) == PinMode.ANALOG_INPUT) {
                                        dispatchPinChangeEvent(pin.getAddress(), newValue);
                                    }
                                }

                                // Wait for the conversion to complete
                                try{
                                    if(conversionDelay > 0){
                                        Thread.sleep(conversionDelay);
                                    }
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            catch(IOException ex){
                                ex.printStackTrace();
                            }
                        }
                    }

                    // ... lets take a short breather ...
                    Thread.currentThread();
                    Thread.sleep(monitorInterval);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void dispatchPinChangeEvent(int pinAddress, double value) {
            // iterate over the pin listeners map
            for (Pin pin : listeners.keySet()) {
                // dispatch this event to the listener
                // if a matching pin address is found
                if (pin.getAddress() == pinAddress) {
                    // dispatch this event to all listener handlers
                    for (PinListener listener : listeners.get(pin)) {
                        listener.handlePinEvent(new PinAnalogValueChangeEvent(this, pin, value));
                    }
                }
            }
        }
    }
}
