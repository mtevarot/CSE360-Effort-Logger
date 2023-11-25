package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;

public class MySQLAccess {
    Connection connection = null;
    PreparedStatement insert = null; 
    PreparedStatement userExists = null;
    ResultSet resultSet = null; 

    
    public void signUpUser(String username, String password, String firstAndLast) throws Exception {
        try {
        
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            connection = DriverManager.getConnection("jdbc:mysql://192.168.7.95:3306/effort--logger-logins", "matteoteva", "Seba1958"); 
            
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
            connection = DriverManager.getConnection("jdbc:mysql://192.168.7.95:3306/effort--logger-logins", "matteoteva", "Seba1958"); 
            
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
             connection = DriverManager.getConnection("jdbc:mysql://192.168.7.95:3306/effort--logger-logins", "matteoteva", "Seba1958"); 
             
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
        } catch (Exception e) {
            throw e;
        } finally {
        	 try {
                 if (resultSet != null) resultSet.close();
                 if (preparedStatement != null) preparedStatement.close();
                 if (connection != null) connection.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
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
