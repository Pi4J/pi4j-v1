package com.pi4j.io.gpio.test;

import java.util.EnumSet;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;

public class MockPin 
{
    public static final Pin DIGITAL_BIDIRECTIONAL_PIN = new PinImpl(MockGpioProvider.NAME, 0, "GPIO-0", 
                                                            EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT),
                                                            PinPullResistance.all());

    public static final Pin DIGITAL_INPUT_PIN = new PinImpl(MockGpioProvider.NAME, 1, "GPIO-1", 
                                                      EnumSet.of(PinMode.DIGITAL_INPUT),
                                                      PinPullResistance.all());

    public static final Pin DIGITAL_OUTPUT_PIN = new PinImpl(MockGpioProvider.NAME, 2, "GPIO-2", 
                                                            EnumSet.of(PinMode.DIGITAL_OUTPUT));
    
    public static final Pin GPIO_01 = createDigitalAndPwmPin(1, "GPIO 1"); // PIN 1 supports PWM output
    
    private static Pin createDigitalAndPwmPin(int address, String name)
    {
        return new PinImpl(MockGpioProvider.NAME, address, name, 
                           EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, PinMode.PWM_OUTPUT),
                           PinPullResistance.all());
    }
}
