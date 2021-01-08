package com.pi4j.system;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SystemInfoFactory.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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


import com.pi4j.concurrent.DefaultExecutorServiceFactory;
import com.pi4j.concurrent.ExecutorServiceFactory;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.impl.GpioControllerImpl;
import com.pi4j.platform.PlatformManager;

/**
 * <p>This factory class provides a static method to create new 'GpioController' instances. </p>
 *
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 *
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see GpioController
 * @see GpioProvider
 *
 * @see <a href="https://www.pi4j.com/">https://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class SystemInfoFactory {

    // we only allow a single default provider to exists
    private static SystemInfoProvider provider = null;

    // private constructor
    private SystemInfoFactory() {
        // forbid object construction
    }

    /**
     * <p>Return default instance of {@link SystemInfoProvider}.</p>
     *
     * @return Return a new SystemInfoProvider impl instance.
     */
    public static SystemInfoProvider getProvider() {
        // if a provider has not been created, then create a new instance
        if (provider == null) {
            // create the provider based on the PlatformManager's selected platform
            provider = PlatformManager.getPlatform().getSystemInfoProvider();
        }

        // return the provider instance
        return provider;
    }

    /**
     * Sets default {@link SystemInfoProvider}.
     *
     * @param provider default system info provider
     */
    public static void setProvider(SystemInfoProvider provider) {
        // set the default provider instance
        SystemInfoFactory.provider = provider;
    }
}
