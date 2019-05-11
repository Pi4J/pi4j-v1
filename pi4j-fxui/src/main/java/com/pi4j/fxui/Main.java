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

/**
 * Main class of the application.
 */
public class Main extends Application {

    private ListView listBoardTypes;

    private TabPane tabPane;
    private Tab tabTable;
    private Tab tabCompactHeader;
    private Tab tabExtendedHeader;

    private TableView<BoardTypeData> tableView;

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
                (ChangeListener<String>) (ov, previous, selected) -> visualize(selected));
    }

    /**
     * Creates a tab pane for the different visualizations.
     */
    private void createTabs() {
        this.tabPane = new TabPane();
        this.tabPane.setMinWidth(1000);

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
        colPinEventsSupported.setStyle("-fx-alignment: TOP-CENTER;");
        colPinEventsSupported.setMinWidth(75);
        colPinEventsSupported.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Boolean>("supportsPinEvents"));

        // Resistance
        TableColumn colPinPullResistances = new TableColumn("Pull resistances");

        TableColumn colPinPullResistancesSupported = new TableColumn("Supported");
        colPinPullResistancesSupported.setStyle("-fx-alignment: TOP-CENTER;");
        colPinPullResistancesSupported.setMinWidth(75);
        colPinPullResistancesSupported.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Boolean>("supportsPinPullResistance"));

        TableColumn colPinPullResistancesTypes = new TableColumn("Types");
        colPinPullResistancesTypes.setMinWidth(300);
        colPinPullResistancesTypes.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, String>("pinPullResistances"));

        colPinPullResistances.getColumns().addAll(colPinPullResistancesSupported, colPinPullResistancesTypes);

        // Edges
        TableColumn colPinEdges = new TableColumn("Edges");

        TableColumn colPinEdgesSupported = new TableColumn("Supported");
        colPinEdgesSupported.setStyle("-fx-alignment: TOP-CENTER;");
        colPinEdgesSupported.setMinWidth(75);
        colPinEdgesSupported.setCellValueFactory(
                new PropertyValueFactory<BoardTypeData, Boolean>("supportsPinEdges"));

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
     * @param selectedBoardName The name of the selected board type.
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
}