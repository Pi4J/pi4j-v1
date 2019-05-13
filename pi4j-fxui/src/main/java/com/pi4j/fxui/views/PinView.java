package com.pi4j.fxui.views;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JavaFX Visualizaion
 * FILENAME      :  PinView.java
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

import com.pi4j.io.gpio.HeaderPin;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Visualizes the {@link HeaderPin}.
 *
 * @author Frank Delporte (<a href="https://www.webtechie.be">https://www.webtechie.be</a>)
 */
class PinView extends HBox {

    /**
     * Constructor to create the visualization of a {@link HeaderPin}.
     *
     * @param pin The {@link HeaderPin} to be visualized.
     * @param extended True for extended view, false for compact view.
     * @param rightToLeft True/False to switch between RL and LR visualization.
     */
    PinView(final HeaderPin pin, final boolean extended, final boolean rightToLeft) {
        this.setNodeOrientation(rightToLeft ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
        this.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-style: solid;");
        this.setPadding(new Insets(1, 1, 1, 1));
        this.setSpacing(1);
        this.setPrefHeight(extended ? 30 : 15);

        // GPIO number
        Label gpioNumber = new Label();
        gpioNumber.setPrefWidth(50);
        gpioNumber.setTextFill(Color.BLUE);
        gpioNumber.setStyle("-fx-font: 20px Tahoma; -fx-alignment: TOP-CENTER;");

        if (pin.getPin() != null && pin.getPin().toString().contains("GPIO")) {
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
        } else {
            sbToolTip.append(pin.getName());
        }

        tooltip.setText(sbToolTip.toString());

        // Name and info
        VBox textBoxes = new VBox();
        textBoxes.setPrefWidth(120);
        textBoxes.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(pin.getName());
        name.setStyle("-fx-font: 12px Tahoma; -fx-font-weight: bold;");
        name.setTooltip(tooltip);

        textBoxes.getChildren().add(name);

        if (extended) {
            Label info = new Label(pin.getInfo());
            info.setStyle("-fx-font: 12px Tahoma; -fx-font-weight: normal;");

            textBoxes.getChildren().add(info);
        }

        this.getChildren().add(textBoxes);


        // Pin number
        Label pinNumber = new Label();
        pinNumber.setPrefWidth(25);
        pinNumber.setStyle("-fx-font: 8px Tahoma; -fx-rotate: -90;");
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
        }

        this.getChildren().add(state);
    }
}
