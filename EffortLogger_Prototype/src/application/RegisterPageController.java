package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RegisterPageController {
	
	@FXML 
	private Button cancelButton; 
	
	@FXML 
	private TextField firstNameField; 
	
	@FXML 
	private TextField usernameField; 
	
	@FXML 
	private PasswordField passwordField; 
	
	@FXML 
	private PasswordField passwordField1; 
	
	
	public void register(ActionEvent event) throws Exception {
		SQLiteAccess dbAccess = new SQLiteAccess();
		
		if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || firstNameField.getText().isEmpty()) { 
		    showAlert("Error", "Please fill in all fields.");
		    return;
		}
		if (!passwordField.getText().equals(passwordField1.getText())) { 
			showAlert("Error", "Passwords Do Not Match");
			return; 
		}
		
	    dbAccess.signUpUser(usernameField.getText(), passwordField.getText(), firstNameField.getText());
	    
	    signedUpLogin(); 
	}
	
	public void backToLoginPage(ActionEvent event) throws IOException { 
	    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
	        Parent loginPageRoot = fxmlLoader.load();
	        
	        Stage stage = (Stage) cancelButton.getScene().getWindow();
	        stage.setScene(new Scene(loginPageRoot));
	        stage.setTitle("EffortLogger Login");
	        stage.show();
	}
	
	public void signedUpLogin() throws IOException { 
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        Parent loginPageRoot = fxmlLoader.load();
        
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(new Scene(loginPageRoot));
        stage.setTitle("EffortLogger Login");
        stage.show();
}
	
	public void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
