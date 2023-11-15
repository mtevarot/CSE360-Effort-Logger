package application;

	import java.io.IOException;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Optional;

	import javafx.application.Platform;
	import javafx.collections.FXCollections;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.Alert;
	import javafx.scene.control.Button;
	import javafx.scene.control.ButtonBar;
	import javafx.scene.control.ButtonType;
	import javafx.scene.control.ListView;
	import javafx.scene.control.Alert.AlertType;
	import javafx.stage.Stage;

	public class ViewOldEffortLogsController {
	    
	    @FXML 
	    private Button backToHomepageButton; 
	    
	    @FXML
	    private ListView<String> effortLogsListView;
	    
	    @FXML
	    public void initialize() {
	    	Platform.runLater(() -> {
	   		 populateEffortLogsListView();
	   	    });
	        
	        effortLogsListView.setOnMouseClicked(event -> {
	            if(event.getClickCount() == 2) {
	                viewEffortLog();
	            }
	        });
	    }
	 
	    public void populateEffortLogsListView() {
	        List<String> effortLogTitles = getEffortLogTitles();
	        effortLogsListView.setItems(FXCollections.observableArrayList(effortLogTitles));
	    }
	    
	    public void viewEffortLog() {
	        try {
	            String selectedLogTitle = effortLogsListView.getSelectionModel().getSelectedItem();
	            
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
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public List<String> getEffortLogTitles() {
	        List<String> titles = new ArrayList<>();
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;

	        try {
	            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effort--logger-logins", "root", "Seba1958");
	            String query = "SELECT DISTINCT project_name FROM effort_logs";
	            preparedStatement = connection.prepareStatement(query);
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
	    
	    public EffortLog getEffortLogDetails(String projectName) {
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;
	        EffortLog effortLog = null;

	        try {
	            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effort--logger-logins", "root", "Seba1958");
	            String query = "SELECT * FROM effort_logs WHERE project_name = ?";
	            preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, projectName);
	            resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                effortLog = new EffortLog(
	                    resultSet.getInt("ideffort_logs"),
	                    resultSet.getString("project_name"),
	                    resultSet.getString("life_cycle_step"),
	                    resultSet.getString("effort_category"),
	                    resultSet.getString("project_type"),
	                    resultSet.getTimestamp("start_time").toLocalDateTime(),
	                    resultSet.getTimestamp("end_time").toLocalDateTime()
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
	        return effortLog;
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
