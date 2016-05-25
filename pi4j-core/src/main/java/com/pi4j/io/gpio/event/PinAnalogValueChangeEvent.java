package com.pi4j.io.gpio.event;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  PinAnalogValueChangeEvent.java
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


import com.pi4j.io.gpio.Pin;

/**
 * GPIO analog pin value change event.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class PinAnalogValueChangeEvent extends PinEvent {

    private static final long serialVersionUID = -6210539419288104794L;
    private final double value;

    /**
     * Default event constructor
     *
     * @param obj Ignore this parameter
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param value  New GPIO pin analog value.
     */
    public PinAnalogValueChangeEvent(Object obj, Pin pin, double value) {
        super(obj, pin, PinEventType.ANALOG_VALUE_CHANGE);
        this.value = value;
    }

    /**
     * Get the new pin value raised in this event.
     *
     * @return GPIO pin value
     */
    public double getValue() {
        return value;
    }
}
