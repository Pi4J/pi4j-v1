package com.pi4j.fxui;

import com.pi4j.fxui.views.HeaderView;
import com.pi4j.platform.Platform;
import com.pi4j.system.SystemInfo.BoardType;
import java.util.Arrays;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        final String javaVersion = System.getProperty("java.version");
        final String javaFxVersion = System.getProperty("javafx.version");

        // PI4J Platforms
        ObservableList<String> platforms = FXCollections.observableArrayList(
                Arrays.stream(Platform.values()).map(Enum::name).toArray(String[]::new)
        );

        final ComboBox cbPlatforms = new ComboBox(platforms);

        // PI4J Boardtypes
        ObservableList<String> boardTypes = FXCollections.observableArrayList(
                Arrays.stream(BoardType.values()).map(Enum::name).toArray(String[]::new)
        );

        final ComboBox cbBoardTypes = new ComboBox(boardTypes);

        // JavaFX window
        Scene scene = new Scene(
                new VBox(
                        new Label("Java version: " + javaVersion + ", FX: " + javaFxVersion),
                        new HBox(cbPlatforms, cbBoardTypes),
                        new HeaderView(BoardType.RaspberryPi_3B_Plus)
                ),
                800,
                600
        );
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}