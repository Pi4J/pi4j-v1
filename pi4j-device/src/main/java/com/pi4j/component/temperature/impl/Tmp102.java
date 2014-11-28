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
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.TemperatureSensorBase;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
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
    public Tmp102(int i2cBus, int i2cAddr) throws IOException {
        this.i2cAddr = i2cAddr;
        this.dev     = I2CFactory.getInstance(i2cBus).getDevice(i2cAddr);
    }

    @Override
    public double getTemperature() {
        double retVal     = 0;
        int    temp       = 0;
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
