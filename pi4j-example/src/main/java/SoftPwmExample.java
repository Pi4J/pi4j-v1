/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  SoftPwmExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.RaspiPin;

/**
 * <p>
 * This example code demonstrates how to setup a software emulated PWM pin
 * </p>
 *
 * @author Robert Savage
 */
public class SoftPwmExample {
    /**
     * @param args the command line arguments
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        // create GPIO controller instance
        GpioController gpio = GpioFactory.getInstance();

        // we will provision the pin as a software emulated PWM output
        // pins that support hardware PWM should be provisioned as normal PWM outputs
        // each software emulated PWM pin does consume additional overhead in
        // terms of CPU usage.
        //
        // Software emulated PWM pins support a range between 0 (off) and 100 (max) by default.
        //
        // Please see: http://wiringpi.com/reference/software-pwm-library/
        // for more details on software emulated PWM
        GpioPinPwmOutput pwm = gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_00);

        // optionally set the PWM range (100 is default range)
        pwm.setPwmRange(100);

        // set the PWM rate to 100 (FULLY ON)
        pwm.setPwm(100);
        System.out.println("Software emulated PWM rate is: " + pwm.getPwm());

        System.out.println("Press ENTER to set the PWM to a rate of 50");
        System.console().readLine();

        // set the PWM rate to 50 (1/2 DUTY CYCLE)
        pwm.setPwm(50);
        System.out.println("Software emulated PWM rate is: " + pwm.getPwm());

        System.out.println("Press ENTER to set the PWM to a rate to 0 (stop PWM)");
        System.console().readLine();

        // set the PWM rate to 0 (FULLY OFF)
        pwm.setPwm(0);
        System.out.println("Software emulated PWM rate is: " + pwm.getPwm());

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}
