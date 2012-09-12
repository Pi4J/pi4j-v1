// START SNIPPET: listen-gpio-snippet
package com.pi4j.example;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinResistor;
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
        // create gpio controller
        Gpio gpio = GpioFactory.createInstance();

        // provision gpio pin #4 as an input pin with its internal pull down resistor enabled
        // (configure pin edge to both rising and falling to get notified for HIGH and LOW state changes)
        GpioPin myButton = gpio.provisionInputPin(Pin.GPIO_04, "MyButton", PinEdge.BOTH, PinResistor.PULL_DOWN);

        // create and register gpio pin listener
        myButton.addListener(new GpioExampleListener());

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