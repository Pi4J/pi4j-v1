package com.pi4j.io.gpio;

public enum PinDirection
{
    IN(0, "in"), 
    OUT(1, "out");

    private int value;
    private String name = null;

    private PinDirection(int value, String name)
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
    
    public static PinDirection getDirection(int direction)
    {
        for (PinDirection item : PinDirection.values())
        {
            if (item.getValue() == direction)
                return item;
        }
        return null;
    }    
}
