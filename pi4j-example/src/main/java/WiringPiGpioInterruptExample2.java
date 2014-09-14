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
 * Copyright (C) 2012 - 2014 Pi4J
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
    
    public static void main(String args[]) throws InterruptedException {

        System.out.println("<--Pi4J--> GPIO interrupt test program");

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        Gpio.pinMode (0, Gpio.INPUT) ;
        Gpio.pinMode (1, Gpio.INPUT) ;
        Gpio.pinMode (2, Gpio.INPUT) ;
        Gpio.pinMode (3, Gpio.INPUT) ;
        Gpio.pinMode (4, Gpio.INPUT) ;
        Gpio.pinMode (5, Gpio.INPUT) ;
        Gpio.pinMode (6, Gpio.INPUT) ;
        Gpio.pinMode (7, Gpio.INPUT) ;

        Gpio.pullUpDnControl(0, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(1, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(2, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(3, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(4, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(5, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(6, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(7, Gpio.PUD_DOWN);

        // setup a pin interrupt callback for pin 7
        Gpio.wiringPiISR(0, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(1, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(2, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(3, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(4, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(5, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(6, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(7, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });

        // wait for user to exit program
        System.console().readLine("Press <ENTER> to exit program.\r\n");
    }
}

