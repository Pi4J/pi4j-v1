package com.pi4j.fxui.data;

import com.pi4j.io.gpio.HeaderPin;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Helper class to be used in the table data list to convert a {@link HeaderPin} to column values.
 */
public class BoardTypeData {

    private final SimpleIntegerProperty pinNumber;
    private final SimpleStringProperty address;
    private final SimpleStringProperty name;
    private final SimpleStringProperty info;
    private final SimpleStringProperty pinName;

    private final SimpleBooleanProperty supportsPinPullResistance;
    private final SimpleBooleanProperty supportsPinEdges;
    private final SimpleBooleanProperty supportsPinEvents;

    private final SimpleStringProperty pinModes;
    private final SimpleStringProperty pinPullResistances;
    private final SimpleStringProperty pinEdges;

    public BoardTypeData(HeaderPin headerPin) {
        this.pinNumber = new SimpleIntegerProperty(headerPin.getPinNumber());
        this.name = new SimpleStringProperty(headerPin.getName());
        this.info = new SimpleStringProperty(headerPin.getInfo());

        if (headerPin.getPin() != null) {
            this.address = new SimpleStringProperty(String.valueOf(headerPin.getPin().getAddress()));
            this.pinName = new SimpleStringProperty(headerPin.getPin().getName());
            this.supportsPinPullResistance = new SimpleBooleanProperty(headerPin.getPin().supportsPinPullResistance());
            this.supportsPinEdges = new SimpleBooleanProperty(headerPin.getPin().supportsPinEdges());
            this.supportsPinEvents = new SimpleBooleanProperty(headerPin.getPin().supportsPinEvents());

            if (!headerPin.getPin().getSupportedPinModes().isEmpty()) {
                this.pinModes = new SimpleStringProperty(headerPin.getPin()
                        .getSupportedPinModes()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(", ")));
            } else {
                this.pinModes = new SimpleStringProperty("");
            }

            if (!headerPin.getPin().getSupportedPinPullResistance().isEmpty()) {
                this.pinPullResistances = new SimpleStringProperty(headerPin.getPin()
                        .getSupportedPinPullResistance()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(", ")));
            } else {
                this.pinPullResistances = new SimpleStringProperty("");
            }

            if (!headerPin.getPin().getSupportedPinEdges().isEmpty()) {
                this.pinEdges = new SimpleStringProperty(headerPin.getPin()
                        .getSupportedPinEdges()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(", ")));
            } else {
                this.pinEdges = new SimpleStringProperty("");
            }
        } else {
            this.address = new SimpleStringProperty("");
            this.pinName = new SimpleStringProperty("");
            this.supportsPinPullResistance = new SimpleBooleanProperty(false);
            this.supportsPinEdges = new SimpleBooleanProperty(false);
            this.supportsPinEvents = new SimpleBooleanProperty(false);
            this.pinModes = new SimpleStringProperty("");
            this.pinPullResistances = new SimpleStringProperty("");
            this.pinEdges = new SimpleStringProperty("");
        }
    }

    public int getPinNumber() {
        return this.pinNumber.get();
    }

    public String getAddress() {
        return this.address.get();
    }

    public String getName() {
        return this.name.get();
    }

    public String getInfo() {
        return this.info.get();
    }

    public String getPinName() {
        return this.pinName.get();
    }

    public boolean getSupportsPinPullResistance() {
        return this.supportsPinPullResistance.get();
    }

    public boolean getSupportsPinEdges() {
        return this.supportsPinEdges.get();
    }

    public boolean getSupportsPinEvents() {
        return this.supportsPinEvents.get();
    }

    public String getPinModes() {
        return this.pinModes.get();
    }

    public String getPinPullResistances() {
        return this.pinPullResistances.get();
    }

    public String getPinEdges() {
        return this.pinEdges.get();
    }
}
