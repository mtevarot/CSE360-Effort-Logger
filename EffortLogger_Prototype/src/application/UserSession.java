package application;

public class UserSession {
    private static String loggedInUserName;

    public static void setLoggedInUserName(String userName) {
        loggedInUserName = userName;
    }

    public static String getLoggedInUserName() {
        return loggedInUserName;
    }
}
