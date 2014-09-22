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
 * Copyright (C) 2012 - 2014 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
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
        return (state == true) ? 1 : 0;
    }
}
