package odroid.xu4;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioListenAllExample.java
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

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import com.pi4j.util.ConsoleColor;

/**
 * This example code demonstrates how to setup a listener
 * for GPIO pin state changes on the Odroid XU4 platform.
 *
 * @author Robert Savage
 */
public class GpioListenAllExample {

    /**
     * [ARGUMENT/OPTION "--pull (up|down|off)" | "-l (up|down|off)" | "--up" | "--down" ]
     * This example program accepts an optional argument for specifying pin pull resistance.
     * Supported values: "up|down" (or simply "1|0").   If no value is specified in the command
     * argument, then the pin pull resistance will be set to PULL_UP by default.
     * -- EXAMPLES: "--pull up", "-pull down", "--pull off", "--up", "--down", "-pull 0", "--pull 1", "-l up", "-l down".
     *
     * @param args
     * @throws InterruptedException
     * @throws PlatformAlreadyAssignedException
     */
    public static void main(String args[]) throws InterruptedException, PlatformAlreadyAssignedException {

        // ####################################################################
        //
        // !!!!! ATTENTION !!!!!  ALL GPIO PINS ON ODROID-XU4 ARE 1.8VDC.
        //
        // THIS MEANS THAT YOU MUST USE A LEVEL SHIFTER IF USING WITH A 3.3VDC/5VDC CIRCUIT.
        // YOU CAN USE THE OPTIONAL ODROID XU4-SHIFTER SHIELD TO PERFORM THE LEVEL SHIFTING:
        //  http://www.hardkernel.com/main/products/prdt_info.php?g_code=G143556253995
        //
        // ####################################################################

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // explicitly assign the platform as the Odroid platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.ODROID);

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "GPIO Listen (All Pins) Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create GPIO controller
        final GpioController gpio = GpioFactory.getInstance();

        // by default we will use gpio pin PULL-UP; however, if an argument
        // has been provided, then use the specified pull resistance
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP,  // default pin pull resistance if no pull argument found
                args);                      // argument array to search in

        // ####################################################################
        //
        // IF YOU ARE USING AN ODROID XU4 PLATFORM, THEN ...
        //    When provisioning a pin, use the OdroidXU4Pin class.
        //
        // ####################################################################

        // prompt user to wait
        console.println(" ... please wait; provisioning GPIO pins with resistance [" + pull + "]");

        // provision gpio input pins with its internal pull down resistor set
        GpioPinDigitalInput[] pins = {
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_00, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_01, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_02, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_03, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_04, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_05, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_06, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_07, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_08, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_09, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_10, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_11, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_12, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_13, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_14, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_15, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_16, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_21, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_22, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_23, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_26, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_27, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_30, pull),
                gpio.provisionDigitalInputPin(OdroidXU4Pin.GPIO_31, pull)
        };

        // unexport the provisioned GPIO pins when program exits
        gpio.setShutdownOptions(true, pins);

        // prompt user that we are ready
        console.println(" ... GPIO pins provisioned and ready for use.");
        console.emptyLine();
        console.box("Please complete the GPIO circuit and see",
                "the listener feedback here in the console.");
        console.emptyLine();

        // --------------------------------
        // EVENT-BASED GPIO PIN MONITORING
        // --------------------------------

        // create and register gpio pin listeners for event pins
        gpio.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                console.println(" --> GPIO PIN STATE CHANGE (EVENT): " + event.getPin() + " = " +
                                ConsoleColor.conditional(
                                        event.getState().isHigh(), // conditional expression
                                        ConsoleColor.GREEN,        // positive conditional color
                                        ConsoleColor.RED,          // negative conditional color
                                        event.getState()));        // text to display
            }
        }, pins);

        // wait (block) for user to exit program using CTRL-C
        console.waitForExit();

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}
