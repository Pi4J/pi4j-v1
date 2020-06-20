/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  SoftPwmExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;

/**
 * <p>
 * This example code demonstrates how to setup a software emulated PWM pin using the RaspberryPi GPIO pins.
 * </p>
 *
 * @author Robert Savage
 */
public class SoftPwmExample {

    /**
     * [ARGUMENT/OPTION "--pin (#)" | "-p (#)" ]
     * This example program accepts an optional argument for specifying the GPIO pin (by number)
     * to use with this GPIO listener example. If no argument is provided, then GPIO #1 will be used.
     * -- EXAMPLE: "--pin 4" or "-p 0".
     *
     * @param args
     * @throws InterruptedException
     * @throws PlatformAlreadyAssignedException
     */
    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "SoftPWM Example (Software-driven PWM Emulation)");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // by default we will use gpio pin #01; however, if an argument
        // has been provided, then lookup the pin by address
        Pin pin = CommandArgumentParser.getPin(
                RaspiPin.class,    // pin provider class to obtain pin instance from
                RaspiPin.GPIO_01,  // default pin if no pin argument found
                args);             // argument array to search in

        // we will provision the pin as a software emulated PWM output
        // pins that support hardware PWM should be provisioned as normal PWM outputs
        // each software emulated PWM pin does consume additional overhead in
        // terms of CPU usage.
        //
        // Software emulated PWM pins support a range between 0 (off) and 100 (max) by default.
        //
        // Please see: http://wiringpi.com/reference/software-pwm-library/
        // for more details on software emulated PWM
        GpioPinPwmOutput pwm = gpio.provisionSoftPwmOutputPin(pin);

        // optionally set the PWM range (100 is default range)
        pwm.setPwmRange(100);

        // prompt user that we are ready
        console.println(" ... Successfully provisioned PWM pin: " + pwm.toString());
        console.emptyLine();

        // set the PWM rate to 100 (FULLY ON)
        pwm.setPwm(100);
        console.println("Software emulated PWM rate is: " + pwm.getPwm());

        console.println("Press ENTER to set the PWM to a rate of 50");
        System.console().readLine();

        // set the PWM rate to 50 (1/2 DUTY CYCLE)
        pwm.setPwm(50);
        console.println("Software emulated PWM rate is: " + pwm.getPwm());

        console.println("Press ENTER to set the PWM to a rate to 0 (stop PWM)");
        System.console().readLine();

        // set the PWM rate to 0 (FULLY OFF)
        pwm.setPwm(0);
        console.println("Software emulated PWM rate is: " + pwm.getPwm());

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}
