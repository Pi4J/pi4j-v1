package bananapro;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioOutputExample.java  
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
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

/**
 * This example code demonstrates how to perform simple state
 * control of a GPIO pin on the BananaPro.
 *
 * @author Robert Savage
 */
public class GpioOutputExample {

    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException {

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // explicitly assign the platform as the BananaPro platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.BANANAPRO);

        System.out.println("<--Pi4J--> GPIO Control Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // ####################################################################
        //
        // When provisioning a pin, use the BananaProPin class.
        //
        // ####################################################################

        // by default we will use gpio pin #01; however, if an argument
        // has been provided, then lookup the pin by address
        Pin selectedPin = BananaProPin.GPIO_01;
        if (args.length > 0){
            int address = Integer.parseInt(args[0]);
            selectedPin = BananaProPin.getPinByAddress(address);
        }

        // provision gpio pin as an output pin and turn on
        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(selectedPin, "MyLED", PinState.HIGH);

        // set shutdown state for this pin: keep as output pin, set to lo state
        pin.setShutdownOptions(false, PinState.LOW);

        System.out.println("--> GPIO [" + pin.toString() + "] state should be: ON");

        Thread.sleep(2000);

        // turn off gpio pin #01
        pin.low();
        System.out.println("--> GPIO [" + pin.toString() + "] state should be: OFF");

        Thread.sleep(2000);

        // toggle the current state of gpio pin #01 (should turn on)
        pin.toggle();
        System.out.println("--> GPIO [" + pin.toString() + "] state should be: ON");

        Thread.sleep(2000);

        // toggle the current state of gpio pin #01  (should turn off)
        pin.toggle();
        System.out.println("--> GPIO [" + pin.toString() + "] state should be: OFF");

        Thread.sleep(2000);

        // turn on gpio pin #01 for 1 second and then off
        System.out.println("--> GPIO [" + pin.toString() + "] state should be: ON for only 1 second");
        pin.pulse(1000, true); // set second argument to 'true' use a blocking call

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting GpioOutputExample");
    }
}
