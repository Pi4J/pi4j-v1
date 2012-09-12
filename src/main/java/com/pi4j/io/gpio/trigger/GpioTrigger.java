package com.pi4j.io.gpio.trigger;

import java.util.List;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;


public interface GpioTrigger
{
    void addPinState(PinState state);
    void addPinState(PinState[] states);
    void addPinState(List<PinState> states);
    boolean hasPinState(PinState state);
    void invoke(GpioPin pin, PinState state);
}
