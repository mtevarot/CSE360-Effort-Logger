package application;

public class CurrentUser {
    
    private static int userId = -1; 

   
    private CurrentUser() {
    }

   
    public static void setUserId(int newUserId) {
        userId = newUserId;
    }

   
    public static int getUserId() {
        return userId;
    }

    
    public static boolean isLoggedIn() {
        return userId != -1; 
    }

    
    public static void logout() {
        userId = -1;
    }
}
