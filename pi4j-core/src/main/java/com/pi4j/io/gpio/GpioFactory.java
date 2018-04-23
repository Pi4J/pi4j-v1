package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioFactory.java
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


import com.pi4j.concurrent.DefaultExecutorServiceFactory;
import com.pi4j.concurrent.ExecutorServiceFactory;
import com.pi4j.io.gpio.impl.GpioControllerImpl;
import com.pi4j.platform.Platform;
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
 * @see com.pi4j.io.gpio.GpioController
 * @see com.pi4j.io.gpio.GpioProvider
 *
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class GpioFactory {

    // we only allow a single controller to exists
    private static GpioController controller = null;

    // we only allow a single default provider to exists
    private static GpioProvider provider = null;

    // we only allow a single default scheduled executor service factory to exists
    private static ExecutorServiceFactory executorServiceFactory = null;

    // private constructor
    private GpioFactory() {
        // forbid object construction
    }

    /**
     * <p>Return default instance of {@link GpioController}.</p>
     * <p>Note: this is not thread safe singleton pattern implementation.
     *    Implementation does not provide any synchronization or mechanisms to prevent
     *    instantiation of two instances.</p>
     *
     * @return Return a new GpioController impl instance.
     */
    public static GpioController getInstance() {
        // if a controller has not been created, then create a new instance
        // Note: this is not thread safe singleton
        if (controller == null) {
            controller = new GpioControllerImpl();
        }
        // else return a copy of the existing controller
        return controller;
    }

    /**
     * <p>Return default instance of {@link GpioProvider}.</p>
     * <p>Note: this is not thread safe singleton pattern implementation.
     *    Implementation does not provide any synchronization or mechanisms to prevent
     *    instantiation of two instances.</p>
     *
     * @return Return a new GpioController impl instance.
     */
    public static GpioProvider getDefaultProvider() {
        // if a provider has not been created, then create a new instance
        if (provider == null) {
            // create the provider based on the PlatformManager's selected platform
            provider = PlatformManager.getPlatform().getGpioProvider();
        }

        // return the provider instance
        return provider;
    }

    /**
     * Sets default {@link GpioProvider}.
     *
     * @param provider default gpio provider
     */
    public static void setDefaultProvider(GpioProvider provider) {
        // set the default provider instance
        GpioFactory.provider = provider;
    }


    /**
     * <p>Return instance of {@link ExecutorServiceFactory}.</p>
     * <p>Note: .</p>
     *
     * @return Return a new GpioController impl instance.
     */
    public static ExecutorServiceFactory getExecutorServiceFactory() {
        // if an executor service provider factory has not been created, then create a new default instance
        if (executorServiceFactory == null) {
            executorServiceFactory = new DefaultExecutorServiceFactory();
        }
        // return the provider instance
        return executorServiceFactory;
    }

    /**
     * Sets default {@link ExecutorServiceFactory}.
     *
     * @param executorServiceFactory service factory instance
     */
    public static void setExecutorServiceFactory(ExecutorServiceFactory executorServiceFactory) {
        // set the default factory instance
        GpioFactory.executorServiceFactory = executorServiceFactory;
    }
}
