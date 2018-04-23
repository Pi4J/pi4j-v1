package bananapi;
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
 * Copyright (C) 2012 - 2018 Pi4J
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
 * for GPIO pin state changes on the BananaPi.
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
        // explicitly assign the platform as the BananaPi platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.BANANAPI);

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "GPIO Listen (All Pins) Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create GPIO controller
        final GpioController gpio = GpioFactory.getInstance();

        // create GPIO listener
        GpioPinListenerDigital listener  = new GpioPinListenerDigital() {
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
        };

        // by default we will use gpio pin PULL-UP; however, if an argument
        // has been provided, then use the specified pull resistance
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP,  // default pin pull resistance if no pull argument found
                args);                      // argument array to search in

        // prompt user to wait
        console.println(" ... please wait; provisioning GPIO pins with resistance [" + pull + "]");

        // ####################################################################
        //
        // When provisioning a pin, use the BananaPiPin class.
        //
        // Please note that not all GPIO pins support edge triggered interrupts
        // and thus not all pins are eligible for pin state change listeners.
        // These pins must be polled to detect state changes.
        //
        // An example of pins that support interrupt listeners as well as pins
        // that support only polling are included below.
        //
        // ####################################################################

        // provision gpio input pins with its internal pull down resistor set
        GpioPinDigitalInput[] event_pins = {
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_00, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_01, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_02, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_03, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_04, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_05, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_06, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_10, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_11, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_12, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_13, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_14, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_15, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_16, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_17, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_19, pull),
        };

        // create and register gpio pin listeners
        gpio.addListener(listener, event_pins);

        // these pins must be polled for input state changes, these pins do not support edge detection and/or interrupts
        // provision gpio input pins with its internal pull resistors configured
        GpioPinDigitalInput[] polled_pins = {
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_07, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_08), // I2C pin permanently pulled up
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_09), // I2C pin permanently pulled up
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_18, pull),
                gpio.provisionDigitalInputPin(BananaPiPin.GPIO_20, pull),
        };

        // unexport the provisioned GPIO pins when program exits
        gpio.setShutdownOptions(true, event_pins);
        gpio.setShutdownOptions(true, polled_pins);

        // prompt user that we are ready
        console.println(" ... GPIO pins provisioned and ready for use.");
        console.emptyLine();
        console.box("Please complete the GPIO circuit and see",
                "the listener feedback here in the console.");
        console.emptyLine();

        // cache the current state of the polled pins
        for(GpioPinDigitalInput pin : polled_pins){
            pin.setProperty("last_known_state", pin.getState().name());
        }

        // keep program running until user aborts (CTRL-C)
        while(console.isRunning()) {
            Thread.sleep(50);

            // poll pin states looking for pin state changes
            for(GpioPinDigitalInput pin : polled_pins){
                if(!pin.getState().name().equals(pin.getProperty("last_known_state"))){
                    // display pin state on console
                    console.println(" --> GPIO PIN STATE CHANGE (POLLED): " + pin + " = " +
                            ConsoleColor.conditional(
                                    pin.getState().isHigh(), // conditional expression
                                    ConsoleColor.GREEN,      // positive conditional color
                                    ConsoleColor.RED,        // negative conditional color
                                    pin.getState()));        // text to display

                    // update last known state cached value
                    pin.setProperty("last_known_state", pin.getState().name());
                }
            }
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}

