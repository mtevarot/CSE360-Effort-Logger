package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DefectLogConsoleController {
	@FXML 
	private Button backToConsoleButton; 
	
	public void backToConsole(ActionEvent Action) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLogConsole.fxml"));
	    Parent effortLogConsoleRoot = fxmlLoader.load();
	    
	    
	    Stage stage = (Stage) backToConsoleButton.getScene().getWindow();
	    stage.setScene(new Scene(effortLogConsoleRoot));
	    stage.setTitle("Effort Log Console");
	    stage.show();
	}
}
