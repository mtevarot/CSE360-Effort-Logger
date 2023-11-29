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
	
	private static final String DATABASE_URL = "jdbc:sqlite:C:/Users/matte/School/CSE 360/Effort Logger Database/effortLoggerDatabase.db";

	
	public void populateEffortLogsDropdown() {
		 Connection connection = null;
		    PreparedStatement preparedStatement = null;
		    ResultSet resultSet = null;
		    try {
		    	connection = DriverManager.getConnection(DATABASE_URL);
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
		        connection = DriverManager.getConnection(DATABASE_URL);
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

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL);
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
            connection = DriverManager.getConnection(DATABASE_URL);
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
	
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
