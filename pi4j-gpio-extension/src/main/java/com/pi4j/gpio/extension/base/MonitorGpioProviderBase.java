package com.pi4j.gpio.extension.base;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MonitorGpioProviderBase.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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

import com.pi4j.io.gpio.GpioProviderBase;

/**
 * <p>
 * This base GPIO provider defined the required interfaces and implements the base functionality for
 * monitor when an input has changed on external pins.
 * </p>
 *
 * <p>
 * The monitoring will be disabled, time based or controled from a main input on board.
 * </p>
 *
 * @author Gregory DEPUILLE
 */
public abstract class MonitorGpioProviderBase extends GpioProviderBase implements MonitorGpioProvider {

    protected MonitoringStateDevice monitor;

    /**
     * This method will be overrided by subclass to provide specific clean on shutdown.
     *
     * @throws Exception An exception may be raised (cf subclass)
     */
    protected void specificShutdown() throws Exception {
        // NOOP. Will be defined in subclass if needed
    }

    @Override
    public void shutdown() {
        // prevent reentrant invocation
        if(isShutdown())
            return;

        // perform shutdown login in base
        super.shutdown();

        // if a monitor is running, then shut it down now
        if (monitor != null) {
            // shutdown monitoring thread
            monitor.shutdown();
            monitor = null;
        }

        try {
            specificShutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
