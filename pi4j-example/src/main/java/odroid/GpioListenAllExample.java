package odroid;
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
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

/**
 * This example code demonstrates how to setup a listener
 * for GPIO pin state changes on the Odroid C1/C1+/XU4 platform.
 *
 * @author Robert Savage
 */
public class GpioListenAllExample {

    public static void main(String args[]) throws InterruptedException, PlatformAlreadyAssignedException {

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // explicitly assign the platform as the Odroid platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.ODROID);

        System.out.println("<--Pi4J--> GPIO Listen (All Pins) Example ... started.");

        // create GPIO controller
        final GpioController gpio = GpioFactory.getInstance();

        // ####################################################################
        //
        // IF YOU ARE USING AN ODROID C1/C1+ PLATFORM, THEN ...
        //    When provisioning a pin, use the OdroidC1Pin class.
        //
        // IF YOU ARE USING AN ODROID XU4 PLATFORM, THEN ...
        //    When provisioning a pin, use the OdroidXU4Pin class.
        //
        // ####################################################################

        // provision gpio input pins with its internal pull down resistor enabled
        GpioPinDigitalInput[] event_pins = {
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_00, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_01, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_02, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_03, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_04, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_05, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_06, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_07, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_10, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_11, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_12, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_13, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_14, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_21, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_22, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_23, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_24, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_26, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_27, PinPullResistance.PULL_DOWN),
        };

        // create and register gpio pin listeners
        gpio.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            }
        }, event_pins);

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
