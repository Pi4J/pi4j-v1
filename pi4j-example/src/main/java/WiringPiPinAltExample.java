/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiPinAltExample.java
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
import com.pi4j.wiringpi.Gpio;


public class WiringPiPinAltExample {

    public static void main(String args[]) throws InterruptedException {
        System.out.println("<--Pi4J--> GPIO ALT MODE test program");

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        // NOTE, this example does not really do anything visible, its just an usage example of settings ALT pin modes

        // iterate through all the available pin modes
        Gpio.pinMode (7, Gpio.INPUT);
        Gpio.pinMode (7, Gpio.OUTPUT);
        Gpio.pinMode (7, Gpio.ALT0);
        Gpio.pinMode (7, Gpio.ALT1);
        Gpio.pinMode (7, Gpio.ALT2);
        Gpio.pinMode (7, Gpio.ALT3);
        Gpio.pinMode (7, Gpio.ALT4);
        Gpio.pinMode (7, Gpio.ALT5);

        System.out.println("Exiting WiringPiPinAltExample");
    }
}

