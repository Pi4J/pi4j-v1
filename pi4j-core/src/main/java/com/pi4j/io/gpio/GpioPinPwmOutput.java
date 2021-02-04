package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinPwmOutput.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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

/**
 * Gpio output pwm pin interface.This interface adds operation to set output pwm value.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public interface GpioPinPwmOutput extends GpioPinPwm, GpioPinOutput {

    /**
     * Set the PWM value/rate to toggle the GPIO pin.
     * If this is a hardware PWM pin, the value should be between a range of 0 to 1024.
     * If this is a software emulated PWM pin, the value should be between a range of 0 to 100.
     *
     * @param value
     */
    void setPwm(int value);

    /**
     * This sets the range register in the PWM generator.
     * The default is 1024 for hardware PWM.
     * The default is 100 for software emulated PWM.
     *
     * @param range
     */
    void setPwmRange(int range);
}
