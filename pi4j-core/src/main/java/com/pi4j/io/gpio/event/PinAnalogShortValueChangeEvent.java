package com.pi4j.io.gpio.event;


import com.pi4j.io.gpio.Pin;

public class PinAnalogShortValueChangeEvent extends PinEvent {
    private static final long serialVersionUID = -6210539419288104794L;

    private final short value;

    public PinAnalogShortValueChangeEvent(Object obj, Pin pin, short value) {
        super(obj, pin, PinEventType.ANALOG_SHORT_VALUE_CHANGE);
        this.value = value;
    }

    public short getValue() {
        return this.value;
    }
}
