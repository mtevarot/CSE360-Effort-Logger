package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EffortLoggerV2HomepageController {
	
	@FXML 
	private Button effortLoggerConsoleButton;
	
	public void goToConsole() throws IOException {
		
	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLogConsole.fxml"));
    Parent effortLogConsoleRoot = fxmlLoader.load();
    
    
    Stage stage = (Stage) effortLoggerConsoleButton.getScene().getWindow();
    stage.setScene(new Scene(effortLogConsoleRoot));
    stage.setTitle("Effort Log Console");
    stage.show();
	}
}
