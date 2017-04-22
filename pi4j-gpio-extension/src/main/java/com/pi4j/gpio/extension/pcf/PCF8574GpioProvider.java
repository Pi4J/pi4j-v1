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
 * Copyright (C) 2012 - 2017 Pi4J
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


import com.pi4j.gpio.extension.base.MonitorGpioProvider;
import com.pi4j.gpio.extension.base.MonitorGpioProviderBase;
import com.pi4j.gpio.extension.base.MonitoringStateDeviceBase;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

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
public class PCF8574GpioProvider extends MonitorGpioProviderBase implements MonitorGpioProvider {

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

    private static final int PCF8574_MAX_IO_PINS = PCF8574Pin.ALL.length;

    private boolean i2cBusOwner = false;
    private I2CBus bus;
    private I2CDevice device;
    private BitSet currentStates = new BitSet(PCF8574_MAX_IO_PINS);
    private BitSet outputMask = new BitSet(PCF8574_MAX_IO_PINS);

    public PCF8574GpioProvider(int busNumber, int address) throws UnsupportedBusNumberException, IOException {
        this(busNumber, address, false);
    }
    public PCF8574GpioProvider(int busNumber, int address, boolean disableMonitor) throws UnsupportedBusNumberException, IOException {
        this(busNumber, address, null, disableMonitor);
    }
    public PCF8574GpioProvider(int busNumber, int address, GpioPinDigitalInput irqPin) throws UnsupportedBusNumberException, IOException {
        this(busNumber,address, irqPin, false);
    }
    public PCF8574GpioProvider(int busNumber, int address, GpioPinDigitalInput irqPin, boolean disableMonitor) throws UnsupportedBusNumberException, IOException {
        // create I2C communications bus instance
        this(I2CFactory.getInstance(busNumber), address, irqPin, disableMonitor);
        i2cBusOwner = true;
    }

    public PCF8574GpioProvider(I2CBus bus, int address) throws IOException {
        this(bus, address, false);
    }
    public PCF8574GpioProvider(I2CBus bus, int address, boolean disableMonitor) throws IOException {
        this(bus, address, null, disableMonitor);
    }
    public PCF8574GpioProvider(I2CBus bus, int address, GpioPinDigitalInput irqPin) throws IOException {
        this(bus, address, irqPin, false);
    }
    public PCF8574GpioProvider(I2CBus bus, int address, GpioPinDigitalInput irqPin, boolean disableMonitor) throws IOException {

        // set reference to I2C communications bus instance
        this.bus = bus;

        // create I2C device instance
        device = bus.getDevice(address);

        // set all default pin cache states to match documented chip power up states
        for (Pin pin : PCF8574Pin.ALL) {
            getPinCache(pin).setState(PinState.HIGH);
            currentStates.set(pin.getAddress(), true);
            outputMask.set(pin.getAddress(), true); // All are in input mode at power on
        }

        // start monitoring thread if isn't disabled
        if (!disableMonitor) {
            monitor = new PCF8574GpioProvider.GpioStateMonitor(device, irqPin);
            ((PCF8574GpioProvider.GpioStateMonitor) monitor).start();
        }
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

        // Save the input pin in the mask
        // Will be write a high level on port to conserve this pin as an input
        outputMask.set(pin.getAddress(), mode == PinMode.DIGITAL_INPUT);
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
            byte valueToWrite = currentStates.isEmpty() ? 0 : currentStates.toByteArray()[0];
            byte mask = outputMask.isEmpty() ? 127 : outputMask.toByteArray()[0];

            // Apply a logical OR to set to high level all the input pin
            // The datasheet from NXP (http://www.nxp.com/documents/data_sheet/PCF8574_PCF8574A.pdf page 7) explain
            // that more quickly
            device.write((byte) (valueToWrite | mask));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PinState getState(Pin pin) {
        return super.getState(pin);
    }

    @Override
    public void specificShutdown() throws Exception {
        // if we are the owner of the I2C bus, then close it
        if(i2cBusOwner) {
            // close the I2C bus communication
            bus.close();
        }
    }

    /**
     * This class/thread is used to to actively monitor for GPIO interrupts
     *
     * @author Robert Savage
     */
    private class GpioStateMonitor extends MonitoringStateDeviceBase<I2CDevice, GpioPinDigitalInput> {

        GpioStateMonitor(I2CDevice device, GpioPinDigitalInput irqPin) {
            super(device, irqPin);
        }

        @Override
        protected boolean irqRead() {
            return getIrqPin().isLow();
        }

        @Override
        protected void doRead() throws IOException {
            byte[] buffer = new byte[1];
            getDevice().read(buffer, 0, 1);
            BitSet pinStates = BitSet.valueOf(buffer);

            // determine if there is a pin state difference
            for (int index = 0; index < PCF8574_MAX_IO_PINS; index++) {
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
