package com.pi4j.io.gpio.event;


import com.pi4j.io.gpio.GpioPin;

public class GpioPinAnalogShortValueChangeEvent extends GpioPinEvent {

    private static final long serialVersionUID = -1036445757629271L;

    private final short value;

    public GpioPinAnalogShortValueChangeEvent(Object obj, GpioPin pin, short value) {
        super(obj, pin, PinEventType.ANALOG_SHORT_VALUE_CHANGE);
        this.value = value;
    }

    public short getValue() {
        return this.value;
    }
}