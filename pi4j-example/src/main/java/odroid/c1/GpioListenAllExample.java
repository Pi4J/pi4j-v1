package odroid.c1;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioListenAllExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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
import com.pi4j.util.Console;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.ConsoleColor;

/**
 * This example code demonstrates how to setup a listener
 * for GPIO pin state changes on the Odroid C1/C1+ platform.
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

        // --------------------
        // !! ATTENTION !!
        // --------------------
        // The Odroid C1/C1+ only permits up to four GPIO pins to be configured with
        // edge detection for both "rising" and "falling" edges (a.k.a., "both").
        // Thus, you can only use a maximum of four GPIO input pins with listener
        // events. Alternatively, you can manually poll for GPIO pin state changes.
        //
        // Let's first explicitly unexport all Odroid C1 pins to make sure no existing
        // pins are already consuming the 4 available edge interrupt slots.
        gpio.unexport(OdroidC1Pin.allPins());

        // by default we will use gpio pin PULL-UP; however, if an argument
        // has been provided, then use the specified pull resistance
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP,  // default pin pull resistance if no pull argument found
                args);                      // argument array to search in

        // ####################################################################
        //
        // IF YOU ARE USING AN ODROID C1/C1+ PLATFORM, THEN ...
        //    When provisioning a pin, use the OdroidC1Pin class.
        //
        // ####################################################################

        // prompt user to wait
        console.println(" ... please wait; provisioning GPIO pins with resistance [" + pull + "]");

        // --------------------
        // !! ATTENTION !!
        // --------------------
        // The Odroid C1/C1+ only permits up to four GPIO pins to be configured with
        // edge detection for both "rising" and "falling" edges (a.k.a., "both").
        // Thus, you can only use a maximum of four GPIO input pins with listener
        // events.

        // provision gpio input pins with its internal pull down resistor set
        GpioPinDigitalInput[] event_pins = {
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_00, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_01, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_02, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_03, pull)
        };

        // unexport the event-based GPIO pin when program exits
        gpio.setShutdownOptions(true, event_pins);

        // we will use a manually polling approach to detect the pin state changes on the remaining pins
        // provision gpio input pins with its internal pull down resistor set
        GpioPinDigitalInput[] polled_pins = {
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_04, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_05, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_06, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_07, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_10, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_11, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_12, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_13, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_14, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_21, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_22, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_23, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_24, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_26, pull),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_27, pull),
        };

        // unexport the polling-based GPIO pin when program exits
        gpio.setShutdownOptions(true, polled_pins);

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
        }, event_pins);

        // --------------------------------
        // POLLING-BASED GPIO PIN MONITORING
        // --------------------------------
        // keep program running until user aborts (CTRL-C)
        while (console.isRunning()) {
            // provide a little delay; we don't want to fully consume the processor
            Thread.sleep(50);

            // poll pin states looking for pin state changes
            for (GpioPinDigitalInput pin : polled_pins) {
                if (!pin.getState().name().equals(pin.getProperty("last_known_state"))) {
                    if(pin.getProperty("last_known_state") != null) {
                        // display pin state on console
                        console.println(" --> GPIO PIN STATE CHANGE (POLLED): " + pin + " = " +
                                ConsoleColor.conditional(
                                        pin.getState().isHigh(), // conditional expression
                                        ConsoleColor.GREEN,      // positive conditional color
                                        ConsoleColor.RED,        // negative conditional color
                                        pin.getState()));        // text to display

                    }
                    pin.setProperty("last_known_state", pin.getState().name());
                }
            }
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}
