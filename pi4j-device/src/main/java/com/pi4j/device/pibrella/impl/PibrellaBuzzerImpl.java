package com.pi4j.device.pibrella.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PibrellaBuzzerImpl.java
 * 
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import com.pi4j.component.buzzer.Buzzer;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.wiringpi.Gpio;

public class PibrellaBuzzerImpl implements Buzzer {

    public static int STOP_FREQUENCY = 0;

    protected final GpioPinPwmOutput pwm;

    public PibrellaBuzzerImpl(GpioPinPwmOutput pwm){
        this.pwm = pwm;
        Gpio.pwmSetMode(Gpio.PWM_MODE_MS); // set PWM mode to MARK-SPACE
    }

    @Override
    public void buzz(int frequency){
        if (frequency == 0) {
            pwm.setPwm(frequency);
        }
        else
        {
            int range = 600000 / frequency ;
            Gpio.pwmSetRange(range);
            pwm.setPwm(frequency / 2);
        }
    }

    @Override
    public void buzz(int frequency, int duration) {
        buzz(frequency);
        Gpio.delay(duration);
        stop();
    }

    @Override
    public void stop() {
        // stop the buzzer by setting the frequency to zero
        buzz(STOP_FREQUENCY);
    }
}
