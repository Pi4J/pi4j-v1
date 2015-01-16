// START SNIPPET: cylon-gpio-snippet


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  CylonGpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * This example code demonstrates how to perform a blinking cycle 
 * (cylon effect) of a series of GPIO pins on the Raspberry Pi.  
 * 
 * @author Robert Savage
 */
public class CylonGpioExample {
    
    public static void main(String[] args) throws InterruptedException {
        
        System.out.println("<--Pi4J--> GPIO Cylon Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // provision gpio pin #01 as an output pin and turn on
        final GpioPinDigitalOutput[] pins = {
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW)};
        System.out.println("--> GPIO state should be: ON");

        // set shutdown options on all pins
        gpio.setShutdownOptions(true, PinState.LOW, pins);
        
        // infinite loop
        while(true) {
            
            for(int index = 0; index <= 6; index++) {
                pins[index].pulse(50);
                Thread.sleep(50);
            }
            
            for(int index = 6; index >= 0; index--) {
                pins[index].pulse(50);
                Thread.sleep(50);
            }
        }
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller        
    }
}
//END SNIPPET: cylon-gpio-snippet
