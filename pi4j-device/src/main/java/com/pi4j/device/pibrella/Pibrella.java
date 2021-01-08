package com.pi4j.device.pibrella;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Pibrella.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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

import com.pi4j.component.button.Button;
import com.pi4j.component.buzzer.Buzzer;
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
