package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ViewOldUserStoriesController {
	
	@FXML 
	private Button backToHomepageButton; 
	
	@FXML
	private ListView<String> userStoriesListView;
	
	@FXML
	public void initialize() {
	 Platform.runLater(() -> {
		 populateUserStoriesListView();
	    });
	 
        userStoriesListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                viewUserStory();
            }
        });
       
    }
	
	private static final String DATABASE_URL = "jdbc:sqlite:C:/Users/matte/School/CSE 360/Effort Logger Database/effortLoggerDatabase.db";
 
	public void populateUserStoriesListView() {
        List<String> userStoryTitles = getUserStoryTitles();
        userStoriesListView.setItems(FXCollections.observableArrayList(userStoryTitles));
    }
	
	public void viewUserStory() {
	    try {
	        String selectedStoryTitle = userStoriesListView.getSelectionModel().getSelectedItem();
	       
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
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
	public List<String> getUserStoryTitles() {
        List<String> titles = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
        	connection = DriverManager.getConnection(DATABASE_URL);
            String query = "SELECT title FROM user_stories";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                titles.add(resultSet.getString("title"));
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
	
	
	public UserStory getUserStoryDetails(String storyTitle) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        UserStory userStory = null;

        try {
        	connection = DriverManager.getConnection(DATABASE_URL);
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
    
	
	public void handleBackAction(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLoggerV2Homepage.fxml"));
        Parent backToHomepageRoot = fxmlLoader.load();
        
        Stage stage = (Stage) backToHomepageButton.getScene().getWindow();
        stage.setScene(new Scene(backToHomepageRoot));
        stage.setTitle("Effort Logger V2 Homepage");
        stage.show();
	}
	
	public static void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
