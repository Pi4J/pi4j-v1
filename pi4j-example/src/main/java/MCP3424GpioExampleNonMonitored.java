
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MCP3424GpioExampleNonMonitored.java
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

import java.io.IOException;

import com.pi4j.gpio.extension.mcp.MCP3424GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3424Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for GPIO pin state control and monitoring.
 * </p>
 *
 * <p>
 * This example implements the MCP3424 GPIO expansion board.
 * More information about the board can be found here: *
 * http://ww1.microchip.com/downloads/en/DeviceDoc/21952b.pdf
 * </p>
 *
 * <p>
 * The MCP3424 is connected via I2C connection to the Raspberry Pi and provides
 * 16 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 *
 * @author Alexander Falkenstern
 */
public class MCP3424GpioExampleNonMonitored {

    public static void main(String args[]) throws InterruptedException, UnsupportedBusNumberException, IOException {

        System.out.println("<--Pi4J--> MCP3424 GPIO Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // create custom MCP3424 GPIO provider
        final MCP3424GpioProvider provider = new MCP3424GpioProvider(I2CBus.BUS_1, 0x6C, 18, 1);

        // provision gpio input pins from MCP3424
        GpioPinAnalogInput inputs[] = { gpio.provisionAnalogInputPin(provider, MCP3424Pin.GPIO_CH0, "Channel-0"),
                gpio.provisionAnalogInputPin(provider, MCP3424Pin.GPIO_CH1, "Channel-1"),
                gpio.provisionAnalogInputPin(provider, MCP3424Pin.GPIO_CH2, "Channel-2"),
                gpio.provisionAnalogInputPin(provider, MCP3424Pin.GPIO_CH3, "Channel-3") };

        // Keep this sample program running for 10 minutes
        for (int count = 0; count < 600; count++) {
            StringBuilder sb  = new StringBuilder();

            // Print current analog input conversion values from each input channel
            for(GpioPinAnalogInput input : inputs){
                double analog = provider.getAnalogValue(input.getPin());
                sb.append(" \t[" + input.getValue() + " -> " + analog + " V] ");
            }

            // Print out all analog input conversion values
            System.out.println("<MCP3424 VALUES> " + sb.toString());

            Thread.sleep(1000);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();

        System.out.println("<--Pi4J--> Exiting MCP3424 GPIO Example.");
    }
}
