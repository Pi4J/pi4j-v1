package com.pi4j.service.gpio;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java remote services (REST + WebSockets)
 * FILENAME      :  GpioControllerSingleton.java
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
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Singleton instance for the {@link GpioFactory}.
 *
 * Based on https://www.pi4j.com/1.2/example/control.html
 *
 * @author Frank Delporte (<a href="https://www.webtechie.be">https://www.webtechie.be</a>)
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GpioControllerSingleton {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The GPIO controller.
     */
    private final GpioController gpio;

    /**
     * List of the provisioned pins with the address as key.
     */
    private final Map<Integer, Object> provisionedPins = new HashMap<>();

    /**
     * Constructor which initializes the {@link GpioController}.
     */
    public GpioControllerSingleton() {
        this.gpio = GpioFactory.getInstance();
    }

    /**
     * Get the pin for the given address.
     *
     * @param address The address of the GPIO pin.
     *
     * @return The {@link Pin} or null when not found.
     */
    private Pin getPinByAddress(int address) {
        Pin pin = RaspiPin.getPinByAddress(address);

        if (pin == null) {
            logger.error("No pin available for address {}", address);
        }

        return pin;
    }

    /**
     * Get the list of provisioned pins.
     *
     * @return Map<Integer, Object>
     */
    public Map<Integer, Object> getProvisionedPins() {
        return this.provisionedPins;
    }

    /**
     * Provision a GPIO as digital output pin.
     *
     * @param address The address of the GPIO pin.
     * @param name The name of the GPIO pin.
     *
     * @return True if successful.
     */
    public boolean provisionDigitalOutputPin(final int address, final String name) {
        if (this.provisionedPins.containsKey(address)) {
            throw new IllegalArgumentException("There is already a provisioned pin at the given address");
        }

        final GpioPinDigitalOutput provisionedPin = this.gpio.provisionDigitalOutputPin(
                this.getPinByAddress(address), name, PinState.HIGH
        );
        provisionedPin.setShutdownOptions(true, PinState.LOW);

        if (provisionedPin != null) {
            this.provisionedPins.put(address, provisionedPin);

            return true;
        }

        return false;
    }

    /**
     * Provision a GPIO as digital output pin.
     *
     * @param address The address of the GPIO pin.
     * @param name The name of the GPIO pin.
     *
     * @return True if successful.
     */
    public boolean provisionDigitalInputPin(final int address, final String name) {
        if (this.provisionedPins.containsKey(address)) {
            throw new IllegalArgumentException("There is already a provisioned pin at the given address");
        }

        final GpioPinDigitalInput provisionedPin = this.gpio.provisionDigitalInputPin(this.getPinByAddress(address), name );

        if (provisionedPin != null) {
            this.provisionedPins.put(address, provisionedPin);

            return true;
        }

        return false;
    }

    /**
     * Get the state of the GPIO pin at the given address.
     *
     * @param address The address of the GPIO pin.
     *
     * @return The {@link PinState}.
     */
    public PinState getState(final int address) {
        logger.info("Get pin state requested for address {}", address);

        Object provisionedPin = this.provisionedPins.get(address);

        if (provisionedPin == null) {
            throw new IllegalArgumentException("There is no pin provisioned at the given address");
        } else {
            if (provisionedPin instanceof GpioPinDigitalInput) {
                return ((GpioPinDigitalInput) provisionedPin).getState();
            } else {
                throw new IllegalArgumentException("The provisioned pin at the given address is not of the type GpioPinDigitalInput");
            }
        }
    }

    /**
     * Set the state of a pin.
     *
     * @param address The address of the GPIO pin.
     * @param value The value, possible values 1 (= HIGH) or 0 and all other (= LOW)
     *
     * @return True if successful.
     */
    public boolean setPinDigitalState(final int address, final int value) {
        logger.info("Set pin digital state requested for address {} to value {}", address, value);

        Object provisionedPin = this.provisionedPins.get(address);

        if (provisionedPin == null) {
            throw new IllegalArgumentException("There is no pin provisioned at the given address");
        } else {
            if (provisionedPin instanceof GpioPinDigitalOutput) {
                if (value == 1) {
                    ((GpioPinDigitalOutput) provisionedPin).high();
                } else {
                    ((GpioPinDigitalOutput) provisionedPin).low();
                }

                return true;
            } else {
                throw new IllegalArgumentException("The provisioned pin at the given address is not of the type GpioPinDigitalOutput");
            }
        }
    }

    /**
     * Toggle a pin.
     *
     * @param address The address of the GPIO pin.
     *
     * @return True if successful.
     */
    public boolean togglePin(final int address) {
        logger.info("Toggle pin requested for address {}", address);

        Object provisionedPin = this.provisionedPins.get(address);

        if (provisionedPin == null) {
            throw new IllegalArgumentException("There is no pin provisioned at the given address");
        } else {
            if (provisionedPin instanceof GpioPinDigitalOutput) {
                ((GpioPinDigitalOutput) provisionedPin).toggle();

                return true;
            } else {
                throw new IllegalArgumentException("The provisioned pin at the given address is not of the type GpioPinDigitalOutput");
            }
        }
    }

    /**
     * Pulse a pin for the given duration.
     *
     * @param address The address of the GPIO pin.
     * @param duration The duration in milliseconds.
     *
     * @return True if successful.
     */
    public boolean pulsePin(final int address, final long duration) {
        logger.info("Pulse pin requested for address {} with duration {}", address, duration);

        Object provisionedPin = this.provisionedPins.get(address);

        if (provisionedPin == null) {
            throw new IllegalArgumentException("There is no pin provisioned at the given address");
        } else {
            if (provisionedPin instanceof GpioPinDigitalOutput) {
                ((GpioPinDigitalOutput) provisionedPin).pulse(duration);

                return true;
            } else {
                throw new IllegalArgumentException("The provisioned pin at the given address is not of the type GpioPinDigitalOutput");
            }
        }
    }
}
