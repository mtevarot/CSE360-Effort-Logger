package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
	/*
	 public void setLogin(Login login)
	
	{
		username.setText("Name: " + login.getName());
		password.setText("Password: " + login.getPassword());
		
	}
	*/
	
    public void startActivity() {
        
            clock.setFill(Color.GREEN);
            clockText.setText("CLOCK IS RUNNING");
    }
    
    public void stopActivity() {
    	clock.setFill(Color.RED);
        clockText.setText("CLOCK IS STOPPED");
    }
    
    public void menuItemAction(ActionEvent event) {
        
        
    }
}
