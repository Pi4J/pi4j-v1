package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinPwmOutput.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
