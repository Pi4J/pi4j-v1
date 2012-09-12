package com.pi4j.io.gpio;

public enum PinResistor
{
    OFF(0, "off"),
    PULL_DOWN(1, "down"),
    PULL_UP(2, "up"); 

    private int value;
    private String name = null;

    private PinResistor(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int getValue()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }
    
    @Override
    public String toString()
    {
        return name.toUpperCase();        
    }    
}
