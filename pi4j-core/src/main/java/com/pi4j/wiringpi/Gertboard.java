package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Gertboard.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @see <a
 *      href="http://wiringpi.com/dev-lib/gertboard-analog/">http://wiringpi.com/dev-lib/gertboard-analog/</a>
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
     *      href="http://wiringpi.com/dev-lib/gertboard-analog/">http://wiringpi.com/dev-lib/gertboard-analog/</a>
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
     *      href="http://wiringpi.com/dev-lib/gertboard-analog/">http://wiringpi.com/dev-lib/gertboard-analog/</a>
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
     *      href="http://wiringpi.com/dev-lib/gertboard-analog/">http://wiringpi.com/dev-lib/gertboard-analog/</a>
     * @return If the return value is < 0 then an error occurred and errno will be set
     *         appropriately. If the return value is '0' or greater than the call was successful.
     */
    public static native int gertboardSPISetup();



    /**
     * <p>
     * This setup routine allocates 2 pins and overlays the analog to digital input pins with the digital to analog
     * output pins. So reading channel pinBase + 0 reads the first analog input channel (pin DA0 on the Gertboard),
     * and writing pinBase + 0 outputs to the first analog output channel. (Pin AD0).
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/gertboard-analog/">http://wiringpi.com/dev-lib/gertboard-analog/</a>
     * @param pinBase pinBase is the base pin that you want the analog ports to appear as
     * @return If the return value is < 0 then an error occurred and errno will be set
     *         appropriately. If the return value is '0' or greater than the call was successful.
     */
    public static native int gertboardAnalogSetup(int pinBase);
}
