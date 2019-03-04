package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  PinProvider.java
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


import com.pi4j.io.gpio.impl.PinImpl;

import java.util.*;

/**
 * Pi4J pin definitions
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public abstract class PinProvider {

    protected static final Map<String, Pin> pins = new HashMap<>();

    protected static Pin createDigitalPin(String providerName, int address, String name) {
        return createDigitalPin(providerName, address, name, EnumSet.allOf(PinEdge.class));
    }

    protected static Pin createDigitalPin(String providerName, int address, String name, EnumSet<PinPullResistance> resistance, EnumSet<PinEdge> edges) {
        return createPin(providerName, address, name,
                EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, PinMode.SOFT_PWM_OUTPUT),
                resistance,
                edges);
    }

    protected static Pin createDigitalPin(String providerName, int address, String name, EnumSet<PinEdge> edges) {
        return createPin(providerName, address, name,
                EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, PinMode.SOFT_PWM_OUTPUT),
                PinPullResistance.all(),
                edges);
    }

    protected static Pin createDigitalAndPwmPin(String providerName, int address, String name, EnumSet<PinEdge> edges) {
        return createPin(providerName, address, name,
                         EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, PinMode.PWM_OUTPUT),
                         PinPullResistance.all(),
                         edges);
    }

    protected static Pin createDigitalAndPwmPin(String providerName, int address, String name) {
        return createDigitalAndPwmPin(providerName, address, name, EnumSet.allOf(PinEdge.class));
    }

    protected static Pin createAnalogInputPin(String providerName, int address, String name) {
        return createPin(providerName, address, name, EnumSet.of(PinMode.ANALOG_INPUT));
    }

    protected static Pin createPin(String providerName, int address, String name, EnumSet<PinMode> modes) {
        Pin pin = new PinImpl(providerName, address, name, modes);
        pins.put(name, pin);
        return pin;
    }

    protected static Pin createPin(String providerName, int address, String name, EnumSet<PinMode> modes,
                                   EnumSet<PinPullResistance> resistance, EnumSet<PinEdge> edges) {
        Pin pin = new PinImpl(providerName, address, name, modes, resistance, edges);
        pins.put(name, pin);
        return pin;
    }

    public static Pin getPinByName(String name) {
    	return pins.get(name);
    }

    public static Pin getPinByAddress(int address) {
        for(Pin pin : pins.values()){
            if(pin.getAddress() == address){
                return pin;
            }
        }
        return null;
    }

    /**
     * Get all pin instances from this provider.
     * @return all pin instances support by this provider
     */
    public static Pin[] allPins() {
        return pins.values().toArray(new Pin[0]);
    }

    /**
     * Get all pin instances from this provider that support one of the provided pin modes.
     * @param mode one or more pin modes that you wish to include in the result set
     * @return pin instances that support the provided pin modes
     */
    public static Pin[] allPins(PinMode ... mode) {
        List<Pin> results = new ArrayList<>();
        for(Pin p : pins.values()){
            EnumSet<PinMode> supported_modes = p.getSupportedPinModes();
            for(PinMode m : mode){
                if(supported_modes.contains(m)){
                    results.add(p);
                    continue;
                }
            }
        }
        return results.toArray(new Pin[0]);
    }
}
