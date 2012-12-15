// START SNIPPET: cycle-gpio-snippet


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  CycleGpioExample.java  
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
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * This example code demonstrates how to perform a blinking cycle 
 * of a series of GPIO pins on the Raspberry Pi.  
 * 
 * @author Robert Savage
 */
public class CycleGpioExample
{
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO Cycle Example ... started.");
        
        // create gpio controller
        GpioController gpio = GpioFactory.getInstance();
        
        // provision gpio pin #01 as an output pin and turn on
        GpioPinDigitalOutput[] pin = {
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "MyLED-0", PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED-1", PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED-2", PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyLED-3", PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED-4", PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "MyLED-5", PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "MyLED-6", PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "MyLED-7", PinState.LOW)};
        System.out.println("--> GPIO state should be: ON");

        // set shutdown options on all pins
        gpio.setShutdownOptions(true, PinState.LOW, pin);
        
        while(true)
        {
            for(int index = 0; index <= 6; index++)
            {
                pin[index].pulse(50);
                Thread.sleep(50);
            }
            
            for(int index = 6; index >= 0; index--)
            {
                pin[index].pulse(50);
                Thread.sleep(50);
            }
        }
    }
}
//END SNIPPET: cycle-gpio-snippet
