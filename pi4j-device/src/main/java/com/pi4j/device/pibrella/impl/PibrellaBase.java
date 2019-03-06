package com.pi4j.device.pibrella.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PibrellaBase.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
import com.pi4j.component.button.impl.GpioButtonComponent;
import com.pi4j.component.buzzer.Buzzer;
import com.pi4j.component.light.LED;
import com.pi4j.component.light.impl.GpioLEDComponent;
import com.pi4j.device.DeviceBase;
import com.pi4j.device.pibrella.*;
import com.pi4j.io.gpio.*;

public abstract class PibrellaBase extends DeviceBase implements Pibrella {

    private final GpioController gpio = GpioFactory.getInstance();

    private GpioPinDigitalInput inputPins[];
    private GpioPinDigitalOutput outputPins[];
    private GpioPinPwmOutput pwmOutput;

    private Button button;
    private LED leds[];
    private Buzzer buzzer;

    // default constructor
    public PibrellaBase() {

        // provision gpio input pins
        inputPins = new GpioPinDigitalInput[] {
                                  gpio.provisionDigitalInputPin(PibrellaInput.A.getPin(), "INPUT A"),
                                  gpio.provisionDigitalInputPin(PibrellaInput.B.getPin(), "INPUT B"),
                                  gpio.provisionDigitalInputPin(PibrellaInput.C.getPin(), "INPUT C"),
                                  gpio.provisionDigitalInputPin(PibrellaInput.D.getPin(), "INPUT D"),
                                  gpio.provisionDigitalInputPin(PibrellaInput.Button.getPin(), "BUTTON")};

        // provision gpio output pins
        outputPins = new GpioPinDigitalOutput[] {
                                  gpio.provisionDigitalOutputPin(PibrellaOutput.E.getPin(), "OUTPUT E"),
                                  gpio.provisionDigitalOutputPin(PibrellaOutput.F.getPin(), "OUTPUT F"),
                                  gpio.provisionDigitalOutputPin(PibrellaOutput.G.getPin(), "OUTPUT G"),
                                  gpio.provisionDigitalOutputPin(PibrellaOutput.H.getPin(), "OUTPUT H"),
                                  gpio.provisionDigitalOutputPin(PibrellaOutput.LED_RED.getPin(), "RED LED"),
                                  gpio.provisionDigitalOutputPin(PibrellaOutput.LED_YELLOW.getPin(), "YELLOW LED"),
                                  gpio.provisionDigitalOutputPin(PibrellaOutput.LED_GREEN.getPin(), "GREEN LED")};

        // provision gpio PWM pin (for buzzer)
        pwmOutput = gpio.provisionPwmOutputPin(PibrellaBuzzer.BUZZER.getPin());

        // setup shutdown options
        gpio.setShutdownOptions(true, inputPins);
        gpio.setShutdownOptions(true, PinState.LOW, pwmOutput);
        gpio.setShutdownOptions(true, PinState.LOW, outputPins);
        pwmOutput.setShutdownOptions(true);

        // setup de-bouncing on the button input
        inputPins[4].setDebounce(20);

        // create switch components
        button = new GpioButtonComponent(inputPins[4], PinState.LOW, PinState.HIGH);

        // create LED components
        leds = new LED[] {        new GpioLEDComponent(outputPins[4]),
                                  new GpioLEDComponent(outputPins[5]),
                                  new GpioLEDComponent(outputPins[6])};

        // create buzzer components
        buzzer = new PibrellaBuzzerImpl(pwmOutput);
        buzzer.stop();
    }

    /**
     * @return the gpio controller
     */
    @Override
    public GpioController getGpio() { return gpio;}

    /**
     * @return the gpio controller
     */
    @Override
    public GpioController gpio() { return getGpio(); }

    /**
     * @return the inputPins
     */
    @Override
    public GpioPinDigitalInput[] getInputPins() { return inputPins; }

    /**
     * @return an inputPin
     */
    @Override
    public GpioPinDigitalInput getInputPin(int index) { return inputPins[index]; }

    /**
     * @return an inputPin
     */
    @Override
    public GpioPinDigitalInput getInputPin(PibrellaInput input) { return getInputPin(input.getIndex()); }

    @Override
    public GpioPinDigitalInput inputA() { return getInputPin(PibrellaInput.A); }

    @Override
    public GpioPinDigitalInput inputB() { return getInputPin(PibrellaInput.B); }

    @Override
    public GpioPinDigitalInput inputC() { return getInputPin(PibrellaInput.C); }

    @Override
    public GpioPinDigitalInput inputD() { return getInputPin(PibrellaInput.D); }

    /**
     * @return the outputPins
     */
    @Override
    public GpioPinDigitalOutput[] getOutputPins() {
        return outputPins;
    }

    /**
     * @return an outputPin
     */
    @Override
    public GpioPinDigitalOutput getOutputPin(int index) { return outputPins[index]; }

    /**
     * @return an outputPin
     */
    @Override
    public GpioPinDigitalOutput getOutputPin(PibrellaOutput output) { return getOutputPin(output.getIndex()); }

    @Override
    public GpioPinDigitalOutput outputE() { return getOutputPin(PibrellaOutput.E); }

    @Override
    public GpioPinDigitalOutput outputF() { return getOutputPin(PibrellaOutput.F); }

    @Override
    public GpioPinDigitalOutput outputG() { return getOutputPin(PibrellaOutput.G); }

    @Override
    public GpioPinDigitalOutput outputH() { return getOutputPin(PibrellaOutput.H); }

    /**
     * @return the leds
     */
    @Override
    public LED[] getLeds() {
        return leds;
    }

    /**
     * @return a led
     */
    @Override
    public LED getLed(int index) {
        return leds[index];
    }

    /**
     * @return a led
     */
    @Override
    public LED getLed(PibrellaLed led) {
        return leds[led.getIndex()];
    }

    @Override
    public LED ledRed() { return getLed(PibrellaLed.RED.getIndex()); }

    @Override
    public LED ledYellow() { return getLed(PibrellaLed.YELLOW.getIndex()); }

    @Override
    public LED ledGreen() { return getLed(PibrellaLed.GREEN.getIndex()); }

    @Override
    public Button getButton() { return button; }

    @Override
    public Button button() { return getButton(); }

    @Override
    public Buzzer getBuzzer() {
        return this.buzzer;
    }

    @Override
    public Buzzer buzzer() { return getBuzzer(); }
}
