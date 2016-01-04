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

/**
 *
 * @author Stefan
 */
public class GpioPinDigitalEdgeDetectionEvent extends GpioPinEvent {

    private final PinEdge edge;

    public GpioPinDigitalEdgeDetectionEvent(Object obj, GpioPin pin, PinEdge edge) {
        super(obj, pin, PinEventType.DIGITAL_STATE_CHANGE);
        this.edge = edge;
    }

    public PinEdge getEdge() {
        return this.edge;
    }

}
