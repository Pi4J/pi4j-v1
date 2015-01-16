/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MCP4725GpioExample.java  
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

import com.pi4j.gpio.extension.mcp.MCP4725GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP4725Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.i2c.I2CBus;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for analog output pin.
 * </p>  
 * 
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
 */
public class MCP4725GpioExample {

    public static void main(String args[]) throws Exception {
        System.out.println("<--Pi4J--> MCP4725 DAC Example ... started.");
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // create gpio provider
        final MCP4725GpioProvider gpioProvider = new MCP4725GpioProvider(I2CBus.BUS_1, MCP4725GpioProvider.MCP4725_ADDRESS_1);

        // create output pin
        final GpioPinAnalogOutput vout = gpio.provisionAnalogOutputPin(gpioProvider, MCP4725Pin.OUTPUT);

        // generate sinus wave on output pin
        for (int i = 0; i < 360; i++) {
            double y = Math.sin(Math.toRadians(i));
            y = y / 2 + 0.5;
            vout.setValue(y);
            if (i == 359) {
                i = 0;
            }
        }
    }
}
