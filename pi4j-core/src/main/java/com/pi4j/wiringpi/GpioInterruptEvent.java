package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioInterruptEvent.java
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


import java.util.EventObject;

/**
 * <p> This class provides the event object for GPIO interrupt state changes. </p>
 *
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class GpioInterruptEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private int pin;
    private boolean state;

    /**
     * <h1>Default event constructor</h1>
     *
     * @param obj Ignore this parameter
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param state New GPIO pin state.
     */
    public GpioInterruptEvent(Object obj, int pin, boolean state) {
        super(obj);
        this.pin = pin;
        this.state = state;
    }

    /**
     * Get the pin number that changed and raised this event.
     *
     * @return GPIO pin number (not header pin number; not wiringPi pin number)
     */
    public int getPin() {
        return pin;
    }

    /**
     * Get the new pin state raised in this event.
     *
     * @return GPIO pin state (HIGH=true, LOW=false)
     */
    public boolean getState() {
        return state;
    }

    /**
     * Get the new pin state value raised in this event.
     *
     * @return GPIO pin state (HIGH=1, LOW=0)
     */
    public int getStateValue() {
        return (state) ? 1 : 0;
    }
}
