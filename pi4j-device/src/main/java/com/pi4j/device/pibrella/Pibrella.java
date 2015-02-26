package com.pi4j.device.pibrella;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Pibrella.java  
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

import com.pi4j.component.button.Button;
import com.pi4j.component.light.LED;
import com.pi4j.device.Device;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public interface Pibrella extends Device {

    LED ledRed();
    LED ledYellow();
    LED ledGreen();

    GpioController gpio();

    Button button();
    Buzzer buzzer();

    GpioPinDigitalInput inputA();
    GpioPinDigitalInput inputB();
    GpioPinDigitalInput inputC();
    GpioPinDigitalInput inputD();

    GpioPinDigitalOutput outputE();
    GpioPinDigitalOutput outputF();
    GpioPinDigitalOutput outputG();
    GpioPinDigitalOutput outputH();

    /**
     * @return the gpio
     */
    GpioController getGpio();

    /**
     * @return the inputPins
     */
    GpioPinDigitalInput[] getInputPins();

    /**
     * @return an inputPin
     */
    GpioPinDigitalInput getInputPin(int index);

    /**
     * @return an inputPin
     */
    GpioPinDigitalInput getInputPin(PibrellaInput input);

    /**
     * @return the outputPins
     */
    GpioPinDigitalOutput[] getOutputPins();

    /**
     * @return an outputPin
     */
    GpioPinDigitalOutput getOutputPin(int index);

    /**
     * @return an outputPin
     */
    GpioPinDigitalOutput getOutputPin(PibrellaOutput output);

    /**
     * @return a button
     */
    Button getButton();

    /**
     * @return a buzzer
     */
    Buzzer getBuzzer();

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
    LED getLed(PibrellaLed led);
}
