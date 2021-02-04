/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioOutputAllExample.java
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
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * This example code demonstrates how to perform simple state
 * control of all GPIO pins on the RaspberryPi.
 *
 * @author Robert Savage
 */
public class GpioOutputAllExample {

    /**
     * @param args
     * @throws InterruptedException
     * @throws PlatformAlreadyAssignedException
     */
    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException, IOException {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();
        final Scanner scanner = new Scanner(System.in);
        GpioPinDigitalOutput selectedOutput;

        // print program title/header
        console.title("<-- The Pi4J Project -->", "GPIO Output All Example");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        Map<Integer, GpioPinDigitalOutput> provisionedPins = new LinkedHashMap<>();
        Pin[] pins;
        Pin pinArg;

        // get a collection of raw pins based on the board type (model)
        SystemInfo.BoardType board = SystemInfo.getBoardType();
        if(board == SystemInfo.BoardType.RaspberryPi_ComputeModule ||
                board == SystemInfo.BoardType.RaspberryPi_ComputeModule3 ||
                board == SystemInfo.BoardType.RaspberryPi_ComputeModule3_Plus) {
            // get all pins for compute module 1 & 3 (CM4 uses 'RaspiPin' pin provider)
            pins = RCMPin.allPins();

            // by default we will use gpio pin #0; however, if an argument
            // has been provided, then lookup the pin by address
            pinArg = CommandArgumentParser.getPin(
                    RaspiPin.class,    // pin provider class to obtain pin instance from
                    RCMPin.GPIO_00,    // default pin if no pin argument found
                    args);             // argument array to search in
        }
        else {
            // get exclusive set of pins based on RaspberryPi model (board type)
            pins = RaspiPin.allPins(board);

            // by default we will use gpio pin #0; however, if an argument
            // has been provided, then lookup the pin by address
            pinArg = CommandArgumentParser.getPin(
                    RaspiPin.class,    // pin provider class to obtain pin instance from
                    RaspiPin.GPIO_00,  // default pin if no pin argument found
                    args);             // argument array to search in
        }

        console.println(" Please wait .. provisioning all GPIO output pins");
        console.emptyLine();

        // provision GPIO input pins for the target platform and SoC board/system
        for (Pin pin : pins) {
            try {
                // provision gpio pin as an output pin and turn off
                GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(pin, "My Output", PinState.LOW);
                provisionedPins.put(pin.getAddress(), output);     // add provisioned output pin to collection

                // set shutdown state for this pin: keep as output pin, set to low state
                output.setShutdownOptions(false, PinState.LOW);

                console.println(" ... provisioned pin: [" + pin + "] to state [" + ConsoleColor.conditional(
                        output.getState().isHigh(), // conditional expression
                        ConsoleColor.GREEN,        // positive conditional color
                        ConsoleColor.RED,          // negative conditional color
                        output.getState()) + "]");

                // create a pin listener to print out changes to the output gpio pin state
                output.addListener(new GpioPinListenerDigital() {
                    @Override
                    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                        if(!console.exiting()) {
                            // display pin state on console
                            console.println("[-GPIO %02d-] : %s", event.getPin().getPin().getAddress(),
                                    ConsoleColor.conditional(
                                        event.getState().isHigh(), // conditional expression
                                        ConsoleColor.GREEN,        // positive conditional color
                                        ConsoleColor.RED,          // negative conditional color
                                        event.getState()));
                        }
                    }
                });

            }
            catch (Exception ex){
                System.err.println(ex.getMessage());
            }
        }

        // prompt user that we are ready
        console.emptyLine();
        console.println(" Successfully provisioned all GPIO output pins");
        console.emptyLine();

        // default selected output pin to address provided in pin argument
        selectedOutput = provisionedPins.get(pinArg.getAddress());

        // allow for user to exit program using CTRL-C
        console.box("   PRESS CTRL-C TO EXIT   ",
                    "   ENTER '?' FOR HELP     ");
        console.emptyLine();

        while(!console.exiting()) {

            // prompt user for a new pin number to select or an output command to apply to the currently selected pin
            //System.out.print("[ENTER COMMAND OR PIN # (H=hi,L=low,T=toggle,P=pulse,B=blink)] : ");
            console.print("[-GPIO %02d-] : ", selectedOutput.getPin().getAddress());
            String arg = scanner.nextLine().toUpperCase(Locale.ROOT).trim();

            // handle empty command input
            if(arg.isEmpty()){
                // do nothing
            }
            // HIGH, ON
            else if(arg.startsWith("H") || arg.startsWith("ON") || arg.startsWith("N")){
                selectedOutput.blink(0);
                selectedOutput.high();
            }
            // LOW, OFF
            else if(arg.startsWith("L") || arg.startsWith("OFF") || arg.startsWith("F")){
                selectedOutput.blink(0);
                selectedOutput.low();
            }
            else if(arg.startsWith("T")){ // TOGGLE
                selectedOutput.blink(0);
                selectedOutput.toggle();
            }
            else if(arg.startsWith("B")){   // BLINK
                selectedOutput.blink(250, TimeUnit.MILLISECONDS);
            }
            else if(arg.startsWith("P")){   // PULSE (pulse starting with inverse of current state)
                selectedOutput.blink(0);
                selectedOutput.pulseSync(1000, PinState.getInverseState(selectedOutput.getState()));
            }
            else if(arg.startsWith("Q")){   // QUIT
                console.exit();
            }
            else if(arg.startsWith("S")){   // GET CURRENTLY SELECTED PIN STATE
                console.println("[-GPIO %02d-] : %s", selectedOutput.getPin().getAddress(),
                        ConsoleColor.conditional(
                                selectedOutput.getState().isHigh(), // conditional expression
                                ConsoleColor.GREEN,        // positive conditional color
                                ConsoleColor.RED,          // negative conditional color
                                selectedOutput.getState()));
            }
            else if(arg.startsWith("A")){   // GET ALL PIN STATES
                console.separatorLine();
                for(GpioPinDigitalOutput output : provisionedPins.values()) {
                    console.println("[-GPIO %02d-] : %s", output.getPin().getAddress(),
                            ConsoleColor.conditional(
                                    output.getState().isHigh(), // conditional expression
                                    ConsoleColor.GREEN,        // positive conditional color
                                    ConsoleColor.RED,          // negative conditional color
                                    output.getState()));

                }
                console.separatorLine();
            }
            else if(arg.startsWith("?")){   // HELP
                console.box(
                        "?      : help (displays this message.)",
                        "H      : set current GPIO output to HIGH",
                        "L      : set current GPIO output to HIGH",
                        "H      : toggle current GPIO output state",
                        "P      : pulse current GPIO output HIGH for 1 second.",
                        "B      : blink current GPIO output on a 1/4 second period.",
                        "A      : show the states of all provisioned outputs.",
                        "Q      : quit/exit this program.",
                        "(0-99) : enter a GPIO pin number to select"
                );
            }
            else {
                try {
                    // attempt to parse command argument as a number
                    Integer address = Integer.parseUnsignedInt(arg);
                    if(provisionedPins.containsKey(address)){
                        selectedOutput = provisionedPins.get(address); // update selected output pin
                    }
                    else{
                        console.println("[-%s-] : INVALID GPIO PIN ADDRESS; [#%s]", ConsoleColor.build(ConsoleColor.YELLOW, "!ERROR!"), address.toString());
                    }
                }
                catch (NumberFormatException e){
                    console.println("[-%s-] : INVALID COMMAND; [\"%s\"]", ConsoleColor.build(ConsoleColor.YELLOW, "!ERROR!"), arg);
                }
            }

            Thread.sleep(50);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}
