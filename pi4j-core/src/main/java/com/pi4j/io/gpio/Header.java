package com.pi4j.io.gpio;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the pinning layout of a header.
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
