package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.event.ActionEvent;
import java.util.Optional;

public class PlanningPokerController {
	
	@FXML 
	private Button backButton; 
	
	@FXML 
	private ComboBox<String> userStoriesComboBox; 
	
	@FXML 
	private ComboBox<String> effortLogComboBox; 
	
	@FXML 
	private Button viewUserStoryButton; 
	
	@FXML 
	private Button viewEffortLogButton; 
	
	@FXML 
	private Button newRoundButton; 
	
	@FXML 
	private Button Button1; 
	@FXML 
	private Button Button2; 
	@FXML 
	private Button Button3; 
	@FXML 
	private Button Button4; 
	@FXML 
	private Button Button5; 
	@FXML 
	private Button Button6; 
	@FXML 
	private Button Button7; 
	@FXML 
	private Button Button8; 
	@FXML 
	private Button Button9; 
	@FXML 
	private Button Button10; 
	
	@FXML 
	private Button generateEstimateButton; 
	
	@FXML 
	private Button endMeetingButton; 
	
	@FXML
	private void initialize() {
	    Platform.runLater(() -> {
	        populateUserStoriesDropdown();
	        populateEffortLogsDropdown(); 
	    });
	}
	
	public void meetingEnded() {
		int estimate = (int)(Math.random() * 10) + 1;
		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ending meeting...");
	    alert.setTitle("Message");
	    alert.setHeaderText(null); 
	    
	    alert.show();
	    
	    PauseTransition delay = new PauseTransition(Duration.seconds(1));
	    delay.setOnFinished(e -> {
	        alert.close(); 
	        try {
				goBack();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			} 
	    });
	    delay.play();
	}
	
	public void generateEstimate() {
		int estimate = (int)(Math.random() * 10) + 1;
		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Generated Estimate: " + estimate);
	    alert.setTitle("Message");
	    alert.setHeaderText(null); 

	    alert.show();
	}
	
	public void newRound(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Starting New Round!!");
	    alert.setTitle("Message");
	    alert.setHeaderText(null); 

	    alert.show();

	    PauseTransition delay = new PauseTransition(Duration.seconds(1));
	    delay.setOnFinished(e -> {
	        alert.close(); 
	        userStoriesComboBox.setValue(null); 
	        effortLogComboBox.setValue(null);
	    });
	    delay.play(); 
	}
	
	public void viewUserStory(ActionEvent event) {
	    try {
	        String selectedStoryTitle = userStoriesComboBox.getValue();
	        if (selectedStoryTitle == null || selectedStoryTitle.isEmpty()) {
	            showAlert("Error", "Select A User Story Before Viewing.");
	        } else {
	            
	            UserStory userStory = getUserStoryDetails(selectedStoryTitle);

	          
	            if (userStory != null) {
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewUserStoryPage.fxml"));
	                Parent root = loader.load();

	                ViewUserStoryPageController controller = loader.getController();
	                controller.displayUserStory(userStory.getTitle(), userStory.getKeywords(), userStory.getDescription());

	                Stage stage = new Stage();
	                stage.setTitle("Viewing User Story: " + selectedStoryTitle);
	                stage.setScene(new Scene(root));
	                stage.show();
	            } else {
	                showAlert("Error", "User Story Details Not Found.");
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public void populateEffortLogsDropdown() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://192.168.7.95:3306/effort--logger-logins", "matteoteva", "Seba1958"); 
            String query = "SELECT project_name FROM effort_logs";
            preparedStatement = connection.prepareStatement(query);
            
            resultSet = preparedStatement.executeQuery();

            effortLogComboBox.getItems().clear();
            
            while (resultSet.next()) {
            	effortLogComboBox.getItems().add(resultSet.getString("project_name"));
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
	
	public void populateUserStoriesDropdown() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://192.168.7.95:3306/effort--logger-logins", "matteoteva", "Seba1958"); 
            String query = "SELECT title FROM user_stories";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            userStoriesComboBox.getItems().clear();
            
            while (resultSet.next()) {
                userStoriesComboBox.getItems().add(resultSet.getString("title"));
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
	
	public void handleBackAction(ActionEvent event) throws IOException {
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Confirmation");
	    alert.setHeaderText("All data will be lost if you leave, Are you sure?");

	    
	    ButtonType buttonTypeYes = new ButtonType("Yes");
	    ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
	    alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

	    Optional<ButtonType> result = alert.showAndWait();
	    
	    if (result.isPresent() && result.get() == buttonTypeYes) {
	        goBack();
	    } 
	}

	public void goBack() throws IOException {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLoggerV2Homepage.fxml"));
    	    Parent effortLogConsoleRoot = fxmlLoader.load();
    	    
    	    
    	    Stage stage = (Stage) backButton.getScene().getWindow();
    	    stage.setScene(new Scene(effortLogConsoleRoot));
    	    stage.setTitle("Effort Log Console");
    	    stage.show();
    }
	
	private void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public void viewEffortLog(ActionEvent event) {
	    try {
	        String selectedLogTitle = effortLogComboBox.getValue();
	        if (selectedLogTitle == null || selectedLogTitle.isEmpty()) {
	            showAlert("Error", "Select An Effort Log Before Viewing.");
	        } else {
	            EffortLog effortLog = getEffortLogDetails(selectedLogTitle);

	            if (effortLog != null) {
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewEffortLog.fxml"));
	                Parent root = loader.load();

	                ViewEffortLogController controller = loader.getController();
	                controller.displayEffortLog(effortLog);

	                Stage stage = new Stage();
	                stage.setTitle("Viewing Effort Log: " + selectedLogTitle);
	                stage.setScene(new Scene(root));
	                stage.show();
	            } else {
	                showAlert("Error", "Effort Log Details Not Found.");
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

		
	private EffortLog getEffortLogDetails(String logTitle) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    EffortLog effortLog = null;

	    try {
	        connection = DriverManager.getConnection("jdbc:mysql://192.168.7.95:3306/effort--logger-logins", "matteoteva", "Seba1958"); 
	        String query = "SELECT project_name, life_cycle_step, effort_category, project_type, start_time, end_time FROM effort_logs WHERE project_name = ?";
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, logTitle);
	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	        	String projectName = resultSet.getString("project_name");
	            String lifeCycleStep = resultSet.getString("life_cycle_step");
	            String effortCategory = resultSet.getString("effort_category");
	            String projectType = resultSet.getString("project_type");
	            LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
	            LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();
	        	effortLog = new EffortLog(
        			 projectName,
                     lifeCycleStep,
                     effortCategory,
                     projectType,
                     startTime,
                     endTime
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Close all connections
	        try {
	            if (resultSet != null) resultSet.close();
	            if (preparedStatement != null) preparedStatement.close();
	            if (connection != null) connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return effortLog;
	}
	
	
	private UserStory getUserStoryDetails(String storyTitle) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    UserStory userStory = null;

	    try {
	        connection = DriverManager.getConnection("jdbc:mysql://192.168.7.95:3306/effort--logger-logins", "matteoteva", "Seba1958"); 
	        String query = "SELECT title, `key words`, description FROM user_stories WHERE title = ?";
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, storyTitle);
	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            userStory = new UserStory(
	                resultSet.getString("title"),
	                resultSet.getString("key words"),
	                resultSet.getString("description")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Close all connections
	        try {
	            if (resultSet != null) resultSet.close();
	            if (preparedStatement != null) preparedStatement.close();
	            if (connection != null) connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return userStory;
	}
}
