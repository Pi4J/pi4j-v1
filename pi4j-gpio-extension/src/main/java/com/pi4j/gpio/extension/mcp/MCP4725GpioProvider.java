package com.pi4j.gpio.extension.mcp;

import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP4725GpioProvider.java  
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

/**
 * <p>
 * This GPIO provider implements the MCP4725 12-Bit Digital-to-Analog Converter as native Pi4J GPIO pins.
 * More information about the board can be found here: 
 * http://http://www.adafruit.com/product/935
 * </p>
 * 
 * <p>
 * The MCP4725 is connected via SPI connection to the Raspberry Pi and provides 1 GPIO analog output pin.
 * </p>
 * 
 * @author Christian Wehrli
 * 
 */
public class MCP4725GpioProvider extends GpioProviderBase {

    public static final String NAME = "com.pi4j.gpio.extension.mcp.MCP4725GpioProvider";
    public static final String DESCRIPTION = "MCP4725 GPIO Provider";
    private boolean i2cBusOwner = false;
    private final I2CBus bus;
    private final I2CDevice device;

    // =======================================================================
    // MCP4725 I2C ADDRESS
    // =======================================================================
    public static final int MCP4725_ADDRESS_1 = 0x62; // ADDRESS 1 : 0x62 (01100010) ADR -> GND (default)
    public static final int MCP4725_ADDRESS_2 = 0x63; // ADDRESS 2 : 0x63 (01100011) ADR -> VDD

    // =======================================================================
    // WRITE REGISTER
    // =======================================================================
    private static final int MCP4725_REG_WRITEDAC = 0x40; // Writes data to the DAC
    private static final int MCP4725_REG_WRITEDAC_EEPROM = 0x60; // not used yet... writes data to the DAC and the EEPROM (persisting the assigned value after reset)

    public MCP4725GpioProvider(int busNumber, int address) throws IOException {
        // create I2C communications bus instance
        this(I2CFactory.getInstance(busNumber), address);
        i2cBusOwner = true;
    }

    public MCP4725GpioProvider(I2CBus bus, int address) throws IOException {

        // set reference to I2C communications bus instance
        this.bus = bus;

        device = bus.getDevice(address);
        resetOutput();
    }

    /**
     * Set output to 0V
     */
    private void resetOutput() {
        try {
            byte packet[] = new byte[3];
            packet[0] = (byte) MCP4725_REG_WRITEDAC;
            packet[1] = (byte) 0x00;
            packet[2] = (byte) 0x00;
            device.write(packet, 0, 3);
        } catch (IOException e) {
            throw new RuntimeException("Unable to reset DAC output.", e);
        }
    }

    @Override
    public void setValue(Pin pin, double percentage) {
        super.setValue(pin, percentage);
        int value = (int) (4095 * percentage); // Value can be 0..4095
        try {
            byte packet[] = new byte[3];
            packet[0] = (byte) MCP4725_REG_WRITEDAC;
            packet[1] = (byte) (value >> 4); // Upper data bits (D11.D10.D9.D8.D7.D6.D5.D4)
            packet[2] = (byte) (value << 4); // Lower data bits (D3.D2.D1.D0.x.x.x.x)
            device.write(packet, 0, 3);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write DAC output value.", e);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void shutdown() {
        if (isShutdown()) {
            return;
        }
        super.shutdown();
        try {
            resetOutput();

            // if we are the owner of the I2C bus, then close it
            if(i2cBusOwner) {
                // close the I2C bus communication
                bus.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
