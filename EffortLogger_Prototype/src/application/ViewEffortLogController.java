package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class ViewEffortLogController {
	
	@FXML 
	private Button quitButton; 
	
	@FXML
    private TextArea titleArea, lifeCycleArea, effortCategoryArea, projectTypeArea, startTimeArea, endTimeArea;

    public void displayEffortLog(EffortLog effortLog) {
    	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    	
        titleArea.setText(effortLog.getProjectName());
        lifeCycleArea.setText(effortLog.getLifeCycleStep());
        effortCategoryArea.setText(effortLog.getEffortCategory());
        projectTypeArea.setText(effortLog.getProjectType());
        startTimeArea.setText("Date: " + effortLog.getStartTime().format(dateFormatter) + "\nTime: " + effortLog.getStartTime().format(timeFormatter));
        endTimeArea.setText("Date: " + effortLog.getEndTime().format(dateFormatter) + "\nTime: " + effortLog.getEndTime().format(timeFormatter));
    }
	
	public void quit() {
	    Stage stage = (Stage) quitButton.getScene().getWindow();
	    stage.close();
	}
}
