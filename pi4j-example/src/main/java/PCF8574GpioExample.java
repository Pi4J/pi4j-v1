/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PCF8574GpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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
import java.io.IOException;

import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for GPIO pin state control and monitoring.
 * </p>  
 * 
 * <p>
 * This example implements the PCF8574 GPIO expansion board.
 * More information about the board can be found here: *
 * http://www.ti.com/lit/ds/symlink/pcf8574.pdf
 * </p>
 * 
 * <p>
 * The PCF8574 is connected via I2C connection to the Raspberry Pi and provides
 * 16 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * 
 * @author Robert Savage
 */
public class PCF8574GpioExample {
    
    public static void main(String args[]) throws InterruptedException, IOException {
        
        System.out.println("<--Pi4J--> PCF8574 GPIO Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // create custom MCP23017 GPIO provider
        final PCF8574GpioProvider gpioProvider = new PCF8574GpioProvider(I2CBus.BUS_1, PCF8574GpioProvider.PCF8574A_0x3F);
        
        // provision gpio input pins from MCP23017
        GpioPinDigitalInput myInputs[] = {
                gpio.provisionDigitalInputPin(gpioProvider, PCF8574Pin.GPIO_00),
                gpio.provisionDigitalInputPin(gpioProvider, PCF8574Pin.GPIO_01),
                gpio.provisionDigitalInputPin(gpioProvider, PCF8574Pin.GPIO_02)
            };
        
        // create and register gpio pin listener
        gpio.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                        + event.getState());
            }
        }, myInputs);
        
        // provision gpio output pins and make sure they are all LOW at startup
        GpioPinDigitalOutput myOutputs[] = { 
            gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_04, PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_05, PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_06, PinState.LOW)
          };

        // on program shutdown, set the pins back to their default state: HIGH 
        gpio.setShutdownOptions(true, PinState.HIGH, myOutputs);
        
        // keep program running for 20 seconds
        for (int count = 0; count < 10; count++) {
            gpio.setState(true, myOutputs);
            Thread.sleep(1000);
            gpio.setState(false, myOutputs);
            Thread.sleep(1000);
        }
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}