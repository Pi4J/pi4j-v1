package com.pi4j.device.pibrella;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PibrellaOutput.java  
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
import com.pi4j.io.gpio.RaspiPin;

public enum PibrellaOutput {
    E(0, RaspiPin.GPIO_03),  // Broadcom GPIO pin #22
    F(1, RaspiPin.GPIO_04),  // Broadcom GPIO pin #23
    G(2, RaspiPin.GPIO_05),  // Broadcom GPIO pin #24
    H(3, RaspiPin.GPIO_06),  // Broadcom GPIO pin #25
    LED_RED(4, RaspiPin.GPIO_02),     // Broadcom GPIO pin #21 on Rev 1; else #27
    LED_YELLOW(5, RaspiPin.GPIO_00),  // Broadcom GPIO pin #17
    LED_GREEN(6, RaspiPin.GPIO_07);   // Broadcom GPIO pin #04

    private int index = -1;
    private Pin pin = null;

    private PibrellaOutput(int index, Pin pin){
        this.index = index;
        this.pin = pin;
    }

    public int getIndex(){
        return index;
    }
    public Pin getPin(){
        return pin;
    }
}
