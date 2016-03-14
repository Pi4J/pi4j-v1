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
