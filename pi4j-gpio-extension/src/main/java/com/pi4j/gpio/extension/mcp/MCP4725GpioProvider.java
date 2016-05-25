package com.pi4j.gpio.extension.mcp;

import com.pi4j.gpio.extension.base.DacGpioProvider;
import com.pi4j.gpio.extension.base.DacGpioProviderBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

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
 * http://www.adafruit.com/product/935
 * Data Sheet: http://www.adafruit.com/datasheets/mcp4725.pdf
 * </p>
 *
 * <p>
 * The MCP4725 is connected via SPI connection to the Raspberry Pi and provides 1 GPIO analog output pin.
 * </p>
 *
 * @author Christian Wehrli, Robert Savage
 *
 */
public class MCP4725GpioProvider extends DacGpioProviderBase implements DacGpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.mcp.MCP4725GpioProvider";
    public static final String DESCRIPTION = "MCP4725 GPIO Provider";
    private boolean i2cBusOwner = false;
    private final I2CBus bus;
    private final I2CDevice device;

    // =======================================================================
    // MCP4725 12-BIT MIN AND MAX VALUES
    // =======================================================================
    public static final int MAX_VALUE = 4095; // 12-bit ADC can produce values from 0 to 4095
    public static final int MIN_VALUE = 0;    // 12-bit ADC can produce values from 0 to 4095

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

    // =======================================================================
    // CONSTRUCTORS
    // =======================================================================

    /**
     * This is the default constructor.  If you use this constructor the
     * MCP4725 will be considered the I2C bus owner and will close the
     * bus communication anytime the class is destroyed and on program
     * shutdown.
     *
     * @param busNumber
     *     the I2C bus number used to communicate with the MCP4725
     * @param address
     *     the address of the MCP4725 on the I2C bus.
     *
     * @throws IOException
     */
    public MCP4725GpioProvider(int busNumber, int address) throws UnsupportedBusNumberException, IOException {
        // create I2C communications bus instance
        this(I2CFactory.getInstance(busNumber), address);
        i2cBusOwner = true;
    }

    /**
     * This is an alternate constructor that can be used to create the
     * MCP4725 instance but not be considered the I2C bus owner and the
     * class will not close the bus communication when the class is destroyed
     * or on program shutdown.
     *
     * @param bus
     *     an existing I2C bus instance defined in the user's code to communicate with the MCP4725
     * @param address
     *     the address of the MCP4725 on the I2C bus.
     *
     * @throws IOException
     */
    public MCP4725GpioProvider(I2CBus bus, int address) throws IOException {
        // seed parent class with all the valid pins for the MCP4725 DAC (only 1 pin).
        super(MCP4725Pin.ALL);

        // set reference to I2C communications bus instance and get I2C device
        this.bus = bus;
        device = bus.getDevice(address);
    }

    /**
     * Set the analog output value to an output pin on the DAC immediately.
     *
     * @param pin analog output pin
     * @param value raw value to send to the DAC. (Between: 0..4095)
     */
    @Override
    public void setValue(Pin pin, double value) {

        // validate range
        if(value <= getMinSupportedValue()){
            value =  getMinSupportedValue();
        }
        else if(value >= getMaxSupportedValue()){
            value = getMaxSupportedValue();
        }

        // the DAC only supports integer values between 0..4095
        int write_value = (int)value;
        try {
            // create data packet and seed targeted value
            byte packet[] = new byte[3];
            packet[0] = (byte) MCP4725_REG_WRITEDAC;
            packet[1] = (byte) (write_value >> 4); // Upper data bits (D11.D10.D9.D8.D7.D6.D5.D4)
            packet[2] = (byte) (write_value << 4); // Lower data bits (D3.D2.D1.D0.x.x.x.x)

            // write packet of data to the I2C bus
            device.write(packet, 0, 3);

            // update the pin cache and dispatch any events
            super.setValue(pin, value);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to write DAC output value.", e);
        }
    }

    /**
     * Gets the name of the DAC provider instance.
     *
     * @return name of the DAC provider instance.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * This method is used by the framework to shutdown the
     * DAC instance and apply any configured shutdown values to the DAC pins.
     *
     * This method will also close the I2C bus for the connected MCP4725.
     */
    @Override
    public void shutdown() {
        // prevent reentrant
        if (isShutdown()) {
            return;
        }
        super.shutdown(); // <-- the shutdown values will be applied to the DAC in the parent class
        try {
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
     * Get the minimum supported analog value for the ADC implementation.
     *
     * @return Returns the minimum supported analog value.
     */
    @Override
    public double getMinSupportedValue(){
        return MIN_VALUE;
    }

    /**
     * Get the maximum supported analog value for the ADC implementation.
     *
     * (For example, a 10 bit ADC's maximum value is 1023 and
     *  a 12-bit ADC's maximum value is 4095.
     *
     * @return Returns the maximum supported analog value.
     */
    @Override
    public double getMaxSupportedValue(){
        return MAX_VALUE;
    }
}
