package com.pi4j.io.gpio.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinAnalogInputTests.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import java.util.Collection;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.gpio.exception.GpioPinExistsException;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;

public class GpioPinAnalogInputTests {

    private static MockGpioProvider provider;
    private static GpioController gpio;
    private static GpioPinAnalogInput pin;
    private static Double pinMonitoredValue;
    private static int eventCounter;

    @BeforeClass
    public static void setup() {
        // create a mock gpio provider and controller
        provider = MockGpioFactory.getMockProvider();
        gpio = MockGpioFactory.getInstance();

        // provision pin for testing
        pin = gpio.provisionAnalogInputPin(MockPin.ANALOG_INPUT_PIN,  "analogInputPin");

        // register pin listener
        pin.addListener(new GpioPinListenerAnalog() {
                @Override
                public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
                    // set pin state
                    if (event.getPin() == pin) {
                        pinMonitoredValue = event.getValue();
                        eventCounter++;
                    }
                }
            });
    }

    @Test
    public void testPinProvisioned()  {
        // make sure that pin is provisioned
        Collection<GpioPin> pins = gpio.getProvisionedPins();
        assertTrue(pins.contains(pin));
    }

    @Test(expected=GpioPinExistsException.class)
    public void testPinDuplicatePovisioning() {
        // make sure that pin cannot be provisioned a second time
        gpio.provisionAnalogOutputPin(MockPin.ANALOG_INPUT_PIN,  "analogInputPin");
    }

    @Test(expected=UnsupportedPinModeException.class)
    public void testPinInvalidModePovisioning() {
        // make sure that pin cannot be provisioned that does not support ANALOG INPUT
        gpio.provisionAnalogInputPin(MockPin.DIGITAL_INPUT_PIN,  "digitalInputPin");
    }

    @Test(expected=InvalidPinException.class)
    public void testInvalidPin() {
        // attempt to export a pin that is not supported by the GPIO provider
        provider.export(RaspiPin.GPIO_01, PinMode.ANALOG_INPUT);
    }

    @Test
    public void testPinProvider() {
        // verify pin mode
        assertEquals(provider, pin.getProvider());
    }

    @Test
    public void testPinExport() {
        // verify is exported
        assertTrue(pin.isExported());
    }

    @Test
    public void testPinInstance() {
        // verify pin instance
        assertEquals(MockPin.ANALOG_INPUT_PIN, pin.getPin());
    }

    @Test
    public void testPinAddress() {
        // verify pin address
        assertEquals(MockPin.ANALOG_INPUT_PIN.getAddress(), pin.getPin().getAddress());
    }

    @Test
    public void testPinName() {
        // verify pin name
        assertEquals("analogInputPin", pin.getName());
    }

    @Test
    public void testPinMode() {
        // verify pin mode
        assertEquals(PinMode.ANALOG_INPUT, pin.getMode());
    }

    @Test
    public void testPinValidSupportedMode() {
        // verify valid pin mode
        assertTrue(pin.getPin().getSupportedPinModes().contains(PinMode.ANALOG_INPUT));
    }

    @Test
    public void testPinInvalidSupportedMode() {
        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.DIGITAL_OUTPUT));

        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.ANALOG_OUTPUT));

        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.DIGITAL_INPUT));

        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.PWM_OUTPUT));
    }

    @Test
    public void testPinDirection() {
        // verify pin direction
        assertEquals(PinDirection.IN, pin.getMode().getDirection());
    }

    public void testPinValue() {
        Random generator = new Random();

        // test ten random numbers
        for (int index = 0; index < 10; index ++) {
            double newValue = generator.nextDouble();

            // explicit mock set on the mock provider
            provider.setMockAnalogValue(MockPin.ANALOG_INPUT_PIN, newValue);

            // verify pin value
            assertTrue(pin.getValue() == newValue);
        }
    }

    @Test(expected=InvalidPinModeException.class)
    public void testPinInvalidSetState() {
        // explicit mock set on the mock provider
        provider.setState(MockPin.ANALOG_INPUT_PIN, PinState.HIGH);
    }

    @Test
    public void testPinUnexport() {
        // unexport pin
        pin.unexport();

        // verify is not exported
        assertFalse(pin.isExported());
    }

    @Test
    public void testPinValueEvent() throws InterruptedException {
        Random generator = new Random();

        // reset event counter
        eventCounter = 0;

        // test five events
        for (int index = 0; index < 5; index ++) {
            double newValue = generator.nextDouble();

            // reset pin monitoring variable
            pinMonitoredValue = null;

            // explicit mock set on the mock provider
            provider.setMockAnalogValue(MockPin.ANALOG_INPUT_PIN, newValue);

            // wait 1/10 second before continuing test
            Thread.sleep(100);

            // verify pin value
            assertTrue(pin.getValue() == pinMonitoredValue);
        }

        // make sure we received the proper number of events
        assertEquals(5, eventCounter);
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
