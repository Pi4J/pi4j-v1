package com.pi4j.io.gpio.trigger.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioToggleStateTriggerTests.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
import com.pi4j.io.gpio.trigger.GpioToggleStateTrigger;

public class GpioToggleStateTriggerTests {

    private static MockGpioProvider provider;
    private static GpioController gpio;
    private static GpioPinDigitalInput inputPin;
    private static GpioPinDigitalOutput outputPin;
    private static GpioToggleStateTrigger trigger;

    @Before
    public void setup() {
        // create a mock gpio provider and controller
        provider = MockGpioFactory.getMockProvider();
        gpio = MockGpioFactory.getInstance();

        // provision pins for testing
        inputPin = gpio.provisionDigitalInputPin(MockPin.DIGITAL_INPUT_PIN,  "digitalInputPin");
        outputPin = gpio.provisionDigitalOutputPin(MockPin.DIGITAL_OUTPUT_PIN,  "digitalOutputPin", PinState.LOW);

        // create trigger
        trigger = new GpioToggleStateTrigger(PinState.HIGH, outputPin);

        // add trigger to input pin
        inputPin.addTrigger(trigger);
    }

    @After
    public void teardown() {
        // remove trigger
        inputPin.removeTrigger(trigger);
    }

    @Test
    public void testHasTrigger()  {
        // verify that the input pin does have a trigger assigned
        assertFalse(inputPin.getTriggers().isEmpty());
    }


    @Test
    public void testTrigger() throws InterruptedException {
        // verify that the output pin state starts in state: low
        assertEquals(PinState.LOW, outputPin.getState());

        // each time the pin state goes high,
        // the output pin state should be toggled
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.HIGH);

        // wait before continuing test
        Thread.sleep(50);

        // verify that the output pin state is now in state: high
        assertEquals(PinState.HIGH, outputPin.getState());

        // wait before continuing test
        Thread.sleep(50);

        // each time the pin state goes high,
        // the output pin state should be toggled
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.LOW);

        // wait before continuing test
        Thread.sleep(50);

        // verify that the output pin state is now in state: high
        assertEquals(PinState.HIGH, outputPin.getState());

        // wait before continuing test
        Thread.sleep(50);

        // each time the pin state goes high,
        // the output pin state should be toggled
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.HIGH);
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.LOW);

        // wait before continuing test
        Thread.sleep(50);

        // verify that the output pin state is now in state: low
        assertEquals(PinState.LOW, outputPin.getState());

        // wait before continuing test
        Thread.sleep(50);

        // each time the pin state goes high,
        // the output pin state should be toggled
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.HIGH);
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.LOW);

        // wait before continuing test
        Thread.sleep(50);

        // verify that the output pin state is now in state: high
        assertEquals(PinState.HIGH, outputPin.getState());

        // wait before continuing test
        Thread.sleep(50);

        // each time the pin state goes high,
        // the output pin state should be toggled
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.HIGH);
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.LOW);

        // wait before continuing test
        Thread.sleep(50);

        // verify that the output pin state is now in state: low
        assertEquals(PinState.LOW, outputPin.getState());
    }
}

