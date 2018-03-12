package com.pi4j.gpio.extension.pcf;

import java.util.EnumSet;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinProvider;

public class PCFAnalogShortPin extends PinProvider {

    public static final Pin GPIO_00 = createPin(0, "ANALOG 0");
    public static final Pin GPIO_01 = createPin(1, "ANALOG 1");
    public static final Pin GPIO_02 = createPin(2, "ANALOG 2");
    public static final Pin GPIO_03 = createPin(3, "ANALOG 3");
    public static final Pin GPIO_04 = createPin(4, "ANALOG 4");
    public static final Pin GPIO_05 = createPin(5, "ANALOG 5");
    public static final Pin GPIO_06 = createPin(6, "ANALOG 6");
    public static final Pin GPIO_07 = createPin(7, "ANALOG 7");

    public static Pin[] ALL = { PCFAnalogShortPin.GPIO_00, PCFAnalogShortPin.GPIO_01, PCFAnalogShortPin.GPIO_02, PCFAnalogShortPin.GPIO_03,
            PCFAnalogShortPin.GPIO_04, PCFAnalogShortPin.GPIO_05, PCFAnalogShortPin.GPIO_06, PCFAnalogShortPin.GPIO_07 };

    private static Pin createPin(int address, String name) {
        return createPin(PCFAnalogShortGpioProvider.NAME, address, name,
                EnumSet.of(PinMode.ANALOG_INPUT, PinMode.ANALOG_OUTPUT));
    }
}