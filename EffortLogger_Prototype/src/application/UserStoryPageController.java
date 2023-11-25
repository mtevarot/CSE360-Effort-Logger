package application;

import java.io.IOException;
import java.util.Optional;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UserStoryPageController {
		
	@FXML 
	private Button backToHomepageButton; 
	
	@FXML 
	private Button clearStoryButton; 
	
	@FXML 
	private TextField titleField; 
	
	@FXML 
	private TextField keyWordsField; 
	
	@FXML 
	private TextField descriptionField; 
	
	@FXML 
	private Button createStoryButton; 
	
	@FXML 
	private Button viewOldStoriesButton; 
	
	public void viewOldStories() throws IOException { 
		int userId = CurrentUser.getUserId();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewOldStories.fxml"));
        Parent viewOldStoriesRoot = fxmlLoader.load();
        
        Stage stage = (Stage) viewOldStoriesButton.getScene().getWindow();
        stage.setScene(new Scene(viewOldStoriesRoot));
        stage.setTitle("View Old User Stories");
        stage.show();
	}

	public void showUserStoryCreatedAlert() {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION, "User Story Created!", ButtonType.OK);
	    alert.setTitle("Message");
	    alert.setHeaderText(null); 

	    
	    Platform.runLater(alert::show);

	    
	    PauseTransition delay = new PauseTransition(Duration.seconds(2));
	    delay.setOnFinished(event -> alert.close());
	    delay.play();
	}
	
	public void createStory(ActionEvent event) throws Exception { 
		if(!titleField.getText().isEmpty() && !keyWordsField.getText().isEmpty() && !descriptionField.getText().isEmpty()) {
			int userId = CurrentUser.getUserId();
			
			MySQLAccess.createUserStory(titleField.getText(), keyWordsField.getText(), descriptionField.getText(), userId); 
			
			showUserStoryCreatedAlert(); 
			clearStory(); 
		} else {
			showAlert("Error", "Please Fill Out All Sections Before Creating User Story");
		}
	}
	
	public void clearStory (ActionEvent event) throws IOException {
		titleField.setText("");
	    keyWordsField.setText("");
	    descriptionField.setText("");
	}
	
	public void clearStory () throws IOException {
		titleField.setText("");
	    keyWordsField.setText("");
	    descriptionField.setText("");
	}
	
	public void backToHomePage() throws IOException { 
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLoggerV2Homepage.fxml"));
        Parent backToHomepageRoot = fxmlLoader.load();
        
        Stage stage = (Stage) backToHomepageButton.getScene().getWindow();
        stage.setScene(new Scene(backToHomepageRoot));
        stage.setTitle("User Story Console");
        stage.show();
	}
	
	public void handleBackAction(ActionEvent event) throws IOException {
		if(!titleField.getText().isEmpty() || !keyWordsField.getText().isEmpty() || !descriptionField.getText().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		    alert.setTitle("Confirmation");
		    alert.setHeaderText("All data will be lost if you leave, Are you sure?");

		    
		    ButtonType buttonTypeYes = new ButtonType("Yes");
		    ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
		    alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

		    Optional<ButtonType> result = alert.showAndWait();
		    
		    if (result.isPresent() && result.get() == buttonTypeYes) {
		    	backToHomePage();
		    }
	    } else {
	    	backToHomePage();
	    }
	}
	
	public static void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
