/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioListenAllExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.system.SystemInfo;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import com.pi4j.util.ConsoleColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This example code demonstrates how to setup a listener
 * for all available GPIO pins on the RaspberryPi (by specific model).
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
     * @throws IOException
     */
    public static void main(String args[]) throws InterruptedException, PlatformAlreadyAssignedException, IOException {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "GPIO Listen (All Pins) Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create GPIO controller
        final GpioController gpio = GpioFactory.getInstance();

        // by default we will use gpio pin PULL-DOWN; however, if an argument
        // has been provided, then use the specified pull resistance
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP,  // default pin pull resistance if no pull argument found
                args);                      // argument array to search in

        // prompt user to wait
        console.println(" ... please wait; provisioning GPIO pins with resistance [" + pull + "]");

        // create pins collection to store provisioned pin instances
        List<GpioPinDigitalInput> provisionedPins = new ArrayList<>();
        Pin[] pins;

        // get a collection of raw pins based on the board type (model)
        SystemInfo.BoardType board = SystemInfo.getBoardType();
        if(board == SystemInfo.BoardType.RaspberryPi_ComputeModule ||
           board == SystemInfo.BoardType.RaspberryPi_ComputeModule3 ||
           board == SystemInfo.BoardType.RaspberryPi_ComputeModule3_Plus) {
            // get all pins for compute module 1 & 3 (CM4 uses 'RaspiPin' pin provider)
            pins = RCMPin.allPins();
        }
        else {
            // get exclusive set of pins based on RaspberryPi model (board type)
            pins = RaspiPin.allPins(board);
        }

        // provision GPIO input pins with its internal pull resistor set
        for (Pin pin : pins) {
            try {
                GpioPinDigitalInput provisionedPin = gpio.provisionDigitalInputPin(pin, pull);
                provisionedPins.add(provisionedPin);

                // un-export the provisioned GPIO pins when program exits
                provisionedPin.setShutdownOptions(true);
            }
            catch (Exception ex){
                System.err.println(ex.getMessage());
            }
        }

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
        }, provisionedPins.toArray(new GpioPinDigitalInput[0]));

        // wait (block) for user to exit program using CTRL-C
        console.waitForExit();

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}

