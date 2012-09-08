package com.pi4j.io.gpio;

public enum GpioPin
{
    GPIO_00(0, "GPIO 0", "SDA"), // HEADER PIN 03
    GPIO_01(1, "GPIO 1", "SCL"), // HEADER PIN 05
    GPIO_04(4, "GPIO 4", "GPCLK0"), // HEADER PIN 07
    GPIO_07(7, "GPIO 7", "CE1"), // HEADER PIN 26
    GPIO_08(8, "GPIO 8", "CE0"), // HEADER PIN 24
    GPIO_09(9, "GPIO 9", "MISO"), // HEADER PIN 21
    GPIO_10(10, "GPIO 10", "MOSI"), // HEADER PIN 19
    GPIO_11(11, "GPIO 11", "SCKL"), // HEADER PIN 23
    GPIO_14(14, "GPIO 14", "TXD"), // HEADER PIN 08
    GPIO_15(15, "GPIO 15", "RXD"), // HEADER PIN 10
    GPIO_17(17, "GPIO 17"), // HEADER PIN 11
    GPIO_18(18, "GPIO 18", "PCM_CLK"), // HEADER PIN 12
    GPIO_21(21, "GPIO 21", "PCM_DOUT"), // HEADER PIN 13
    GPIO_22(22, "GPIO 22"), // HEADER PIN 15
    GPIO_23(23, "GPIO 23"), // HEADER PIN 16
    GPIO_24(24, "GPIO 24"), // HEADER PIN 18
    GPIO_25(25, "GPIO 25"); // HEADER PIN 22

    private int value;
    private String name = null;
    private String altFunction = null;

    private GpioPin(int value, String name, String altFunction)
    {
        this.value = value;
        this.name = name;
        this.altFunction = altFunction;
    }

    private GpioPin(int value, String name)
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

    public static GpioPin[] allPins()
    {
        return GpioPin.values();
    }

    public static GpioPin getPin(int pinNumber)
    {
        for (GpioPin pin : GpioPin.values())
        {
            if (pin.getValue() == pinNumber)
                return pin;
        }
        return null;
    }

    public static GpioPin getPin(String pinName)
    {
        for (GpioPin pin : GpioPin.values())
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
