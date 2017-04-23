/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PCF8574GpioIrqExample.java
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

import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.io.IOException;

/**
 * <p>The PCF8574 is connected via I2C connection to the Raspberry Pi and provides
 * 8 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * <p>
 * The IRQ output on PCF8574 is connected on GPIO_01
 * </p>
 *
 * @author Gregory DEPUILLE
 */
public class PCF8574GpioIrqExample {

    public static void main(String args[]) throws InterruptedException, UnsupportedBusNumberException, IOException {

        System.out.println("<--Pi4J--> PCF8574 GPIO with interrupt on GPIO input Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalInput irqPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01);

        // create custom PCF8574 GPIO provider
        final PCF8574GpioProvider provider = new PCF8574GpioProvider(I2CBus.BUS_1, PCF8574GpioProvider.PCF8574A_0x38, irqPin);

        // provision gpio input pins from PCF8574
        GpioPinDigitalInput in0 = gpio.provisionDigitalInputPin(provider, PCF8574Pin.GPIO_00);
        GpioPinDigitalInput in1 = gpio.provisionDigitalInputPin(provider, PCF8574Pin.GPIO_01);
        GpioPinDigitalInput in2 = gpio.provisionDigitalInputPin(provider, PCF8574Pin.GPIO_02);

        // create and register gpio pin listener
        GpioPinListenerDigital l = (event) -> System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
        gpio.addListener(l, in0, in1, in2);

        // provision gpio output pins and make sure they are all LOW at startup
        GpioPinDigitalOutput out5 = gpio.provisionDigitalOutputPin(provider, PCF8574Pin.GPIO_05, PinState.LOW);
        GpioPinDigitalOutput out6 = gpio.provisionDigitalOutputPin(provider, PCF8574Pin.GPIO_06, PinState.LOW);
        GpioPinDigitalOutput out7 = gpio.provisionDigitalOutputPin(provider, PCF8574Pin.GPIO_07, PinState.LOW);

        // on program shutdown, set the pins back to their default state: HIGH
        gpio.setShutdownOptions(true, PinState.HIGH, out5, out6, out7);

        // keep program running for 30 seconds
        for (int count = 0; count < 30; count++) {
            out5.setState(count % 2 > 0);
            out6.setState(count % 3 > 0);
            out7.setState(count % 4 > 0);
            Thread.sleep(1000);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting PCF8574GpioIrqExample");
    }
}
