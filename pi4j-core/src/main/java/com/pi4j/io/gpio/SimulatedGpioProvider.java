package com.pi4j.io.gpio;

import java.util.Map;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  RCMPin.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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
 * SimulatedPlatform=<Real Platform's Name>
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

        // If no specific platform is specified we default to simulating the raspberry
        // pi.
        if (config == null)
            NAME = RaspiGpioProvider.NAME;

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
