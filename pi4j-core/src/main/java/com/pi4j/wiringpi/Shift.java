package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Shift.java
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


import com.pi4j.util.NativeLibraryLoader;

/**
 * <h1>WiringPi Shift Library</h1>
 * 
 * <p>
 * WiringPi includes a shift library which more or less mimics the one in the Arduino system. This
 * allows you to shift 8-bit data values out of the Pi, or into the Pi from devices such as
 * shift-registers (e.g. 74,595) and so-on, although it can also be used in some bit-banging
 * scenarios.
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
 * Gordon Henderson @ <a href="http://wiringpi.com">http://wiringpi.com</a>)
 * </blockquote>
 * </p>
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @see <a
 *      href="http://wiringpi.com/reference/shift-library/">http://wiringpi.com/reference/shift-library/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Shift {

    public static final int LSBFIRST = 0;
    public static final int MSBFIRST = 1;

    // private constructor 
    private Shift() {
        // forbid object construction 
    }
    
    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * <p>uint8_t shiftIn (uint8_t dPin, uint8_t cPin, uint8_t order);</p>
     * 
     * <p>
     * This shifts an 8-bit data value in with the data appearing on the dPin and the clock being
     * sent out on the cPin. Order is either LSBFIRST or MSBFIRST. The data is sampled after the
     * cPin goes high. (So cPin high, sample data, cPin low, repeat for 8 bits) The 8-bit value is
     * returned by the function.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/shift-library/">http://wiringpi.com/reference/shift-library/</a>
     * @param dPin data pin
     * @param cPin clock pin
     * @param order order is either LSBFIRST or MSBFIRST
     * @return <p>
     *         The 8-bit shifted value is returned by the function.
     *         </p>
     */
    public static native byte shiftIn(byte dPin, byte cPin, byte order);

    /**
     * <p>void shiftOut (uint8_t dPin, uint8_t cPin, uint8_t order, uint8_t val);</p>
     * 
     * <p>
     * The shifts an 8-bit data value val out with the data being sent out on dPin and the clock
     * being sent out on the cPin. order is as above. Data is clocked out on the rising or falling
     * edge; ie. dPin is set, then cPin is taken high then low and repeated for the 8 bits.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/shift-library/">http://wiringpi.com/reference/shift-library/</a>
     * @param dPin data pin
     * @param cPin clock pin
     * @param order order is either LSBFIRST or MSBFIRST
     * @param val 8-bit data value
     */
    public static native void shiftOut(byte dPin, byte cPin, byte order, byte val);
}
