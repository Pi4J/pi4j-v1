/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  BroadcomPinNumberingGpioListenerExample.java
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
import com.pi4j.util.Console;
import com.pi4j.util.ConsoleColor;

/**
 * This example code demonstrates how to setup a listener
 * for GPIO pin state changes on the RaspberryPi
 * using the Broadcom chipset GPIO pin numbering scheme.
 *
 * @author Robert Savage
 */
public class BroadcomPinNumberingGpioListenerExample {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("<--Pi4J--> GPIO Listener Example ... started.");

        // in order to use the Broadcom GPIO pin numbering scheme, we need to configure the
        // GPIO factory to use a custom configured Raspberry Pi GPIO provider
        GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "GPIO Listen Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision broadcom gpio pin #13 as an input pin
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiBcmPin.GPIO_13, "MyButton", PinPullResistance.PULL_DOWN);

        // unexport the GPIO pin when program exits
        myButton.setShutdownOptions(true);

        // prompt user that we are ready
        console.println(" ... Successfully provisioned [" + myButton.getPin() + "] with PULL resistance = [" + myButton.getPin() + "]");
        console.emptyLine();
        console.box("Please complete the [" + myButton.getPin() + "] circuit and see",
                "the listener feedback here in the console.");
        console.emptyLine();

        // create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                console.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " +
                        ConsoleColor.conditional(
                                event.getState().isHigh(), // conditional expression
                                ConsoleColor.GREEN,        // positive conditional color
                                ConsoleColor.RED,          // negative conditional color
                                event.getState()));        // text to display
            }

        });

        // wait (block) for user to exit program using CTRL-C
        console.waitForExit();

        // forcefully shutdown all GPIO monitoring threads and scheduled tasks
        gpio.shutdown();
    }
}
