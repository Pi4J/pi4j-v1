package com.pi4j.gpio.extension.base;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MonitoringStateDeviceBase.java
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

import com.pi4j.io.gpio.GpioPinDigital;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Base class/thread to monitor actively GPIO Provider.
 * </p>
 *
 * @author Robert Savage (initial thread for PCF8574 and ADS1x15)
 * @author Gregory DEPUILLE
 */
public abstract class MonitoringStateDeviceBase<D, P extends GpioPinDigital> extends Thread implements MonitoringStateDevice {

    @Getter(AccessLevel.PROTECTED)
    private final D device;

    @Getter(AccessLevel.PROTECTED)
    private P irqPin = null;

    private boolean shuttingDown = false;

    @Getter
    private long waitTime = 50;

    protected abstract boolean irqRead();
    protected abstract void doRead() throws IOException;

    protected MonitoringStateDeviceBase(D device) {
        this.device = device;
    }

    protected MonitoringStateDeviceBase(D device, P irqPin) {
        this(device);
        this.irqPin = irqPin;
    }

    protected MonitoringStateDeviceBase(D device, P irqPin, long waitTimeMs) {
        this(device, irqPin);
        waitTime = waitTimeMs;
    }

    protected MonitoringStateDeviceBase(D device, P irqPin, long waitTime, TimeUnit unit) {
        this(device, irqPin);
        this.waitTime = unit.toMillis(waitTime);
    }

    @Override
    public void setWaitTime(long waitTimeMs) {
        this.waitTime = waitTimeMs;
    }

    @Override
    public void setWaitTime(long waitTime, TimeUnit unit) {
        this.waitTime = unit.toMillis(waitTime);
    }

    @Override
    public final void shutdown() {
        shuttingDown = true;
    }

    @Override
    public final void run() {
        while (!shuttingDown) {
            try {
                // read device pins state.
                // if an IRQ pin is registered, read only if an interruption is read
                // /!\ Usage of IRQ pin isn't compatible when the same pin is used for more than 1 device
                if (irqPin == null || irqRead()) {
                    doRead();
                }

                // ... lets take a short breather ...
                Thread.currentThread();
                Thread.sleep(waitTime);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
