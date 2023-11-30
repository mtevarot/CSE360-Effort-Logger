package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class EstimateDisplayController {
    
    @FXML
    private TableView<Guess> guessTableView;

    @FXML
    private TableColumn<Guess, String> employeeNameColumn;

    @FXML
    private TableColumn<Guess, Integer> guessColumn;

    private ObservableList<Guess> guessData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
    	centerTextInColumn(employeeNameColumn);
        centerTextInColumn(guessColumn);
        employeeNameColumn.setCellValueFactory(cellData -> cellData.getValue().employeeNameProperty());
        guessColumn.setCellValueFactory(cellData -> cellData.getValue().guessProperty().asObject());
        guessTableView.setItems(guessData);
    }

    public void setGuessData(ObservableList<Guess> guessData) {
        this.guessData.setAll(guessData);
    }
    
    private <T> void centerTextInColumn(TableColumn<Guess, T> column) {
        column.setCellFactory(tc -> new TableCell<Guess, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

}
