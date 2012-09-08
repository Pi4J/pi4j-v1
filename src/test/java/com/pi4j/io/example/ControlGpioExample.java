// START SNIPPET: control-gpio-snippet
package com.pi4j.io.example;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDirection;
import com.pi4j.io.gpio.GpioPinState;

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
        
        // setup gpio pin #4 as an output pin  
        gpio.setup(GpioPin.GPIO_04, GpioPinDirection.OUT);
        
        // turn on gpio pin #4
        gpio.setState(GpioPin.GPIO_04, GpioPinState.HIGH);

        Thread.sleep(5000);
        
        // turn off gpio pin #4
        gpio.setState(GpioPin.GPIO_04, GpioPinState.LOW);

        Thread.sleep(5000);

        // toggle the current state of gpio pin #4
        gpio.toggleState(GpioPin.GPIO_04);

        Thread.sleep(5000);

        // turn on gpio pin #4 for 1 second and then off
        gpio.pulse(GpioPin.GPIO_04, 1000);        
    }
}
//END SNIPPET: control-gpio-snippet