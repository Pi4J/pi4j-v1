/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pi4j.io.gpio.event;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.PinState;

/**
 *
 * @author Stefan
 */
public class GpioPinDigitalEdgeDetector implements GpioPinListenerDigital {

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        PinState state = event.getState();
        PinEdge edge = PinEdge.NONE;
        GpioPin pin = event.getPin();
        if (state == PinState.HIGH) {
            edge = PinEdge.RISING;
        } else if (state == PinState.LOW) {
            edge = PinEdge.FALLING;
        }
        GpioPinDigitalEdgeDetectionEvent ev = new GpioPinDigitalEdgeDetectionEvent(this, pin, edge);
        for (GpioPinListener listener : pin.getListeners()) {
            if (listener instanceof GpioPinDigitalEdgeDetectionListener) {
                ((GpioPinDigitalEdgeDetectionListener) listener).handleGpioPinDigitalEdgeDetectionEvent(ev);
            }
        }
    }

}
