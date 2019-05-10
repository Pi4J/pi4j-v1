package com.pi4j.fxui.views;

import com.pi4j.io.gpio.Header;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.system.SystemInfo.BoardType;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HeaderView extends VBox {
    public HeaderView(BoardType boardType, boolean extended) {
        this.setSpacing(1);

        Header header = RaspiPin.getHeader(boardType);
        Label lbl = new Label();
        lbl.setStyle("-fx-font: 12px Tahoma;\n" +
                "-fx-font-weight: bold;\n");
        lbl.setPadding(new Insets(1, 1, 1, 1));
        this.getChildren().add(lbl);

        if (header == null) {
            lbl.setText("Header is not defined for: " + boardType.name());
        } else {
            lbl.setText(boardType.name() + " has " + header.getPins().size() + " pins");

            if (header != null) {
                HBox rows = new HBox();

                rows.getChildren().add(this.getRow(header, extended, 0, false));
                rows.getChildren().add(this.getRow(header, extended, 1, true));

                this.getChildren().add(rows);
            }
        }
    }

    private VBox getRow(Header header, boolean extended, int startIndex, boolean rightToLeft) {
        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(1, 1, 1, 1));
        vBox.setSpacing(extended ? 5 : 1);

        for (int i = startIndex; i < header.getPins().size(); i += 2) {
            vBox.getChildren().add(new PinView(extended, header.getPins().get(i), rightToLeft));
        }


        return vBox;
    }
}
