/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  StepperMotorGpioExample2.java  
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
// START SNIPPET: stepper-motor-gpio-snippet

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * This example code demonstrates how to control a stepper motor
 * using the GPIO pins on the Raspberry Pi.  
 * 
 * @author Robert Savage
 */
public class StepperMotorGpioExample
{
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO Stepper Motor Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // provision gpio pins #00 to #03 as output pins and ensure in LOW state
        final GpioPinDigitalOutput[] pins = {
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW)};

        // this will ensure that the motor is stopped when the program terminates
        gpio.setShutdownOptions(true, PinState.LOW, pins);
        
        // create motor component
        GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);

        // create byte array for eight sequence steps
        byte[] sequence = new byte[8];
        sequence[0] = (byte) 0b0001;  
        sequence[1] = (byte) 0b0011;
        sequence[2] = (byte) 0b0010;
        sequence[3] = (byte) 0b0110;
        sequence[4] = (byte) 0b0100;
        sequence[5] = (byte) 0b1100;
        sequence[6] = (byte) 0b1000;
        sequence[7] = (byte) 0b1001;

        // define stepper parameters before attempting to control motor
        motor.setStepInterval(1);
        motor.setStepSequence(sequence);

        // test motor control 

        System.out.println("   Motor FORWARD for 5 seconds.");
        motor.forward(5000);
        
        System.out.println("   Motor STOPPED for 3 seconds.");
        Thread.sleep(3000);
        
        System.out.println("   Motor REVERSE for 7 seconds.");
        motor.reverse(7000);
        
        System.out.println("   Motor STOPPED for 3 seconds.");
        Thread.sleep(3000);
        
        System.out.println("   Motor FORWARD with slower speed for 10 seconds.");
        motor.setStepInterval(10);
        motor.forward(10000);

        System.out.println("   Motor STOPPED.");

        motor.stop();
    }
}
//END SNIPPET: stepper-motor-gpio-snippet
