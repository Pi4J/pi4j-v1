package com.pi4j.io.gpio.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinDigitalInputTests.java  
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
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.exception.GpioPinExistsException;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;

public class GpioPinDigitalInputTests    
{
    private static MockGpioProvider provider;
    private static GpioController gpio;
    private static GpioPinDigitalInput pin;
    private static PinState pinMonitoredState;

    @BeforeClass 
    public static void setup()
    {
        // create a mock gpio provider and controller
        provider = MockGpioFactory.getMockProvider();
        gpio = MockGpioFactory.getInstance();

        // provision pin for testing
        pin = gpio.provisionDigitalInputPin(MockPin.DIGITAL_INPUT_PIN,  "digitalInputPin", PinPullResistance.PULL_DOWN);
        
        // register pin listener
        pin.addListener(new GpioPinListenerDigital()
            {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event)
                {
                    // set pin state
                    if(event.getPin() == pin)
                    {
                        pinMonitoredState = event.getState();
                    }
                }                
            });
    }

    @Test
    public void testPinProvisioned() 
    {
        // make sure that pin is provisioned
        Collection<GpioPin> pins = gpio.getProvisionedPins();        
        assertTrue(pins.contains(pin));
    }    

    @Test(expected=GpioPinExistsException.class)
    public void testPinDuplicatePovisioning() 
    {
        // make sure that pin cannot be provisioned a second time
        gpio.provisionDigitalOutputPin(MockPin.DIGITAL_INPUT_PIN,  "digitalInputPin", PinState.LOW);
    }    
    
    @Test(expected=UnsupportedPinModeException.class)
    public void testPinInvalidModePovisioning() 
    {       
        // make sure that pin cannot be provisioned that does not support DIGITAL INPUT 
        gpio.provisionDigitalInputPin(MockPin.ANALOG_INPUT_PIN,  "digitalInputPin");
    }    
    
    @Test(expected=InvalidPinException.class)
    public void testInvalidPin()
    {
        // attempt to export a pin that is not supported by the GPIO provider 
        provider.export(RaspiPin.GPIO_01, PinMode.DIGITAL_INPUT);
    }

    @Test
    public void testPinProvider()
    {
        // verify pin mode
        assertEquals(provider, pin.getProvider());                
    }
    
    @Test
    public void testPinExport()
    {
        // verify is exported
        assertTrue(pin.isExported());
    }
    
    @Test
    public void testPinInstance()
    {
        // verify pin instance
        assertEquals(MockPin.DIGITAL_INPUT_PIN, pin.getPin());                
    }
    
    @Test
    public void testPinAddress()
    {    
        // verify pin address
        assertEquals(MockPin.DIGITAL_INPUT_PIN.getAddress(), pin.getPin().getAddress());
    }

    @Test
    public void testPinName()
    {
        // verify pin name
        assertEquals("digitalInputPin", pin.getName());
    }
     
    @Test
    public void testPinMode()
    {
        // verify pin mode
        assertEquals(PinMode.DIGITAL_INPUT, pin.getMode());
    }

    @Test
    public void testPinValidSupportedMode()
    {
        // verify valid pin mode
        assertTrue(pin.getPin().getSupportedPinModes().contains(PinMode.DIGITAL_INPUT));
    }

    @Test
    public void testPinInvalidSupportedMode()
    {
        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.DIGITAL_OUTPUT));
        
        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.ANALOG_OUTPUT));
        
        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.ANALOG_INPUT));        

        // verify invalid pin mode
        assertFalse(pin.getPin().getSupportedPinModes().contains(PinMode.PWM_OUTPUT));              
    }
    
    @Test
    public void testPinDirection()
    {
        // verify pin direction
        assertEquals(PinDirection.IN, pin.getMode().getDirection());                
    }

    public void testPinHiState()
    {
        // explicit mock set on the mock provider 
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.HIGH);

        // verify pin hi state
        assertTrue(pin.isHigh());
        assertEquals(PinState.HIGH, pin.getState());                
    }

    @Test
    public void testPinLowState()
    {
        // explicit mock set on the mock provider 
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.LOW);

        // verify pin low state
        assertTrue(pin.isLow());
        assertEquals(PinState.LOW, pin.getState());                
    }

    @Test(expected=InvalidPinModeException.class)
    public void testPinInvalidSetHiState()
    {
        // explicit mock set on the mock provider 
        provider.setState(MockPin.DIGITAL_INPUT_PIN, PinState.HIGH);
    }

    @Test(expected=InvalidPinModeException.class)
    public void testPinInvalidSetLowState()
    {
        // explicit mock set on the mock provider 
        provider.setState(MockPin.DIGITAL_INPUT_PIN, PinState.LOW);
    }
    
    @Test
    public void testPinUnexport() 
    {
        // unexport pin
        pin.unexport();
        
        // verify is not exported
        assertFalse(pin.isExported());
    }
    
    @Test
    public void testPinLowEvent() throws InterruptedException
    {
        // explicit mock set on the mock provider 
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.HIGH);
        
        // reset pin monitoring variable
        pinMonitoredState = null;

        // explicit mock set on the mock provider 
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.LOW);
        
        // wait 1/10 second before continuing test
        Thread.sleep(100);
        
        // verify pin low state
        assertEquals(PinState.LOW, pinMonitoredState);                
    }    

    @Test
    public void testPinHiEvent() throws InterruptedException
    {
        // explicit mock set on the mock provider 
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.LOW);
        
        // reset pin monitoring variable
        pinMonitoredState = null;

        // explicit mock set on the mock provider 
        provider.setMockState(MockPin.DIGITAL_INPUT_PIN, PinState.HIGH);
        
        // wait 1/10 second before continuing test
        Thread.sleep(100);
        
        // verify pin hi state
        assertEquals(PinState.HIGH, pinMonitoredState);                
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
