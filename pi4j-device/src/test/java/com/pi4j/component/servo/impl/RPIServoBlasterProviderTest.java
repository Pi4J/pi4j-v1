package com.pi4j.component.servo.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  RPIServoBlasterProviderTest.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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

/*
 * Class:     RPIServoBlasterProviderTest
 * Created:   5th February 2017
 *
 * @author Sam Hough
 * @version 1.1, 5th February 2017
 */
public class RPIServoBlasterProviderTest {
    private String resolvePhysical(Pin pin) {
        return RPIServoBlasterProvider.PIN_MAP.get(pin);
    }

    @Test
    public void wiringPi30Mapping() {
        assertEquals("P1-27", resolvePhysical(RaspiPin.GPIO_30));
    }

    @Test
    public void wiringPi31Mapping() {
        assertEquals("P1-28", resolvePhysical(RaspiPin.GPIO_31));
    }

    @Test
    public void wiringPi21Mapping() {
        assertEquals("P1-29", resolvePhysical(RaspiPin.GPIO_21));
    }

    @Test
    public void wiringPi22Mapping() {
        assertEquals("P1-31", resolvePhysical(RaspiPin.GPIO_22));
    }

    @Test
    public void wiringPi26Mapping() {
        assertEquals("P1-32", resolvePhysical(RaspiPin.GPIO_26));
    }

    @Test
    public void wiringPi23Mapping() {
        assertEquals("P1-33", resolvePhysical(RaspiPin.GPIO_23));
    }

    @Test
    public void wiringPi24Mapping() {
        assertEquals("P1-35", resolvePhysical(RaspiPin.GPIO_24));
    }

    @Test
    public void wiringPi27Mapping() {
        assertEquals("P1-36", resolvePhysical(RaspiPin.GPIO_27));
    }

    @Test
    public void wiringPi25Mapping() {
        assertEquals("P1-37", resolvePhysical(RaspiPin.GPIO_25));
    }

    @Test
    public void wiringPi28Mapping() {
        assertEquals("P1-38", resolvePhysical(RaspiPin.GPIO_28));
    }

    @Test
    public void wiringPi29Mapping() {
        assertEquals("P1-40", resolvePhysical(RaspiPin.GPIO_29));
    }
}
