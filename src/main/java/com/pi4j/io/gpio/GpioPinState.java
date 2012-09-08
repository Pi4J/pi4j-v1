package com.pi4j.io.gpio;

public enum GpioPinState
{
    LOW(0, "HIGH"), 
    HIGH(1, "LOW"); 

    private int value;
    private String name = null;

    private GpioPinState(int value, String name)
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
    
    public static GpioPinState getState(int state)
    {
        for (GpioPinState item : GpioPinState.values())
        {
            if (item.getValue() == state)
                return item;
        }
        return null;
    }
    
    public static GpioPinState getState(boolean state)
    {
        if(state == true)
            return GpioPinState.HIGH;
        else
            return GpioPinState.LOW;
    }        

    public static GpioPinState[] allStates()
    {
        return GpioPinState.values();
    }    
}
