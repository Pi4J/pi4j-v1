// START SNIPPET: listen-gpio-snippet
package com.pi4j.io.example;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDirection;
import com.pi4j.io.gpio.GpioPinEdge;
import com.pi4j.io.gpio.GpioPinResistor;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.event.GpioPinStateChangeEvent;

/**
 * This example code demonstrates how to setup a listener
 * for GPIO pin state changes on the Raspberry Pi.  
 * 
 * @author Robert Savage
 */
public class ListenGpioExample
{
    public static void main(String args[]) throws InterruptedException
    {
        // create gpio listener & callback handler
        GpioExampleListener listener = new GpioExampleListener();

        // create gpio controller
        Gpio gpio = GpioFactory.createInstance();

        // add gpio listener
        gpio.addListener(listener);

        // setup gpio pin #4 as an input pin with its internal pull down resistor enabled
        // (configure pin edge to both rising and falling to get notified for HIGH and LOW state changes)
        gpio.setup(GpioPin.GPIO_04, GpioPinDirection.IN, GpioPinEdge.BOTH,
                   GpioPinResistor.PULL_DOWN);

        // keep program running until user aborts (CTRL-C)
        for (;;)
        {
            Thread.sleep(500);
        }
    }
}

/**
 * This class implements the GPIO listener interface
 * with the callback method for event notifications
 * when GPIO pin states change.
 * 
 * @see GpioListener
 * @author Robert Savage
 */
class GpioExampleListener implements GpioListener
{
    public void pinStateChanged(GpioPinStateChangeEvent event)
    {
        System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                + event.getState());
    }
}
// END SNIPPET: listen-gpio-snippet