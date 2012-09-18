package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Pin.java  
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


public enum Pin
{
    GPIO_00(0, "GPIO 0"),             // P1 HEADER PIN 11
    GPIO_01(1, "GPIO 1","PCM_CLK"),   // P1 HEADER PIN 12
    GPIO_02(2, "GPIO 2", "PCM_DOUT"), // P1 HEADER PIN 13
    GPIO_03(3, "GPIO 3"),             // P1 HEADER PIN 15
    GPIO_04(4, "GPIO 4"),             // P1 HEADER PIN 16
    GPIO_05(5, "GPIO 5"),             // P1 HEADER PIN 18
    GPIO_06(6, "GPIO 6"),             // P1 HEADER PIN 22
    GPIO_07(7, "GPIO 7", "GPCLK0"),   // P1 HEADER PIN 07
    GPIO_08(8, "GPIO 8", "SDA0"),     // P1 HEADER PIN 03
    GPIO_09(9, "GPIO 9", "SCL0"),     // P1 HEADER PIN 05
    GPIO_10(10, "GPIO 10", "CE0"),    // P1 HEADER PIN 24
    GPIO_11(11, "GPIO 11", "CE1"),    // P1 HEADER PIN 26
    GPIO_12(12, "GPIO 12", "MOSI"),   // P1 HEADER PIN 19
    GPIO_13(13, "GPIO 13", "MISO"),   // P1 HEADER PIN 21
    GPIO_14(14, "GPIO 14", "SCLK"),   // P1 HEADER PIN 23
    GPIO_15(15, "GPIO 15", "TxD"),    // P1 HEADER PIN 08
    GPIO_16(16, "GPIO 16", "RxD"),    // P1 HEADER PIN 10
    GPIO_17(17, "GPIO 17"),           // P5 HEADER PIN 3
    GPIO_18(18, "GPIO 18"),           // P5 HEADER PIN 4 
    GPIO_19(19, "GPIO 19"),           // P5 HEADER PIN 5
    GPIO_20(20, "GPIO 20");           // P5 HEADER PIN 6

    private int value;
    private String name = null;
    private String altFunction = null;

    private Pin(int value, String name, String altFunction)
    {
        this.value = value;
        this.name = name;
        this.altFunction = altFunction;
    }

    private Pin(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int getValue()
    {
        return value;
    }

    public String getValueString()
    {
        return Integer.toString(value);
    }
    
    public String getName()
    {
        return name;
    }

    public String getAltFunction()
    {
        return altFunction;
    }

    public boolean hasAltFunction()
    {
        return (altFunction != null && !altFunction.isEmpty());
    }

    @Override
    public String toString()
    {
        if (hasAltFunction())
            return name + " (" + altFunction + ")";
        else
            return name;
    }

    public static Pin[] allPins()
    {
        return Pin.values();
    }

    public static Pin getPin(int pinNumber)
    {
        for (Pin pin : Pin.values())
        {
            if (pin.getValue() == pinNumber)
                return pin;
        }
        return null;
    }

    public static Pin getPin(String pinName)
    {
        for (Pin pin : Pin.values())
        {
            if (pin.getValueString().equalsIgnoreCase(pinName.trim()))
                return pin;

            else if (pin.getName().equalsIgnoreCase(pinName.trim()))
                return pin;
            
            else if (pin.hasAltFunction() && pin.getAltFunction().equalsIgnoreCase(pinName.trim()))
                return pin;
        }
        return null;
    }
}
