package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;
import javafx.scene.control.ButtonBar;
import javafx.event.ActionEvent;
import java.util.Optional;

public class PlanningPokerController {
	
	@FXML 
	private Button backButton; 
	
	public void handleBackAction(ActionEvent event) throws IOException {
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Confirmation");
	    alert.setHeaderText("All data will be lost if you leave, Are you sure?");

	    
	    ButtonType buttonTypeYes = new ButtonType("Yes");
	    ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
	    alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

	    Optional<ButtonType> result = alert.showAndWait();
	    
	    if (result.isPresent() && result.get() == buttonTypeYes) {
	        goBack();
	    } 
	}

	public void goBack() throws IOException {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLoggerV2Homepage.fxml"));
    	    Parent effortLogConsoleRoot = fxmlLoader.load();
    	    
    	    
    	    Stage stage = (Stage) backButton.getScene().getWindow();
    	    stage.setScene(new Scene(effortLogConsoleRoot));
    	    stage.setTitle("Effort Log Console");
    	    stage.show();
    }
}
