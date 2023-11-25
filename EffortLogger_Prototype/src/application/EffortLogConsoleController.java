package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    private ComboBox projectField;
    
    @FXML
    private SplitMenuButton projectField1;
    
    @FXML
    private SplitMenuButton projectField2;
    
    @FXML
    private SplitMenuButton projectField3;
    
    @FXML 
    private Button backButton; 
    
    @FXML 
    private Button effortLogEditorButton;
    
    @FXML
    private Button defectLogButton; 
    
    private LocalDateTime startTime; 
    
    private LocalDateTime stopTime; 
    
   
    @FXML
    public void initialize() {
        populateProjectField();
    }
    
    public void populateProjectField() {
        ObservableList<String> projectTitles = getProjectTitlesFromDatabase();
        projectField.setItems(projectTitles);
    }
    
    public void goToDefectLog(ActionEvent event) throws IOException {
    	if(!clockStatus()) {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DefectLogConsole.fxml"));
    	    Parent defectLogRoot = fxmlLoader.load();
    	    
    	    
    	    Stage stage = (Stage) defectLogButton.getScene().getWindow();
    	    stage.setScene(new Scene(defectLogRoot));
    	    stage.setTitle("Defect Log Console");
    	    stage.show();
    	}
    	else {
    		showAlert("Error", "End the Activity Before Going to the Defect Log Console!"); 
    	}
    }
    
    public ObservableList<String> getProjectTitlesFromDatabase() {
        ObservableList<String> titles = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://192.168.7.95:3306/effort--logger-logins", "matteoteva", "Seba1958"); 
            String query = "SELECT DISTINCT project_name FROM effort_logs WHERE user_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, CurrentUser.getUserId());
            
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                titles.add(resultSet.getString("project_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return titles;
    }
    
    public void goToEditor(ActionEvent event) throws IOException {
    	if(!clockStatus()) {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLogEditor.fxml"));
    	    Parent effortLogEditorRoot = fxmlLoader.load();
    	    
    	    
    	    Stage stage = (Stage) effortLogEditorButton.getScene().getWindow();
    	    stage.setScene(new Scene(effortLogEditorRoot));
    	    stage.setTitle("Effort Log Editor");
    	    stage.show();
    	}
    	else {
    		showAlert("Error", "End the Activity Before Going to the Effort Log Editor!"); 
    	}
    }
    
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
    
    
    public void menuItemAction1(ActionEvent event) {
        updateSplitMenuButtonText1(((MenuItem) event.getSource()).getText());
    }
    
    public void menuItemAction2(ActionEvent event) {
        updateSplitMenuButtonText2(((MenuItem) event.getSource()).getText());
    }
    
    public void menuItemAction3(ActionEvent event) {
        updateSplitMenuButtonText3(((MenuItem) event.getSource()).getText());
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
            startTime = LocalDateTime.now();
    }
    
    public boolean clockStatus() {
    	if("CLOCK IS STOPPED".equals(clockText.getText())) {
    		return false; 
    	}
    	return true;
    }
    
    public void stopActivity() {
        if (clockStatus()) {
            clock.setFill(Color.RED);
            clockText.setText("CLOCK IS STOPPED");

            stopTime = LocalDateTime.now();
            	
            int userId = CurrentUser.getUserId();
            
            saveTime(startTime, stopTime);
            saveEffortLogData(
                projectField.getEditor().getText(),
                projectField1.getText(),
                projectField2.getText(),
                projectField3.getText(),
                startTime,
                stopTime,
                userId 
            );

            projectField.setPromptText("");
            projectField1.setText("");
            projectField2.setText("");
            projectField3.setText("");
        } else {
            showAlert("Error", "Start an Activity First!");
        }
    }
    
    public void saveEffortLogData(String projectName, String lifeCycleStep, String effortCategory, String projectType, LocalDateTime startTime, LocalDateTime endTime, int user_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://192.168.7.95:3306/effort--logger-logins", "matteoteva", "Seba1958"); 
            String query = "INSERT INTO effort_logs (project_name, life_cycle_step, effort_category, project_type, start_time, end_time, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, lifeCycleStep);
            preparedStatement.setString(3, effortCategory);
            preparedStatement.setString(4, projectType);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(endTime));
            preparedStatement.setInt(7, user_id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
    
    private void saveTime(LocalDateTime start, LocalDateTime stop) {
    	String startf = start.format(formatter);
    	String stopf = stop.format(formatter);
    }
    
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
