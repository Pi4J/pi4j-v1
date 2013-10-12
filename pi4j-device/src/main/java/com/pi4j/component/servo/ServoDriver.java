package com.pi4j.component.servo;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  ServoDriver.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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


/**
 * This interface represents a driver hardware to produce pulses needed for driving
 * a servo.
 * 
 * @author Daniel Sendula
 */
public interface ServoDriver {

    /**
     * This method returns current servo pulse width. Zero may represent
     * this driver stopped producing pulses. Also, value of -1
     * may define undefined situation when this abstraction didn't get
     * initial value yet and there is no way telling what real, hardware
     * or software driver is sending.
     * 
     * @return current servo pulse this driver is producing
     */
    int getServoPulseWidth();
    
    /**
     * Sets servo pulse width in resolution provided by {@link #getServoPulseResolution()}.
     * Zero value may mean that this driver is currently not producing pulse.
     * Negative values may, generally, be invalid.
     * 
     * @param width pulse width in resolution read from {@link #getServoPulseResolution()}
     */
    void setServoPulseWidth(int width);
    
    /**
     * This is read only value driver is to provide to users of this class.
     * It defines resolution {@link #getServoPulseWidth()} and {@link #setServoPulseWidth(int)}
     * methods are operating in. Resolution is provided in 1/n (ms) where value returned
     * from this method is n.
     * 
     * @return resolution of servo pulse widths used in this interface
     */
    int getServoPulseResolution();

    Pin getPin();
}
