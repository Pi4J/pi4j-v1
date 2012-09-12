// START SNIPPET: trigger-gpio-snippet
package com.pi4j.example;

import java.util.concurrent.Callable;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinResistor;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;

/**
 * This example code demonstrates how to setup simple triggers for GPIO pins on the Raspberry Pi.
 * 
 * @author Robert Savage
 */
public class TriggerGpioExample
{
    public static void main(String[] args) throws InterruptedException
    {
        // create gpio controller
        Gpio gpio = GpioFactory.createInstance();

        // provision gpio pin #4 as an input pin with its internal pull down resistor enabled
        // (configure pin edge to both rising and falling to get notified for HIGH and LOW state
        // changes)
        GpioPin myButton = gpio.provisionInputPin(Pin.GPIO_04, "MyButton", PinEdge.BOTH, PinResistor.PULL_DOWN);

        // setup gpio pins #21,22,23 as an output pins and make sure they are all LOW at startup
        GpioPin myLed[] = { 
                gpio.provisionOuputPin(Pin.GPIO_21, "LED #1", PinState.LOW), 
                gpio.provisionOuputPin(Pin.GPIO_22, "LED #2", PinState.LOW),
                gpio.provisionOuputPin(Pin.GPIO_23, "LED #3", PinState.LOW) };
        
        // create a gpio control trigger on gpio pin #4; when #4 goes HIGH, also set pin #21 to HIGH
        myButton.addTrigger(new GpioSetStateTrigger(PinState.HIGH, myLed[0], PinState.HIGH));

        // create a gpio control trigger on gpio pin #4; when #4 goes LOW, also set pin #21 to LOW
        myButton.addTrigger(new GpioSetStateTrigger(PinState.LOW, myLed[0], PinState.LOW));
        
        // create a gpio synchronization trigger on gpio pin#4; when #4 changes, also set pin #22 to
        // same state
        myButton.addTrigger(new GpioSyncStateTrigger(myLed[1]));

        // create a gpio pulse trigger on gpio pin#4; when #4 goes HIGH, also pulse pin #23 to the
        // HIGH state for 1 second
        myButton.addTrigger(new GpioPulseStateTrigger(PinState.HIGH, myLed[2], 1000));
        
        // create a gpio callback trigger on gpio pin#4; when #4 changes state, perform a callback
        // invocation on the user defined 'Callable' class instance
        myButton.addTrigger(new GpioCallbackTrigger(new Callable<Void>()
                {
                    public Void call() throws Exception
                    {
                        System.out.println(" --> GPIO TRIGGER CALLBACK RECEIVED ");
                        return null;
                    }
                }));

        // keep program running until user aborts (CTRL-C)
        for (;;)
        {
            Thread.sleep(500);
        }
    }
}
// END SNIPPET: trigger-gpio-snippet