package com.pi4j.device.piface;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PiFace.java  
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

import com.pi4j.component.light.LED;
import com.pi4j.component.relay.Relay;
import com.pi4j.component.switches.Switch;
import com.pi4j.device.Device;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public interface PiFace extends Device {
    
    public static final byte DEFAULT_ADDRESS = 0b01000000; // 0x40
    
    /**
     * @return the inputPins
     */
    GpioPinDigitalInput[] getInputPins();

    /**
     * @return an inputPin
     */
    GpioPinDigitalInput getInputPin(int index);
    
    /**
     * @return the outputPins
     */
    GpioPinDigitalOutput[] getOutputPins();

    /**
     * @return an outputPin
     */
    GpioPinDigitalOutput getOutputPin(int index);
    
    /**
     * @return the relays
     */
    Relay[] getRelays();

    /**
     * @return a relay
     */
    Relay getRelay(int index);

    /**
     * @return a relay
     */
    Relay getRelay(PiFaceRelay relay);
    
    /**
     * @return the switches
     */
    Switch[] getSwitches();

    /**
     * @return a switch
     */
    Switch getSwitch(int index);

    /**
     * @return a switch
     */
    Switch getSwitch(PiFaceSwitch switchValue);
    
    /**
     * @return the leds
     */
    LED[] getLeds();
    
    /**
     * @return a led
     */
    LED getLed(int index);
    
    /**
     * @return a led
     */
    LED getLed(PiFaceLed led);
}
