package com.pi4j.fxui;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JavaFX Visualizaion
 * FILENAME      :  Main.java
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

import com.pi4j.fxui.data.BoardTypeData;
import com.pi4j.fxui.views.BooleanTableCell;
import com.pi4j.fxui.views.HeaderView;
import com.pi4j.io.gpio.Header;
import com.pi4j.io.gpio.HeaderPin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.system.SystemInfo.BoardType;
import java.util.Arrays;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main class of the application.
 */
public class Main extends Application {

    private ListView listBoardTypes;

    private Label lblSelectedBoard;

    private TabPane tabPane;
    private Tab tabTable;
    private Tab tabCompactHeader;
    private Tab tabExtendedHeader;

    private TableView<BoardTypeData> tableView;
    private ObservableList<BoardTypeData> data = FXCollections.observableArrayList();

    /**
     * Entry point of the application.
     *
     * @param args Start-up arguments.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) {
        // PI4J Boardtypes
        this.createBoardsList();

        // Label which will show the selected board
        this.lblSelectedBoard = new Label("Select a board from the list");
        this.lblSelectedBoard.setPadding(new Insets(3, 3, 3, 7));
        this.lblSelectedBoard.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Tabs for the different visualizations
        this.createTabs();

        // Holder to align the elements correctly
        BorderPane holder = new BorderPane();
        holder.setTop(this.lblSelectedBoard);
        holder.setLeft(this.listBoardTypes);
        holder.setCenter(this.tabPane);

        Scene scene = new Scene(holder);
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.show();
    }

    /**
     * Creates a list with all the predefined board types in select list.
     */
    private void createBoardsList() {
        ObservableList<String> boardTypes = FXCollections.observableArrayList(
                Arrays.stream(BoardType.values()).map(Enum::name).toArray(String[]::new)
        );

        this.listBoardTypes = new ListView(boardTypes);
        this.listBoardTypes.setMinWidth(200);

        this.listBoardTypes.getSelectionModel().selectedItemProperty().addListener(
                (ChangeListener<String>) (ov, previous, selected) -> visualize(selected));
    }

    /**
     * Creates a tab pane for the different visualizations.
     */
    private void createTabs() {
        this.tabPane = new TabPane();

        this.tabTable = new Tab("Table view");
        this.tabPane.getTabs().add(this.tabTable);
        this.createTable();
        this.tabTable.setContent(this.tableView);

        this.tabCompactHeader = new Tab("Compact header view");
        this.tabPane.getTabs().add(this.tabCompactHeader);

        this.tabExtendedHeader = new Tab("Extended header view");
        this.tabPane.getTabs().add(this.tabExtendedHeader);
    }

    /**
     * Creates a table with all the columns.
     */
    private void createTable() {
        this.tableView = new TableView<>();

        TableColumn colPinNumber = new TableColumn("Pin");
        colPinNumber.setStyle("-fx-alignment: TOP-CENTER;");
        colPinNumber.setMinWidth(70);
        colPinNumber.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Integer>("pinNumber"));

        // Description
        TableColumn colDescription = new TableColumn("Description");

        TableColumn colName = new TableColumn("Name");
        colName.setMinWidth(75);
        colName.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Integer>("name"));

        TableColumn colInfo = new TableColumn("Info");
        colInfo.setMinWidth(125);
        colInfo.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Integer>("Info"));

        TableColumn colAddress = new TableColumn("Address");
        colAddress.setStyle("-fx-alignment: TOP-CENTER;");
        colAddress.setMinWidth(70);
        colAddress.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Integer>("address"));

        colDescription.getColumns().addAll(colName, colInfo, colAddress);

        // Modes
        TableColumn colPinModes = new TableColumn("Modes");
        colPinModes.setMinWidth(300);
        colPinModes.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, String>("pinModes"));

        // Events
        TableColumn colPinEventsSupported = new TableColumn("Events");
        colPinEventsSupported.setMinWidth(75);
        colPinEventsSupported.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, String>("supportsPinEvents"));
        colPinEventsSupported.setCellFactory(factory -> new BooleanTableCell());

        // Resistance
        TableColumn colPinPullResistances = new TableColumn("Pull resistances");

        TableColumn colPinPullResistancesSupported = new TableColumn("Supported");
        colPinPullResistancesSupported.setMinWidth(75);
        colPinPullResistancesSupported.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Boolean>("supportsPinPullResistance"));
        colPinPullResistancesSupported.setCellFactory(factory -> new BooleanTableCell());

        TableColumn colPinPullResistancesTypes = new TableColumn("Types");
        colPinPullResistancesTypes.setMinWidth(300);
        colPinPullResistancesTypes.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, String>("pinPullResistances"));

        colPinPullResistances.getColumns().addAll(colPinPullResistancesSupported, colPinPullResistancesTypes);

        // Edges
        TableColumn colPinEdges = new TableColumn("Edges");

        TableColumn colPinEdgesSupported = new TableColumn("Supported");
        colPinEdgesSupported.setMinWidth(75);
        colPinEdgesSupported.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Boolean>("supportsPinEdges"));
        colPinEdgesSupported.setCellFactory(factory -> new BooleanTableCell());

        TableColumn colPinEdgesTypes = new TableColumn("Types");
        colPinEdgesTypes.setMinWidth(300);
        colPinEdgesTypes.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, String>("pinEdges"));

        colPinEdges.getColumns().addAll(colPinEdgesSupported, colPinEdgesTypes);

        this.tableView.getColumns().addAll(colPinNumber,
                colDescription,
                colPinModes,
                colPinEventsSupported,
                colPinPullResistances,
                colPinEdges);
    }

    /**
     * Fill the table data provider with a list of {@link BoardTypeData}.
     *
     * @param boardType The {@link BoardType} from which all {@link HeaderPin} need to be added to the table.
     */
    private void fillTableData(BoardType boardType) {
        this.data.clear();

        if (boardType != null) {
            Header header = RaspiPin.getHeader(boardType);

            if (header != null) {
                for (HeaderPin pin : header.getPins()) {
                    this.data.add(new BoardTypeData(pin));
                }
            }
        }

        this.tableView.setItems(data);
    }

    /**
     * Fill the tabs with the pinning visualization of the selected board type.
     *
     * @param selectedBoardName The name of the selected board type.
     */
    private void visualize(String selectedBoardName) {
        BoardType selectedBoardType = BoardType.valueOf(selectedBoardName);

        System.out.println("Drawing " + selectedBoardType);

        if (selectedBoardType != null) {
            Header header = RaspiPin.getHeader(selectedBoardType);

            this.lblSelectedBoard.setText(selectedBoardType.name()
                    + (header == null ? ": header is not defined" : ": " + header.getPins().size() + " pins")
            );

            ScrollPane spCompact = new ScrollPane();
            spCompact.setContent(new HeaderView(selectedBoardType, false));
            this.tabCompactHeader.setContent(spCompact);

            ScrollPane spExtended = new ScrollPane();
            spExtended.setContent(new HeaderView(selectedBoardType, true));
            this.tabExtendedHeader.setContent(spExtended);

            this.fillTableData(selectedBoardType);

            this.tabPane.setDisable(false);
        } else {
            this.lblSelectedBoard.setText("Board type not found for: " + selectedBoardName);

            this.tabCompactHeader.setContent(null);
            this.tabExtendedHeader.setContent(null);
            this.fillTableData(null);

            this.tabPane.setDisable(true);
        }
    }
}