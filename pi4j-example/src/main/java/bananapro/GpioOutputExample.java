package bananapro;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioOutputExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import com.pi4j.io.gpio.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.util.CommandArgumentParser;

/**
 * This example code demonstrates how to perform simple state
 * control of a GPIO pin on the BananaPro.
 *
 * @author Robert Savage
 */
public class GpioOutputExample {

    /**
     * [ARGUMENT/OPTION "--pin (#)" | "-p (#)" ]
     * This example program accepts an optional argument for specifying the GPIO pin (by number)
     * to use with this GPIO listener example. If no argument is provided, then GPIO #1 will be used.
     * -- EXAMPLE: "--pin 4" or "-p 0".
     *
     * @param args
     * @throws InterruptedException
     * @throws PlatformAlreadyAssignedException
     */
    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException {

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // explicitly assign the platform as the BananaPro platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.BANANAPRO);

        System.out.println("<--Pi4J--> GPIO Control Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // ####################################################################
        //
        // When provisioning a pin, use the BananaProPin class.
        //
        // ####################################################################

        // by default we will use gpio pin #01; however, if an argument
        // has been provided, then lookup the pin by address
        Pin pin = CommandArgumentParser.getPin(
                BananaProPin.class,    // pin provider class to obtain pin instance from
                BananaProPin.GPIO_01,  // default pin if no pin argument found
                args);                 // argument array to search in

        // provision gpio pin as an output pin and turn on
        final GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(pin, "MyLED", PinState.HIGH);

        // set shutdown state for this pin: keep as output pin, set to lo state
        output.setShutdownOptions(false, PinState.LOW);

        System.out.println("--> GPIO [" + pin.toString() + "] state should be: ON");

        Thread.sleep(1000);

        // turn off gpio pin #01
        output.low();
        System.out.println("--> GPIO [" + pin.toString() + "] state should be: OFF");

        Thread.sleep(1000);

        // toggle the current state of gpio pin #01 (should turn on)
        output.toggle();
        System.out.println("--> GPIO [" + pin.toString() + "] state should be: ON");

        Thread.sleep(1000);

        // toggle the current state of gpio pin #01  (should turn off)
        output.toggle();
        System.out.println("--> GPIO [" + pin.toString() + "] state should be: OFF");

        Thread.sleep(1000);

        // turn on gpio pin #01 for 1 second and then off
        System.out.println("--> GPIO [" + pin.toString() + "] state should be: ON for only 1 second");
        output.pulse(1000, true); // set second argument to 'true' use a blocking call

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting GpioOutputExample");
    }
}
