package bananapi;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioInputExample.java
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
 * This example code demonstrates how to perform simple GPIO
 * pin state reading on the BananaPi platform.
 *
 * @author Robert Savage
 */
public class GpioInputExample {

    /**
     * [ARGUMENT/OPTION "--pin (#)" | "-p (#)" ]
     * This example program accepts an optional argument for specifying the GPIO pin (by number)
     * to use with this GPIO listener example. If no argument is provided, then GPIO #1 will be used.
     * -- EXAMPLE: "--pin 4" or "-p 0".
     *
     * [ARGUMENT/OPTION "--pull (up|down)" | "-u (up|down)" | "--up" | "--down" ]
     * This example program accepts an optional argument for specifying pin pull resistance.
     * Supported values: "up|down" (or simply "1|0").   If no value is specified in the command
     * argument, then the pin pull resistance will be set to PULL_UP by default.
     * -- EXAMPLES: "--pull up", "-pull down", "--up", "--down", "-pull 0", "--pull 1", "-u up", "-u down.
     *
     * @param args
     * @throws InterruptedException
     * @throws PlatformAlreadyAssignedException
     */
    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException {

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // explicitly assign the platform as the BananaPi platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.BANANAPI);

        System.out.println("<--Pi4J--> GPIO Input Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // ####################################################################
        //
        // When provisioning a pin, use the BananaPiPin class.
        //
        // ####################################################################

        // by default we will use gpio pin #01; however, if an argument
        // has been provided, then lookup the pin by address
        Pin pin = CommandArgumentParser.getPin(
                BananaPiPin.class,    // pin provider class to obtain pin instance from
                BananaPiPin.GPIO_01,  // default pin if no pin argument found
                args);                // argument array to search in

        // by default we will use gpio pin PULL-UP; however, if an argument
        // has been provided, then use the specified pull resistance
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP,  // default pin pull resistance if no pull argument found
                args);                      // argument array to search in

        // provision gpio pin as an output pin and turn on
        final GpioPinDigitalInput input = gpio.provisionDigitalInputPin(pin, "MyInput", pull);

        // set shutdown state for this pin: unexport the pin
        input.setShutdownOptions(true);

        // prompt user that we are ready
        System.out.println("Successfully provisioned [" + pin + "] with PULL resistance = [" + pull + "]");

        // get current pin state
        PinState state = input.getState();

        // display pin state
        System.out.println();
        System.out.println("**********************************************************");
        System.out.println();
        System.out.println(" [" + pin.toString() + "] state is: " + state);
        System.out.println();
        System.out.println("**********************************************************");
        System.out.println();

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting GpioInputExample");
    }
}
