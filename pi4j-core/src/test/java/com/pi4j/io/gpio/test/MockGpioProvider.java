package com.pi4j.io.gpio.test;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderBase;

public class MockGpioProvider extends GpioProviderBase implements GpioProvider 
{
    public static final String NAME = "MockGpioProvider";

    @Override
    public String getName()
    {
        return NAME;
    }
}
