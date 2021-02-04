// START SNIPPET: blink-trigger-gpio-snippet


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  BlinkTriggerGpioExample.java
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

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioBlinkStateTrigger;
import com.pi4j.io.gpio.trigger.GpioBlinkStopStateTrigger;

/**
 * This example code demonstrates how to setup blinking triggers for GPIO pins on the Raspberry Pi.
 *
 * @author Robert Savage
 */
public class BlinkTriggerGpioExample {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("<--Pi4J--> GPIO Blink Trigger Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
                                                  PinPullResistance.PULL_DOWN);

        System.out.println(" ... complete the GPIO #02 circuit and see the blink trigger take effect.");

        // setup gpio pins #04 an output pins and make sure they are all LOW at startup
        final GpioPinDigitalOutput myLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);

        // create a gpio control trigger on the input pin ; when the input goes HIGH, turn on blinking
        myButton.addTrigger(new GpioBlinkStateTrigger(PinState.HIGH, myLed, 250));

        // create a gpio control trigger on the input pin ; when the input goes LOW, turn off blinking
        myButton.addTrigger(new GpioBlinkStopStateTrigger(PinState.LOW, myLed));

        // keep program running until user aborts (CTRL-C)
        while(true) {
            Thread.sleep(500);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }
}
// END SNIPPET: blink-trigger-gpio-snippet
