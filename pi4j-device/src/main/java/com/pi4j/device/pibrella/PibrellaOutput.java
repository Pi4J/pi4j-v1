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
 * Copyright (C) 2012 - 2015 Pi4J
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
