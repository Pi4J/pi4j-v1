package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Gertboard.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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


import com.pi4j.util.NativeLibraryLoader;

/**
 * <p>
 * The Gertboard has an on-board Digital to Analog (DAC) converter and an Analog to Digital (ADC)
 * converters. These are connected via the SPI bus back to the Raspberry Pi host.
 * </p>
 * 
 * <p>
 * Each of the DAC and ADC chips has 2 channels.
 * </p>
 * 
 * <p>
 * The DAC has a resolution of 8 bits and produces an output voltage between 0 and 2.047 volts, the
 * ADC has a resolution of 10 bits and can take an input voltage between 0 and 3.3 volts.
 * </p>
 * 
 * <p>
 * Part of the wiringPi library includes code to setup and drive these chips in an easy to use
 * manner.
 * </p>
 * 
 * <p>
 * To use in a program, first you need to make sure that the 5 SPI jumpers are present on the
 * Gertboard (there are 7 in total, 5 for the SPI, 2 to connect the serial to the ATmega), the
 * photo below shows all 7 jumpers in-place.
 * </p>
 * 
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @see <a
 *      href="https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/">https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Gertboard {

    public static final int SPI_ADC_SPEED = 1000000;
    public static final int SPI_DAC_SPEED = 1000000;
    public static final int SPI_A2D = 0;
    public static final int SPI_D2A = 1;

    // private constructor 
    private Gertboard() {
        // forbid object construction 
    }
    
    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * <p> This outputs the supplied value (0-255) to the given channel (0 or 1). The output voltage is:
     * 
     * <pre>
     * vOut = value / 255 * 2.047
     * </pre>
     * 
     * or to find the value for a given voltage:
     * 
     * <pre>
     * value = vOut / 2.047 * 255
     * </pre>
     * </p>
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/">https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/</a>
     * @param chan  Analog channel to write to (0 or 1).
     * @param value The output value (0-255) supplied to the given channel (0 or 1).
     */
    public static native void gertboardAnalogWrite(int chan, int value);

    /**
     * <p> This returns a value from 0 to 1023 representing the value on the supplied channel (0 or 1).
     * To convert this to a voltage, use the following formula:
     * 
     * <pre>
     * vIn = value * 3.3 / 1023
     * </pre>
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/">https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/</a>
     * @param chan Analog channel to read from (0 or 1).
     * @return This returns a value from 0 to 1023 representing the value on the supplied channel (0
     *         or 1).
     */
    public static native int gertboardAnalogRead(int chan);

    /**
     * <p> This must be called to initialize the SPI bus to communicate with the Gertboards ADC and DAC
     * chips. If the return value is < 0 then an error occurred and errno will be set appropriately.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/">https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/</a>
     * @return If the return value is < 0 then an error occurred and errno will be set
     *         appropriately. If the return value is '0' or greater than the call was successful.
     */
    public static native int gertboardSPISetup();
}
