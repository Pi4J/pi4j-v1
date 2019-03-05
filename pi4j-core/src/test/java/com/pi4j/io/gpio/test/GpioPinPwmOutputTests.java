package com.pi4j.io.gpio.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinPwmOutputTests.java
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

import java.util.Collection;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.exception.GpioPinExistsException;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;

public class GpioPinPwmOutputTests {

    private static GpioController gpio;
    private static GpioPinPwmOutput pin;
    private static int initialValue = 2;

    @Before
    public void setup() {
        // create a mock gpio provider and controller
        gpio = MockGpioFactory.getInstance();

        // provision pin for testing
        pin = gpio.provisionPwmOutputPin(MockPin.PWM_OUTPUT_PIN,  "pwmOutputPin", initialValue);
    }

    @Test
    public void testPinProvisioned() {
        // make sure that pin is provisioned
        Collection<GpioPin> pins = gpio.getProvisionedPins();
        assertTrue(pins.contains(pin));
    }

    @Test(expected=GpioPinExistsException.class)
    public void testPinDuplicatePovisioning() {
        // make sure that pin cannot be provisioned a second time
        gpio.provisionPwmOutputPin(MockPin.PWM_OUTPUT_PIN,  "pwmOutputPin");
    }

    @Test(expected=UnsupportedPinModeException.class)
    public void testPinInvalidModePovisioning() {
        // make sure that pin cannot be provisioned that does not support PWM OUTPUT
        gpio.provisionPwmOutputPin(MockPin.DIGITAL_OUTPUT_PIN,  "digitalOutputPin");
    }

    @Test(expected=InvalidPinException.class)
    public void testInvalidPin() {
        // attempt to export a pin that is not supported by the GPIO provider
        pin.getProvider().export(RaspiPin.GPIO_00, PinMode.PWM_OUTPUT);
    }

    @Test
    public void testPinProvider() {
        // verify pin provider
        assertTrue(pin.getProvider() instanceof MockGpioProvider);
    }

    @Test
    public void testPinExport() {
        // verify is exported
        assertTrue(pin.isExported());
    }

    @Test
    public void testPinInstance() {
        // verify pin instance
        assertEquals(MockPin.PWM_OUTPUT_PIN, pin.getPin());
    }

    @Test
    public void testPinAddress() {
        // verify pin address
        assertEquals(MockPin.PWM_OUTPUT_PIN.getAddress(), pin.getPin().getAddress());
    }

    @Test
    public void testPinName() {
        // verify pin name
        assertEquals("pwmOutputPin", pin.getName());
    }

    @Test
    public void testPinMode() {
        // verify pin mode
        assertEquals(PinMode.PWM_OUTPUT, pin.getMode());
    }

    @Test
    public void testPinValidSupportedMode() {
        // verify valid pin mode
        assertTrue(pin.getPin().getSupportedPinModes().contains(PinMode.PWM_OUTPUT));
    }

    @Test
    public void testPinInvalidSupportedMode() {
        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.DIGITAL_INPUT));

        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.DIGITAL_OUTPUT));

        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.ANALOG_INPUT));

        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.ANALOG_OUTPUT));
    }

    @Test
    public void testPinDirection() {
        // verify pin direction
        assertEquals(PinDirection.OUT, pin.getMode().getDirection());
    }

    @Test
    public void testPinInitialValue() {
        // verify pin initial state
        assertTrue(pin.getPwm() == initialValue);
    }

    @Test
    public void testPinSetPwmValue() {
        Random generator = new Random();

        // test ten random numbers
        for (int index = 0; index < 10; index ++) {
            int newValue = generator.nextInt();

            // explicit mock set on the mock provider
            pin.setPwm(newValue);

            // verify pin value
            assertTrue(pin.getPwm() == newValue);
        }
    }

    @Test
    public void testPinUnexport() {
        // unexport pin
        pin.unexport();

        // verify is not exported
        assertFalse(pin.isExported());
    }

    @Test
    public void testPinUnprovision() {
        // make sure that pin is provisioned before we start
        Collection<GpioPin> pins = gpio.getProvisionedPins();
        assertTrue(pins.contains(pin));

        // un-provision pin
        gpio.unprovisionPin(pin);

        // make sure that pin is no longer provisioned
        pins = gpio.getProvisionedPins();
        assertFalse(pins.contains(pin));
    }

}
