package com.pi4j.service.websocket;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java REST services
 * FILENAME      :  HelloWorld.java
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

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.service.AppConfig;
import com.pi4j.service.GpioControllerInstance;
import java.util.Collection;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Provides a websocket controller for the pins.
 *
 * Based on https://www.pi4j.com/1.2/example/control.html
 */
@Controller
public class PinsController {

    /**
     * Get the current state of the pins.
     *
     * @return
     */
    @MessageMapping("/pins/states")
    @SendTo("/topic/pins/states")
    public Collection<GpioPin> getStates() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        return context.getBean(GpioControllerInstance.class).getGpioController().getProvisionedPins();
    }
}
