/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MCP4725GpioExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
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

import com.pi4j.gpio.extension.base.DacGpioProvider;
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
 * The MCP4725 is connected via I2C connection to the Raspberry Pi and provides 1 GPIO analog output pin.
 * </p>
 *
 * @author Christian Wehrli, Robert Savage
 */
public class MCP4725GpioExample {

    public static void main(String args[]) throws Exception {
        System.out.println("<--Pi4J--> MCP4725 DAC Example ... started.");
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // create gpio provider
        final DacGpioProvider provider = new MCP4725GpioProvider(
                I2CBus.BUS_1,                          // I2C BUS 1 on newer Raspberry Pi's
                MCP4725GpioProvider.MCP4725_ADDRESS_1  // default address
        );

        // create output pin
        final GpioPinAnalogOutput output = gpio.provisionAnalogOutputPin(provider, MCP4725Pin.OUTPUT);

        // set a startup value for the output pin
        output.setValue(0);

        // set a shutdown value for the output pin
        provider.setShutdownValue(MCP4725GpioProvider.MAX_VALUE, output);

        // generate sinus wave on output pin
        for (int i = 0; i < 360; i++) {
            double y = Math.sin(Math.toRadians(i));
            y = (y / 2 + 0.5) * 100;

            // assign a percentage value instead of raw value
            // (we have to use a helper method within the provider instead of directly on the pin instance)
            provider.setPercentValue(output, y);

            // rollover
            if (i == 359) { i = 0; }
        }

        // generate a square wave on output pin
//        for(int i = 0; i < 100000; i++) {
//            output.setValue(gpioProvider.getMaxSupportedValue());
//            Thread.sleep(2);
//            output.setValue(gpioProvider.getMinSupportedValue());
//            Thread.sleep(2);
//        }

        // When your program is finished, make sure to stop all GPIO activity/threads by shutting
        // down the GPIO controller (this method will forcefully shutdown all GPIO monitoring threads
        // and background scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting MCP4725GpioExample");
    }
}
