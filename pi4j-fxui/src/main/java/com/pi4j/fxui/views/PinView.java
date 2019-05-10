package com.pi4j.fxui.views;

import com.pi4j.io.gpio.HeaderPin;
import com.pi4j.io.gpio.PinMode;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PinView extends HBox {
    public PinView(final boolean extended, final HeaderPin pin, final boolean rightToLeft) {
        this.setNodeOrientation(rightToLeft ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
        this.setStyle("-fx-border-color: black;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 2;\n" +
                "-fx-border-style: solid;\n");
        this.setPadding(new Insets(1, 1, 1, 1));
        this.setSpacing(1);
        this.setPrefHeight(extended ? 30 : 15);

        // GPIO number
        Label gpioNumber = new Label();
        gpioNumber.setPrefWidth(40);
        gpioNumber.setStyle("-fx-font: 20px Tahoma;\n");

        if (pin.getPin() != null) {
            final String lbl = pin.getPin().toString().replace("GPIO ", "");
            gpioNumber.setText(lbl);
        }

        this.getChildren().add(gpioNumber);

        // Tooltip
        final Tooltip tooltip = new Tooltip();

        StringBuilder sbToolTip = new StringBuilder();

        if (pin.getPin() != null) {
            sbToolTip.append(pin.getPin().getName()).append("\n");

            if (!pin.getPin().getSupportedPinModes().isEmpty()) {
                sbToolTip
                        .append("Supported pin modes:")
                        .append("\n")
                        .append(pin.getPin().getSupportedPinModes()
                            .stream()
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")))
                        .append("\n");
            }
        }

        tooltip.setText(sbToolTip.toString());

        // Name and info
        VBox textBoxes = new VBox();
        textBoxes.setPrefWidth(120);
        textBoxes.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(pin.getName());
        name.setStyle("-fx-font: 12px Tahoma;\n" +
                "-fx-font-weight: bold;\n");
        name.setTooltip(tooltip);

        textBoxes.getChildren().add(name);

        if (extended) {
            Label info = new Label(pin.getInfo());
            info.setStyle("-fx-font: 12px Tahoma;\n" +
                    "-fx-font-weight: normal;\n");

            textBoxes.getChildren().add(info);
        }

        this.getChildren().add(textBoxes);


        // Pin number
        Label pinNumber = new Label();
        pinNumber.setPrefWidth(25);
        pinNumber.setStyle("-fx-font: 8px Tahoma;\n" +
                "-fx-rotate: -90;\n");
        pinNumber.setText(String.valueOf(pin.getPinNumber()));

        this.getChildren().add(pinNumber);

        // State visualization
        VBox state = new VBox();
        state.setPrefWidth(25);
        state.setPrefHeight(25);
        state.setAlignment(Pos.CENTER);

        if (pin.getPin() != null) {
            System.out.println(pin.getPin() + ": " + pin.getPin().getSupportedPinModes());

            CheckBox cb = new CheckBox();
            state.getChildren().add(cb);
        } else {

        }

        this.getChildren().add(state);
    }
}
