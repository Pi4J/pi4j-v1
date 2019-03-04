package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OdroidGpioProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
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

import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;
import com.pi4j.jni.AnalogInputEvent;
import com.pi4j.jni.AnalogInputListener;
import com.pi4j.platform.Platform;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioUtil;

/**
 * Odroid-C1/C1+/XU4 {@link GpioProvider} implementation.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class OdroidGpioProvider extends WiringPiGpioProviderBase implements GpioProvider, GpioInterruptListener, AnalogInputListener {

    public static final String NAME = "Odroid GPIO Provider";

    // analog input pin addresses are assigned in a virtual range starting above the
    // maximum number of physical pins supported by WiringPi
    public static final int AIN_ADDRESS_OFFSET = Gpio.NUM_PINS + 1;

    public static final int DEFAULT_ANALOG_INPUT_POLLING_RATE = 50; // milliseconds
    public static final double DEFAULT_ANALOG_INPUT_LISTENER_CHANGE_THRESHOLD = 0.0f;

    protected static int analogInputPollingRate = DEFAULT_ANALOG_INPUT_POLLING_RATE;
    protected static double analogInputListenerChangeThreshold = DEFAULT_ANALOG_INPUT_LISTENER_CHANGE_THRESHOLD;

    /**
     * Get the analog input monitor polling rate in milliseconds.
     * This is the rate at which the internal analog input monitoring thread will poll for analog input value changes
     * and dispatch analog input value change event for subscribed analog input listeners.
     *
     * (The DEFAULT polling rate is 50 milliseconds)
     *
     * @return polling rate in milliseconds
     */
    public static int getAnalogInputPollingRate(){
        return analogInputPollingRate;
    }

    /**
     * Get the analog input listener change value threshold.
     * This is the threshold delta value that the internal analog input monitoring thread must cross before
     * dispatching a new analog input value change event.  The analog input value must change in excess of this
     * defined value from the last event dispatched before dispatching a new analog input value change event.
     *
     * NOTE: This threshold value is a valuable tool to filter/limit the analog input value change events that
     * your program may receive.
     *
     * (The DEFAULT change threshold value is 0)
     *
     * @return change threshold value (delta)
     */
    public static double getAnalogInputListenerChangeThreshold(){
        return analogInputListenerChangeThreshold;
    }

    /**
     * Set the analog input monitor polling rate in milliseconds.
     * This is the rate at which the internal analog input monitoring thread will poll for analog input value changes
     * and dispatch analog input value change event for subscribed analog input listeners.
     *
     * NOTE:  Be aware that lower polling rates can impact/increase the CPU usage for your application.
     *
     * (The DEFAULT polling rate is 50 milliseconds)
     *
     * @param milliseconds polling rate in milliseconds; this value must be a positive number
     *                     else a default polling rate is used
     */
    public static void setAnalogInputPollingRate(int milliseconds){
        if(milliseconds > 0) analogInputPollingRate = milliseconds;
    }

    /**
     * Set the analog input listener change value threshold.
     * This is the threshold delta value that the internal analog input monitoring thread must cross before
     * dispatching a new analog input value change event.  The analog input value must change in excess of this
     * defined value from the last event dispatched before dispatching a new analog input value change event.
     *
     * NOTE: This threshold value is a valuable tool to filter/limit the analog input value change events that
     * your program may receive.
     *
     * (The DEFAULT change threshold value is 0)
     *
     * @param threshold change threshold value; this value must be zero or greater.  If the threshold value is set
     *                  to zero, then any change in value will dispatch a new analog input value event.
     */
    public static void setAnalogInputListenerChangeThreshold(double threshold){
        if(threshold > 0) analogInputListenerChangeThreshold = threshold;
    }

    /**
     * Default Constructor
     */
    public OdroidGpioProvider() {

        // configure the Pi4J platform to use the "odroid" implementation
        System.setProperty("pi4j.platform", Platform.ODROID.id());

        // set wiringPi interface for internal use
        // we will use the WiringPi pin number scheme with the wiringPi library
        com.pi4j.wiringpi.Gpio.wiringPiSetup();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void export(Pin pin, PinMode mode, PinState defaultState) {
        // no need to export an Odroid AIN pin
        if (mode == PinMode.ANALOG_INPUT) {

            // set the pin input/output mode
            setMode(pin, mode);

            return;
        }

        super.export(pin, mode, defaultState);
    }


    @Override
    public boolean isExported(Pin pin) {
        // Odroid AIN pins are not exported
        if (getMode(pin) == PinMode.ANALOG_INPUT) {
            return false;
        }

        return super.isExported(pin);
    }

    @Override
    public void unexport(Pin pin) {
        // no need to unexport an Odroid AIN pin
        if (pin.getSupportedPinModes().contains(PinMode.ANALOG_INPUT)) {
            return;
        }

        super.unexport(pin);
    }

    @Override
    public void setMode(Pin pin, PinMode mode) {

        // no need to export an Odroid AIN pin
        if (mode == PinMode.ANALOG_INPUT) {

            if (!pin.getSupportedPinModes().contains(mode)) {
                throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName() + "]; pin [" + pin.getName() + "] does not support this mode.");
            }

            // local pin mode cache
            pinModeCache[pin.getAddress()] = mode;

            // cache mode
            getPinCache(pin).setMode(mode);

            return;
        }

        super.setMode(pin, mode);
    }

    @Override
    public double getValue(Pin pin) {

        // the getMode() will validate the pin exists with the hasPin() function
        PinMode mode = getMode(pin);

        // handle analog input reading for Odroid boards
        if (mode == PinMode.ANALOG_INPUT) {
            // read latest analog input value from WiringPi
            // we need to re-address the pin for Odroid boards (analog_address = assigned_pin_address - AIN_ADDRESS_OFFSET)
            double value = com.pi4j.wiringpi.Gpio.analogRead(pin.getAddress() - AIN_ADDRESS_OFFSET);

            // cache latest analog input value
            getPinCache(pin).setAnalogValue(value);

            // return latest analog input value
            return value;
        }

        return super.getValue(pin);
    }

    @Override
    public void pinValueChange(AnalogInputEvent event){

        // we need to re-address the pin for Odroid boards
        int analogPinAddress = event.getPin() + AIN_ADDRESS_OFFSET;

        // iterate over the pin listeners map
        for (Pin pin : listeners.keySet()) {
            // dispatch this event to the listener
            // if a matching pin address is found
            if (pin.getAddress() == analogPinAddress) {
                dispatchPinAnalogValueChangeEvent(pin, event.getValue());
            }
        }
    }

    // internal
    @Override
    protected void updateInterruptListener(Pin pin) {

        // enable or disable single static listener with the native impl
        if (listeners.size() > 0) {

            // ------------------------
            // DIGITAL INPUT PINS
            // ------------------------

            // setup interrupt listener native thread and enable callbacks
            if (!com.pi4j.wiringpi.GpioInterrupt.hasListener(this)) {
                com.pi4j.wiringpi.GpioInterrupt.addListener(this);
            }

            // only configure WiringPi interrupts for digital input pins
            if(pinModeCache[pin.getAddress()] == PinMode.DIGITAL_INPUT) {

                // enable or disable the individual pin listener
                if(listeners.containsKey(pin) && listeners.get(pin).size() > 0) {
                    // enable interrupt listener for this pin
                    com.pi4j.wiringpi.GpioInterrupt.enablePinStateChangeCallback(pin.getAddress());
                }
                else {
                    // disable interrupt listener for this pin
                    com.pi4j.wiringpi.GpioInterrupt.disablePinStateChangeCallback(pin.getAddress());
                }
            }

            // ------------------------
            // ANALOG INPUT PINS
            // ------------------------

            // setup analog input monitor/listener native thread and enable callbacks
            if (!com.pi4j.jni.AnalogInputMonitor.hasListener(this)) {
                com.pi4j.jni.AnalogInputMonitor.addListener(this);
            }

            // configure analog monitor for analog input pins
            if(pinModeCache[pin.getAddress()] == PinMode.ANALOG_INPUT) {
                // we need to re-address the pin for Odroid boards (analog_address = assigned_pin_address - AIN_ADDRESS_OFFSET)
                int analogPinAddress = pin.getAddress() - AIN_ADDRESS_OFFSET;

                // enable or disable the individual pin listener
                if(listeners.containsKey(pin) && listeners.get(pin).size() > 0) {
                    // enable interrupt listener for this pin
                    com.pi4j.jni.AnalogInputMonitor.enablePinValueChangeCallback(analogPinAddress,
                                                                                 analogInputPollingRate,
                                                                                 analogInputListenerChangeThreshold);
                }
                else {
                    // disable interrupt listener for this pin
                    com.pi4j.jni.AnalogInputMonitor.disablePinValueChangeCallback(analogPinAddress);
                }
            }
        }
        else {
            // ------------------------
            // DIGITAL INPUT PINS
            // ------------------------

            // disable interrupt listener for this pins
            com.pi4j.wiringpi.GpioInterrupt.disablePinStateChangeCallback(pin.getAddress());

            // remove interrupt listener, disable native thread and callbacks
            if (com.pi4j.wiringpi.GpioInterrupt.hasListener(this)) {
                com.pi4j.wiringpi.GpioInterrupt.removeListener(this);
            }

            // ------------------------
            // ANALOG INPUT PINS
            // ------------------------

            // we need to re-address the pin for Odroid boards (analog_address = assigned_pin_address - AIN_ADDRESS_OFFSET)
            int analogPinAddress = pin.getAddress() - AIN_ADDRESS_OFFSET;

            // disable analog input monitor/listener for this pins
            com.pi4j.jni.AnalogInputMonitor.disablePinValueChangeCallback(analogPinAddress);

            // remove analog input monitor/listener, disable native thread and callbacks
            if (com.pi4j.jni.AnalogInputMonitor.hasListener(this)) {
                com.pi4j.jni.AnalogInputMonitor.removeListener(this);
            }
        }
    }
}
