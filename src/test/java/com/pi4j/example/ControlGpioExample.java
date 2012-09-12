// START SNIPPET: control-gpio-snippet
package com.pi4j.example;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

/**
 * This example code demonstrates how to perform simple state
 * control of a GPIO pin on the Raspberry Pi.  
 * 
 * @author Robert Savage
 */
public class ControlGpioExample
{
    public static void main(String[] args) throws InterruptedException
    {
        // create gpio controller
        Gpio gpio = GpioFactory.createInstance();
        
        // provision gpio pin #4 as an output pin and turn on
        GpioPin pin = gpio.provisionOuputPin(Pin.GPIO_04, "MyLED", PinState.HIGH);
        
        Thread.sleep(5000);
        
        // turn off gpio pin #4
        pin.low();

        Thread.sleep(5000);

        // toggle the current state of gpio pin #4 (should turn on)
        pin.toggle();

        Thread.sleep(5000);

        // toggle the current state of gpio pin #4  (should turn off)
        pin.toggle();
        
        Thread.sleep(5000);

        // turn on gpio pin #4 for 1 second and then off
        pin.pulse(1000);
    }
}
//END SNIPPET: control-gpio-snippet