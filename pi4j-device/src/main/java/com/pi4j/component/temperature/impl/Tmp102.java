/*
 * @(#)Tmp102.java   09/12/13
 *
 *
 */



package com.pi4j.component.temperature.impl;

//~--- non-JDK imports --------------------------------------------------------

/*
* #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Tmp102.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.TemperatureSensorBase;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.temperature.TemperatureScale;

//~--- JDK imports ------------------------------------------------------------


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- classes ----------------------------------------------------------------

/**
 *
 * @author andy
 */
public class Tmp102 extends TemperatureSensorBase implements TemperatureSensor {
    int       i2cAddr;
    I2CDevice dev;

    /**
     * Constructs ...
     *
     *
     * @param i2cBus
     * @param i2cAddr
     *
     * @throws IOException
     */
    public Tmp102(int i2cBus, int i2cAddr) throws UnsupportedBusNumberException, IOException {
        this.i2cAddr = i2cAddr;
        this.dev     = I2CFactory.getInstance(i2cBus).getDevice(i2cAddr);
    }

    @Override
    public double getTemperature() {
        double retVal     = 0;
        byte[] tempBuffer = new byte[2];

        try {
            dev.read(tempBuffer, 0, 2);

            int msb = (tempBuffer[0] < 0)
                      ? 256 + tempBuffer[0]
                      : tempBuffer[0];
            int lsb = (tempBuffer[1] < 0)
                      ? 256 + tempBuffer[1]
                      : tempBuffer[1];

            msb = msb << 4;
            lsb = lsb >> 4;

            int count = msb | lsb;

            retVal =  count * 0.0625;
        } catch (IOException ex) {
            Logger.getLogger(Tmp102.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retVal;
    }

    public double getTemperature(TemperatureScale t) {
        double retVal  = 0;
        double rawTemp = getTemperature();

        if (TemperatureScale.FARENHEIT == t) {
            System.out.println("Converting to farenheit");
            System.out.println("RawTemp = " + rawTemp);
            retVal = (rawTemp * 9 / 5) + 32;
        }

        return retVal;
    }

    @Override
    public TemperatureScale getScale() {
        return TemperatureScale.CELSIUS;
    }
}
