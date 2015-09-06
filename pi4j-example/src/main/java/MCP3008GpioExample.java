/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MCP3008GpioExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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
import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.gpio.extension.mcp.MCP4725GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP4725Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.spi.SpiChannel;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for analog output pin using the MCP3008 ADC chip.
 * </p>
 *
 * <p>
 * This GPIO provider implements the MCP3008 10-Bit Analog-to-Digital Converter (ADC) as native Pi4J GPIO pins.
 * </p>
 *
 * <p>
 * The MCP3008 is connected via SPI connection to the Raspberry Pi and provides 8 GPIO analog input pins.
 * </p>
 *
 * @author Christian Wehrli
 */
public class MCP3008GpioExample {

    public static void main(String args[]) throws Exception {

        System.out.println("<--Pi4J--> MCP3008 ADC Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // create custom MCP3008 analog gpio provider
        // we must specify which chip select (CS) that that ADC chip is physically connected to.
        final MCP3008GpioProvider gpioProvider = new MCP3008GpioProvider(SpiChannel.CS0);

        // provision gpio analog input pins for all channels of the MCP3008.
        final GpioPinAnalogInput inputs[] = {
                gpio.provisionAnalogInputPin(gpioProvider, MCP3008Pin.CH0, "MyAnalogInput-CH0"),
                gpio.provisionAnalogInputPin(gpioProvider, MCP3008Pin.CH1, "MyAnalogInput-CH1"),
                gpio.provisionAnalogInputPin(gpioProvider, MCP3008Pin.CH2, "MyAnalogInput-CH2"),
                gpio.provisionAnalogInputPin(gpioProvider, MCP3008Pin.CH3, "MyAnalogInput-CH3"),
                gpio.provisionAnalogInputPin(gpioProvider, MCP3008Pin.CH4, "MyAnalogInput-CH4"),
                gpio.provisionAnalogInputPin(gpioProvider, MCP3008Pin.CH5, "MyAnalogInput-CH5"),
                gpio.provisionAnalogInputPin(gpioProvider, MCP3008Pin.CH6, "MyAnalogInput-CH6"),
                gpio.provisionAnalogInputPin(gpioProvider, MCP3008Pin.CH7, "MyAnalogInput-CH7")
        };

// TODO: MCP3008 EVENTING NOT YET WORKING
//
//        // create analog pin value change listener
//        GpioPinListenerAnalog listener = new GpioPinListenerAnalog()
//        {
//            @Override
//            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event)
//            {
//                // get RAW value
//                double value = event.getValue();
//
//                // display output
//                System.out.println("<CHANGED VALUE> [" + event.getPin().getName() + "] : RAW VALUE = " + value);
//            }
//        };
//
//        // register the gpio analog input listener for all input pins
//        gpio.addListener(listener, inputs);

        // keep program running for 10 minutes
        for (int count = 0; count < 600; count++) {

            // get immediate values from each channel
            for(GpioPinAnalogInput input : inputs){
                System.out.println(" - [" + input.getName() + "] : RAW VALUE = " + input.getValue());
            }

            // display output
            Thread.sleep(1000);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
        System.out.println("GOODBYE");
    }
}
