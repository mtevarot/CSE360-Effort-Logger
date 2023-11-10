package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login {
	private String name; 
	private String password; 
	
	public void setName(String name) {
		this.name = name; 
	}
	
	public void setPassword(String password) {
		this.password = password; 
	}
	
	public String getName() {
		return this.name; 
	}
	
	public String getPassword() {
		return this.password; 
	}
	
	public boolean validateLogin(String u, String p) throws Exception {	
		if(MySQLAccess.logInUser(u, p))
		{
			return true;
		}
		return false; 
	}
}
