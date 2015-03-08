package com.pi4j.gpio.extension.pcf;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  PCF8574GpioProvider.java  
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


import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.util.BitSet;

/**
 * <p>
 * This GPIO provider implements the PCF8574 I2C GPIO expansion board as native Pi4J GPIO pins.
 * More information about the board can be found here: *
 * http://www.ti.com/lit/ds/symlink/pcf8574.pdf
 * </p>
 * 
 * <p>
 * The PCF8574 is connected via I2C connection to the Raspberry Pi and provides
 * 8 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * 
 * @author Robert Savage
 * 
 */
public class PCF8574GpioProvider extends GpioProviderBase implements GpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.ti.PCF8574GpioProvider";
    public static final String DESCRIPTION = "PCF8574 GPIO Provider";

    //these addresses belong to PCF8574(P)
    public static final int PCF8574_0x20 = 0x20; // 000
    public static final int PCF8574_0x21 = 0x21; // 001
    public static final int PCF8574_0x22 = 0x22; // 010
    public static final int PCF8574_0x23 = 0x23; // 011
    public static final int PCF8574_0x24 = 0x24; // 100
    public static final int PCF8574_0x25 = 0x25; // 101
    public static final int PCF8574_0x26 = 0x26; // 110
    public static final int PCF8574_0x27 = 0x27; // 111
    //these addresses belong to PCF8574A(P)
    public static final int PCF8574A_0x38 = 0x38; // 000
    public static final int PCF8574A_0x39 = 0x39; // 001
    public static final int PCF8574A_0x3A = 0x3A; // 010
    public static final int PCF8574A_0x3B = 0x3B; // 011
    public static final int PCF8574A_0x3C = 0x3C; // 100
    public static final int PCF8574A_0x3D = 0x3D; // 101
    public static final int PCF8574A_0x3E = 0x3E; // 110
    public static final int PCF8574A_0x3F = 0x3F; // 111
    
    public static final int PCF8574_MAX_IO_PINS = 8;

    private boolean i2cBusOwner = false;
    private I2CBus bus;
    private I2CDevice device;
    private GpioStateMonitor monitor = null;
    private BitSet currentStates = new BitSet(PCF8574_MAX_IO_PINS);

    public PCF8574GpioProvider(int busNumber, int address) throws IOException {
        // create I2C communications bus instance
        this(I2CFactory.getInstance(busNumber), address);
        i2cBusOwner = true;
    }

    public PCF8574GpioProvider(I2CBus bus, int address) throws IOException {

        // set reference to I2C communications bus instance
        this.bus = bus;

        // create I2C device instance
        device = bus.getDevice(address);

        // set all default pin cache states to match documented chip power up states
        for (Pin pin : PCF8574Pin.ALL) {
            getPinCache(pin).setState(PinState.HIGH);
            currentStates.set(pin.getAddress(), true);
        }
        
        // start monitoring thread            
        monitor = new PCF8574GpioProvider.GpioStateMonitor(device);
        monitor.start();
    }


    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void export(Pin pin, PinMode mode) {
        // make sure to set the pin mode
        super.export(pin, mode);
        setMode(pin, mode);
    }

    @Override
    public void unexport(Pin pin) {
        super.unexport(pin);
        setMode(pin, PinMode.DIGITAL_OUTPUT);
    }

    @Override
    public void setMode(Pin pin, PinMode mode) {
        super.setMode(pin, mode);
    }


    @Override
    public PinMode getMode(Pin pin) {
        return super.getMode(pin);
    }

    @Override
    public void setState(Pin pin, PinState state) {
        super.setState(pin, state);

        try {
            // set state value for pin bit 
            currentStates.set(pin.getAddress(), state.isHigh());

            // update state value
            device.write(currentStates.isEmpty() ? 0 : currentStates.toByteArray()[0]);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PinState getState(Pin pin) {
        return super.getState(pin);
    }

    @Override
    public void shutdown() {

        // prevent reentrant invocation
        if(isShutdown())
            return;

        // perform shutdown login in base
        super.shutdown();

        try {
            // if a monitor is running, then shut it down now
            if (monitor != null) {
                // shutdown monitoring thread
                monitor.shutdown();
                monitor = null;
            }

            // if we are the owner of the I2C bus, then close it
            if(i2cBusOwner) {
                // close the I2C bus communication
                bus.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
    /**
     * This class/thread is used to to actively monitor for GPIO interrupts
     * 
     * @author Robert Savage
     * 
     */
    private class GpioStateMonitor extends Thread {
        
        private I2CDevice device;
        private boolean shuttingDown = false;

        public GpioStateMonitor(I2CDevice device) {
            this.device = device;
        }

        public void shutdown() {
            shuttingDown = true;
        }

        public void run() {
            while (!shuttingDown) {
                try {
                    // read device pins state
                    byte[] buffer = new byte[1];
                    device.read(buffer, 0, 1);
                    BitSet pinStates = BitSet.valueOf(buffer);
                    
                    // determine if there is a pin state difference
                    for (int index = 0; index < pinStates.size(); index++) {
                        if (pinStates.get(index) != currentStates.get(index)) {
                            Pin pin = PCF8574Pin.ALL[index];
                            PinState newState = (pinStates.get(index)) ? PinState.HIGH : PinState.LOW;

                            // cache state
                            getPinCache(pin).setState(newState);
                            currentStates.set(index, pinStates.get(index));
                            
                            // only dispatch events for input pins
                            if (getMode(pin) == PinMode.DIGITAL_INPUT) {
                                // change detected for INPUT PIN
                                // System.out.println("<<< CHANGE >>> " + pin.getName() + " : " + state);
                                dispatchPinChangeEvent(pin.getAddress(), newState);
                            }
                        }
                    }

                    // ... lets take a short breather ...
                    Thread.currentThread();
                    Thread.sleep(50);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void dispatchPinChangeEvent(int pinAddress, PinState state) {
            // iterate over the pin listeners map
            for (Pin pin : listeners.keySet()) {
                // System.out.println("<<< DISPATCH >>> " + pin.getName() + " : " +
                // state.getName());

                // dispatch this event to the listener
                // if a matching pin address is found
                if (pin.getAddress() == pinAddress) {
                    // dispatch this event to all listener handlers
                    for (PinListener listener : listeners.get(pin)) {
                        listener.handlePinEvent(new PinDigitalStateChangeEvent(this, pin, state));
                    }
                }
            }
        }
    }
}
