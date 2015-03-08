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
 * Copyright (C) 2012 - 2015 Pi4J
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
