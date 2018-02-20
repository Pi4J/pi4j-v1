package com.pi4j.device.envirophat.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  EnviropHatDevice.java
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

import com.pi4j.device.DeviceBase;
import com.pi4j.device.envirophat.EnviropHat;
import com.pi4j.device.envirophat.EnviropHatException;
import com.pi4j.device.envirophat.EnviropHatTempType;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public class EnviropHatDevice extends DeviceBase implements EnviropHat {

    private static final double DEFAULT_TEMP = Double.MIN_VALUE;

    private I2CDevice device;

    public EnviropHatDevice() throws EnviropHatException {

        try {
            // Create I2C bus
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);

            // Get I2C device, BMP280 I2C address is 0x76(108)
            device = bus.getDevice(0x77);

        } catch (IOException | I2CFactory.UnsupportedBusNumberException e) {
            throw new EnviropHatException("Can't init Enviro pHat", e);
        }
    }

    @Override
    public double temperature(EnviropHatTempType tempType) throws EnviropHatException {

        double result = DEFAULT_TEMP;

        try {
            // Read 24 bytes of data from address 0x88(136)
            byte[] b1 = new byte[24];
            device.read(0x88, b1, 0, 24);

            // Convert the data
            // temp coefficients
            int dig_T1 = (b1[0] & 0xFF) + ((b1[1] & 0xFF) * 256);
            int dig_T2 = (b1[2] & 0xFF) + ((b1[3] & 0xFF) * 256);
            if (dig_T2 > 32767) {
                dig_T2 -= 65536;
            }
            int dig_T3 = (b1[4] & 0xFF) + ((b1[5] & 0xFF) * 256);
            if (dig_T3 > 32767) {
                dig_T3 -= 65536;
            }

            // Select control measurement register
            // Normal mode, temp and pressure over sampling rate = 1
            device.write(0xF4, (byte) 0x27);

            // Select config register
            // Stand_by time = 1000 ms
            device.write(0xF5, (byte) 0xA0);
            Thread.sleep(500);

            // Read 8 bytes of data from address 0xF7(247)
            // pressure msb1, pressure msb, pressure lsb, temp msb1, temp msb, temp lsb, humidity lsb, humidity msb
            byte[] data = new byte[8];
            device.read(0xF7, data, 0, 8);

            // Convert pressure and temperature data to 19-bits
            long adc_t = (((long) (data[3] & 0xFF) * 65536) + ((long) (data[4] & 0xFF) * 256) + (long) (data[5] & 0xF0)) / 16;

            // Temperature offset calculations
            double var1 = (((double) adc_t) / 16384.0 - ((double) dig_T1) / 1024.0) * ((double) dig_T2);
            double var2 = ((((double) adc_t) / 131072.0 - ((double) dig_T1) / 8192.0) *
                    (((double) adc_t) / 131072.0 - ((double) dig_T1) / 8192.0)) * ((double) dig_T3);

            double cTemp = (var1 + var2) / 5120.0;
            double fTemp = cTemp * 1.8 + 32;

            switch (tempType) {
                case CELSIUS:
                    result = cTemp;
                    break;
                case FAHRENHEIT:
                    result = fTemp;
                    break;
            }

        } catch (InterruptedException | IOException e) {
            throw new EnviropHatException("Can't load data from Enviro pHat", e);
        }

        return result;
    }

}
