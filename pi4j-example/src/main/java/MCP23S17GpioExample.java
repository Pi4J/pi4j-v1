

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MCP23S17GpioExample.java  
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


import java.io.IOException;

import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23S17Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Spi;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for GPIO pin state control and monitoring.
 * </p>  
 * 
 * <p>
 * This example implements the MCP23S17 GPIO expansion board.
 * More information about the board can be found here: *
 * http://ww1.microchip.com/downloads/en/DeviceDoc/21952b.pdf
 * </p>
 * 
 * <p>
 * The MCP23S17 is connected via SPI connection to the Raspberry Pi and provides
 * 16 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * 
 * @author Robert Savage
 */
public class MCP23S17GpioExample {
    
    public static void main(String args[]) throws InterruptedException, IOException {
        
        System.out.println("<--Pi4J--> MCP23S17 GPIO Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // create custom MCP23017 GPIO provider
        final MCP23S17GpioProvider gpioProvider = new MCP23S17GpioProvider(MCP23S17GpioProvider.DEFAULT_ADDRESS, Spi.CHANNEL_0);
        
        // provision gpio input pins from MCP23S17
        GpioPinDigitalInput myInputs[] = {
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B0, "MyInput-B0", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B1, "MyInput-B1", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B2, "MyInput-B2", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B3, "MyInput-B3", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B4, "MyInput-B4", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B5, "MyInput-B5", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B6, "MyInput-B6", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B7, "MyInput-B7", PinPullResistance.PULL_UP),
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
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A0, "MyOutput-A0", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A1, "MyOutput-A1", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A2, "MyOutput-A2", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A3, "MyOutput-A3", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A4, "MyOutput-A4", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A5, "MyOutput-A5", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A6, "MyOutput-A6", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A7, "MyOutput-A7", PinState.LOW)
          };
        
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

