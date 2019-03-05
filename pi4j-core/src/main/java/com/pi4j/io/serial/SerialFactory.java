package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialFactory.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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


import com.pi4j.concurrent.DefaultExecutorServiceFactory;
import com.pi4j.concurrent.ExecutorServiceFactory;
import com.pi4j.io.serial.impl.SerialImpl;

/**
 * <p> This factory class provide a static method to create new 'Serial' instances. </p>
 *
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see com.pi4j.io.serial.Serial
 * @see com.pi4j.io.serial.SerialDataEvent
 * @see SerialDataEventListener
 *
 * @see <a href="https://www.pi4j.com/">https://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SerialFactory {

    private static boolean isshutdown = false;

    // we only allow a single default scheduled executor service factory to exists
    private static ExecutorServiceFactory executorServiceFactory = null;

    // private constructor
    private SerialFactory() {
        // forbid object construction
    }

    /**
     * Create New Serial instance
     *
     * @return Return a new Serial implementation instance.
     */
    public static Serial createInstance() {
        isshutdown = false;
        return new SerialImpl();
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
        SerialFactory.executorServiceFactory = executorServiceFactory;
    }

    /**
     * This method returns TRUE if the serial controller has been shutdown.
     *
     * @return shutdown state
     */
    public static boolean isShutdown(){
        return isshutdown;
    }


    /**
     * This method can be called to forcefully shutdown all serial port
     * monitoring, listening, and task threads/executors.
     */
    public static void shutdown()
    {
        // prevent reentrant invocation
        if(isShutdown())
            return;

        // shutdown all executor services
        //
        // NOTE: we are not permitted to access the shutdown() method of the individual
        // executor services, we must perform the shutdown with the factory
        SerialFactory.getExecutorServiceFactory().shutdown();

        // set is shutdown tracking variable
        isshutdown = true;
    }
}
