

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MCP23017GpioExample.java  
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

import com.pi4j.gpio.extension.piface.PiFaceGpioProvider;
import com.pi4j.gpio.extension.piface.PiFacePin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
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
 * This example implements the MCP23017 GPIO expansion board.
 * More information about the board can be found here: *
 * http://ww1.microchip.com/downloads/en/DeviceDoc/21952b.pdf
 * </p>
 * 
 * <p>
 * The MCP23017 is connected via I2C connection to the Raspberry Pi and provides
 * 16 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * 
 * @author Robert Savage
 */
public class PiFaceGpioExample {
    
    public static void main(String args[]) throws InterruptedException, IOException {
        
        System.out.println("<--Pi4J--> PiFace (MCP23017) GPIO Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // create custom PiFace GPIO provider
        final PiFaceGpioProvider gpioProvider = new PiFaceGpioProvider(PiFaceGpioProvider.DEFAULT_ADDRESS,Spi.CHANNEL_0);
        
        // provision gpio input pins from PiFaceGpioProvider
        GpioPinDigitalInput myInputs[] = {
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_00),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_01),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_02),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_03),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_04),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_05),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_06),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_07)
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
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_00),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_01),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_02),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_03),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_04),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_05),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_06),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_07),
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

