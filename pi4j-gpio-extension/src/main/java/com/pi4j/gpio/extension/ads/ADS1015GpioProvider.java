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


import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

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
    public ADS1015GpioProvider(int busNumber, int address) throws UnsupportedBusNumberException, IOException {
        // call super constructor in abstract class
        super(busNumber, address);

        // define specific chip configuration properties
        this.allPins = ADS1015Pin.ALL;
        this.conversionDelay = ADS1015_CONVERSIONDELAY;
        this.bitShift = 4; // Shift 12-bit results right 4 bits for the ADS1015
    }

    public ADS1015GpioProvider(I2CBus bus, int address) throws IOException {
        // call super constructor in abstract class
        super(bus, address);

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
