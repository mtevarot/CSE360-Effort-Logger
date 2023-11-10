package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.event.ActionEvent; 

public class EffortLoggerV2HomepageController {
	
	@FXML 
	private Button effortLoggerConsoleButton;
	
	@FXML 
	private Button quitButton; 
	
	@FXML 
	private Button logOutButton; 
	
	public void goToConsole() throws IOException {
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLogConsole.fxml"));
	    Parent effortLogConsoleRoot = fxmlLoader.load();
	    
	    
	    Stage stage = (Stage) effortLoggerConsoleButton.getScene().getWindow();
	    stage.setScene(new Scene(effortLogConsoleRoot));
	    stage.setTitle("Effort Log Console");
	    stage.show();
	}
	
	public void logOut(ActionEvent event) throws IOException{ 
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        Parent loginPageRoot = fxmlLoader.load();
        
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.setScene(new Scene(loginPageRoot));
        stage.setTitle("EffortLogger Login");
        stage.show();
}
	
	public void quit() throws IOException {
		Platform.exit();
	}
}
