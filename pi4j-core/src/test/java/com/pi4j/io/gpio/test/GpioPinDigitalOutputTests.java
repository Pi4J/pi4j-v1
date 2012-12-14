package com.pi4j.io.gpio.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinDigitalOutputTests.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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

import org.junit.BeforeClass;
import org.junit.Test;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.exception.GpioPinExistsException;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;

public class GpioPinDigitalOutputTests {

    private static GpioController gpio;
    private static GpioPinDigitalOutput pin;
    
    @BeforeClass 
    public static void setup() {
        // create a mock gpio provider and controller
        gpio = MockGpioFactory.getInstance();
        
        // provision pin for testing
        pin = gpio.provisionDigitalOutputPin(MockPin.DIGITAL_OUTPUT_PIN,  "digitalOutputPin", PinState.LOW);
    }

    @Test
    public void testPinProvisioned()  {
        // make sure that pin is provisioned
        Collection<GpioPin> pins = gpio.getProvisionedPins();        
        assertTrue(pins.contains(pin));
    }    

    @Test(expected=GpioPinExistsException.class)
    public void testPinDuplicatePovisioning()  {
        // make sure that pin cannot be provisioned a second time
        gpio.provisionDigitalOutputPin(MockPin.DIGITAL_OUTPUT_PIN,  "digitalOutputPin", PinState.LOW);
    }    
    
    @Test(expected=UnsupportedPinModeException.class)
    public void testPinInvalidModePovisioning() {
        // make sure that pin cannot be provisioned that does not support DIGITAL OUTPUT 
        gpio.provisionDigitalOutputPin(MockPin.DIGITAL_INPUT_PIN,  "analogOutputPin");
    }    
    
    @Test(expected=InvalidPinException.class)
    public void testInvalidPin() {
        // attempt to export a pin that is not supported by the GPIO provider
        pin.getProvider().export(RaspiPin.GPIO_01, PinMode.DIGITAL_OUTPUT);
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
        assertEquals(MockPin.DIGITAL_OUTPUT_PIN, pin.getPin());                
    }
    
    @Test
    public void testPinAddress() {
        // verify pin address
        assertEquals(MockPin.DIGITAL_OUTPUT_PIN.getAddress(), pin.getPin().getAddress());
    }

    @Test
    public void testPinName() {
        // verify pin name
        assertEquals("digitalOutputPin", pin.getName());
    }
     
    @Test
    public void testPinMode() {
        // verify pin mode
        assertEquals(PinMode.DIGITAL_OUTPUT, pin.getMode());
    }

    @Test
    public void testPinValidSupportedMode() {
        // verify valid pin mode
        assertTrue(pin.getPin().getSupportedPinModes().contains(PinMode.DIGITAL_OUTPUT));
    }

    @Test
    public void testPinInvalidSupportedMode() {
        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.DIGITAL_INPUT));
        
        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.ANALOG_OUTPUT));
        
        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.ANALOG_INPUT));        

        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.PWM_OUTPUT));              
    }
    
    @Test
    public void testPinDirection() {
        // verify pin direction
        assertEquals(PinDirection.OUT, pin.getMode().getDirection());                
    }

    @Test
    public void testPinInitialState() {
        // verify pin initial state
        assertTrue(pin.isLow());
        assertEquals(PinState.LOW, pin.getState());                
    }

    @Test
    public void testPinHiState() {
        pin.setState(PinState.HIGH);

        // verify pin hi state
        assertTrue(pin.isHigh());
        assertEquals(PinState.HIGH, pin.getState());                
    }

    @Test
    public void testPinLowState() {
        pin.setState(PinState.LOW);

        // verify pin low state
        assertTrue(pin.isLow());
        assertEquals(PinState.LOW, pin.getState());                
    }

    @Test
    public void testPinHiStateBoolean() {
        pin.setState(true);

        // verify pin hi state
        assertTrue(pin.isHigh());
        assertEquals(PinState.HIGH, pin.getState());                
    }

    @Test
    public void testPinLowStateBoolean() {
        pin.setState(false);

        // verify pin low state
        assertTrue(pin.isLow());
        assertEquals(PinState.LOW, pin.getState());                
    }

    @Test
    public void testPinHi() {
        pin.high();

        // verify pin hi state
        assertTrue(pin.isHigh());
        assertEquals(PinState.HIGH, pin.getState());                
    }

    @Test
    public void testPinLow() {
        pin.low();

        // verify pin low state
        assertTrue(pin.isLow());
        assertEquals(PinState.LOW, pin.getState());                
    }

    @Test
    public void testPinToggle() {
        // set known start state
        pin.low();
        
        // toggle hi
        pin.toggle();

        // verify pin hi state
        assertTrue(pin.isHigh());
        
        // toggle low
        pin.toggle();

        // verify pin low state
        assertTrue(pin.isLow());
    }

    @Test
    public void testPinPulse() throws InterruptedException {
        // set known start state
        pin.low();
        
        // pulse pin hi for 1/5 second
        pin.pulse(200, PinState.HIGH);

        // verify pin hi state
        assertTrue(pin.isHigh());

        // wait 1/2 second before continuing test
        Thread.sleep(500);
        
        // verify pin low state
        assertTrue(pin.isLow());
    }
    
    @Test
    public void testPinBlink() throws InterruptedException {
        // set known start state
        pin.low();
        
        // blink pin for 1 seconds with a blink rate of 1/5 second 
        pin.blink(200, 1000, PinState.HIGH);

        // verify pin hi state
        assertTrue(pin.isHigh());

        // wait before continuing test
        Thread.sleep(250);

        // verify pin low state
        assertTrue(pin.isLow());

        // wait before continuing test
        Thread.sleep(250);
        
        // verify pin hi state
        assertTrue(pin.isHigh());

        // wait before continuing test
        Thread.sleep(250);

        // verify pin low state
        assertTrue(pin.isLow());
        
        // wait before continuing test
        Thread.sleep(500);
        
        // verify pin low state
        assertTrue(pin.isLow());
    }
    
    @Test
    public void testPinUnexport() {
        // unexport pin
        pin.unexport();
        
        // verify is not exported
        assertFalse(pin.isExported());
    }
    
    @Test
    public void testPinUnprovision() 
    {
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
