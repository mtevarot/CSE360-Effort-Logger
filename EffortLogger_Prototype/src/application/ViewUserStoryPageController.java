package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ViewUserStoryPageController {
	
	@FXML 
	private Button quitButton; 
	
	 @FXML 
    private TextArea titleArea; 
    
    @FXML 
    private TextArea keywordsArea;
    
    @FXML 
    private TextArea descriptionArea;
	
	public void displayUserStory(String title, String keywords, String description) {
		titleArea.setText(title);
        keywordsArea.setText(keywords);
        descriptionArea.setText(description);
	}
	
	public void quit() {
	    Stage stage = (Stage) quitButton.getScene().getWindow();
	    stage.close();
	}
}
