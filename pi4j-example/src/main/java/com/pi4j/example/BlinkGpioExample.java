// START SNIPPET: blink-gpio-snippet
package com.pi4j.example;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  BlinkGpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

/**
 * This example code demonstrates how to perform simple 
 * blinking LED logic of a GPIO pin on the Raspberry Pi
 * using the Pi4J library.  
 * 
 * @author Robert Savage
 */
public class BlinkGpioExample
{
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO Blink Example ... started.");
        
        // create gpio controller
        GpioController gpio = GpioFactory.getInstance();
        
        // provision gpio pin #01 as an output pin and turn on
        GpioPinDigitalOutput led = gpio.provisionDigitalOuputPin(RaspiPin.GPIO_01, "MyLED");

        // continuously blink the led every 1 second
        led.blink(1000);

        Thread.sleep(5000);
        
        led.blink(0);
        
        System.out.println(" ... the LED will continue blinking until the program is terminated.");
        System.out.println(" ... PRESS <CTRL-C> TO STOP THE PROGRAM.");
        
        // keep program running until user aborts (CTRL-C)
        for (;;)
        {
            Thread.sleep(500);
        }
    }
}
//END SNIPPET: blink-gpio-snippet
