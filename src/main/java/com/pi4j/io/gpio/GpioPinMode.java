package com.pi4j.io.gpio;

public enum GpioPinMode
{
    INPUT(0, "input"), 
    OUTPUT(1, "output"),
    PWM_OUTPUT(2, "pwm_output");

    private int value;
    private String name = null;

    private GpioPinMode(int value, String name)
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
