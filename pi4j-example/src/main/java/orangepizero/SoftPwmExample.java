package orangepizero;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  SoftPwmExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
import com.pi4j.util.Console;

/**
 * <p>
 * This example code demonstrates how to setup a software emulated PWM pin using the OrangePiZero GPIO pins.
 * </p>
 *
 * @author Robert Savage
 * @author Balazs Matyas
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

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // explicitly assign the platform as the OrangePiZero platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.ORANGEPIZERO);

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "SoftPWM Example (Software-driven PWM Emulation)");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // ####################################################################
        //
        // When provisioning a pin, use the OrangePiZeroPin class.
        //
        // ####################################################################

        // by default we will use gpio pin #01; however, if an argument
        // has been provided, then lookup the pin by address
        Pin pin = CommandArgumentParser.getPin(
                OrangePiZeroPin.class,    // pin provider class to obtain pin instance from
                OrangePiZeroPin.GPIO_01,  // default pin if no pin argument found
                args);                // argument array to search in

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
