package com.pi4j.gpio.extension.ads;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  ADS1015GpioProvider.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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


import com.pi4j.io.gpio.GpioProvider;

import java.io.IOException;

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
 * 
 * @author Robert Savage
 * 
 */
public class ADS1015GpioProvider extends ADS1x15GpioProvider implements GpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.ads.ADS1015GpioProvider";
    public static final String DESCRIPTION = "ADS1015 GPIO Provider";

    protected static final int ADS1015_MAX_IO_PINS = 4;
    
    // =======================================================================
    // ADS1015 I2C ADDRESS
    // =======================================================================
    public static final int ADS1015_ADDRESS_0x48 = 0x48; // ADDRESS 1 : 0x48 (1001000) ADR -> GND
    public static final int ADS1015_ADDRESS_0x49 = 0x49; // ADDRESS 2 : 0x49 (1001001) ADR -> VDD
    public static final int ADS1015_ADDRESS_0x4A = 0x4A; // ADDRESS 3 : 0x4A (1001010) ADR -> SDA
    public static final int ADS1015_ADDRESS_0x4B = 0x4B; // ADDRESS 4 : 0x4B (1001011) ADR -> SCL
    
    // =======================================================================
    // ADS1015 VALUE RANGES
    // =======================================================================
    public static final int ADS1015_RANGE_MAX_VALUE =  2047; //0x7FF (12 bits)
    public static final int ADS1015_RANGE_MIN_VALUE = -2048; //0x800 (12 bits)
    
    // =======================================================================
    // CONVERSION DELAY (in mS)
    // =======================================================================
    protected static final int ADS1015_CONVERSIONDELAY       = 0x01;
    
    
    // default constructor
    public ADS1015GpioProvider(int busNumber, int address) throws IOException {
        // call super constructor in abstract class
        super(busNumber, address);

        // define specific chip configuration properties
        this.allPins = ADS1015Pin.ALL;
        this.conversionDelay = ADS1015_CONVERSIONDELAY;
        this.bitShift = 4; // Shift 12-bit results right 4 bits for the ADS1015
    }
    
    @Override
    public String getName() {
        return NAME;
    }    
}
