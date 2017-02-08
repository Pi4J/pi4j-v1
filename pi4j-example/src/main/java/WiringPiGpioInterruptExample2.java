/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiGpioInterruptExample2.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterruptCallback;

public class WiringPiGpioInterruptExample2 {

    /**
     * Example program to demonstrate the usage of WiringPiISR()
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String args[]) throws InterruptedException {

        System.out.println("<--Pi4J--> GPIO interrupt test program");

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        // configure pins as input pins
        Gpio.pinMode(0, Gpio.INPUT) ;
        Gpio.pinMode(1, Gpio.INPUT) ;
        Gpio.pinMode(2, Gpio.INPUT) ;

        // configure pins with pull down resistance
        Gpio.pullUpDnControl(0, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(1, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(2, Gpio.PUD_DOWN);

        // setup a pin interrupt callback for each pin
        //
        // NOTE: YOU CANNOT SETUP SEPARATE ISRs FOR RISING AND FALLING,
        // EACH GPIO PIN CAN ONLY BE CONFIGURED WITH A SINGLE EDGE TYPE AT ANY GIVEN TIME.
        // YOU CAN USE THE 'INT_EDGE_BOTH' IF YOU WISH TO CATCH BOTH CASES IN A SINGLE CALLBACK.
        //

        // example: single callback for discrete FALLING edge for pin 0
        Gpio.wiringPiISR(0, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED <FALLING>");
            }
        });

        // example: single callback for both RISING and FALLING edges for pin 1
        Gpio.wiringPiISR(1, Gpio.INT_EDGE_BOTH, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED <RISING|FALLING>");
            }
        });

        // here is another approach using a custom callback class/instance instead of an anonymous method
        SampleCallbackClass risingCallbackInstance = new SampleCallbackClass("RISING");

        // setup a pin interrupt callbacks for pin 2
        Gpio.wiringPiISR(2, Gpio.INT_EDGE_RISING, risingCallbackInstance);

        // wait for user to exit program
        System.console().readLine("Press <ENTER> to exit program.\r\n");
    }

    // sample callback class implementation
    public static class SampleCallbackClass implements GpioInterruptCallback {

        private String direction = "UKNOWN";

        public SampleCallbackClass(String direction){
            this.direction = direction;
        }

        @Override
        public void callback(int pin) {
            System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED <" + direction.toUpperCase() + ">");
        }
    }
}
