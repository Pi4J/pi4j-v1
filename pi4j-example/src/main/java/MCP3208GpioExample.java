/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MCP3208GpioExample.java
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
import com.pi4j.gpio.extension.mcp.MCP3208GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3208Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.spi.SpiChannel;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for analog output pin using the MCP3208 ADC chip.
 * </p>
 *
 * <p>
 * This GPIO provider implements the MCP3208 12-Bit Analog-to-Digital Converter (ADC) as native Pi4J GPIO pins.
 * </p>
 *
 * <p>
 * The MCP3208 is connected via SPI connection to the Raspberry Pi and provides 8 GPIO analog input pins.
 * </p>
 *
 * @author Robert Savage
 */
public class MCP3208GpioExample {

    public static void main(String args[]) throws Exception {

        System.out.println("<--Pi4J--> MCP3208 ADC Example ... started.");

        // Create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // Create custom MCP3208 analog gpio provider
        // we must specify which chip select (CS) that that ADC chip is physically connected to.
        final AdcGpioProvider provider = new MCP3208GpioProvider(SpiChannel.CS0);

        // Provision gpio analog input pins for all channels of the MCP3208.
        // (you don't have to define them all if you only use a subset in your project)
        final GpioPinAnalogInput inputs[] = {
                gpio.provisionAnalogInputPin(provider, MCP3208Pin.CH0, "MyAnalogInput-CH0"),
                gpio.provisionAnalogInputPin(provider, MCP3208Pin.CH1, "MyAnalogInput-CH1"),
                gpio.provisionAnalogInputPin(provider, MCP3208Pin.CH2, "MyAnalogInput-CH2"),
                gpio.provisionAnalogInputPin(provider, MCP3208Pin.CH3, "MyAnalogInput-CH3"),
                gpio.provisionAnalogInputPin(provider, MCP3208Pin.CH4, "MyAnalogInput-CH4"),
                gpio.provisionAnalogInputPin(provider, MCP3208Pin.CH5, "MyAnalogInput-CH5"),
                gpio.provisionAnalogInputPin(provider, MCP3208Pin.CH6, "MyAnalogInput-CH6"),
                gpio.provisionAnalogInputPin(provider, MCP3208Pin.CH7, "MyAnalogInput-CH7")
        };


        // Define the amount that the ADC input conversion value must change before
        // a 'GpioPinAnalogValueChangeEvent' is raised.  This is used to prevent unnecessary
        // event dispatching for an analog input that may have an acceptable or expected
        // range of value drift.
        provider.setEventThreshold(100, inputs); // all inputs; alternatively you can set thresholds on each input discretely

        // Set the background monitoring interval timer for the underlying framework to
        // interrogate the ADC chip for input conversion values.  The acceptable monitoring
        // interval will be highly dependant on your specific project.  The lower this value
        // is set, the more CPU time will be spend collecting analog input conversion values
        // on a regular basis.  The higher this value the slower your application will get
        // analog input value change events/notifications.  Try to find a reasonable balance
        // for your project needs.
        provider.setMonitorInterval(250); // milliseconds

        // Print current analog input conversion values from each input channel
        for(GpioPinAnalogInput input : inputs){
            System.out.println("<INITIAL VALUE> [" + input.getName() + "] : RAW VALUE = " + input.getValue());
        }

        // Create an analog pin value change listener
        GpioPinListenerAnalog listener = new GpioPinListenerAnalog()
        {
            @Override
            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event)
            {
                // get RAW value
                double value = event.getValue();

                // display output
                System.out.println("<CHANGED VALUE> [" + event.getPin().getName() + "] : RAW VALUE = " + value);
            }
        };

        // Register the gpio analog input listener for all input pins
        gpio.addListener(listener, inputs);

        // Keep this sample program running for 10 minutes
        for (int count = 0; count < 600; count++) {
            Thread.sleep(1000);
        }

        // When your program is finished, make sure to stop all GPIO activity/threads by shutting
        // down the GPIO controller (this method will forcefully shutdown all GPIO monitoring threads
        // and background scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting MCP3208GpioExample");
    }
}
