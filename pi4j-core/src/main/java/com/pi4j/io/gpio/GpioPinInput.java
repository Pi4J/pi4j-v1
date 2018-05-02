package com.pi4j.io.gpio;

import com.pi4j.io.gpio.trigger.GpioTrigger;

import java.util.Collection;
import java.util.List;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinInput.java
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

/**
 * Gpio input pin interface. This interface is extension of {@link com.pi4j.io.gpio.GpioPin} interface
 * with listeners and triggers support..
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public interface GpioPinInput extends GpioPin {

    Collection<GpioTrigger> getTriggers();
    void addTrigger(GpioTrigger... trigger);
    void addTrigger(List<? extends GpioTrigger> triggers);

    void removeTrigger(GpioTrigger... trigger);
    void removeTrigger(List<? extends GpioTrigger> triggers);
    void removeAllTriggers();

}
