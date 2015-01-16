// START SNIPPET: control-gpio-snippet

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  ComputeModuleGpioExample.java  
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

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * This example code demonstrates how to perform simple state
 * control of a GPIO pin on the Raspberry Pi.  
 * 
 * @author Robert Savage
 */
public class ComputeModuleGpioExample {
    
    public static void main(String[] args) throws InterruptedException {
        
        System.out.println("<--Pi4J--> GPIO Compute Module Example ... started.");

        // create GPIO controller
        final GpioController gpio = GpioFactory.getInstance();

        // create GPIO listener
        GpioPinListenerDigital listener  = new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            }
        };

        // provision gpio input pins with its internal pull down resistor enabled
        GpioPinDigitalInput[] pins = {
                gpio.provisionDigitalInputPin(RCMPin.GPIO_00, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_01, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_02, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_03, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_04, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_05, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_06, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_07, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_08, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_09, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_10, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_11, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_12, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_13, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_14, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_15, PinPullResistance.PULL_DOWN),
                // gpio.provisionDigitalInputPin(RCMPin.GPIO_16, PinPullResistance.PULL_DOWN), (error exporting GPIO 16?)
                gpio.provisionDigitalInputPin(RCMPin.GPIO_17, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_18, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_19, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_20, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_21, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_22, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_23, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_24, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_25, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_26, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_27, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_28, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_29, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_30, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_31, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_32, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_33, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_34, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_35, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_36, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_37, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_38, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_39, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_40, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_41, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_42, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_43, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_44, PinPullResistance.PULL_DOWN),
                gpio.provisionDigitalInputPin(RCMPin.GPIO_45, PinPullResistance.PULL_DOWN)
        };

        // create and register gpio pin listener
        gpio.addListener(listener, pins);

        System.out.println(" ... complete the any GPIO circuit (00 - 45) and see the listener feedback here in the console.");

        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(500);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }
}
//END SNIPPET: control-gpio-snippet
