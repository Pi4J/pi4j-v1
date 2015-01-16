package com.pi4j.device.pibrella;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PibrellaInput.java  
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

public enum PibrellaInput {
    A(0, RaspiPin.GPIO_13),  // Broadcom GPIO pin #09
    B(1, RaspiPin.GPIO_11),  // Broadcom GPIO pin #07
    C(2, RaspiPin.GPIO_10),  // Broadcom GPIO pin #08
    D(3, RaspiPin.GPIO_12),  // Broadcom GPIO pin #10
    Button(4 , RaspiPin.GPIO_14);  // Broadcom GPIO pin #11

    private int index = -1;
    private Pin pin = null;

    private PibrellaInput(int index, Pin pin){
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
