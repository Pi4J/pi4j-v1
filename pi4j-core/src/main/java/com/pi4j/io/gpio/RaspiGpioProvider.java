package com.pi4j.io.gpio;

import com.pi4j.wiringpi.GpioInterruptListener;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  RaspiGpioProvider.java
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

/**
 * Raspberry PI {@link GpioProvider} implementation.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class RaspiGpioProvider extends WiringPiGpioProviderBase implements GpioProvider, GpioInterruptListener {

    public static final String NAME = "RaspberryPi GPIO Provider";

    /**
     * Default Constructor
     */
    public RaspiGpioProvider() {
        // set wiringPi interface for internal use
        // we will use the (default) WiringPi pin number scheme with the wiringPi library
        this(RaspiPinNumberingScheme.DEFAULT_PIN_NUMBERING);
    }

    /**
     * Alternate Constructor allowing user to override default pin numbering scheme
     *
     * @param pinNumberingScheme
     */
    public RaspiGpioProvider(RaspiPinNumberingScheme pinNumberingScheme) {
        // set wiringPi interface for internal use
        switch(pinNumberingScheme){
            case BROADCOM_PIN_NUMBERING: {
                // we will use the raw/direct Broadcom GPIO pin number scheme with the wiringPi library
                com.pi4j.wiringpi.Gpio.wiringPiSetupGpio();
                break;
            }
            case DEFAULT_PIN_NUMBERING: {
                // we will use the WiringPi pin number scheme with the wiringPi library
                com.pi4j.wiringpi.Gpio.wiringPiSetup();
                break;
            }
            default: {
                throw new RuntimeException("Unsupported pin numbering scheme: " + pinNumberingScheme.name());
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
