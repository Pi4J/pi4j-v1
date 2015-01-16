package com.pi4j.device.piface;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PiFaceBase.java  
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


import com.pi4j.component.light.LED;
import com.pi4j.component.light.impl.GpioLEDComponent;
import com.pi4j.component.relay.Relay;
import com.pi4j.component.relay.impl.GpioRelayComponent;
import com.pi4j.component.switches.Switch;
import com.pi4j.component.switches.impl.GpioSwitchComponent;
import com.pi4j.device.DeviceBase;
import com.pi4j.gpio.extension.piface.PiFaceGpioProvider;
import com.pi4j.gpio.extension.piface.PiFacePin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.spi.SpiChannel;

import java.io.IOException;

public abstract class PiFaceBase extends DeviceBase implements PiFace {
    
    private final GpioController gpio = GpioFactory.getInstance();
    private PiFaceGpioProvider gpioProvider;
    private GpioPinDigitalInput inputPins[];
    private GpioPinDigitalOutput outputPins[];
    private Relay relays[];
    private Switch switches[];
    private LED leds[];

    // default constructor
    public PiFaceBase(byte spiAddress, int spiChannel) throws IOException {
        this(spiAddress, SpiChannel.getByNumber(spiChannel));
    }

    // default constructor
    public PiFaceBase(byte spiAddress, SpiChannel spiChannel) throws IOException {
    
        // create Pi-Face GPIO provider
        gpioProvider = new PiFaceGpioProvider(spiAddress, spiChannel);

        // provision gpio input pins for the Pi-Face board 
        inputPins = new GpioPinDigitalInput[] {
                                  gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_00),
                                  gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_01),
                                  gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_02),
                                  gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_03),
                                  gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_04),
                                  gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_05),
                                  gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_06),
                                  gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_07) };
        
        // provision gpio output pins for the Pi-Face board    
        outputPins = new GpioPinDigitalOutput[] {
                                  gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_00),
                                  gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_01),
                                  gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_02),
                                  gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_03),
                                  gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_04),
                                  gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_05),
                                  gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_06),
                                  gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_07) };
        
        // create relay components for the first two output pins on the Pi-Face board
        relays = new Relay[] {    new GpioRelayComponent(outputPins[0]),
                                  new GpioRelayComponent(outputPins[1]) };

        // create switch components for the first four input pins on the Pi-Face board
        switches = new Switch[] { new GpioSwitchComponent(inputPins[0], PinState.HIGH, PinState.LOW),
                                  new GpioSwitchComponent(inputPins[1], PinState.HIGH, PinState.LOW),
                                  new GpioSwitchComponent(inputPins[2], PinState.HIGH, PinState.LOW),
                                  new GpioSwitchComponent(inputPins[3], PinState.HIGH, PinState.LOW) };
        
        // create LED components for the eight output pins on the Pi-Face board
        leds = new LED[] {        new GpioLEDComponent(outputPins[0]),
                                  new GpioLEDComponent(outputPins[1]),
                                  new GpioLEDComponent(outputPins[2]),
                                  new GpioLEDComponent(outputPins[3]),
                                  new GpioLEDComponent(outputPins[4]),
                                  new GpioLEDComponent(outputPins[5]),
                                  new GpioLEDComponent(outputPins[6]),
                                  new GpioLEDComponent(outputPins[7]) };

    }

    /**
     * @return the gpio
     */
    public GpioController getGpio() {
        return gpio;
    }

    /**
     * @return the gpioProvider
     */
    public PiFaceGpioProvider getGpioProvider() {
        return gpioProvider;
    }

    /**
     * @return the inputPins
     */
    @Override
    public GpioPinDigitalInput[] getInputPins() {
        return inputPins;
    }

    /**
     * @return an inputPin
     */
    @Override
    public GpioPinDigitalInput getInputPin(int index) {
        return inputPins[index];
    }
    
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
    public GpioPinDigitalOutput getOutputPin(int index) {
        return outputPins[index];
    }
    
    /**
     * @return the relays
     */
    @Override
    public Relay[] getRelays() {
        return relays;
    }

    /**
     * @return a relay
     */
    @Override
    public Relay getRelay(int index) {
        return relays[index];
    }

    /**
     * @return a relay
     */
    @Override
    public Relay getRelay(PiFaceRelay relay) {
        return relays[relay.getIndex()];
    }
    
    /**
     * @return the switches
     */
    @Override
    public Switch[] getSwitches() {
        return switches;
    }

    /**
     * @return a switch
     */
    @Override
    public Switch getSwitch(int index) {
        return switches[index];
    }

    /**
     * @return a switch
     */
    @Override
    public Switch getSwitch(PiFaceSwitch switchValue) {
        return switches[switchValue.getIndex()];
    }
    
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
    public LED getLed(PiFaceLed led) {
        return leds[led.getIndex()];
    }    
}
