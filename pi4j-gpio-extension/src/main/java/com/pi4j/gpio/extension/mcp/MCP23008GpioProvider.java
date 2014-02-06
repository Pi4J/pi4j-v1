package com.pi4j.gpio.extension.mcp;

import java.io.IOException;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinPullResistanceException;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP23008GpioProvider.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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

/**
 * <p>
 * This GPIO provider implements the MCP23008 I2C GPIO expansion board as native Pi4J GPIO pins.
 * More information about the board can be found here: *
 * http://ww1.microchip.com/downloads/en/DeviceDoc/21919e.pdf
 * http://learn.adafruit.com/mcp230xx-gpio-expander-on-the-raspberry-pi/overview
 * </p>
 * 
 * <p>
 * The MCP23008 is connected via I2C connection to the Raspberry Pi and provides
 * 8 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * 
 * @author Robert Savage
 * 
 */
public class MCP23008GpioProvider extends GpioProviderBase implements GpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.mcp.MCP23008GpioProvider";
    public static final String DESCRIPTION = "MCP23008 GPIO Provider";
    
    public static final int REGISTER_IODIR = 0x00;
    private static final int REGISTER_GPINTEN = 0x02;
    private static final int REGISTER_DEFVAL = 0x03;
    private static final int REGISTER_INTCON = 0x04;
    private static final int REGISTER_GPPU = 0x06;
    private static final int REGISTER_INTF = 0x07;
    // private static final int REGISTER_INTCAP = 0x08;
    public static final int REGISTER_GPIO  = 0x09;

    private int currentStates = 0;
    private int currentDirection = 0;
    private int currentPullup = 0;

    private I2CBus bus;
    private I2CDevice device;
    private GpioStateMonitor monitor = null;

    public MCP23008GpioProvider(int busNumber, int address) throws IOException {
        // create I2C communications bus instance
        bus = I2CFactory.getInstance(busNumber);

        // create I2C device instance
        device = bus.getDevice(address);

        // set all default pins directions
        device.write(REGISTER_IODIR, (byte) currentDirection);

        // set all default pin interrupts
        device.write(REGISTER_GPINTEN, (byte) currentDirection);

        // set all default pin interrupt default values
        device.write(REGISTER_DEFVAL, (byte) 0x00);

        // set all default pin interrupt comparison behaviors
        device.write(REGISTER_INTCON, (byte) 0x00);

        // set all default pin states
        device.write(REGISTER_GPIO, (byte) currentStates);

        // set all default pin pull up resistors
        device.write(REGISTER_GPPU, (byte) currentPullup);
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
        // validate
        if (!pin.getSupportedPinModes().contains(mode)) {
            throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName()
                    + "]; pin [" + pin.getName() + "] does not support this mode.");
        }
        // validate
        if (!pin.getSupportedPinModes().contains(mode)) {
            throw new UnsupportedPinModeException(pin, mode);
        }
        try {
            // determine register and pin address
            int pinAddress = pin.getAddress();

            // determine update direction value based on mode
            if (mode == PinMode.DIGITAL_INPUT) {
                currentDirection |= pinAddress;
            } else if (mode == PinMode.DIGITAL_OUTPUT) {
                currentDirection &= ~pinAddress;
            }

            // next update direction value
            device.write(REGISTER_IODIR, (byte) currentDirection);

            // enable interrupts; interrupt on any change from previous state
            device.write(REGISTER_GPINTEN, (byte) currentDirection);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // cache mode
        getPinCache(pin).setMode(mode);

        // if any pins are configured as input pins, then we need to start the interrupt monitoring
        // thread
        if (currentDirection > 0) {
            // if the monitor has not been started, then start it now
            if (monitor == null) {
                // start monitoring thread
                monitor = new GpioStateMonitor(device);
                monitor.start();
            }
        } else {
            // shutdown and destroy monitoring thread since there are no input pins configured
            if (monitor != null) {
                monitor.shutdown();
                monitor = null;
            }
        }
    }


    @Override
    public PinMode getMode(Pin pin) {
        return super.getMode(pin);
    }

    @Override
    public void setState(Pin pin, PinState state) {
        // validate
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        // only permit invocation on pins set to DIGITAL_OUTPUT modes
        if (getPinCache(pin).getMode() != PinMode.DIGITAL_OUTPUT) {
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName()
                    + "]; cannot setState() when pin mode is ["
                    + getPinCache(pin).getMode().getName() + "]");
        }
        try {
            // determine pin address
            int pinAddress = pin.getAddress();

            // determine state value for pin bit
            if (state.isHigh()) {
                currentStates |= pinAddress;
            } else {
                currentStates &= ~pinAddress;
            }

            // update state value
            device.write(REGISTER_GPIO, (byte) currentStates);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // cache pin state
        getPinCache(pin).setState(state);
    }

    @Override
    public PinState getState(Pin pin) {
        // call super method to perform validation on pin
        PinState result  = super.getState(pin);
                
        // determine pin address
        int pinAddress = pin.getAddress();
        
        // determine pin state
        result = (currentStates & pinAddress) == pinAddress ? PinState.HIGH : PinState.LOW;

        // cache state
        getPinCache(pin).setState(result);
        
        return result;
    }
    
    @Override
    public void setPullResistance(Pin pin, PinPullResistance resistance) {
        // validate
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        // validate
        if (!pin.getSupportedPinPullResistance().contains(resistance)) {
            throw new UnsupportedPinPullResistanceException(pin, resistance);
        }
        try {
            // determine pin address
            int pinAddress = pin.getAddress();

            // determine pull up value for pin bit
            if (resistance == PinPullResistance.PULL_UP) {
                currentPullup |= pinAddress;
            } else {
                currentPullup &= ~pinAddress;
            }

            // next update pull up resistor value
            device.write(REGISTER_GPPU, (byte) currentPullup);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // cache resistance
        getPinCache(pin).setResistance(resistance);
    }

    @Override
    public PinPullResistance getPullResistance(Pin pin) {
        return super.getPullResistance(pin);
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

            // close the I2C bus communication
            bus.close();
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
                    // only process for interrupts if a pin is configured as an input pin
                    if (currentDirection > 0) {
                        // process interrupts 
                        int pinInterrupt = device.read(REGISTER_INTF);

                        // validate that there is at least one interrupt active 
                        if (pinInterrupt > 0) {
                            // read the current pin states 
                            int pinInterruptState = device.read(REGISTER_GPIO);

                            // loop over the available pins 
                            for (Pin pin : MCP23008Pin.ALL) {
                                // is there an interrupt flag on this pin?
                                if ((pinInterrupt & pin.getAddress()) > 0) {
                                    // System.out.println("INTERRUPT ON PIN [" + pin.getName() + "]");
                                    evaluatePinForChange(pin, pinInterruptState);
                                }
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

        private void evaluatePinForChange(Pin pin, int state) {
            if (getPinCache(pin).isExported()) {
                // determine pin address
                int pinAddress = pin.getAddress();

                if ((state & pinAddress) != (currentStates & pinAddress)) {
                    PinState newState = (state & pinAddress) == pinAddress ? PinState.HIGH
                            : PinState.LOW;

                    // cache state
                    getPinCache(pin).setState(newState);

                    // determine and cache state value for pin bit
                    if (newState.isHigh()) {
                        currentStates |= pinAddress;
                    } else {
                        currentStates &= ~pinAddress;
                    }

                    // change detected for INPUT PIN
                    // System.out.println("<<< CHANGE >>> " + pin.getName() + " : " + state);
                    dispatchPinChangeEvent(pin.getAddress(), newState);
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
