package com.pi4j.gpio.extension.ads;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  ADS1115Pin.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;


/**
 * <p>
 * This GPIO provider implements the TI ADS1115 analog to digital converter chip as native Pi4J GPIO pins.
 * 
 * More information about the board can be found here: *
 * http://www.ti.com/lit/ds/symlink/ads1115.pdf
 * http://adafruit.com/datasheets/ads1115.pdf
 * 
 * </p>
 * 
 * <p>
 * The ADS1115 is connected via I2C connection to the Raspberry Pi and provides
 * 2 GPIO pins that can be used for analog input pins.
 * </p>
 * 
 * @author Robert Savage
 * 
 */
public class ADS1115Pin {

    public static final Pin INPUT_A0 = createAnalogInputPin(0, "ANALOG INPUT 0");
    public static final Pin INPUT_A1 = createAnalogInputPin(1, "ANALOG INPUT 1");
    public static final Pin INPUT_A2 = createAnalogInputPin(2, "ANALOG INPUT 2");
    public static final Pin INPUT_A3 = createAnalogInputPin(3, "ANALOG INPUT 3");

    public static Pin[] ALL = { ADS1115Pin.INPUT_A0, ADS1115Pin.INPUT_A1, ADS1115Pin.INPUT_A2, ADS1115Pin.INPUT_A3 };
    
    private static Pin createAnalogInputPin(int address, String name) {
        return new PinImpl(ADS1115GpioProvider.NAME, address, name, EnumSet.of(PinMode.ANALOG_INPUT));
    }       
}
