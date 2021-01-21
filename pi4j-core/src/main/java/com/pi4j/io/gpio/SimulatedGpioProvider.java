package com.pi4j.io.gpio;

import java.util.Map;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SimulatedGpioProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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
 * A simulator to aid in development of RI Pi systems using a standard PC dev
 * environment.
 *
 * To use the simulator you need two environment variables:
 *
 * The standard PI4J platform statement MUST point to the simulator:
 *
 * PI4J_PLATFORM=Simulated
 *
 * A second environment variable that defines the platform that is to be
 * simulated.
 *
 * SimulatedPlatform={Real Platform's Name}
 *
 * e.g.
 *
 * SimultatedPlatform=RaspberryPi GPIO Provider
 *
 * If you don't provide a value for theSimulatedPlatform the system assumes that
 * you want to use the raspberry pi platform: RaspiGpioProvider
 *
 * @author bsutton
 *
 */
public class SimulatedGpioProvider extends GpioProviderBase implements GpioProvider {

    // We use the name of the platform that we are simulating.
    public static String NAME;

    public SimulatedGpioProvider() {
        Map<String, String> env = System.getenv();

        String config = env.get("SimulatedPlatform");

        // If no specific platform is specified we default to simulating the raspberry pi
        if (config == null) {
            NAME = RaspiGpioProvider.NAME;
            return;
        }

        NAME = config;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public void setState(Pin pin, PinState state) {
        // cache pin state
        getPinCache(pin).setState(state);

        // dispatch event
        dispatchPinDigitalStateChangeEvent(pin, state);
    }

    public void setAnalogValue(Pin pin, double value) {
        // cache pin state
        getPinCache(pin).setAnalogValue(value);

        // dispatch event
        dispatchPinAnalogValueChangeEvent(pin, value);
    }

}
