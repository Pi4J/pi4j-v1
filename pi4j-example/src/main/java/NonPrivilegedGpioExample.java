/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  NonPrivilegedGpioExample.java
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
import com.pi4j.wiringpi.GpioUtil;

/**
 * This example code demonstrates how to use Pi4J with
 * non-privileged access (non-root/sudo) to GPIO pins on
 * the Raspberry Pi.
 *
 * @author Robert Savage
 */
public class NonPrivilegedGpioExample {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("<--Pi4J--> Non-Privileged GPIO Example ... started.");

        // we can use this utility method to pre-check to determine if
        // privileged access is required on the running system
        if(GpioUtil.isPrivilegedAccessRequired()){
            System.err.println("*****************************************************************");
            System.err.println("Privileged access is required on this system to access GPIO pins!");
            System.err.println("*****************************************************************");
            return;
        }

        // ----------------------
        // ATTENTION
        // ----------------------
        // YOU CANNOT USE ANY HARDWARE PWM OR CLOCK FUNCTIONS WHILE ACCESSING NON-PRIVILEGED GPIO.
        // THIS METHOD MUST BE INVOKED BEFORE CREATING A GPIO CONTROLLER INSTANCE.
        GpioUtil.enableNonPrivilegedAccess();

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // ------------
        // OUTPUT PIN
        // ------------

        // provision gpio pin #01 as an output pin and blink it
        final GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);

        // set shutdown state for the output pin (and un-export the pin)
        output.setShutdownOptions(true, PinState.LOW);

        // blink output pin every one second
        output.blink(1000);

        // display info to user
        System.out.println("Pin [" + output.getName() + "] should be blinking/toggling every 1 second.");

        // ------------
        // INPUT PIN
        // ------------

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput input = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

        // set shutdown state for the input pin (and un-export the pin)
        input.setShutdownOptions(true);

        // create and register gpio pin listener
        input.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            }
        });

        // display info to user
        System.out.println("You can connect pin [" + input.getName() + "] to +3VDC to capture input state changes.");

        // ----------------
        // WAIT & SHUTDOWN
        // ----------------

        // sleep for 1 minute, then shutdown
        Thread.sleep(60000);

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting NonPrivilegedGpioExample");
    }
}
