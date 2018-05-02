package com.pi4j.device.piface;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PiFace.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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

import com.pi4j.component.light.LED;
import com.pi4j.component.relay.Relay;
import com.pi4j.component.switches.Switch;
import com.pi4j.device.Device;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public interface PiFace extends Device {

    public static final byte ADDRESS_0 = 0b01000000; // 0x40 [0100 0000]
    public static final byte ADDRESS_1 = 0b01000010; // 0x42 [0100 0010]
    public static final byte ADDRESS_2 = 0b01000100; // 0x44 [0100 0100]
    public static final byte ADDRESS_3 = 0b01000110; // 0x46 [0100 0110]

    public static final byte DEFAULT_ADDRESS = ADDRESS_0;


    /**
     * @return the inputPins
     */
    GpioPinDigitalInput[] getInputPins();

    /**
     * @return an inputPin
     */
    GpioPinDigitalInput getInputPin(int index);

    /**
     * @return the outputPins
     */
    GpioPinDigitalOutput[] getOutputPins();

    /**
     * @return an outputPin
     */
    GpioPinDigitalOutput getOutputPin(int index);

    /**
     * @return the relays
     */
    Relay[] getRelays();

    /**
     * @return a relay
     */
    Relay getRelay(int index);

    /**
     * @return a relay
     */
    Relay getRelay(PiFaceRelay relay);

    /**
     * @return the switches
     */
    Switch[] getSwitches();

    /**
     * @return a switch
     */
    Switch getSwitch(int index);

    /**
     * @return a switch
     */
    Switch getSwitch(PiFaceSwitch switchValue);

    /**
     * @return the leds
     */
    LED[] getLeds();

    /**
     * @return a led
     */
    LED getLed(int index);

    /**
     * @return a led
     */
    LED getLed(PiFaceLed led);
}
