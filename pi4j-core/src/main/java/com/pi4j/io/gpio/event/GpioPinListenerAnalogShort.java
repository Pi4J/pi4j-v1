package com.pi4j.io.gpio.event;

public interface GpioPinListenerAnalogShort extends GpioPinListener {
    void handleGpioPinAnalogShortValueChangeEvent(GpioPinAnalogShortValueChangeEvent event);
}
