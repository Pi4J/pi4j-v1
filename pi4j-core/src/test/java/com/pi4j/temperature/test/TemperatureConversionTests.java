package com.pi4j.temperature.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  TemperatureConversionTests.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
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


import static org.junit.Assert.*;

import org.junit.Test;

import com.pi4j.temperature.TemperatureConversion;
import com.pi4j.temperature.TemperatureScale;

public class TemperatureConversionTests {

    // **********************************************
    // FARENHEIT CONVERSION TESTS
    // **********************************************

    @Test
    public void testFarenheitToCelsius()  {
        assertEquals(300, TemperatureConversion.convert(TemperatureScale.FARENHEIT, TemperatureScale.CELSIUS, 572), 0);
    }

    @Test
    public void testFarenheitToKelvin()  {
        assertEquals(573.15, TemperatureConversion.convert(TemperatureScale.FARENHEIT, TemperatureScale.KELVIN, 572), 0.001);
    }

    @Test
    public void testFarenheitToRankine()  {
        assertEquals(1031.67, TemperatureConversion.convert(TemperatureScale.FARENHEIT, TemperatureScale.RANKINE, 572), 0);
    }

    // **********************************************
    // CELSIUS CONVERSION TESTS
    // **********************************************

    @Test
    public void testCelsiusToFarenheit()  {
        assertEquals(572, TemperatureConversion.convert(TemperatureScale.CELSIUS, TemperatureScale.FARENHEIT, 300), 0);
    }

    @Test
    public void testCelsiusToKelvin()  {
        assertEquals(573.15, TemperatureConversion.convert(TemperatureScale.CELSIUS, TemperatureScale.KELVIN, 300), 0.001);
    }

    @Test
    public void testCelsiusToRankine()  {
        assertEquals(1031.67, TemperatureConversion.convert(TemperatureScale.CELSIUS, TemperatureScale.RANKINE, 300), 0.001);
    }


    // **********************************************
    // KELVIN CONVERSION TESTS
    // **********************************************

    @Test
    public void testKelvinToFarenheit()  {
        assertEquals(338, TemperatureConversion.convert(TemperatureScale.KELVIN, TemperatureScale.FARENHEIT, 443.15), 0.001);
    }

    @Test
    public void testKelvinToCelsius()  {
        assertEquals(170, TemperatureConversion.convert(TemperatureScale.KELVIN, TemperatureScale.CELSIUS, 443.15), 0.001);
    }

    @Test
    public void testKelvinToRankine()  {
        assertEquals(797.67, TemperatureConversion.convert(TemperatureScale.KELVIN, TemperatureScale.RANKINE, 443.15), 0.001);
    }


    // **********************************************
    // RANKINE CONVERSION TESTS
    // **********************************************

    @Test
    public void testRankineToFarenheit()  {
        assertEquals(-459.67, TemperatureConversion.convert(TemperatureScale.RANKINE, TemperatureScale.FARENHEIT, 0), 0.001);
    }

    @Test
    public void testRankineToCelsius()  {
        assertEquals(-273.15, TemperatureConversion.convert(TemperatureScale.RANKINE, TemperatureScale.CELSIUS, 0), 0.001);
    }

    @Test
    public void testRankineToKelvin()  {
        assertEquals(0, TemperatureConversion.convert(TemperatureScale.RANKINE, TemperatureScale.KELVIN, 0), 0.001);
    }

}
