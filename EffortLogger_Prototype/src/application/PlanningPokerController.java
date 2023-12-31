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
import javafx.collections.FXCollections;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
	    	clearGuesses();
	        populateUserStoriesDropdown();
	        populateEffortLogsDropdown(); 
	    });
	}
	
	@FXML
	public void guessSelected(ActionEvent event) {
	    if (event.getSource() instanceof Button) {
	        String employeeName = UserSession.getLoggedInUserName();
	        Button clickedButton = (Button) event.getSource();
	        int guess = Integer.parseInt(clickedButton.getText());

	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation");
	        alert.setHeaderText("You have selected " + guess + " as your estimate, Are you sure?");

	        ButtonType buttonTypeYes = new ButtonType("Yes");
	        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
	        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

	        Optional<ButtonType> result = alert.showAndWait();

	        if (result.isPresent() && result.get() == buttonTypeYes) {
	            saveGuess(employeeName, guess);
	        }
	    }
	}
	
	private static final String DATABASE_URL = "jdbc:mysql://162.248.102.123:3306/eflDatabase";
    private static final String DATABASE_USER = "matteoteva";
    private static final String DATABASE_PASSWORD = "Seba1958";
	
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
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("Estimates.fxml"));
	        Parent root = loader.load();

	        EstimateDisplayController controller = loader.getController();
	        controller.setGuessData(FXCollections.observableArrayList(getAllGuesses()));

	        Stage stage = new Stage();
	        stage.setTitle("Estimates");
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	public void saveGuess(String employeeName, int guess) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    try {
	        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
	        String sql = "INSERT INTO planning_poker (employee_name, guess) VALUES (?, ?)";
	        preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setString(1, employeeName);
	        preparedStatement.setInt(2, guess);
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
	
	public List<Guess> getAllGuesses() {
	    List<Guess> guesses = new ArrayList<>();
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
	        String sql = "SELECT employee_name, guess FROM planning_poker";
	        preparedStatement = connection.prepareStatement(sql);
	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            String employeeName = resultSet.getString("employee_name");
	            int guess = resultSet.getInt("guess");
	            guesses.add(new Guess(employeeName, guess));
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
	    return guesses;
	}
	
	public void clearGuesses() {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    try {
	        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
	        String sql = "DELETE FROM planning_poker";
	        preparedStatement = connection.prepareStatement(sql);
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

	public void newRound(ActionEvent event) {
		clearGuesses();
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
	        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
	        String query = "SELECT project_name, start_time, end_time FROM effort_logs";
	        preparedStatement = connection.prepareStatement(query);
	        
	        resultSet = preparedStatement.executeQuery();

	        List<String> entries = new ArrayList<>();
	        
	        while (resultSet.next()) {
	            String entry = resultSet.getString("project_name") + "  --->  "
	                           + resultSet.getString("start_time") + " | "
	                           + resultSet.getString("end_time");
	            entries.add(entry);
	        }

	        Collections.sort(entries); 
	        effortLogComboBox.getItems().clear();
	        effortLogComboBox.getItems().addAll(entries);

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
        	connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
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
	
	public void backButtonPressed(ActionEvent event) throws IOException {
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
	        String[] logParts = logTitle.split("  --->  | \\| ");
	        String projectName = logParts[0];
	        String startTimeString = logParts[1];
	        String endTimeString = logParts[2];

	        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
	        String query = "SELECT * FROM effort_logs WHERE project_name = ? AND start_time = ? AND end_time = ?";
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, projectName);
	        preparedStatement.setString(2, startTimeString);
	        preparedStatement.setString(3, endTimeString);
	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
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
	    	connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
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
