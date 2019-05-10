package com.pi4j.fxui;

import com.pi4j.fxui.views.HeaderView;
import com.pi4j.platform.Platform;
import com.pi4j.system.SystemInfo.BoardType;
import java.util.Arrays;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private ComboBox comboBoxBoardTypes;
    private CheckBox checkBoxFull;
    private HBox holder;

    @Override
    public void start(Stage stage) {

        // Holder for the visualization of the header
        this.holder = new HBox();

        // PI4J Boardtypes
        ObservableList<String> boardTypes = FXCollections.observableArrayList(
                Arrays.stream(BoardType.values()).map(Enum::name).toArray(String[]::new)
        );

        this.comboBoxBoardTypes = new ComboBox(boardTypes);
        this.comboBoxBoardTypes.setOnAction(this::visualize);

        // Compact or full visualization
        this.checkBoxFull = new CheckBox("Extended visualization");
        this.checkBoxFull.setSelected(true);
        this.checkBoxFull.setOnAction(this::visualize);

        // JavaFX window
        Scene scene = new Scene(
                new VBox(comboBoxBoardTypes, checkBoxFull, this.holder),
                800,
                600
        );
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void visualize(Event event) {
        this.holder.getChildren().clear();

        BoardType selectedBoardType = BoardType.valueOf(this.comboBoxBoardTypes.getValue().toString());
        boolean showExtended = this.checkBoxFull.isSelected();

        System.out.println("Drawing " + selectedBoardType + " with " + (showExtended ? "extended" : "compact") + " view");

        if (selectedBoardType != null) {
            this.holder.getChildren().add(new HeaderView(selectedBoardType, showExtended));
        } else {
            this.holder.getChildren().add(new Label("Board type not found for: " + this.comboBoxBoardTypes.getValue().toString()));
        }
    }

    public static void main(String[] args) {
        launch();
    }
}