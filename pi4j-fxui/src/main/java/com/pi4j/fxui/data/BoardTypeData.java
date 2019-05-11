package com.pi4j.fxui.data;

import com.pi4j.io.gpio.HeaderPin;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BoardTypeData {

    private final SimpleIntegerProperty pinNumber;
    private final SimpleIntegerProperty address;
    private final SimpleStringProperty name;
    private final SimpleStringProperty pinModes;

    public BoardTypeData(HeaderPin headerPin) {
        this.pinNumber = new SimpleIntegerProperty(headerPin.getPinNumber());

        if (headerPin.getPin() != null) {
            this.address = new SimpleIntegerProperty(headerPin.getPin().getAddress());
            this.name = new SimpleStringProperty(headerPin.getPin().getName());

            if (!headerPin.getPin().getSupportedPinModes().isEmpty()) {
                this.pinModes = new SimpleStringProperty(headerPin.getPin()
                        .getSupportedPinModes()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(", ")));
            } else {
                this.pinModes = new SimpleStringProperty("");
            }
        } else {
            this.address = new SimpleIntegerProperty(0);
            this.name = new SimpleStringProperty("");
            this.pinModes = new SimpleStringProperty("");
        }
    }

    public int getPinNumber() {
        return this.pinNumber.get();
    }

    public int getAddress() {
        return this.address.get();
    }

    public String getName() {
        return this.name.get();
    }

    public String getPinModes() {
        return this.pinModes.get();
    }
}
