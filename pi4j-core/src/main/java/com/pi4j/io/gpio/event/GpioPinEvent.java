package com.pi4j.io.gpio.event;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinEvent.java  
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

import com.pi4j.io.gpio.GpioPin;

/**
 * GPIO pin event.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class GpioPinEvent extends EventObject {

    private static final long serialVersionUID = -1036445757629271L;
    protected final GpioPin pin;
    protected final PinEventType type;

    public GpioPinEvent(Object obj, GpioPin pin, PinEventType type) {
        super(obj);
        this.pin = pin;
        this.type = type;        
    }

    /**
     * Get the pin number that changed and raised this event.
     * 
     * @return GPIO pin number (not header pin number; not wiringPi pin number)
     */
    public GpioPin getPin() {
        return pin;
    }

    public PinEventType getEventType() {
        return type;
    }
}
