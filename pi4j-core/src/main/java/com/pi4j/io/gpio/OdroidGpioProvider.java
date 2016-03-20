package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OdroidGpioProvider.java
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

import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;
import com.pi4j.platform.Platform;
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioUtil;

/**
 * Odroid-C1/C1+/XU4 {@link GpioProvider} implementation.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class OdroidGpioProvider extends WiringPiGpioProviderBase implements GpioProvider, GpioInterruptListener {

    public static final String NAME = "Odroid GPIO Provider";
    public static final int AIN_ADDRESS_OFFSET = 48;

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
            double value = com.pi4j.wiringpi.Gpio.analogRead(pin.getAddress()-AIN_ADDRESS_OFFSET);

            // cache latest analog input value
            getPinCache(pin).setAnalogValue(value);

            // return latest analog input value
            return value;
        }

        return super.getValue(pin);
    }
}
