package com.pi4j.component.servo;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  ServoProvider.java
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

import java.io.IOException;
import java.util.List;

import com.pi4j.io.gpio.Pin;

/**
 * This interface allows factory to create/cache {@link ServoDriver} objects.
 *
 * @author Daniel Sendula
 */
public interface ServoProvider {

    /**
     * This method returns a list of pins this provider implementation
     * can drive.
     *
     * @return list of pins
     * @throws IOException in case there is an error providing list of pins
     */
    List<Pin> getDefinedServoPins() throws IOException;

    /**
     * This method returns a {@link ServoDriver} for asked pin.
     * It may return IOException in case that driver does not know of asked
     * pin or cannot drive servo from it. Or there is any other initialization
     * error.
     *
     * @param servoPin pin driver is needed for
     * @return a servo driver
     * @throws IOException in case that servo driver cannnot be provided for asked pin
     */
    ServoDriver getServoDriver(Pin servoPin) throws IOException;

}
