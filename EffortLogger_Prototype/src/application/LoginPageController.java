package application;

import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;


public class LoginPageController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    
    @FXML 
    private Button registerButton; 

    private Login login = new Login();
    
    public void submitLogin(ActionEvent event) throws Exception {
        try {
  
            boolean loginSuccessful = login.validateLogin(usernameField.getText(), passwordField.getText());

            if (loginSuccessful) {
                
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLoggerV2Homepage.fxml"));
                Parent effortLoggerV2HomepageRoot = fxmlLoader.load();
                
    
                
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(effortLoggerV2HomepageRoot));
                stage.setTitle("Effort Log Console");
                stage.show();
            } else {
                showAlert("Error", "Incorrect username or password, please try again.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while loading the next screen. ");
        }
    }
    
    public void register(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegisterPage.fxml"));
        Parent registerPageRoot = fxmlLoader.load();
        
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setScene(new Scene(registerPageRoot));
        stage.setTitle("Effort Logger Account Creation");
        stage.show();
    }
    
    private void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
