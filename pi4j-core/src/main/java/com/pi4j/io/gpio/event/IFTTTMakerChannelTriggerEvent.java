package com.pi4j.io.gpio.event;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  IFTTTMakerChannelTriggerEvent.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
import com.pi4j.io.gpio.PinState;
import com.pi4j.util.StringUtil;

import java.util.EventObject;


public class IFTTTMakerChannelTriggerEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    protected final GpioPin pin;
    protected final PinState state;
    private final String eventName;
    private String value1 = StringUtil.EMPTY;
    private String value2 = StringUtil.EMPTY;
    private String value3 = StringUtil.EMPTY;

    public IFTTTMakerChannelTriggerEvent(Object obj, GpioPin pin, PinState state, String eventName, String value1, String value2, String value3) {
        super(obj);
        this.pin = pin;
        this.state = state;
        this.eventName = eventName;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    /**
     * Get the pin number that changed and raised this event.
     *
     * @return GPIO pin number (not header pin number; not wiringPi pin number)
     */
    public GpioPin getPin() {
        return this.pin;
    }

    /**
     * Get the pin state that activated this trigger.
     *
     * @return GPIO pin state
     */
    public PinState getState() { return this.state; }

    /**
     * Get the IFTTT event name configured for this trigger.
     *
     * @return IFTTT event name
     */
    public String getEventName() { return this.eventName; }

    /**
     * Get the IFTTT value1 data for this triggered event.
     * By default, this is the GPIO pin name.
     * The consumer can optionally override this value using the 'setValue1()' method.
     *
     * @return IFTTT value1 data
     */
    public String getValue1() { return this.value1; }

    /**
     * Set (override) the value1 data that will be sent to the IFTTT trigger event.
     *
     * @param data new value data/string
     */
    public void setValue1(String data) { this.value1 = data; }

    /**
     * Get the IFTTT value2 data for this triggered event.
     * By default, this is the GPIO state value (integer).
     * The consumer can optionally override this value using the 'setValue2()' method.
     *
     * @return IFTTT value2 data
     */
    public String getValue2() { return this.value2; }

    /**
     * Set (override) the value2 data that will be sent to the IFTTT trigger event.
     *
     * @param data new value data/string
     */
    public void setValue2(String data) { this.value2 = data; }

    /**
     * Get the IFTTT value2 data for this triggered event.
     * By default, this is a JSON string of data including all details about the GPIO pin and PinState.
     * The consumer can optionally override this value using the 'setValue3()' method.
     *
     * @return IFTTT value2 data
     */
    public String getValue3() { return this.value3; }

    /**
     * Set (override) the value3 data that will be sent to the IFTTT trigger event.
     *
     * @param data new value data/string
     */
    public void setValue3(String data) { this.value3 = data; }
}
