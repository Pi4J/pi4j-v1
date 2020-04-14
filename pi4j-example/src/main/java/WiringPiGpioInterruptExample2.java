/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiGpioInterruptExample2.java
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
