package com.pi4j.io.gpio.trigger.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioSetStateTriggerTests.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
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
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.test.MockGpioFactory;
import com.pi4j.io.gpio.test.MockGpioProvider;
import com.pi4j.io.gpio.test.MockPin;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;

public class GpioSetStateTriggerTests {

    private static MockGpioProvider provider;
    private static GpioController gpio;
    private static GpioPinDigitalInput inputPin;
    private static GpioPinDigitalOutput outputPin;
    private static GpioSetStateTrigger triggerHigh;
    private static GpioSetStateTrigger triggerLow;

    @Before
    public void setup() {
        // create a mock gpio provider and controller
        provider = MockGpioFactory.getMockProvider();
        gpio = MockGpioFactory.getInstance();

        // provision pins for testing
        inputPin = gpio.provisionDigitalInputPin(MockPin.DIGITAL_INPUT_PIN,  "digitalInputPin");
        outputPin = gpio.provisionDigitalOutputPin(MockPin.DIGITAL_OUTPUT_PIN,  "digitalOutputPin");

        // create triggers
        triggerHigh = new GpioSetStateTrigger(PinState.HIGH, outputPin, PinState.HIGH);
        triggerLow = new GpioSetStateTrigger(PinState.LOW, outputPin, PinState.LOW);

        // add triggers to input pin
        inputPin.addTrigger(triggerHigh);
        inputPin.addTrigger(triggerLow);
    }

    @After
    public void teardown() {
        // remove triggers
        inputPin.removeTrigger(triggerHigh);
        inputPin.removeTrigger(triggerLow);
    }

    @Test
    public void testHasTrigger() {
        // verify that the input pin does have a trigger assigned
        assertFalse(inputPin.getTriggers().isEmpty());
    }

    @Test
    public void testTriggerHigh() throws InterruptedException {
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.LOW);
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.HIGH);

        // wait before continuing test
        Thread.sleep(50);

        // verify that the output pin state is high
        assertEquals(PinState.HIGH, outputPin.getState());
    }

    @Test
    public void testTriggerLow() throws InterruptedException {
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.HIGH);
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.LOW);

        // wait before continuing test
        Thread.sleep(50);

        // verify that the output pin state is low
        assertEquals(PinState.LOW, outputPin.getState());
    }

}

