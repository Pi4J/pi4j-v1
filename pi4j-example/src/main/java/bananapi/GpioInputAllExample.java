package bananapi;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioInputAllExample.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * This example code demonstrates how to perform simple GPIO
 * pin state reading on the BananaPi platform fro all pins.
 *
 * @author Robert Savage
 */
public class GpioInputAllExample {

    /**
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

        System.out.println("<--Pi4J--> GPIO Input (ALL PINS) Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // by default we will use gpio pin PULL-UP; however, if an argument
        // has been provided, then use the specified pull resistance
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP,  // default pin pull resistance if no pull argument found
                args);                      // argument array to search in

        // ####################################################################
        //
        // When provisioning a pin, use the BananaPiPin class.
        //
        // ####################################################################

        List<GpioPinDigitalInput> provisionedPins = new ArrayList<>();

        // provision GPIO input pins
        for (Pin pin : BananaPiPin.allPins()) {
            try {
                GpioPinDigitalInput provisionedPin = gpio.provisionDigitalInputPin(pin, pull);
                provisionedPin.setShutdownOptions(true); // unexport pin on program shutdown
                provisionedPins.add(provisionedPin);     // add provisioned pin to collection
            }
            catch (Exception ex){
                System.err.println(ex.getMessage());
            }
        }

        // display pin states for all pins
        System.out.println();
        System.out.println("**********************************************************");
        System.out.println();
        for(GpioPinDigitalInput pin : provisionedPins) {
            System.out.println(" [" + pin.toString() + "] state is: " + pin.getState());
        }
        System.out.println();
        System.out.println("**********************************************************");
        System.out.println();

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting GpioInputAllExample");
    }
}
