package com.pi4j.gpio.extension.ads;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  ADS1015Pin.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;


/**
 * <p>
 * This GPIO provider implements the TI ADS1015 analog to digital converter chip as native Pi4J GPIO pins.
 * 
 * More information about the board can be found here: *
 * http://www.ti.com/lit/ds/symlink/ads1015.pdf
 * http://adafruit.com/datasheets/ads1015.pdf
 * 
 * </p>
 * 
 * <p>
 * The ADS1015 is connected via I2C connection to the Raspberry Pi and provides
 * 2 GPIO pins that can be used for analog input pins.
 * </p>
 * 
 * @author Robert Savage
 * 
 */
public class ADS1015Pin {

    public static final Pin INPUT_A0 = createAnalogInputPin(0, "ANALOG INPUT 0");
    public static final Pin INPUT_A1 = createAnalogInputPin(1, "ANALOG INPUT 1");
    public static final Pin INPUT_A2 = createAnalogInputPin(2, "ANALOG INPUT 2");
    public static final Pin INPUT_A3 = createAnalogInputPin(3, "ANALOG INPUT 3");

    public static Pin[] ALL = { ADS1015Pin.INPUT_A0, ADS1015Pin.INPUT_A1, ADS1015Pin.INPUT_A2, ADS1015Pin.INPUT_A3 };
    
    private static Pin createAnalogInputPin(int address, String name) {
        return new PinImpl(ADS1015GpioProvider.NAME, address, name, EnumSet.of(PinMode.ANALOG_INPUT));
    }       
}
