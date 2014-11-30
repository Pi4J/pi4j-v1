package com.pi4j.gpio.extension.mcp;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;

import java.util.EnumSet;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP3008Pin.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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
 * 
 * <p>
 * This GPIO provider implements the MCP3008 SPI GPIO expansion board as native Pi4J GPIO pins. It is a 10-bit ADC providing 8 input
 * channels. More information about the board can be found here: * http://ww1.microchip.com/downloads/en/DeviceDoc/21295d.pdf
 * </p>
 * 
 * <p>
 * The MCP3008 is connected via SPI connection to the Raspberry Pi and provides 8 GPIO pins that can be used for analog input pins.
 * </p>
 * 
 * @author pojd
 */
public class MCP3008Pin {

    public static final Pin CH0 = createAnalogInputPin(0, "ANALOG INPUT 0");
    public static final Pin CH1 = createAnalogInputPin(1, "ANALOG INPUT 1");
    public static final Pin CH2 = createAnalogInputPin(2, "ANALOG INPUT 2");
    public static final Pin CH3 = createAnalogInputPin(3, "ANALOG INPUT 3");
    public static final Pin CH4 = createAnalogInputPin(3, "ANALOG INPUT 4");
    public static final Pin CH5 = createAnalogInputPin(3, "ANALOG INPUT 5");
    public static final Pin CH6 = createAnalogInputPin(3, "ANALOG INPUT 6");
    public static final Pin CH7 = createAnalogInputPin(3, "ANALOG INPUT 7");

    public static Pin[] ALL = { MCP3008Pin.CH0,
                                MCP3008Pin.CH1,
                                MCP3008Pin.CH2,
                                MCP3008Pin.CH3,
                                MCP3008Pin.CH4,
                                MCP3008Pin.CH5,
                                MCP3008Pin.CH6,
                                MCP3008Pin.CH7 };

    private static Pin createAnalogInputPin(int channel, String name) {
        return new PinImpl(MCP3008GpioProvider.NAME, channel, name, EnumSet.of(PinMode.ANALOG_INPUT));
    }
}
