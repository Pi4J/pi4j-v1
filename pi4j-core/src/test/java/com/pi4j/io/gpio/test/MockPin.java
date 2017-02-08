package com.pi4j.io.gpio.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  MockPin.java
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

import java.util.EnumSet;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;

public class MockPin {

    public static final Pin DIGITAL_BIDIRECTIONAL_PIN = new PinImpl(MockGpioProvider.NAME, 0, "GPIO-0",
                                                            EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT),
                                                            PinPullResistance.all());

    public static final Pin DIGITAL_INPUT_PIN = new PinImpl(MockGpioProvider.NAME, 1, "GPIO-1",
                                                      EnumSet.of(PinMode.DIGITAL_INPUT),
                                                      PinPullResistance.all());

    public static final Pin DIGITAL_OUTPUT_PIN = new PinImpl(MockGpioProvider.NAME, 2, "GPIO-2",
                                                            EnumSet.of(PinMode.DIGITAL_OUTPUT));

    public static final Pin PWM_OUTPUT_PIN = new PinImpl(MockGpioProvider.NAME, 3, "GPIO-3",
                                                         EnumSet.of(PinMode.PWM_OUTPUT));

    public static final Pin ANALOG_BIDIRECTIONAL_PIN  = new PinImpl(MockGpioProvider.NAME, 4, "GPIO-4",
                                                                    EnumSet.of(PinMode.ANALOG_INPUT,
                                                                               PinMode.ANALOG_OUTPUT));
    public static final Pin ANALOG_INPUT_PIN = new PinImpl(MockGpioProvider.NAME, 5, "GPIO-5",
                                                          EnumSet.of(PinMode.ANALOG_INPUT));

    public static final Pin ANALOG_OUTPUT_PIN = new PinImpl(MockGpioProvider.NAME, 6, "GPIO-6",
                                                            EnumSet.of(PinMode.ANALOG_OUTPUT));
}
