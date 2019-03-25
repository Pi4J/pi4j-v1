package com.pi4j.io.gpio;

/**
 * Describes a pin in the header.
 */
public class HeaderPin {
    private final int pinNumber;
    private final Integer wiringPiNumber;
    private final String name;
    private final String info;
    private final Pin pin;

    /**
     * Constructor.
     *
     * @param pinNumber Number of the pin, starting at 1.
     * @param wiringPiNumber Number of the pin according to the Pi4J/WiringPi GPIO numbering scheme.
     * @param name Name of the pin.
     * @param info Info of the pin.
     * @param pin The type of pin.
     */
    public HeaderPin(final int pinNumber, final Integer wiringPiNumber, final String name, final String info, final Pin pin) {
        this.pinNumber = pinNumber;
        this.wiringPiNumber = wiringPiNumber;
        this.name = name;
        this.info = info;
        this.pin = pin;
    }

    /**
     * @return The pin number, starting at 1.
     */
    public int getPinNumber() {
        return pinNumber;
    }

    /**
     * @return Number of the pin according to the Pi4J/WiringPi GPIO numbering scheme.
     */
    public Integer getWiringPiNumber() {
        return wiringPiNumber;
    }

    /**
     * @return Name of the pin.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Info of the pin.
     */
    public String getInfo() {
        return info;
    }

    /**
     * @return The type of pin.
     */
    public Pin getPin() {
        return pin;
    }
}
