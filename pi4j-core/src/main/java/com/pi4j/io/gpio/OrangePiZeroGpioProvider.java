package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OrangePiZeroGpioProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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

import com.pi4j.platform.Platform;
import com.pi4j.wiringpi.GpioInterruptListener;

/**
 * OrangePiZero {@link GpioProvider} implementation.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 * @author Balazs Matyas
 */
@SuppressWarnings("unused")
public class OrangePiZeroGpioProvider extends WiringPiGpioProviderBase implements GpioProvider, GpioInterruptListener {

    public static final String NAME = "OrangePiZero GPIO Provider";

    /**
     * Default Constructor
     */
    public OrangePiZeroGpioProvider() {

        // configure the Pi4J platform to use the "bananapro" implementation
        System.setProperty("pi4j.platform", Platform.ORANGEPIZERO.id());

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
        // it is not possible to export stat led
        if (pin.getAddress() == OrangePiZeroPin.STAT_LED.getAddress()) {
            // set the pin input/output mode
            setMode(pin, mode);

            return;
        }

        super.export(pin, mode, defaultState);
    }

    @Override
    public boolean isExported(Pin pin) {
        // it is not possible to export stat led
        if (pin.getAddress() == OrangePiZeroPin.STAT_LED.getAddress()) {
            return false;
        }

        return super.isExported(pin);
    }

    @Override
    public void unexport(Pin pin) {
        // it is not possible to export stat led
        if (pin.getAddress() == OrangePiZeroPin.STAT_LED.getAddress()) {
            return;
        }

        super.unexport(pin);
    }

}
