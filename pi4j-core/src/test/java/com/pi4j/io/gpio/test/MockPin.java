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
 * Copyright (C) 2012 Pi4J
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

import java.util.EnumSet;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;

public class MockPin 
{
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
