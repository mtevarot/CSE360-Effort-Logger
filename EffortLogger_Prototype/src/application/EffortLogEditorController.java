package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class EffortLogEditorController {
	
	@FXML 
	private Button backToConsoleButton; 
	
	@FXML 
	private ComboBox<String> effortLogComboBox; 
	
	@FXML 
	private ComboBox<String> effortLogComboBox2; 
	
	 @FXML
    private SplitMenuButton projectField1;
    
    @FXML
    private SplitMenuButton projectField2;
    
    @FXML
    private SplitMenuButton projectField3;
    
    @FXML 
    private Button clearLogButton; 
    
    @FXML
    private TextField dateField; 
    
    @FXML
    private TextField startField; 
    
    @FXML
    private TextField stopField; 
    
    @FXML 
    private Button deleteLogButton;
    
    @FXML 
    private Button splitButton;
    
	@FXML
	private void initialize() {
	    Platform.runLater(() -> {
	        populateEffortLogsDropdown();
	    });

	    effortLogComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
	    	if (newValue != null) {
	            populateSecondEffortLogsDropdown(newValue.toString());
	        } else {
	            effortLogComboBox2.getItems().clear();
	        }
	    });
	    
	    effortLogComboBox2.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
	        if (newValue != null) {
	            fillLogDetails(newValue);
	        }
	    });
	    
	    effortLogComboBox2.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
	        if (isNowShowing && !projectSelected()) {
	            showAlert("Error", "Select a project first.");
	            effortLogComboBox2.hide(); 
	        }
	    });

	    projectField1.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
	        if (isNowShowing && !projectSelected()) {
	            showAlert("Error", "Select a project first.");
	            projectField1.hide(); 
	        }
	    });
	    
	    projectField2.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
	        if (isNowShowing && !projectSelected()) {
	            showAlert("Error", "Select a project first.");
	            projectField2.hide(); 
	        }
	    });
	    
	    projectField3.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
	        if (isNowShowing && !projectSelected()) {
	            showAlert("Error", "Select a project first.");
	            projectField3.hide(); 
	        }
	    });
	}
	
	private static final String DATABASE_URL = "jdbc:mysql://162.248.102.123:3306/eflDatabase";
    private static final String DATABASE_USER = "matteoteva";
    private static final String DATABASE_PASSWORD = "Seba1958";

	
	public void populateEffortLogsDropdown() {
		 Connection connection = null;
		    PreparedStatement preparedStatement = null;
		    ResultSet resultSet = null;
		    try {
		    	connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
		        String query = "SELECT DISTINCT project_name FROM effort_logs";
		        preparedStatement = connection.prepareStatement(query);
		        
		        resultSet = preparedStatement.executeQuery();
		
		        effortLogComboBox.getItems().clear();
		        
		        while (resultSet.next()) {
		        	String comboBoxEntry = resultSet.getString("project_name");
		        	effortLogComboBox.getItems().add(comboBoxEntry);    
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
		}
	
	public void populateSecondEffortLogsDropdown(String selectedProjectName) {
		if(projectSelected()) {
			Connection connection = null;
		    PreparedStatement preparedStatement = null;
		    ResultSet resultSet = null;
		    try {
		        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
		        String query = "SELECT project_name, life_cycle_step, effort_category, project_type, start_time, end_time FROM effort_logs WHERE project_name = ?";
		        preparedStatement = connection.prepareStatement(query);
		        preparedStatement.setString(1, selectedProjectName);  

		        resultSet = preparedStatement.executeQuery();

		        effortLogComboBox2.getItems().clear();

		        while (resultSet.next()) {
		            String comboBoxEntry2 = resultSet.getString("project_name") + " - " 
		                    + resultSet.getString("life_cycle_step") + " - "
		                    + resultSet.getString("effort_category") + " - "
		                    + resultSet.getString("project_type") + " - " 
		                    + resultSet.getString("start_time") + " - " 
		                    + resultSet.getString("end_time");     
		            effortLogComboBox2.getItems().add(comboBoxEntry2);    
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
		} else {
			showAlert("Error", "Select a project first."); 
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
    	if(projectSelected()) {
    		projectField1.setText(text);
    	} else {
			showAlert("Error", "Select a project first."); 
		}
    }
    
    public void updateSplitMenuButtonText2(String text) {
    	if(projectSelected()) {
    		projectField2.setText(text);
    	} else {
			showAlert("Error", "Select a project first."); 
		}
    }
    
    public void updateSplitMenuButtonText3(String text) {
    	if(projectSelected()) {
    		projectField3.setText(text);
    	} else {
			showAlert("Error", "Select a project first."); 
		}
    }
	
    public void updateLog(ActionEvent event) {
        if (effortLogComboBox2.getValue() == null) {
            showAlert("Error", "Select a log entry to update.");
            return;
        }
        
        String selectedLog = effortLogComboBox2.getValue();
        String[] logParts = selectedLog.split(" - ");
        String originalStartTime = logParts[4];
        String originalEndTime = logParts[5];

        String lifeCycleStep = projectField1.getText(); 
        String effortCategory = projectField2.getText(); 
        String projectType = projectField3.getText(); 
        String newDate = dateField.getText();
        String newStartTime = newDate + " " + startField.getText();
        String newEndTime = newDate + " " + stopField.getText();
        
        if (!isTimeFormatCorrect(startField.getText()) || !isTimeFormatCorrect(stopField.getText())) {
            showAlert("Error", "Time must be in the format HH:mm:ss.");
            return;
        }
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            String query = "UPDATE effort_logs SET life_cycle_step = ?, effort_category = ?, project_type = ?, start_time = ?, end_time = ? WHERE project_name = ? AND start_time = ? AND end_time = ?";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, lifeCycleStep);
            preparedStatement.setString(2, effortCategory);
            preparedStatement.setString(3, projectType);
            preparedStatement.setString(4, newStartTime);
            preparedStatement.setString(5, newEndTime);
            preparedStatement.setString(6, logParts[0]);
            preparedStatement.setString(7, originalStartTime);
            preparedStatement.setString(8, originalEndTime);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showSuccess("Success", "Log updated successfully.");
            } else {
                showAlert("Error", "Log update failed.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        initialize();
        clearLogs();
    }

    public void deleteLog(ActionEvent event) {
        if (effortLogComboBox2.getValue() == null) {
            showAlert("Error", "Select a log entry to delete.");
            return;
        }

        String selectedLog = effortLogComboBox2.getValue();
        String[] logParts = selectedLog.split(" - ");
        String projectName = logParts[0];
        String originalStartTime = logParts[4];
        String originalEndTime = logParts[5];

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            String query = "DELETE FROM effort_logs WHERE project_name = ? AND start_time = ? AND end_time = ?";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, originalStartTime);
            preparedStatement.setString(3, originalEndTime);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showSuccess("Success", "Log deleted successfully.");
            } else {
                showAlert("Error", "Log deletion failed.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        clearLogs();
    }
    
	public void backToConsole(ActionEvent Action) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLogConsole.fxml"));
	    Parent effortLogConsoleRoot = fxmlLoader.load();
	    
	    
	    Stage stage = (Stage) backToConsoleButton.getScene().getWindow();
	    stage.setScene(new Scene(effortLogConsoleRoot));
	    stage.setTitle("Effort Log Console");
	    stage.show();
	}
	
	public boolean projectSelected() { return effortLogComboBox.getValue() != null; }
	
	public void showAlert(String title, String content) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(title);
			alert.setHeaderText(null);
			alert.setContentText(content);
			alert.showAndWait();
	}
	
	public void showSuccess(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
}
	
	public void clearLogs(ActionEvent event) {
	    effortLogComboBox.setValue(null);
	    effortLogComboBox2.getItems().clear();
	    projectField1.setText("");
	    projectField2.setText("");
	    projectField3.setText(""); 
	    dateField.setText(""); 
	    startField.setText("");
	    stopField.setText(""); 
	}
	
	public void clearLogs() {
	    effortLogComboBox.setValue(null);
	    effortLogComboBox2.getItems().clear();
	    projectField1.setText("");
	    projectField2.setText("");
	    projectField3.setText(""); 
	    dateField.setText(""); 
	    startField.setText("");
	    stopField.setText(""); 
	}
	
	public void fillLogDetails(String logDetail) {
	    String[] logParts = logDetail.split(" - ");
	    if (logParts.length >= 6) {
	        projectField1.setText(logParts[1]); // life cycle step
	        projectField2.setText(logParts[2]); // effort category
	        projectField3.setText(logParts[3]); // project type

	        LocalDateTime startTime = LocalDateTime.parse(logParts[4], formatter);
	        LocalDateTime endTime = LocalDateTime.parse(logParts[5], formatter);
	        
	        dateField.setText(startTime.toLocalDate().toString());
	        startField.setText(startTime.toLocalTime().toString());
	        stopField.setText(endTime.toLocalTime().toString());
	    }
	}
	
	public void split(ActionEvent event) {
	    // Extract values from input fields
	    String projectName = effortLogComboBox.getValue();
	    String lifeCycleStep = projectField1.getText(); 
	    String effortCategory = projectField2.getText(); 
	    String projectType = projectField3.getText(); 
	    String date = dateField.getText();
	    String startTime = startField.getText();
	    String endTime = stopField.getText();
	    
	    // Build the new log's start and end time
	    String newStartTime = date + " " + startTime;
	    String newEndTime = date + " " + endTime;

	    // Database connection and SQL preparation
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    try {
	        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
	        String insertQuery = "INSERT INTO effort_logs (project_name, life_cycle_step, effort_category, project_type, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)";
	        preparedStatement = connection.prepareStatement(insertQuery);

	        // Set parameters for the insert query
	        preparedStatement.setString(1, projectName);
	        preparedStatement.setString(2, lifeCycleStep);
	        preparedStatement.setString(3, effortCategory);
	        preparedStatement.setString(4, projectType);
	        preparedStatement.setString(5, newStartTime);
	        preparedStatement.setString(6, newEndTime);

	        // Execute the insert query
	        int rowsAffected = preparedStatement.executeUpdate();
	        if (rowsAffected > 0) {
	            showSuccess("Success", "Log split successfully.");
	        } else {
	            showAlert("Error", "Failed to create new log.");
	        }
	    } catch (SQLException e) {
	        showAlert("Error", "Database error: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        try {
	            if (preparedStatement != null) preparedStatement.close();
	            if (connection != null) connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    initialize();
	    clearLogs();
	}
	
	//format time for database
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	//make sure time has two initial digits
	private boolean isTimeFormatCorrect(String time) {
		return time.matches("\\d{2}:\\d{2}:\\d{2}");
	}
}
