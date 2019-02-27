/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MCP3204GpioExampleNonMonitored.java
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
import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3204GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3204Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for analog output pin using the MCP3204 ADC chip.  This example
 * configures the MCP3204 without background monitoring and eventing.
 * </p>
 *
 * <p>
 * This GPIO provider implements the MCP3204 12-Bit Analog-to-Digital Converter (ADC) as native Pi4J GPIO pins.
 * </p>
 *
 * <p>
 * The MCP3204 is connected via SPI connection to the Raspberry Pi and provides 8 GPIO analog input pins.
 * </p>
 *
 * @author Christian Wehrli, Robert Savage
 */
public class MCP3204GpioExampleNonMonitored {

    public static void main(String args[]) throws Exception {

        System.out.println("<--Pi4J--> MCP3204 ADC Example (NON-MONITORED) ... started.");

        // Create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // Create custom MCP3204 analog gpio provider
        // we must specify which chip select (CS) that that ADC chip is physically connected to.
        final AdcGpioProvider provider = new MCP3204GpioProvider(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED,
                SpiDevice.DEFAULT_SPI_MODE,
                false);   // <<-- the 'false' value here disable the base background monitoring thread

        // So why would I want to disable the background monitoring thread?
        // Well, that depends on how you plan on integrating this into your project.
        // If you need/want pin event notification, then you must keep the background
        // monitoring thread enabled.  If you only need to periodically obtain analog
        // input conversion values or only need to acquire the value as the result of
        // some other event or condition in your application, then you can disable the
        // background monitoring thread to reduce the runtime overhead.
        // If its disabled, then anytime you request the pin.getValue() to get an analog
        // conversion value it will get the value directly from the ADC chip synchronously
        // in your process call.  If background monitoring is enabled, then calls to
        // pin.getValue() return you the last acquired (cached) value and does not
        // perform an immediate data acquisition.

        // Provision gpio analog input pins for all channels of the MCP3204.
        // (you don't have to define them all if you only use a subset in your project)
        final GpioPinAnalogInput inputs[] = {
                gpio.provisionAnalogInputPin(provider, MCP3204Pin.CH0, "MyAnalogInput-CH0"),
                gpio.provisionAnalogInputPin(provider, MCP3204Pin.CH1, "MyAnalogInput-CH1"),
                gpio.provisionAnalogInputPin(provider, MCP3204Pin.CH2, "MyAnalogInput-CH2"),
                gpio.provisionAnalogInputPin(provider, MCP3204Pin.CH3, "MyAnalogInput-CH3")
        };

        // Keep this sample program running for 10 minutes
        for (int count = 0; count < 600; count++) {
            StringBuilder sb  = new StringBuilder();

            // Print current analog input conversion values from each input channel
            for(GpioPinAnalogInput input : inputs){
                sb.append(" \t[" + input.getValue() + "] ");
            }

            // Print out all analog input conversion values
            System.out.println("<MCP3204 VALUES> " + sb.toString());

            Thread.sleep(1000);
        }

        // When your program is finished, make sure to stop all GPIO activity/threads by shutting
        // down the GPIO controller (this method will forcefully shutdown all GPIO monitoring threads
        // and background scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting MCP3204GpioExampleNonMonitored");
    }
}
