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

public class EffortLogConsoleController {
	@FXML
	private Label username;
	
	@FXML
	private Label password;
	
	@FXML
    private Button startActivity;

    @FXML
    private Rectangle clock;
	
    @FXML 
    private Label clockText; 
    
    @FXML
    private SplitMenuButton projectField;
    
    @FXML
    private SplitMenuButton projectField1;
    
    @FXML
    private SplitMenuButton projectField2;
    
    @FXML
    private SplitMenuButton projectField3;
    
    @FXML 
    private Button backButton; 

    
    public void goBack(ActionEvent event) throws IOException {
    	if(!clockStatus()) {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLoggerV2Homepage.fxml"));
    	    Parent effortLogConsoleRoot = fxmlLoader.load();
    	    
    	    
    	    Stage stage = (Stage) backButton.getScene().getWindow();
    	    stage.setScene(new Scene(effortLogConsoleRoot));
    	    stage.setTitle("Effort Log Console");
    	    stage.show();
    	}
    	else {
    		showAlert("Error", "End the Activity Before Going Back!"); 
    	}
    }
    
    public void menuItemAction(ActionEvent event) {
        updateSplitMenuButtonText(((MenuItem) event.getSource()).getText());
    }
    
    public void menuItemAction1(ActionEvent event) {
        updateSplitMenuButtonText1(((MenuItem) event.getSource()).getText());
    }
    
    public void menuItemAction2(ActionEvent event) {
        updateSplitMenuButtonText2(((MenuItem) event.getSource()).getText());
    }
    
    public void menuItemAction3(ActionEvent event) {
        updateSplitMenuButtonText3(((MenuItem) event.getSource()).getText());
    }

    public void updateSplitMenuButtonText(String text) {
    	if(clockStatus()) {
    		projectField.setText(text);
    	}
    	else {
    		showAlert("Error", "Start an Activity First!");
    	}
    }
    
    public void updateSplitMenuButtonText1(String text) {
    	if(clockStatus()) {
    		projectField1.setText(text);
    	}
    	else {
    		showAlert("Error", "Start an Activity First!");
    	}
    }
    
    public void updateSplitMenuButtonText2(String text) {
    	if(clockStatus()) {
    		projectField2.setText(text);
    	}
    	else {
    		showAlert("Error", "Start an Activity First!");
    	}
    }
    
    public void updateSplitMenuButtonText3(String text) {
    	if(clockStatus()) {
    		projectField3.setText(text);
    	}
    	else {
    		showAlert("Error", "Start an Activity First!");
    	}
    }
	
    public void startActivity() {
        
            clock.setFill(Color.GREEN);
            clockText.setText("CLOCK IS RUNNING");
    }
    
    public boolean clockStatus() {
    	if("CLOCK IS STOPPED".equals(clockText.getText())) {
    		return false; 
    	}
    	return true;
    }
    
    public void stopActivity() {
    	clock.setFill(Color.RED);
        clockText.setText("CLOCK IS STOPPED");
        projectField.setText("");
        projectField1.setText("");
        projectField2.setText("");
        projectField3.setText("");
    }
    
    private void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
