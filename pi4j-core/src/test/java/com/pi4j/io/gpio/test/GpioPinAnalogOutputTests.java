package com.pi4j.io.gpio.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinAnalogOutputTests.java
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
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.exception.GpioPinExistsException;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;

public class GpioPinAnalogOutputTests {

    private static GpioController gpio;
    private static GpioPinAnalogOutput pin;
    
    @BeforeClass 
    public static void setup() {
        // create a mock gpio provider and controller
        gpio = MockGpioFactory.getInstance();
        
        // provision pin for testing
        pin = gpio.provisionAnalogOutputPin(MockPin.ANALOG_OUTPUT_PIN,  "analogOutputPin", 3.1416);
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
        gpio.provisionAnalogOutputPin(MockPin.ANALOG_OUTPUT_PIN,  "analogOutputPin");
    }    
    
    @Test(expected=UnsupportedPinModeException.class)
    public void testPinInvalidModePovisioning() {
        // make sure that pin cannot be provisioned that does not support ANALOG OUTPUT 
        gpio.provisionAnalogOutputPin(MockPin.DIGITAL_OUTPUT_PIN,  "digitalOutputPin");
    }    
    
    @Test(expected=InvalidPinException.class)
    public void testInvalidPin() {
        // attempt to export a pin that is not supported by the GPIO provider
        pin.getProvider().export(RaspiPin.GPIO_00, PinMode.ANALOG_OUTPUT);
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
        assertEquals(MockPin.ANALOG_OUTPUT_PIN, pin.getPin());                
    }
    
    @Test
    public void testPinAddress() { 
        // verify pin address
        assertEquals(MockPin.ANALOG_OUTPUT_PIN.getAddress(), pin.getPin().getAddress());
    }

    @Test
    public void testPinName() {
        // verify pin name
        assertEquals("analogOutputPin", pin.getName());
    }
     
    @Test
    public void testPinMode() {
        // verify pin mode
        assertEquals(pin.getMode(), PinMode.ANALOG_OUTPUT);
    }

    @Test
    public void testPinValidSupportedMode() {
        // verify valid pin mode
        assertTrue(pin.getPin().getSupportedPinModes().contains(PinMode.ANALOG_OUTPUT));
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
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.PWM_OUTPUT));              
    }
    
    @Test
    public void testPinDirection() {
        // verify pin direction
        assertEquals(PinDirection.OUT, pin.getMode().getDirection());                
    }

    @Test
    public void testPinInitialValue() {
        // verify pin initial state
        assertTrue(pin.getValue() == 3.1416);
    }

    @Test
    public void testPinSetValue() {
        Random generator = new Random();
        
        // test ten random numbers
        for (int index = 0; index < 10; index ++) {
            double newValue = generator.nextDouble();
            
            // explicit mock set on the mock provider 
            pin.setValue(newValue);
    
            // verify pin value
            assertTrue(pin.getValue() == newValue);
        }
    }

    @Test
    public void testPinUnexport()  {
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
