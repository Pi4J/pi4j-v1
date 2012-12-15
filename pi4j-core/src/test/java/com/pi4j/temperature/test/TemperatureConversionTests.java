package com.pi4j.temperature.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinAnalogInputTests.java  
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


import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.pi4j.temperature.TemperatureConversion;
import com.pi4j.temperature.TemperatureScale;

public class TemperatureConversionTests {

    @BeforeClass 
    public static void setup() {
    }

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
