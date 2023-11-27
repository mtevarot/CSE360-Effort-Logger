package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class SQLiteAccess  {
    private static final String DATABASE_URL = "jdbc:sqlite:C:/Users/matte/School/CSE 360/Effort Logger Database/effortLoggerDatabase.db";
    Connection connection = null;
    PreparedStatement insert = null; 
    PreparedStatement userExists = null;
    ResultSet resultSet = null; 

    
    public void signUpUser(String username, String password, String firstAndLast) throws Exception {
        try {
        	Class.forName("org.sqlite.JDBC");

        	connection = DriverManager.getConnection(DATABASE_URL);
            userExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            userExists.setString(1, username); 
            resultSet = userExists.executeQuery();
            
            if(resultSet.isBeforeFirst()) {
            	showAlert("Error", "Username Already Taken");
            } else {

                insert = connection.prepareStatement("INSERT INTO users (username, password, firstAndLast) VALUES (?, ?, ?)");
                insert.setString(1, username);
                insert.setString(2, password); 
                insert.setString(3, firstAndLast);
                insert.executeUpdate();
            }
            
        } catch (Exception e) {
            throw e;
        } finally {
        	 try {
                 if (resultSet != null) resultSet.close();
                 if (userExists != null) userExists.close();
                 if (insert != null) insert.close();
                 if (connection != null) connection.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        }
    }
    
    public static void createUserStory(String title, String keyWords, String description) throws Exception {
    	Connection connection = null;
    	PreparedStatement psInsert = null;  
    	ResultSet resultSet = null; 
    	
    	try { 
    		connection = DriverManager.getConnection(DATABASE_URL);
            
            String sql = "INSERT INTO user_stories (title, `key words`, description) VALUES (?, ?, ?)";
            psInsert = connection.prepareStatement(sql);
            
            psInsert.setString(1, title);
            psInsert.setString(2, keyWords);
            psInsert.setString(3, description);
            
            psInsert.executeUpdate();
            
        } catch (SQLException e) {
           
            e.printStackTrace();
        } finally {
       	 try {
             if (resultSet != null) resultSet.close();
             if (psInsert != null) psInsert.close();
             if (connection != null) connection.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
    }
    }
    
    public static boolean logInUser(String username, String password) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null; 

        try {
        	Class.forName("org.sqlite.JDBC");
        	
        	connection = DriverManager.getConnection(DATABASE_URL);
            preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()) { 
           	 showAlert("Error", "Incorrect username or password, please try again."); 
            } else {
           	 while(resultSet.next()) {
           		 String retrievedPassword = resultSet.getString("password"); 
           		 if(retrievedPassword.equals(password)) {
           			 try {
                            if (resultSet != null) resultSet.close();
                            if (preparedStatement != null) preparedStatement.close();
                            if (connection != null) connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
           			 return true; 
           		 }
           	 }
            }
            showAlert("Error", "Incorrect username or password, please try again.");
        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
        return false;
    }

    
    public static void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
    
    
}
