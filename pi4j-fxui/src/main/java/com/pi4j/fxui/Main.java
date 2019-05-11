package com.pi4j.fxui;

import com.pi4j.fxui.data.BoardTypeData;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    private ListView listBoardTypes;

    private TabPane tabPane;
    private Tab tabTable;
    private Tab tabCompactHeader;
    private Tab tabExtendedHeader;

    private TableView<BoardTypeData> tableView;

    @Override
    public void start(Stage stage) {

        // PI4J Boardtypes
        this.createBoardsList();

        // Tabs for the different visualizations
        this.createTabs();

        // JavaFX window
        HBox holder = new HBox(this.listBoardTypes, this.tabPane);
        holder.setSpacing(5);

        Scene scene = new Scene(holder, 1024, 600);
        stage.setScene(scene);
        stage.setMaximized(true);
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

        this.listBoardTypes.getSelectionModel().selectedItemProperty().addListener(
                (ChangeListener<String>) (ov, old_val, new_val) -> visualize(new_val));
    }

    /**
     * Creates a tab pane for the different visualizations
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

    private void createTable() {
        this.tableView = new TableView<>();

        TableColumn colPinNumber = new TableColumn("Pin");
        colPinNumber.setMinWidth(100);
        colPinNumber.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Integer>("pinNumber"));

        TableColumn colAddress = new TableColumn("Address");
        colAddress.setMinWidth(100);
        colAddress.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Integer>("address"));

        TableColumn colName = new TableColumn("Name");
        colName.setMinWidth(100);
        colName.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, String>("name"));

        TableColumn colPinModes = new TableColumn("Pin modes");
        colPinModes.setMinWidth(200);
        colPinModes.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, String>("pinModes"));

        this.tableView.getColumns().addAll(colPinNumber, colAddress, colName, colPinModes);
    }

    private void fillTableData(BoardType boardType) {
        ObservableList<BoardTypeData> data = FXCollections.observableArrayList();

        if (boardType != null) {
            Header header = RaspiPin.getHeader(boardType);

            if (header != null) {
                for (HeaderPin pin : header.getPins()) {
                    data.add(new BoardTypeData(pin));
                }
            }
        }

        this.tableView.setItems(data);
    }

    /**
     * Fill the tabs with the pinning visualization of the selected board type.
     *
     * @param selectedBoardName
     */
    private void visualize(String selectedBoardName) {
        BoardType selectedBoardType = BoardType.valueOf(selectedBoardName);

        System.out.println("Drawing " + selectedBoardType);

        if (selectedBoardType != null) {
            this.tabCompactHeader.setContent(new HeaderView(selectedBoardType, false));
            this.tabExtendedHeader.setContent(new HeaderView(selectedBoardType, true));
            this.fillTableData(selectedBoardType);

        } else {
            this.tabCompactHeader.setContent(new Label("Board type not found for: " + selectedBoardName));
            this.tabExtendedHeader.setContent(new Label("Board type not found for: " + selectedBoardName));
            this.fillTableData(null);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}