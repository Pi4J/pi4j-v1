/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pi4j.io.gpio.event;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinDigitalEdgeDetector.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinEdge;
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
