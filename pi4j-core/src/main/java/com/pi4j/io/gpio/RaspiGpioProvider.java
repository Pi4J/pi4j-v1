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
 * Copyright (C) 2012 - 2020 Pi4J
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
