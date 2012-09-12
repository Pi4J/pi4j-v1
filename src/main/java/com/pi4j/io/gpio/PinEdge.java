package com.pi4j.io.gpio;

public enum PinEdge
{
    NONE(0, "none"), 
    BOTH(1, "both"),
    RISING(2, "rising"), 
    FALLING(3, "falling");

    private int value;
    private String name = null;

    private PinEdge(int value, String name)
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
        return name;        
    }    
    
    public static PinEdge getEdge(int edge)
    {
        for (PinEdge item : PinEdge.values())
        {
            if (item.getValue() == edge)
                return item;
        }
        return null;
    }     
}

