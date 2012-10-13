/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioTest.java  
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
import java.util.concurrent.Callable;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinEvent;
import com.pi4j.io.gpio.event.PinEventType;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioInverseSyncStateTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;

public class GpioTest
{
    public static void main(String args[]) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO test program");

        // create gpio listener & callback handler
        GpioTestListener listener = new GpioTestListener();

        // create gpio controller
        GpioController gpio = GpioFactory.getInstance();

        // setup gpio pin #07 as an input pin whose biased to ground and receives +3VDC to be
        // triggered
        GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, "Test-Input", PinPullResistance.PULL_DOWN);

        // add gpio listener
        inputPin.addListener(listener);

        // setup gpio pin #04, #05, #06 as output pins
        GpioPinDigitalOutput pinLed1 = gpio.provisionDigitalOuputPin(RaspiPin.GPIO_04, "LED-One", PinState.LOW);
        GpioPinDigitalOutput pinLed2 = gpio.provisionDigitalOuputPin(RaspiPin.GPIO_05, "LED-Two", PinState.HIGH);
        GpioPinDigitalOutput pinLed3 = gpio.provisionDigitalOuputPin(RaspiPin.GPIO_06, "LED-Three", PinState.LOW);

        // create gpio triggers
        inputPin.addTrigger(new GpioSetStateTrigger(PinState.HIGH, pinLed1, PinState.HIGH));
        inputPin.addTrigger(new GpioSetStateTrigger(PinState.LOW, pinLed1, PinState.LOW));
        inputPin.addTrigger(new GpioInverseSyncStateTrigger(pinLed2));
        inputPin.addTrigger(new GpioPulseStateTrigger(PinState.HIGH, pinLed3, 1000));
        inputPin.addTrigger(new GpioCallbackTrigger(PinState.allStates(), new Callable<Void>()
        {
            public Void call() throws Exception
            {
                System.out.println(" --> GPIO TRIGGER CALLBACK RECEIVED ");
                return null;
            }
        }));

        // keep program alive
        for (;;)
        {
            Thread.sleep(500);
        }
    }
}

class GpioTestListener implements GpioListener
{
    @Override
    public void handleGpioPinEvent(GpioPinEvent event)
    {
        if(event.getEventType() == PinEventType.DIGITAL_STATE_CHANGE)
        {
            // cast to digital state change event
            GpioPinDigitalStateChangeEvent evt = (GpioPinDigitalStateChangeEvent)event;
            
            // display pin state on console
            System.out.println(" --> GPIO PIN LISTENER STATE CHANGE: " + event.getPin() + " = "
                    + evt.getState());
        }
    }    
}
