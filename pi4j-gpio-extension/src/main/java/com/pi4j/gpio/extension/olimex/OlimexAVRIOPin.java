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
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
public class OlimexAVRIOPin
{
    public static final Pin RELAY_01 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 1, "RELAY 1",
            EnumSet.of(PinMode.DIGITAL_OUTPUT));

    public static final Pin RELAY_02 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 2, "RELAY 2",
            EnumSet.of(PinMode.DIGITAL_OUTPUT));

    public static final Pin RELAY_03 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 3, "RELAY 3",
            EnumSet.of(PinMode.DIGITAL_OUTPUT));

    public static final Pin RELAY_04 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 4, "RELAY 4",
            EnumSet.of(PinMode.DIGITAL_OUTPUT));

    public static final Pin IN_01 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 5, "INPUT 1",
            EnumSet.of(PinMode.DIGITAL_INPUT));

    public static final Pin IN_02 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 6, "INPUT 2",
            EnumSet.of(PinMode.DIGITAL_INPUT));

    public static final Pin IN_03 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 7, "INPUT 3",
            EnumSet.of(PinMode.DIGITAL_INPUT));

    public static final Pin IN_04 = new PinImpl(OlimexAVRIOGpioProvider.NAME, 8, "INPUT 4",
            EnumSet.of(PinMode.DIGITAL_INPUT));
    
    public static Pin[] INPUTS = { OlimexAVRIOPin.IN_01, OlimexAVRIOPin.IN_02, OlimexAVRIOPin.IN_03, OlimexAVRIOPin.IN_04 };
    public static Pin[] RELAYS = { OlimexAVRIOPin.RELAY_01, OlimexAVRIOPin.RELAY_02, OlimexAVRIOPin.RELAY_03, OlimexAVRIOPin.RELAY_04 };    
}
