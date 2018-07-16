package com.pi4j.io.gpio.exception;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  PinProviderException.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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


import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;

/**
 * Pin provider exception.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class PinProviderException extends RuntimeException {

    private static final long serialVersionUID = -519207741462960871L;
    private final Pin pin;
    private final GpioProvider gpioProvider;

    public PinProviderException(GpioProvider provider, Pin pin) {
        super("GPIO pin [" + pin.toString() + "] expects provider [" + pin.getProvider() + "] but is attempting to be provisioned with provider [" + provider.getName() + "]; provisioning failed.");
        this.pin = pin;
        this.gpioProvider = provider;
    }

    public Pin getPin() {
        return pin;
    }
    public GpioProvider getGpioProvider() {
        return gpioProvider;
    }
}
