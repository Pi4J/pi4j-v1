package com.pi4j.gpio.extension.olimex;

import java.util.EnumSet;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  OlimexAVRIOPin.java
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

/**
 * <p>
 * This GPIO provider implements the Olimex AVR-IO-M-16 expansion board as native Pi4J GPIO pins.
 * More information about the board can be found here: *
 * https://www.olimex.com/Products/AVR/Development/AVR-IO-M16/
 * </p>
 *
 * <p>
 * The Olimex AVR-IO board is connected via RS232 serial connection to the Raspberry Pi and provides
 * 4 electromechanical RELAYs and 4 opto-isolated INPUT pins.
 * </p>
 *
 * @see https://www.olimex.com/Products/AVR/Development/AVR-IO-M16/
 * @author Robert Savage
 *
 */
public class OlimexAVRIOPin {

    public static final Pin RELAY_01 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 1, "RELAY 1", EnumSet.of(PinMode.DIGITAL_OUTPUT));

    public static final Pin RELAY_02 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 2, "RELAY 2", EnumSet.of(PinMode.DIGITAL_OUTPUT));

    public static final Pin RELAY_03 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 3, "RELAY 3", EnumSet.of(PinMode.DIGITAL_OUTPUT));

    public static final Pin RELAY_04 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 4, "RELAY 4", EnumSet.of(PinMode.DIGITAL_OUTPUT));

    public static final Pin IN_01 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 5, "INPUT 1", EnumSet.of(PinMode.DIGITAL_INPUT));

    public static final Pin IN_02 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 6, "INPUT 2", EnumSet.of(PinMode.DIGITAL_INPUT));

    public static final Pin IN_03 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 7, "INPUT 3", EnumSet.of(PinMode.DIGITAL_INPUT));

    public static final Pin IN_04 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 8, "INPUT 4", EnumSet.of(PinMode.DIGITAL_INPUT));

    public static Pin[] INPUTS = { OlimexAVRIOPin.IN_01, OlimexAVRIOPin.IN_02, OlimexAVRIOPin.IN_03, OlimexAVRIOPin.IN_04 };
    public static Pin[] RELAYS = { OlimexAVRIOPin.RELAY_01, OlimexAVRIOPin.RELAY_02, OlimexAVRIOPin.RELAY_03, OlimexAVRIOPin.RELAY_04 };
}
