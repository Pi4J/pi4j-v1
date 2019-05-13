package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Header.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the pinning layout of a header.
 *
 * @author Frank Delporte (<a href="https://www.webtechie.be">https://www.webtechie.be</a>)
 */
public class Header {
    private final int numberOfPins;
    private final List<HeaderPin> headerPins;

    /**
     * Constructor.
     *
     * @param numberOfPins The number of pins on the connector.
     */
    public Header(final int numberOfPins) {
        this.numberOfPins = numberOfPins;
        this.headerPins = new ArrayList<>(numberOfPins);
    }

    /**
     * Add a pin to the list.
     *
     * @param pinNumber
     * @param wiringPiNumber
     * @param name
     * @param description
     * @param pin
     */
    public void addHeaderPin(final int pinNumber, final Integer wiringPiNumber, final String name, final String description, final Pin pin) {
        if (pinNumber > this.numberOfPins) {
            throw new IllegalArgumentException("The given pin number exceeds the size of " + this.numberOfPins);
        }

        if (pinNumber < 1) {
            throw new IllegalArgumentException("The pin number needs to start at 1");
        }

        this.headerPins.add(pinNumber - 1, new HeaderPin(pinNumber, wiringPiNumber, name, description, pin));
    }

    /**
     * @return The list of pins in this header.
     */
    public List<HeaderPin> getPins() {
        return this.headerPins;
    }
}
