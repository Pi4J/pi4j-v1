// START SNIPPET: trigger-gpio-snippet
package com.pi4j.io.example;

import java.util.concurrent.Callable;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDirection;
import com.pi4j.io.gpio.GpioPinEdge;
import com.pi4j.io.gpio.GpioPinResistor;
import com.pi4j.io.gpio.GpioPinState;
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

        // create a gpio control trigger on gpio pin#4; when #4 goes HIGH, also set pin #21 to HIGH
        GpioSetStateTrigger triggerOn = new GpioSetStateTrigger(GpioPin.GPIO_04, GpioPinState.HIGH,
                GpioPin.GPIO_21, GpioPinState.HIGH);

        // create a gpio control trigger on gpio pin#4; when #4 goes LOW, also set pin #21 to LOW
        GpioSetStateTrigger triggerOff = new GpioSetStateTrigger(GpioPin.GPIO_04, GpioPinState.LOW,
                GpioPin.GPIO_21, GpioPinState.LOW);

        // create a gpio synchronization trigger on gpio pin#4; when #4 changes, also set pin #22 to
        // same state
        GpioSyncStateTrigger triggerSync = new GpioSyncStateTrigger(GpioPin.GPIO_04,
                GpioPinState.allStates(), GpioPin.GPIO_22);

        // create a gpio pulse trigger on gpio pin#4; when #4 goes HIGH, also pulse pin #23 to the
        // HIGH state for 1 second
        GpioPulseStateTrigger triggerPulse = new GpioPulseStateTrigger(GpioPin.GPIO_04,
                GpioPinState.HIGH, GpioPin.GPIO_23, 1000);

        // create a gpio callback trigger on gpio pin#4; when #4 changes state, perform a callback
        // invocation on the user defined 'Callable' class instance
        GpioCallbackTrigger triggerCallback = new GpioCallbackTrigger(GpioPin.GPIO_04,
                GpioPinState.allStates(), new Callable<Void>()
                {
                    public Void call() throws Exception
                    {
                        System.out.println(" --> GPIO TRIGGER CALLBACK RECEIVED ");
                        return null;
                    }
                });

        // register all the gpio triggers with the gpio controller
        gpio.addTrigger(triggerOn);
        gpio.addTrigger(triggerOff);
        gpio.addTrigger(triggerSync);
        gpio.addTrigger(triggerCallback);
        gpio.addTrigger(triggerPulse);

        // setup gpio pin #4 as an input pin with its internal pull down resistor enabled
        // (configure pin edge to both rising and falling to get notified for HIGH and LOW state
        // changes)
        gpio.setup(GpioPin.GPIO_04, GpioPinDirection.IN, GpioPinEdge.BOTH,
                   GpioPinResistor.PULL_DOWN);

        // setup gpio pins #21,23,23 as an output pins and make sure they are all LOW at startup
        GpioPin[] pins = { GpioPin.GPIO_21, GpioPin.GPIO_22, GpioPin.GPIO_23 };
        gpio.setup(pins, GpioPinDirection.OUT);
        gpio.setLow(pins);

        // keep program running until user aborts (CTRL-C)
        for (;;)
        {
            Thread.sleep(500);
        }
    }
}
// END SNIPPET: trigger-gpio-snippet