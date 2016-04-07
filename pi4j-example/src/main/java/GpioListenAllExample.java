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
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.system.SystemInfo;
import com.pi4j.util.CommandArgumentParser;

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

        System.out.println("<--Pi4J--> GPIO Listen (All Pins) Example ... started.");

        // create GPIO controller
        final GpioController gpio = GpioFactory.getInstance();

        // create GPIO listener
        GpioPinListenerDigital listener  = new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            }
        };

        // by default we will use gpio pin PULL-DOWN; however, if an argument
        // has been provided, then use the specified pull resistance
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP,  // default pin pull resistance if no pull argument found
                args);                      // argument array to search in

        List<GpioPinDigitalInput> provisionedPins = new ArrayList<>();
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

        // provision GPIO input pins with its internal pull resistor set
        for (Pin pin : RaspiPin.allPins(board)) {
            try {
                GpioPinDigitalInput provisionedPin = gpio.provisionDigitalInputPin(pin, pull);
                provisionedPins.add(provisionedPin);
            }
            catch (Exception ex){
                System.err.println(ex.getMessage());
            }
        }

        // create and register gpio pin listeners
        gpio.addListener(listener, provisionedPins.toArray(new GpioPinDigitalInput[0]));

        System.out.println(" ... complete the GPIO circuit and see the listener feedback here in the console.");

        // keep program running until user aborts (CTRL-C)
        while(true) {
            Thread.sleep(50);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }
}

