/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioInputPollingAllExample.java
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
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.system.SystemInfo;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import com.pi4j.util.ConsoleColor;
import com.sun.glass.ui.PlatformFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This example code demonstrates how to perform simple GPIO
 * pin state reading using a polling loop on the RaspberryPi
 * platform for all pins.
 *
 * @author Robert Savage
 */
public class GpioInputPollingAllExample {

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
    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException, IOException {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "GPIO Input (ALL PINS) Polling Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // by default we will use gpio pin PULL-UP; however, if an argument
        // has been provided, then use the specified pull resistance
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP,  // default pin pull resistance if no pull argument found
                args);                      // argument array to search in

        List<GpioPinDigitalInput> provisionedPins = new ArrayList<>();
        Map<Integer, PinState> lastKnownState = new HashMap<>();
        Pin[] pins;

        // get a collection of raw pins based on the board type (model)
        SystemInfo.BoardType board = SystemInfo.getBoardType();
        if(board == SystemInfo.BoardType.RaspberryPi_ComputeModule) {
            // get all pins for compute module
            pins = RCMPin.allPins();
        }
        else {
            // get exclusive set of pins based on RaspberryPi model (board type)
            pins = RaspiPin.allPins(board);
        }

        // provision GPIO input pins for the target platform and SoC board/system
        for (Pin pin : pins) {
            try {
                GpioPinDigitalInput provisionedPin = gpio.provisionDigitalInputPin(pin, pull);
                provisionedPin.setShutdownOptions(true); // unexport pin on program shutdown
                provisionedPins.add(provisionedPin);     // add provisioned pin to collection
                lastKnownState.put(pin.getAddress(), null);
            }
            catch (Exception ex){
                System.err.println(ex.getMessage());
            }
        }

        // prompt user that we are ready
        console.println(" ... Successfully provisioned all GPIO input pins");
        console.emptyLine();
        console.box("Polling for GPIO state changes.", "The GPIO input pins states will be displayed below.");
        console.emptyLine();

        // display pin state
        while(!console.exiting()) {

            // display pin states for all pins
            for(GpioPinDigitalInput input : provisionedPins) {

                // get pin address and current state
                Integer address = input.getPin().getAddress();
                PinState state = input.getState();

                // if the pin state has changed, then print out new pin state
                if(lastKnownState.containsKey(address) && lastKnownState.get(address) != state){
                    lastKnownState.put(address, state);
                    console.println(" [" + input.toString() + "] digital state is: " + ConsoleColor.conditional(
                            state.isHigh(),      // conditional expression
                            ConsoleColor.GREEN,  // positive conditional color
                            ConsoleColor.RED,    // negative conditional color
                            input.getState()));
                }
            }
            Thread.sleep(50);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}
