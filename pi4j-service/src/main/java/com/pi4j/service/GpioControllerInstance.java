package com.pi4j.service;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java remote services (REST + WebSockets)
 * FILENAME      :  GpioControllerInstance.java
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

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Singleton instance for the {@link GpioFactory}
 *
 * @author Frank Delporte (<a href="https://www.webtechie.be">https://www.webtechie.be</a>)
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GpioControllerInstance {

    /**
     * The GPIO controller.
     */
    private final GpioController gpio;

    /**
     * Constructor
     */
    public GpioControllerInstance() {
        this.gpio = GpioFactory.getInstance();

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // This application is running on Windows, so we need to configure Pi4J to run in simulated mode
            // TODO here?
        }
    }

    /**
     * @return The singleton instance of the {@link GpioController}.
     */
    public GpioController getGpioController() {
        return this.gpio;
    }
}
