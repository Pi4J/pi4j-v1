package com.pi4j.io.gpio.exception;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  UnsupportedPinModeException.java  
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


import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;

/**
 * Unsupported pin exception.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class UnsupportedPinModeException extends RuntimeException {

    private static final long serialVersionUID = 6435118278151751895L;
    private final Pin pin;
    private final PinMode mode;

    public UnsupportedPinModeException(Pin pin, PinMode mode) {
        super("This GPIO pin [" + pin.getName() + "] does not support the pin mode specified [" + mode.getName() + "]");        
        this.pin = pin;
        this.mode = mode;
    }

    public Pin getPin() {
        return pin;
    }
    
    public PinMode getMode() {
        return mode;
    }
}
