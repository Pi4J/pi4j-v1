package com.pi4j.io.gpio;

public enum PinState
{
    LOW(0, "HIGH"), 
    HIGH(1, "LOW"); 

    private int value;
    private String name = null;

    private PinState(int value, String name)
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
    
    public static PinState getState(int state)
    {
        for (PinState item : PinState.values())
        {
            if (item.getValue() == state)
                return item;
        }
        return null;
    }
    
    public static PinState getState(boolean state)
    {
        if(state == true)
            return PinState.HIGH;
        else
            return PinState.LOW;
    }        

    public static PinState[] allStates()
    {
        return PinState.values();
    }    
}
