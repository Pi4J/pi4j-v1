package com.pi4j.fxui.views;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JavaFX Visualizaion
 * FILENAME      :  HeaderView.java
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

import com.pi4j.io.gpio.Header;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.system.SystemInfo.BoardType;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Visualizes the {@link Header} for the given {@link BoardType} in two columns, as it looks on the board.
 */
public class HeaderView extends VBox {

    /**
     * Constructor to create the visualization of the {@link Header}.
     *
     * @param boardType {@link BoardType} for which the {@link Header} must be visualized.
     * @param extended True for extended view, false for compact view.
     */
    public HeaderView(BoardType boardType, boolean extended) {
        this.setSpacing(1);

        Header header = RaspiPin.getHeader(boardType);

        if (header != null) {
            HBox rows = new HBox();

            rows.getChildren().add(this.getRow(header, extended, true));
            rows.getChildren().add(this.getRow(header, extended, false));

            this.getChildren().add(rows);
        }
    }

    /**
     * Visualizes one side of the header.
     *
     * @param header The {@link Header} to be visualized.
     * @param extended True for extended view, false for compact view.
     * @param firstRow True for first row, false for second.
     *
     * @return {@link VBox} with the pins of the header row.
     */
    private VBox getRow(Header header, boolean extended, boolean firstRow) {
        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(1, 1, 1, 1));
        vBox.setSpacing(extended ? 5 : 1);

        for (int i = (firstRow ? 0 : 1); i < header.getPins().size(); i += 2) {
            vBox.getChildren().add(new PinView(header.getPins().get(i), extended, !firstRow));
        }

        return vBox;
    }
}
