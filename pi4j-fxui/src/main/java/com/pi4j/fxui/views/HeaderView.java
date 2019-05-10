package com.pi4j.fxui.views;

import com.pi4j.io.gpio.Header;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.system.SystemInfo.BoardType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HeaderView extends HBox {
    public HeaderView(BoardType boardType) {
        this.setSpacing(1);

        Header header = RaspiPin.getHeader(boardType);

        if (header != null) {
            this.getChildren().add(this.getRow(header, 0, false));
            this.getChildren().add(this.getRow(header, 1, true));
        }
    }

    private VBox getRow(Header header, int startIndex, boolean rightToLeft) {
        final VBox vBox = new VBox();

        for (int i = startIndex; i < header.getPins().size(); i += 2) {
            vBox.getChildren().add(new PinView(header.getPins().get(i), rightToLeft));
        }


        return vBox;
    }
}
