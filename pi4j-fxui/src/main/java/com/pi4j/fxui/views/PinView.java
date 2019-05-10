package com.pi4j.fxui.views;

import com.pi4j.io.gpio.HeaderPin;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PinView extends HBox {
    public PinView(final HeaderPin pin, final boolean rightToLeft) {
        this.setNodeOrientation(rightToLeft ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
        this.setStyle("-fx-border-color: black;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 2;\n" +
                "-fx-border-style: solid;\n");
        this.setPadding(new Insets(1, 1, 1, 1));
        this.setSpacing(1);
        this.setPrefHeight(30);

        // GPIO number
        Label gpioNumber = new Label();
        gpioNumber.setPrefWidth(40);
        gpioNumber.setStyle("-fx-font: 20px Tahoma;\n");

        if (pin.getPin() != null) {
            final String lbl = pin.getPin().toString().replace("GPIO ", "");
            gpioNumber.setText(lbl);
        }

        this.getChildren().add(gpioNumber);

        // Name and info
        VBox textBoxes = new VBox();
        textBoxes.setPrefWidth(120);
        textBoxes.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(pin.getName());
        name.setStyle("-fx-font: 12px Tahoma;\n" +
                "-fx-font-weight: bold;\n");

        textBoxes.getChildren().add(name);

        Label info = new Label(pin.getInfo());
        info.setStyle("-fx-font: 12px Tahoma;\n" +
                "-fx-font-weight: normal;\n");

        textBoxes.getChildren().add(info);

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
            CheckBox cb = new CheckBox();
            state.getChildren().add(cb);
        } else {

        }


        this.getChildren().add(state);

    }
}
