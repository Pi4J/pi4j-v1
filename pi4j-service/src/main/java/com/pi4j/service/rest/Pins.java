package com.pi4j.service.rest;

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

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides a REST interface with the pins.
 *
 * Based on https://www.pi4j.com/1.2/example/control.html
 */
@RestController
public class Pins {

    /**
     * The GPIO controller.
     */
    private final GpioController gpio;

    /**
     * Constructor.
     */
    private Pins() {
        this.gpio = GpioFactory.getInstance();
    }

    /**
     * Get the current state of the pins.
     *
     * @return
     */
    @GetMapping(path = "/pins/state", produces = "application/json")
    public Collection<GpioPin> getStates() {
        return this.gpio.getProvisionedPins();
    }

    /**
     * Set the state of a pin.
     *
     * @param pin
     * @param name
     * @param pinState
     *
     * @return
     */
    @PostMapping(path = "/pins/state/set", consumes = "application/json", produces = "application/json")
    public GpioPinDigitalOutput setPinState(@RequestBody Pin pin,
            @RequestBody String name,
            @RequestBody PinState pinState) {
        return this.gpio.provisionDigitalOutputPin(pin, name, pinState);
    }

    /**
     * Toggle a pin.
     *
     * @param pin
     */
    @PostMapping(path = "/pin/toggle", consumes = "application/json", produces = "application/json")
    public void togglePin(@RequestBody Pin pin) {
        this.gpio.provisionDigitalOutputPin(pin).toggle();
    }

    /**
     * Toggle a pin.
     *
     * @param pin
     */
    @PostMapping(path = "/pin/pulse", consumes = "application/json", produces = "application/json")
    public void pulsePin(@RequestBody Pin pin, @RequestBody int duration) {
        this.gpio.provisionDigitalOutputPin(pin).pulse(duration);
    }
}
